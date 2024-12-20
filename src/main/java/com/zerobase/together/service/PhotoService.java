package com.zerobase.together.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.zerobase.together.dto.PhotoDto;
import com.zerobase.together.dto.UserDto;
import com.zerobase.together.entity.PhotoEntity;
import com.zerobase.together.repository.PhotoRepository;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PhotoService {

  private final PhotoRepository photoRepository;
  private final AmazonS3Client amazonS3Client;
  private final UserService userService;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;
  @Value("${cloud.aws.region.static}")
  private String region;

  private String urlPrefix;

  @PostConstruct
  public void init() {
    urlPrefix = "https://" + bucket + ".s3." + region + ".amazonaws.com/";
  }

  @Transactional
  public String save(MultipartFile file) {
    UserDto user = this.userService.getLoginUser();
    try {
      String fileName = file.getOriginalFilename();
      ObjectMetadata metadata = new ObjectMetadata();
      metadata.setContentType(file.getContentType());
      metadata.setContentLength(file.getSize());
      if (amazonS3Client.doesObjectExist(bucket, user.getCoupleId() + "/" + fileName)) {
        throw new ResponseStatusException(HttpStatus.CONFLICT, "File already exists");
      }
      this.amazonS3Client.putObject(bucket, user.getCoupleId() + "/" + fileName,
          file.getInputStream(),
          metadata);
      this.photoRepository.save(PhotoEntity.builder()
          .coupleId(user.getCoupleId())
          .userId(user.getId())
          .imgUrl(user.getCoupleId() + "/" + fileName)
          .build());
      return urlPrefix + user.getCoupleId() + "/" + fileName;
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }

  @Transactional
  public void savePhotos(List<MultipartFile> files) {
    for (MultipartFile file : files) {
      this.save(file);
    }
  }

  public Page<PhotoDto> showPhotos(Integer pageNum) {
    Pageable pageable = PageRequest.of(pageNum, 10);
    return this.photoRepository.findAllByCoupleIdOrderByCoupleIdDesc(
            this.userService.getCoupleId(), pageable)
        .map(photoEntity -> PhotoDto.toDto(photoEntity, urlPrefix));
  }

  @Transactional
  public void delete(String fileUrl) {
    this.amazonS3Client.deleteObject(bucket, fileUrl.substring(urlPrefix.length()));
    this.photoRepository.deleteByImgUrl(fileUrl.substring(urlPrefix.length()));
  }

}
