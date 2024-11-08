package com.zerobase.together.controller;

import com.zerobase.together.dto.HistoryDto;
import com.zerobase.together.service.HistoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HistoryController {

  private final HistoryService historyService;

  @GetMapping("/historys/{pageNum}")
  public List<HistoryDto> readHistory(@PathVariable Integer pageNum) {
    var result = this.historyService.readHistory(pageNum);
    return result;
  }

}
