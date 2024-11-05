package com.zerobase.together.service;

import com.zerobase.together.dto.HistoryDto;
import com.zerobase.together.dto.PostDto;
import com.zerobase.together.entity.PostEntity;
import com.zerobase.together.entity.UserEntity;
import com.zerobase.together.repository.PostRepository;
import com.zerobase.together.repository.UserRepository;
import com.zerobase.together.type.HistoryAction;
import com.zerobase.together.type.HistoryTarget;
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
  private final HistoryService historyService;

  public PostDto createPost(PostDto post) {
    UserEntity user = getLoginUser();
    PostEntity postEntity = this.postRepository.save(PostEntity.builder()
        .coupleId(user.getCoupleId())
        .userId(user.getId())
        .imgUrl(post.getImgUrl())
        .description(post.getDescription())
        .build());
    this.historyService.createHistory(HistoryDto.builder()
        .coupleId(user.getCoupleId())
        .userId(user.getId())
        .targetId(postEntity.getId())
        .historyTarget(HistoryTarget.POST)
        .historyAction(HistoryAction.CREATE)
        .build());
    return PostDto.toDto(postEntity);
  }

  public PostDto readPost(Long postId) {
    UserEntity user = getLoginUser();
    PostEntity postEntity = this.postRepository.findById(postId)
        .orElseThrow(() -> new RuntimeException("해당 포스트가 존재하지 않습니다."));
    if (postEntity.getCoupleId() != user.getCoupleId()) {
      throw new RuntimeException("포스트를 읽을 권한이 없습니다.");
    }
    return PostDto.toDto(postEntity);
  }

  public List<PostDto> readAllPosts() {
    UserEntity user = getLoginUser();
    return this.postRepository.getPostEntitiesByCoupleId(user.getCoupleId()).stream()
        .map(PostDto::toDto).toList();
  }

  public PostDto updatePost(Long postId, PostDto post) {
    UserEntity user = getLoginUser();
    PostEntity postEntity = this.postRepository.findById(postId)
        .orElseThrow(() -> new RuntimeException("해당 포스트가 존재하지 않습니다."));
    if (postEntity.getUserId() != user.getId()) {
      throw new RuntimeException("포스트 작성자가 아닙니다.");
    }
    postEntity.setImgUrl(post.getImgUrl());
    postEntity.setDescription(post.getDescription());
    this.historyService.createHistory(HistoryDto.builder()
        .coupleId(user.getCoupleId())
        .userId(user.getId())
        .targetId(postEntity.getId())
        .historyTarget(HistoryTarget.POST)
        .historyAction(HistoryAction.UPDATE)
        .build());
    return PostDto.toDto(this.postRepository.save(postEntity));
  }

  public void deletePost(Long postId) {
    UserEntity user = getLoginUser();
    PostEntity postEntity = this.postRepository.findById(postId)
        .orElseThrow(() -> new RuntimeException("해당 포스트가 존재하지 않습니다."));
    if (user.getId() != postEntity.getUserId()) {
      throw new RuntimeException("포스트 작성자가 아닙니다.");
    }

    this.postRepository.deleteById(postId);
    this.historyService.createHistory(HistoryDto.builder()
        .coupleId(user.getCoupleId())
        .userId(user.getId())
        .targetId(null)
        .historyTarget(HistoryTarget.POST)
        .historyAction(HistoryAction.DELETE)
        .build());
  }

  private UserEntity getLoginUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    return this.userRepository.findByUsername(userDetails.getUsername())
        .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));
  }
}
