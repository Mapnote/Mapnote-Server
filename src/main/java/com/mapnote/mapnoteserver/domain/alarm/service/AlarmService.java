package com.mapnote.mapnoteserver.domain.alarm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.mapnote.mapnoteserver.domain.alarm.dto.AlarmRequest;
import com.mapnote.mapnoteserver.domain.alarm.dto.AlarmResponse;
import com.mapnote.mapnoteserver.domain.alarm.dto.FcmMessage;
import com.mapnote.mapnoteserver.domain.alarm.util.AlarmConverter;
import com.mapnote.mapnoteserver.domain.schedule.repository.ScheduleRepository;
import com.mapnote.mapnoteserver.domain.user.repository.UserRepository;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class AlarmService {
  @Value("${firebase.message.url}")
  private String API_URL;
  private final ObjectMapper objectMapper;
  private final UserRepository userRepository;
  private final ScheduleRepository scheduleRepository;
  public AlarmService(
      ObjectMapper objectMapper,
      ScheduleRepository scheduleRepository,
      UserRepository userRepository) {
    this.objectMapper = objectMapper;
    this.scheduleRepository=scheduleRepository;
    this.userRepository=userRepository;
  }

  public AlarmResponse.FireBaseToken getAccessToken() throws IOException {
    String firebaseConfigPath = "firebase/fcm_key.json";
    GoogleCredentials googleCredentials = GoogleCredentials
        .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
        .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
    googleCredentials.refreshIfExpired();
    System.out.println(googleCredentials.getAccessToken().getTokenValue());
    String token=googleCredentials.getAccessToken().getTokenValue();
    return AlarmConverter.toTokenDetail(token);
  }
  private String makeMessage(String token, String title, String body) throws JsonProcessingException {
    FcmMessage fcmMessage = FcmMessage.builder()
        .message(FcmMessage.Message.builder()
            .token(token)
            .notification(FcmMessage.Notification.builder()
                .title(title)
                .body(body)
                .image(null)
                .build()
            )
            .build()
        )
        .validate_only(false)
        .build();
    return objectMapper.writeValueAsString(fcmMessage);
  }

  public void sendMessageTo(String token, String title, String body) throws IOException {
    String message = makeMessage(token, title, body);
    OkHttpClient client = new OkHttpClient();
    RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
    Request request = new Request.Builder()
        .url(API_URL)
        .post(requestBody)
        .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
        .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
        .build();
    Response response = client.newCall(request)
        .execute();
    System.out.println(response.body().string());
  }

//  public void pushAlarm(UUID uuid, AlarmRequest.CurLocation curLocation){}
  public void pushAlarm(AlarmRequest.CurLocation curLocation) throws IOException {
    sendMessageTo(curLocation.getToken(),"test","test");
  }


}
