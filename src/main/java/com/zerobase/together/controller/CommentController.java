package com.zerobase.together.controller;

import com.zerobase.together.dto.CommentDto;
import com.zerobase.together.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  @PostMapping("/comment")
  public ResponseEntity<?> createComment(@RequestBody CommentDto request) {
    var result = this.commentService.createComment(request);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/comments/{postId}/{pageNum}")
  public ResponseEntity<?> readComments(@PathVariable Long postId, @PathVariable Integer pageNum) {
    var result = this.commentService.readComments(postId, pageNum);
    return ResponseEntity.ok(result);
  }

  @PutMapping("/comment")
  public ResponseEntity<?> updateComment(@RequestBody CommentDto request) {
    var result = this.commentService.updateComment(request);
    return ResponseEntity.ok(result);
  }

  @DeleteMapping("/comment/{commentId}")
  public void deleteComment(@PathVariable Long commentId) {
    this.commentService.deleteComment(commentId);
  }

}
