package com.mapnote.mapnoteserver.log.config;

import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories
public class ElasticsearchConfig extends AbstractElasticsearchConfig {

  private final ElasticsearchProperties elasticsearchProperties;

  public ElasticsearchConfig(ElasticsearchProperties elasticsearchProperties) {
    this.elasticsearchProperties = elasticsearchProperties;
  }

  @Override
  public RestHighLevelClient elasticsearchClient() {
    return new RestHighLevelClient(RestClient.builder(elasticsearchProperties.httpHost()));
  }
}
