package com.mapnote.mapnoteserver.log.config;

import net.gpedro.integrations.slack.SlackApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SlackLogAppenderConfig {

  @Value("${logging.slack.webhook.uri}")
  private String slackWebhookURI;

  @Value("${logging.slack.token}")
  private String token;

  @Bean
  public SlackApi getSlackApi() {
    return new SlackApi(slackWebhookURI + token);
  }
}
