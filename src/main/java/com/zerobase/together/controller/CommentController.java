package com.zerobase.together.controller;

import com.zerobase.together.dto.CommentDto;
import com.zerobase.together.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  @PostMapping("/create")
  public ResponseEntity<?> createComment(@RequestBody CommentDto request) {
    var result = this.commentService.createComment(request);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/read")
  public ResponseEntity<?> readComments(@RequestParam Long postId) {
    var result = this.commentService.readComments(postId);
    return ResponseEntity.ok(result);
  }

  @PutMapping("/update")
  public ResponseEntity<?> updateComment(@RequestParam Long commentId,
      @RequestBody CommentDto request) {
    var result = this.commentService.updateComment(commentId, request);
    return ResponseEntity.ok(result);
  }

  @DeleteMapping("/delete")
  public void deleteComment(@RequestParam Long commentId) {
    this.commentService.deleteComment(commentId);
  }

}
