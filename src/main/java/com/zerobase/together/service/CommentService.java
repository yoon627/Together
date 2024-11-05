package com.zerobase.together.service;

import com.zerobase.together.dto.CommentDto;
import com.zerobase.together.entity.CommentEntity;
import com.zerobase.together.entity.PostEntity;
import com.zerobase.together.entity.UserEntity;
import com.zerobase.together.repository.CommentRepository;
import com.zerobase.together.repository.PostRepository;
import com.zerobase.together.repository.UserRepository;
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

  public CommentDto createComment(CommentDto request) {
    UserEntity user = getLoginUser();
    PostEntity post = postRepository.findById(request.getPostId())
        .orElseThrow(() -> new RuntimeException("해당 포스트가 존재하지 않습니다."));
    if (user.getCoupleId() != post.getCoupleId()) {
      throw new RuntimeException("댓글 작성 권한이 없습니다.");
    }
    return CommentDto.toDto(this.commentRepository.save(CommentEntity.builder()
        .postId(request.getPostId())
        .coupleId(user.getCoupleId())
        .userId(user.getId())
        .description(request.getDescription())
        .build()));
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
  }

  private UserEntity getLoginUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    return this.userRepository.findByUsername(userDetails.getUsername())
        .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));
  }
}
