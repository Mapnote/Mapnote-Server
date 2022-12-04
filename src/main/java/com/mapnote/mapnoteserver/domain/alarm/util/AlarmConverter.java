package com.mapnote.mapnoteserver.domain.alarm.util;

import com.mapnote.mapnoteserver.domain.alarm.dto.AlarmResponse;

public class AlarmConverter {
  public static AlarmResponse.FireBaseToken toTokenDetail(String token) {
    return AlarmResponse.FireBaseToken.builder()
        .token(token)
        .build();
  }
}
