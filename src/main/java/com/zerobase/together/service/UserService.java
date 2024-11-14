package com.zerobase.together.service;

import static com.zerobase.together.type.ErrorCode.INVALID_COUPLE_ID;
import static com.zerobase.together.type.ErrorCode.INVALID_PARTNER_NAME;
import static com.zerobase.together.type.ErrorCode.INVALID_USER_NAME;
import static com.zerobase.together.type.ErrorCode.PARTNER_ALREADY_COUPLE;
import static com.zerobase.together.type.ErrorCode.PARTNER_NOT_FOUND;
import static com.zerobase.together.type.ErrorCode.PASSWORD_UN_MATCH;
import static com.zerobase.together.type.ErrorCode.USERNAME_ALREADY_EXISTS;

import com.zerobase.together.dto.AuthDto;
import com.zerobase.together.dto.AuthDto.SignUpWithPartner;
import com.zerobase.together.dto.UserDto;
import com.zerobase.together.entity.CoupleEntity;
import com.zerobase.together.entity.UserEntity;
import com.zerobase.together.exception.CustomException;
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
      throw new CustomException(USERNAME_ALREADY_EXISTS);
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
      throw new CustomException(USERNAME_ALREADY_EXISTS);
    }

    UserEntity partner = this.userRepository.findByUsername(user.getPartnername())
        .orElseThrow(
            () -> new CustomException(INVALID_PARTNER_NAME));
    CoupleEntity coupleEntity = this.coupleRepository.findById(partner.getCoupleId())
        .orElseThrow(() -> new CustomException(INVALID_COUPLE_ID));
    if (coupleEntity.isAuthorized()) {
      throw new CustomException(PARTNER_ALREADY_COUPLE);
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
        .orElseThrow(() -> new CustomException(INVALID_USER_NAME));

    if (!this.passwordEncoder.matches(loginUser.getPassword(), user.getPassword())) {
      throw new CustomException(PASSWORD_UN_MATCH);
    }

    CoupleEntity coupleEntity = coupleRepository.findById(user.getCoupleId())
        .orElseThrow(() -> new CustomException(INVALID_COUPLE_ID));

    if (!coupleEntity.isAuthorized()) {
      throw new CustomException(PARTNER_NOT_FOUND);
    }

    return UserDto.fromEntity(user);
  }

  @Override
  public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
    return UserDto.fromEntity(this.userRepository.findByUsername(userId)
        .orElseThrow(() -> new CustomException(INVALID_USER_NAME)));
  }

  public UserDto getLoginUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    return UserDto.fromEntity(this.userRepository.findByUsername(userDetails.getUsername())
        .orElseThrow(() -> new CustomException(INVALID_USER_NAME)));
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
        .orElseThrow(() -> new CustomException(INVALID_USER_NAME));
    UserEntity partner = this.userRepository.findByCoupleIdAndUsernameNot(user.getCoupleId(),
        username).orElseThrow(() -> new CustomException(PARTNER_NOT_FOUND));
    return partner.getUsername();
  }
}
