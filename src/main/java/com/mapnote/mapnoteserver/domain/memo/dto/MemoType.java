package com.mapnote.mapnoteserver.domain.memo.dto;

import lombok.Getter;

@Getter
public enum MemoType {
    MEAL(0), DATE(1);
    private Integer code;
    MemoType(Integer code){
        this.code=code;
    }
}
