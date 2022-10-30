package com.mapnote.mapnoteserver.domain.map.controller;

import com.mapnote.mapnoteserver.domain.common.dto.DataResponse;
import com.mapnote.mapnoteserver.domain.common.dto.ErrorResponse;
import com.mapnote.mapnoteserver.domain.map.dto.KakaoApiResponse;
import com.mapnote.mapnoteserver.domain.map.dto.MapRequest;
import com.mapnote.mapnoteserver.domain.map.service.MapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "map", description = "장소 검색 API")
@RestController
@RequestMapping("/api/v1/map")
public class MapController {

  private final MapService mapService;

  public MapController(MapService mapService) {
    this.mapService = mapService;
  }

  @Operation(summary = "search by keyword", description = "검색 키워드로 장소 정보 검색")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @GetMapping("/keyword")
  public ResponseEntity<DataResponse<KakaoApiResponse.KeywordResult>> searchByKeyword(
      @Validated @ModelAttribute MapRequest.KeywordQuery keywordQuery) {

    KakaoApiResponse.KeywordResult keywordResult = mapService.searchByKeyword(keywordQuery);
    DataResponse<KakaoApiResponse.KeywordResult> response = new DataResponse<>(
        MapResponseCode.SEARCH_SUCCESS, keywordResult);

    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @Operation(summary = "search by coordinate", description = "좌표로 장소 정보 검색")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @GetMapping("/coordinate")
  public ResponseEntity<DataResponse<KakaoApiResponse.CoordinateResult>> searchCoordinate(
      @Validated @ModelAttribute MapRequest.Coordinate coordinate) {

    KakaoApiResponse.CoordinateResult coordinateResult = mapService.searchByCoordinate(coordinate);
    DataResponse<KakaoApiResponse.CoordinateResult> response = new DataResponse<>(
        MapResponseCode.SEARCH_SUCCESS, coordinateResult);

    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }
}
