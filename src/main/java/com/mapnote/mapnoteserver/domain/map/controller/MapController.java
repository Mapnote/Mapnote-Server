package com.mapnote.mapnoteserver.domain.map.controller;

import com.mapnote.mapnoteserver.domain.common.dto.DataResponse;
import com.mapnote.mapnoteserver.domain.map.dto.KakaoApiResponse;
import com.mapnote.mapnoteserver.domain.map.dto.MapRequest;
import com.mapnote.mapnoteserver.domain.map.service.MapService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/map")
public class MapController {

  private final MapService mapService;

  public MapController(MapService mapService){
    this.mapService = mapService;
  }

  @GetMapping("/keyword")
  public ResponseEntity<DataResponse<KakaoApiResponse.KeywordResult>> searchByKeyword(@Validated @RequestBody MapRequest.KeywordQuery keywordQuery){
    KakaoApiResponse.KeywordResult keywordResult = mapService.searchByKeyword(keywordQuery);
    DataResponse<KakaoApiResponse.KeywordResult> response = new DataResponse<>(MapResponseCode.SEARCH_SUCCESS, keywordResult);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @GetMapping("/coordinate")
  public ResponseEntity<DataResponse<KakaoApiResponse.CoordinateResult>> searchCoordinate(@Validated @RequestBody MapRequest.Coordinate coordinate){
    KakaoApiResponse.CoordinateResult coordinateResult = mapService.searchByCoordinate(coordinate);
    DataResponse<KakaoApiResponse.CoordinateResult> response = new DataResponse<>(MapResponseCode.SEARCH_SUCCESS, coordinateResult);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }
}
