package com.zerobase.together.service;

import com.zerobase.together.dto.HistoryDto;
import com.zerobase.together.entity.HistoryEntity;
import com.zerobase.together.entity.UserEntity;
import com.zerobase.together.repository.HistoryRepository;
import com.zerobase.together.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HistoryService {

  private final HistoryRepository historyRepository;
  private final UserRepository userRepository;

  public void createHistory(HistoryDto historyDto) {
    this.historyRepository.save(HistoryEntity.builder()
        .coupleId(historyDto.getCoupleId())
        .userId(historyDto.getUserId())
        .targetId(historyDto.getTargetId())
        .postContent(historyDto.getPostContent())
        .commentContent(historyDto.getCommentContent())
        .historyTarget(historyDto.getHistoryTarget())
        .historyAction(historyDto.getHistoryAction())
        .build());
  }

  public List<HistoryDto> readHistory(Integer pageNum) {
    UserEntity user = getLoginUser();
    Pageable pageable = PageRequest.of(pageNum, 10);
    Page<HistoryEntity> result = historyRepository.findAllByCoupleIdOrderByCreatedDateTimeDesc(
        user.getCoupleId(),
        pageable);
    return result.stream().map(HistoryDto::toDto).toList();
  }

  public String shortenContent(String content) {
    if (content.length() < 10) {
      return content;
    }
    return content.substring(0, 10) + "...";
  }

  private UserEntity getLoginUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    return this.userRepository.findByUsername(userDetails.getUsername())
        .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));
  }
}
