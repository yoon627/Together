package com.zerobase.together.controller;

import com.zerobase.together.dto.PhotoDto;
import com.zerobase.together.service.PhotoService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/photo")
@RequiredArgsConstructor
public class PhotoController {

  private final PhotoService photoService;

  @PostMapping
  public String savePhoto(@RequestParam("file") MultipartFile file) {
    return photoService.save(file);
  }

  @PostMapping("/list")
  public void savePhotos(@RequestParam("files") MultipartFile[] files) {
    photoService.savePhotos(List.of(files));
  }

  @GetMapping
  public Page<PhotoDto> showPhotos(@RequestParam Integer pageNum) {
    return photoService.showPhotos(pageNum);
  }

  @DeleteMapping
  public void deleteFile(@RequestParam("fileName") String fileName) {
    photoService.delete(fileName);
  }
}
