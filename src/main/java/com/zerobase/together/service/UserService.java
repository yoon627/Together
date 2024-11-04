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

    if (this.userRepository.existsByUserId(user.getUserId())) {
      throw new RuntimeException("이미 사용중인 아이디 입니다. -> " + user.getUserId());
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
    if (this.userRepository.existsByUserId(user.getUserId())) {
      throw new RuntimeException("이미 사용중인 아이디 입니다. -> " + user.getUserId());
    }

    UserEntity partner = this.userRepository.findByUserId(user.getPartnerId())
        .orElseThrow(
            () -> new UsernameNotFoundException("파트너가 존재하지 않습니다. -> " + user.getPartnerId()));
    user.setCoupleId(partner.getCoupleId());
    user.setPassword(this.passwordEncoder.encode(user.getPassword()));

    CoupleEntity coupleEntity = coupleRepository.findById(partner.getCoupleId())
        .orElseThrow(() -> new RuntimeException("커플아이디가 존재하지 않습니다. -> " + user.getUserId()));
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
    var user = this.userRepository.findByUserId(loginUser.getUserId())
        .orElseThrow(() -> new RuntimeException("존재하지 않는 ID 입니다."));

    if (!this.passwordEncoder.matches(loginUser.getPassword(), user.getPassword())) {
      throw new RuntimeException("비밀번호가 일치하지 않습니다.");
    }

    CoupleEntity coupleEntity = coupleRepository.findById(user.getCoupleId())
        .orElseThrow(() -> new RuntimeException("커플아이디가 존재하지 않습니다. -> " + user.getUserId()));

    if (!coupleEntity.isAuthorized()) {
      throw new RuntimeException("상대방이 가입하지 않았습니다.");
    }

    return UserDto.fromEntity(user);
  }

  @Override
  public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
    return UserDto.fromEntity(this.userRepository.findByUserId(userId)
        .orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다.")));
  }
}
