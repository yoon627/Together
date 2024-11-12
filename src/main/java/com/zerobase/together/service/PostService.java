package com.zerobase.together.service;

import com.zerobase.together.dto.HistoryDto;
import com.zerobase.together.dto.PostDto;
import com.zerobase.together.dto.UserDto;
import com.zerobase.together.entity.PostEntity;
import com.zerobase.together.repository.PostRepository;
import com.zerobase.together.type.HistoryAction;
import com.zerobase.together.type.HistoryTarget;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class PostService {

  private final PostRepository postRepository;
  private final HistoryService historyService;
  private final UserService userService;

  @Transactional
  public PostDto createPost(PostDto post) {
    UserDto user = this.userService.getLoginUser();
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
        .postContent(this.historyService.shortenContent(postEntity.getDescription()))
        .historyTarget(HistoryTarget.POST)
        .historyAction(HistoryAction.CREATE)
        .build());
    return PostDto.toDto(postEntity);
  }

  public PostDto readPost(Long postId) {
    UserDto user = this.userService.getLoginUser();
    PostEntity postEntity = this.postRepository.findById(postId)
        .orElseThrow(() -> new RuntimeException("해당 포스트가 존재하지 않습니다."));
    if (postEntity.getCoupleId() != user.getCoupleId()) {
      throw new RuntimeException("포스트를 읽을 권한이 없습니다.");
    }
    if (postEntity.getDeletedDateTime() != null) {
      throw new RuntimeException("삭제된 게시글입니다.");
    }
    return PostDto.toDto(postEntity);
  }

  public List<PostDto> readPostPage(int pageNum) {
    UserDto user = this.userService.getLoginUser();
    Pageable pageable = PageRequest.of(pageNum, 10);
    Page<PostEntity> result = this.postRepository.findAllByCoupleIdAndDeletedDateTimeOrderByCreatedDateTimeDesc(
        user.getCoupleId(), null, pageable);
    return result.stream().map(PostDto::toDto).toList();
  }

  @Transactional
  public PostDto updatePost(PostDto post) {
    UserDto user = this.userService.getLoginUser();
    PostEntity postEntity = this.postRepository.findById(post.getPostId())
        .orElseThrow(() -> new RuntimeException("해당 포스트가 존재하지 않습니다."));
    if (postEntity.getUserId() != user.getId()) {
      throw new RuntimeException("포스트 작성자가 아닙니다.");
    }
    if (postEntity.getDeletedDateTime() != null) {
      throw new RuntimeException("삭제된 게시글입니다.");
    }
    postEntity.setImgUrl(post.getImgUrl());
    postEntity.setDescription(post.getDescription());
    this.historyService.createHistory(HistoryDto.builder()
        .coupleId(user.getCoupleId())
        .userId(user.getId())
        .targetId(postEntity.getId())
        .postContent(this.historyService.shortenContent(postEntity.getDescription()))
        .historyTarget(HistoryTarget.POST)
        .historyAction(HistoryAction.UPDATE)
        .build());
    return PostDto.toDto(this.postRepository.save(postEntity));
  }

  @Transactional
  public void deletePost(Long postId) {
    UserDto user = this.userService.getLoginUser();
    PostEntity postEntity = this.postRepository.findById(postId)
        .orElseThrow(() -> new RuntimeException("해당 포스트가 존재하지 않습니다."));
    if (user.getId() != postEntity.getUserId()) {
      throw new RuntimeException("포스트 작성자가 아닙니다.");
    }
    if (postEntity.getDeletedDateTime() != null) {
      throw new RuntimeException("삭제된 게시글입니다.");
    }
    postEntity.setDeletedDateTime(LocalDateTime.now());
    this.postRepository.save(postEntity);
    this.historyService.createHistory(HistoryDto.builder()
        .coupleId(user.getCoupleId())
        .userId(user.getId())
        .targetId(postEntity.getId())
        .postContent(this.historyService.shortenContent(postEntity.getDescription()))
        .historyTarget(HistoryTarget.POST)
        .historyAction(HistoryAction.DELETE)
        .build());
  }
}
