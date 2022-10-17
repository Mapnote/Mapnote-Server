package com.mapnote.mapnoteserver.security.redis;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@PropertySource("classpath:application.yml")
public class RedisProperties {

  @Value("${spring.redis.host}")
  private String host;

  @Value("${spring.redis.port}")
  private Integer port;

}
