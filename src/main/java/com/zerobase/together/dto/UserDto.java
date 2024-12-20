package com.zerobase.together.dto;

import com.zerobase.together.entity.UserEntity;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto implements UserDetails {

  private Long id;
  private Long coupleId;
  private String username;


  public static UserDto fromEntity(UserEntity userEntity) {
    return UserDto.builder()
        .id(userEntity.getId())
        .coupleId(userEntity.getCoupleId())
        .username(userEntity.getUsername())
        .build();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

  @Override
  public String getPassword() {
    return "";
  }

  @Override
  public String getUsername() {
    return this.username;
  }
}
