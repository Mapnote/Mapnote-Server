package com.mapnote.mapnoteserver.domain.map.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapnote.mapnoteserver.domain.common.exception.BadRequestException;
import com.mapnote.mapnoteserver.domain.common.exception.ErrorCode;
import com.mapnote.mapnoteserver.domain.map.dto.KakaoApiResponse;
import com.mapnote.mapnoteserver.domain.map.dto.MapRequest;
import com.mapnote.mapnoteserver.domain.map.service.openapi.KakaoApi;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class MapService {

  private final KakaoApi kakaoApi;

  public MapService(KakaoApi kakaoApi){
    this.kakaoApi = kakaoApi;
  }

  public KakaoApiResponse.KeywordResult searchByKeyword(MapRequest.KeywordQuery keywordQuery) {
    ResponseEntity<String> response = kakaoApi.searchByKeyword(keywordQuery);

    return parseBody(response.getBody(), KakaoApiResponse.KeywordResult.class);
  }

  public KakaoApiResponse.CoordinateResult searchByCoordinate(MapRequest.Coordinate coordinate) {
    ResponseEntity<String> response = kakaoApi.searchByCoordinate(coordinate);

    KakaoApiResponse.CoordinateResult documents = parseBody(response.getBody(), KakaoApiResponse.CoordinateResult.class);

    if(documents.getMeta().getTotal_count() == 0) throw new BadRequestException("해당 좌표의 장소 정보는 지원하지 않습니다.", ErrorCode.INVALID_INPUT_VALUE);
    return documents;
  }

  //JSON String -> Places
  private <T> T parseBody (String response, Class<T> dtoClass) {
    try{
      ObjectMapper mapper = new ObjectMapper();
      return mapper.readValue(response, dtoClass);
    } catch (JsonProcessingException e){
      throw new RuntimeException(e);
    }
  }
}
