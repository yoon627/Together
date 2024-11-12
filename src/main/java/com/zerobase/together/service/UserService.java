package com.zerobase.together.service;

import com.zerobase.together.dto.AuthDto;
import com.zerobase.together.dto.AuthDto.SignUpWithPartner;
import com.zerobase.together.dto.UserDto;
import com.zerobase.together.entity.CoupleEntity;
import com.zerobase.together.entity.UserEntity;
import com.zerobase.together.repository.CoupleRepository;
import com.zerobase.together.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

  private final PasswordEncoder passwordEncoder;
  private final CoupleRepository coupleRepository;
  private final UserRepository userRepository;

  /**
   * 커플 중 처음 회원 등록하는 메서드
   *
   * @param user (회원에 대한 정보)
   * @return 저장된 회원 정보
   */
  public UserDto register(AuthDto.SignUp user) {

    if (this.userRepository.existsByUsername(user.getUsername())) {
      throw new RuntimeException("이미 사용중인 아이디 입니다. -> " + user.getUsername());
    }

    CoupleEntity coupleEntity = coupleRepository.save(new CoupleEntity());

    user.setCoupleId(coupleEntity.getCoupleId());
    user.setPassword(this.passwordEncoder.encode(user.getPassword()));

    return UserDto.fromEntity(this.userRepository.save(user.toEntity()));
  }

  /**
   * 상대방이 이미 가입한 상태인 회원 등록하는 메서드
   *
   * @param user (회원에 대한 정보)
   * @return 저장된 회원 정보
   */
  public UserDto registerWithPartner(SignUpWithPartner user) {
    if (this.userRepository.existsByUsername(user.getUsername())) {
      throw new RuntimeException("이미 사용중인 아이디 입니다. -> " + user.getUsername());
    }

    UserEntity partner = this.userRepository.findByUsername(user.getPartnername())
        .orElseThrow(
            () -> new UsernameNotFoundException("파트너가 존재하지 않습니다. -> " + user.getPartnername()));
    CoupleEntity coupleEntity = this.coupleRepository.findById(partner.getCoupleId())
        .orElseThrow(() -> new RuntimeException("존재하지 않는 커플아이디입니다."));
    if (coupleEntity.isAuthorized()) {
      throw new RuntimeException("상대방은 이미 커플입니다.");
    }
    user.setCoupleId(partner.getCoupleId());
    user.setPassword(this.passwordEncoder.encode(user.getPassword()));

    coupleEntity.setAuthorized(true);
    coupleRepository.save(coupleEntity);

    return UserDto.fromEntity(this.userRepository.save(user.toEntity()));
  }

  /**
   * 로그인하는 매서드
   *
   * @param loginUser (로그인 정보)
   * @return 로그인된 회원 정보
   */
  public UserDto authenticate(AuthDto.SignIn loginUser) {
    var user = this.userRepository.findByUsername(loginUser.getUsername())
        .orElseThrow(() -> new RuntimeException("존재하지 않는 ID 입니다."));

    if (!this.passwordEncoder.matches(loginUser.getPassword(), user.getPassword())) {
      throw new RuntimeException("비밀번호가 일치하지 않습니다.");
    }

    CoupleEntity coupleEntity = coupleRepository.findById(user.getCoupleId())
        .orElseThrow(() -> new RuntimeException("커플아이디가 존재하지 않습니다. -> " + user.getCoupleId()));

    if (!coupleEntity.isAuthorized()) {
      throw new RuntimeException("상대방이 가입하지 않았습니다.");
    }

    return UserDto.fromEntity(user);
  }

  @Override
  public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
    return UserDto.fromEntity(this.userRepository.findByUsername(userId)
        .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다.")));
  }

  public UserDto getLoginUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    return UserDto.fromEntity(this.userRepository.findByUsername(userDetails.getUsername())
        .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다.")));
  }

  public String getUsername() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    return userDetails.getUsername();
  }

  public Long getCoupleId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDto userDto = (UserDto) authentication.getPrincipal();
    return userDto.getCoupleId();
  }

  public String getPartnerName(String username) {
    UserEntity user = this.userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));
    UserEntity partner = this.userRepository.findByCoupleIdAndUsernameNot(user.getCoupleId(),
        username).orElseThrow(() -> new RuntimeException("파트너가 존재하지 않습니다."));
    return partner.getUsername();
  }
}
