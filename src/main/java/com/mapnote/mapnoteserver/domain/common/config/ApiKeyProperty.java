package com.mapnote.mapnoteserver.domain.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "api.kakao")
public class ApiKeyProperty {
  private String key;
}
