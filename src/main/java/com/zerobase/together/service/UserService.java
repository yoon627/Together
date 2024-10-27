package com.zerobase.together.service;

import com.zerobase.together.model.Auth;
import com.zerobase.together.model.Auth.SignUpWithPartner;
import com.zerobase.together.persist.CoupleRepository;
import com.zerobase.together.persist.UserRepository;
import com.zerobase.together.persist.entity.CoupleEntity;
import com.zerobase.together.persist.entity.UserEntity;
import java.time.LocalDateTime;
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
   * userId로 회원 정보를 불러오는 메서드
   *
   * @param userId
   * @return 회원 정보
   * @throws UsernameNotFoundException
   */
  @Override
  public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
    return this.userRepository.findByUserId(userId)
        .orElseThrow(() -> new UsernameNotFoundException("유저가 존재하지 않습니다. -> " + userId));
  }

  /**
   * 커플 중 처음 회원 등록하는 메서드
   *
   * @param user (회원에 대한 정보)
   * @return 저장된 회원 정보
   */
  public UserEntity register(Auth.SignUp user) {
    boolean exists = this.userRepository.existsByUserId(user.getUserId());
    if (exists) {
      throw new RuntimeException("이미 사용중인 아이디 입니다. -> " + user.getUserId());
    }

    CoupleEntity coupleEntity = coupleRepository.save(new CoupleEntity());

    user.setCoupleId(coupleEntity.getCoupleId());
    user.setPassword(this.passwordEncoder.encode(user.getPassword()));
    user.setCreatedDate(LocalDateTime.now());

    return this.userRepository.save(user.toEntity());
  }

  /**
   * 상대방이 이미 가입한 상태인 회원 등록하는 메서드
   *
   * @param user (회원에 대한 정보)
   * @return 저장된 회원 정보
   */
  public UserEntity registerWithPartner(SignUpWithPartner user) {
    boolean exists = this.userRepository.existsByUserId(user.getUserId());
    if (exists) {
      throw new RuntimeException("이미 사용중인 아이디 입니다. -> " + user.getUserId());
    }

    UserEntity partner = this.userRepository.findByUserId(user.getPartnerId())
        .orElseThrow(
            () -> new UsernameNotFoundException("파트너가 존재하지 않습니다. -> " + user.getPartnerId()));
    user.setCoupleId(partner.getCoupleId());
    user.setPassword(this.passwordEncoder.encode(user.getPassword()));
    user.setCreatedDate(LocalDateTime.now());

    CoupleEntity coupleEntity = coupleRepository.findById(partner.getCoupleId())
        .orElseThrow(() -> new RuntimeException("커플아이디가 존재하지 않습니다. -> " + user.getUserId()));
    coupleEntity.setAuthorized(true);
    coupleEntity.setCreatedDate(LocalDateTime.now());
    coupleRepository.save(coupleEntity);

    return this.userRepository.save(user.toEntity());
  }

  /**
   * 로그인하는 매서드
   *
   * @param loginUser (로그인 정보)
   * @return 로그인된 회원 정보
   */
  public UserEntity authenticate(Auth.SignIn loginUser) {
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

    return user;
  }

}
