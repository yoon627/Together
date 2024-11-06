package com.zerobase.together.controller;

import com.zerobase.together.dto.PostDto;
import com.zerobase.together.service.PostService;
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
public class PostController {

  private final PostService postService;

  @PostMapping("/post")
  public ResponseEntity<?> createPost(@RequestBody PostDto request) {
    var result = this.postService.createPost(request);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/post/{postId}")
  public ResponseEntity<?> readPost(@PathVariable Long postId) {
    var result = this.postService.readPost(postId);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/posts/{pageNum}")
  public ResponseEntity<?> readAllPosts(@PathVariable Integer pageNum) {
    var result = this.postService.readPostPage(pageNum);
    return ResponseEntity.ok(result);
  }

  @PutMapping("/post")
  public ResponseEntity<?> updatePost(@RequestBody PostDto request) {
    var result = this.postService.updatePost(request);
    return ResponseEntity.ok(result);
  }

  @DeleteMapping("/post/{postId}")
  public void deletePost(@PathVariable Long postId) {
    this.postService.deletePost(postId);
  }

}
