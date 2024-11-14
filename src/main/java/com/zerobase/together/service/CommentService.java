package com.zerobase.together.service;

import static com.zerobase.together.type.ErrorCode.INVALID_COMMENT;
import static com.zerobase.together.type.ErrorCode.INVALID_POST;
import static com.zerobase.together.type.ErrorCode.UNAUTHORIZED;

import com.zerobase.together.dto.CommentDto;
import com.zerobase.together.dto.HistoryDto;
import com.zerobase.together.dto.UserDto;
import com.zerobase.together.entity.CommentEntity;
import com.zerobase.together.entity.PostEntity;
import com.zerobase.together.exception.AuthorityException;
import com.zerobase.together.exception.CustomException;
import com.zerobase.together.repository.CommentRepository;
import com.zerobase.together.repository.PostRepository;
import com.zerobase.together.type.HistoryAction;
import com.zerobase.together.type.HistoryTarget;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;
  private final PostRepository postRepository;
  private final UserService userService;
  private final HistoryService historyService;

  @Transactional
  public CommentDto createComment(CommentDto request) {
    UserDto user = this.userService.getLoginUser();
    PostEntity postEntity = postRepository.findById(request.getPostId())
        .orElseThrow(() -> new CustomException(INVALID_POST));
    if (user.getCoupleId() != postEntity.getCoupleId()) {
      throw new AuthorityException(UNAUTHORIZED);
    }
    if (postEntity.getDeletedDateTime() != null) {
      throw new CustomException(INVALID_POST);
    }

    CommentEntity commentEntity = this.commentRepository.save(CommentEntity.builder()
        .postId(request.getPostId())
        .coupleId(user.getCoupleId())
        .userId(user.getId())
        .description(request.getDescription())
        .build());

    this.historyService.createHistory(HistoryDto.builder()
        .coupleId(user.getCoupleId())
        .userId(user.getId())
        .targetId(commentEntity.getId())
        .postContent(this.historyService.shortenContent(postEntity.getDescription()))
        .commentContent(this.historyService.shortenContent(commentEntity.getDescription()))
        .historyTarget(HistoryTarget.COMMENT)
        .historyAction(HistoryAction.CREATE)
        .build());

    return CommentDto.toDto(commentEntity);
  }

  public List<CommentDto> readComments(Long postId, Integer pageNum) {
    UserDto user = this.userService.getLoginUser();
    if (user.getCoupleId() != this.postRepository.findById(postId).get().getCoupleId()) {
      throw new AuthorityException(UNAUTHORIZED);
    }

    Pageable pageable = PageRequest.of(pageNum, 10);
    Page<CommentEntity> result = this.commentRepository.findAllByPostIdAndDeletedDateTimeOrderByCreatedDateTimeDesc(
        postId, null, pageable);

    return result.stream().map(CommentDto::toDto).toList();
  }

  @Transactional
  public CommentDto updateComment(CommentDto request) {
    UserDto user = this.userService.getLoginUser();
    CommentEntity commentEntity = this.commentRepository.findById(request.getCommentId())
        .orElseThrow(() -> new CustomException(INVALID_COMMENT));
    if (user.getId() != commentEntity.getUserId()) {
      throw new AuthorityException(UNAUTHORIZED);
    }
    if (commentEntity.getDeletedDateTime() != null) {
      throw new CustomException(INVALID_COMMENT);
    }
    commentEntity.setDescription(request.getDescription());
    PostEntity postEntity = this.postRepository.findById(commentEntity.getPostId())
        .orElseThrow(() -> new CustomException(INVALID_POST));
    this.historyService.createHistory(HistoryDto.builder()
        .coupleId(user.getCoupleId())
        .userId(user.getId())
        .targetId(commentEntity.getId())
        .postContent(historyService.shortenContent(postEntity.getDescription()))
        .commentContent(historyService.shortenContent(commentEntity.getDescription()))
        .historyTarget(HistoryTarget.COMMENT)
        .historyAction(HistoryAction.UPDATE)
        .build());

    return CommentDto.toDto(this.commentRepository.save(commentEntity));
  }

  @Transactional
  public void deleteComment(Long commentId) {
    UserDto user = this.userService.getLoginUser();
    CommentEntity commentEntity = this.commentRepository.findById(commentId)
        .orElseThrow(() -> new CustomException(INVALID_COMMENT));
    if (user.getId() != commentEntity.getUserId()) {
      throw new AuthorityException(UNAUTHORIZED);
    }
    if (commentEntity.getDeletedDateTime() != null) {
      throw new CustomException(INVALID_COMMENT);
    }
    PostEntity postEntity = this.postRepository.findById(commentEntity.getPostId())
        .orElseThrow(() -> new CustomException(INVALID_POST));
    commentEntity.setDeletedDateTime(LocalDateTime.now());
    this.commentRepository.save(commentEntity);
    this.historyService.createHistory(HistoryDto.builder()
        .coupleId(user.getCoupleId())
        .userId(user.getId())
        .targetId(commentEntity.getId())
        .postContent(historyService.shortenContent(postEntity.getDescription()))
        .commentContent(historyService.shortenContent(commentEntity.getDescription()))
        .historyTarget(HistoryTarget.COMMENT)
        .historyAction(HistoryAction.DELETE)
        .build());
  }

}
