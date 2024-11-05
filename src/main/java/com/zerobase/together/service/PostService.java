package com.zerobase.together.service;

import com.zerobase.together.dto.PostDto;
import com.zerobase.together.entity.PostEntity;
import com.zerobase.together.entity.UserEntity;
import com.zerobase.together.repository.PostRepository;
import com.zerobase.together.repository.UserRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class PostService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;

  public PostDto createPost(PostDto post) {
    return PostDto.toEntity(this.postRepository.save(PostEntity.builder()
        .coupleId(post.getCoupleId())
        .userId(post.getUserId())
        .imgUrl(post.getImgUrl())
        .description(post.getDescription())
        .build()
    ));
  }

  public List<PostDto> readPost() {
    String userId = getLoginUserId();
    UserEntity user = this.userRepository.findByUserId(userId)
        .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));
    return this.postRepository.getPostEntitiesByCoupleId(user.getCoupleId()).stream()
        .map(PostDto::toEntity).toList();
  }

  public PostDto updatePost(Long postId, PostDto post) {
    String userId = getLoginUserId();
    UserEntity user = this.userRepository.findByUserId(userId)
        .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));
    PostEntity postEntity = this.postRepository.findById(postId)
        .orElseThrow(() -> new RuntimeException("해당 포스트가 존재하지 않습니다."));
    if (!postEntity.getUserId().equals(user.getId())) {
      throw new RuntimeException("포스트 작성자가 아닙니다.");
    }
    postEntity.setImgUrl(post.getImgUrl());
    postEntity.setDescription(post.getDescription());
    return PostDto.toEntity(this.postRepository.save(postEntity));
  }

  public void deletePost(Long postId) {
    //TODO update와 로그인 유저 확인하는 부분 중복없애는 방법 고민
    String userId = getLoginUserId();
    UserEntity user = this.userRepository.findByUserId(userId)
        .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));
    PostEntity postEntity = this.postRepository.findById(postId)
        .orElseThrow(() -> new RuntimeException("해당 포스트가 존재하지 않습니다."));
    System.out.println(postEntity.getId());
    if (!postEntity.getUserId().equals(user.getId())) {
      throw new RuntimeException("포스트 작성자가 아닙니다.");
    }
    this.postRepository.deleteById(postId);
  }

  private String getLoginUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    return userDetails.getUsername();
  }
}
