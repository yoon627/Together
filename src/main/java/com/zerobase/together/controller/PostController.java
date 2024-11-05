package com.zerobase.together.controller;

import com.zerobase.together.dto.PostDto;
import com.zerobase.together.service.PostService;
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
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

  private final PostService postService;

  @PostMapping("/create")
  public ResponseEntity<?> createPost(@RequestBody PostDto request) {
    var result = this.postService.createPost(request);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/read")
  public ResponseEntity<?> readPost(@RequestParam Long postId) {
    var result = this.postService.readPost(postId);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/readAll")
  public ResponseEntity<?> readAllPosts() {
    var result = this.postService.readAllPosts();
    return ResponseEntity.ok(result);
  }

  @PutMapping("/update")
  public ResponseEntity<?> updatePost(@RequestParam Long postId, @RequestBody PostDto request) {
    var result = this.postService.updatePost(postId, request);
    return ResponseEntity.ok(result);
  }

  @DeleteMapping("/delete")
  public void deletePost(@RequestParam Long postId) {
    this.postService.deletePost(postId);
  }

}
