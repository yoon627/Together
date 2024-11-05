package com.zerobase.together.controller;

import com.zerobase.together.dto.HistoryDto;
import com.zerobase.together.service.HistoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/history")
@RequiredArgsConstructor
public class HistoryController {

  private final HistoryService historyService;

  @GetMapping("/read")
  public List<HistoryDto> readHistory() {
    var result = this.historyService.readHistory();
    return result;
  }

}
