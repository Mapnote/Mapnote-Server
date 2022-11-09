package com.mapnote.mapnoteserver.domain.map.service.openapi;

import com.mapnote.mapnoteserver.domain.map.dto.MapRequest;
import com.mapnote.mapnoteserver.domain.map.dto.MapRequest.Coordinate;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class KakaoApiService {

  private static final String AUTHORIZATION = "Authorization";
  private static final String BASE_URL = "https://dapi.kakao.com/";
  private static final String KEYWORD_PATH = "v2/local/search/keyword.json";
  private static final String COORDINATE_PATH = "v2/local/geo/coord2address.json";

  // Authorization header의 body에 api key 앞에 붙이는 string
  private static final String API_KEY_PREFIX = "KakaoAK ";

  @Value("${api.kakao.key}")
  private String restApiKey;

  public ResponseEntity<String> searchByKeyword(MapRequest.KeywordQuery keywordQuery) {
    //uri 설정
    URI uri = UriComponentsBuilder
        .fromUriString(BASE_URL)
        .path(KEYWORD_PATH)
        .queryParam("query", keywordQuery.getQuery())
        .queryParam("y", keywordQuery.getY())
        .queryParam("x", keywordQuery.getX())
        .queryParam("radius", keywordQuery.getRadius())
        .encode()
        .build()
        .toUri();

    return sendRequest(uri);
  }

  public ResponseEntity<String> searchByCoordinate(Coordinate coordinate) {
    //uri 설정
    URI uri = UriComponentsBuilder
        .fromUriString(BASE_URL)
        .path(COORDINATE_PATH)
        .queryParam("y", coordinate.getY())
        .queryParam("x", coordinate.getX())
        .encode()
        .build()
        .toUri();

    //header 설정
    return sendRequest(uri);
  }

  private ResponseEntity<String> sendRequest(URI uri) {
    //header 설정
    HttpHeaders headers = new HttpHeaders();
    headers.set(AUTHORIZATION, API_KEY_PREFIX + restApiKey);
    MediaType mediaType = new MediaType("application", "json", StandardCharsets.UTF_8);
    headers.setContentType(mediaType);

    //kakao api로 request
    RequestEntity<String> request = new RequestEntity<>(headers, HttpMethod.GET, uri);
    RestTemplate restTemplate = new RestTemplate();
    return restTemplate.exchange(
        uri,
        HttpMethod.GET,
        request,
        String.class
    );
  }
}
