package com.zerobase.together.service;

import com.zerobase.together.dto.CommentDto;
import com.zerobase.together.dto.HistoryDto;
import com.zerobase.together.entity.CommentEntity;
import com.zerobase.together.entity.PostEntity;
import com.zerobase.together.entity.UserEntity;
import com.zerobase.together.repository.CommentRepository;
import com.zerobase.together.repository.PostRepository;
import com.zerobase.together.repository.UserRepository;
import com.zerobase.together.type.HistoryAction;
import com.zerobase.together.type.HistoryTarget;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;
  private final UserRepository userRepository;
  private final PostRepository postRepository;
  private final HistoryService historyService;

  public CommentDto createComment(CommentDto request) {
    UserEntity user = getLoginUser();
    PostEntity post = postRepository.findById(request.getPostId())
        .orElseThrow(() -> new RuntimeException("해당 포스트가 존재하지 않습니다."));
    if (user.getCoupleId() != post.getCoupleId()) {
      throw new RuntimeException("댓글 작성 권한이 없습니다.");
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
        .historyTarget(HistoryTarget.COMMENT)
        .historyAction(HistoryAction.CREATE)
        .build());

    return CommentDto.toDto(commentEntity);
  }

  public List<CommentDto> readComments(Long postId) {
    UserEntity user = getLoginUser();
    if (user.getCoupleId() != this.postRepository.findById(postId).get().getCoupleId()) {
      throw new RuntimeException("댓글 조회 권한이 없습니다.");
    }
    return this.commentRepository.getCommentEntitiesByPostId(postId).stream()
        .map(CommentDto::toDto)
        .toList();
  }

  public CommentDto updateComment(Long commentId, CommentDto request) {
    UserEntity user = getLoginUser();
    CommentEntity commentEntity = this.commentRepository.findById(commentId)
        .orElseThrow(() -> new RuntimeException("해당 댓글이 존재하지 않습니다."));
    if (user.getId() != commentEntity.getUserId()) {
      throw new RuntimeException("댓글 수정 권한이 없습니다.");
    }
    commentEntity.setDescription(request.getDescription());

    this.historyService.createHistory(HistoryDto.builder()
        .coupleId(user.getCoupleId())
        .userId(user.getId())
        .targetId(commentEntity.getId())
        .historyTarget(HistoryTarget.COMMENT)
        .historyAction(HistoryAction.UPDATE)
        .build());

    return CommentDto.toDto(this.commentRepository.save(commentEntity));
  }

  public void deleteComment(Long commentId) {
    UserEntity user = getLoginUser();
    CommentEntity commentEntity = this.commentRepository.findById(commentId)
        .orElseThrow(() -> new RuntimeException("해당 댓글이 존재하지 않습니다."));
    if (user.getId() != commentEntity.getUserId()) {
      throw new RuntimeException("댓글 삭제 권한이 없습니다.");
    }

    this.commentRepository.deleteById(commentId);
    this.historyService.createHistory(HistoryDto.builder()
        .coupleId(user.getCoupleId())
        .userId(user.getId())
        .targetId(null)
        .historyTarget(HistoryTarget.COMMENT)
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
