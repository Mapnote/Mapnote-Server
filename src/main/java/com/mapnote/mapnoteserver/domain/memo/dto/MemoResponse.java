package com.mapnote.mapnoteserver.domain.memo.dto;

import com.mapnote.mapnoteserver.domain.memo.entity.Memo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

public class MemoResponse {

  @Getter
  @Builder
  @AllArgsConstructor
  public static class MemoLoadResponse {
    private String email;
    private String name;
    private Long boundary;
    private List<Memo> memoList;
  }

  @Getter
  @AllArgsConstructor
  public static class MemoSaveResponse {
    private Long id;
  }

}
