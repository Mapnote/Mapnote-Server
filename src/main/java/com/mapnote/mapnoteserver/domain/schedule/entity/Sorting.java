package com.mapnote.mapnoteserver.domain.schedule.entity;

import static com.mapnote.mapnoteserver.domain.schedule.entity.QSchedules.schedules;

import com.querydsl.core.types.OrderSpecifier;

public enum Sorting {
  LATEST {
    @Override
    public OrderSpecifier<?> expression() {
      return schedules.createdAt.desc();
    }
  },
  // TODO : 중요도 생성 시 사용
  IMPORTANCE {
    @Override
    public OrderSpecifier<?> expression() {
      return null;
    }
  };


  public abstract OrderSpecifier<?> expression();

}
