package com.mapnote.mapnoteserver.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapnote.mapnoteserver.log.config.ElasticsearchConfig;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EsLogSender {

  private static final Logger log = LoggerFactory.getLogger(EsLogSender.class);
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

  private final ElasticsearchConfig elasticsearchConfig;

  private final RestHighLevelClient client;
  private final ObjectMapper objectMapper;

  public EsLogSender(RestHighLevelClient client,
      ElasticsearchConfig elasticsearchConfig,
      ObjectMapper objectMapper) {
    this.elasticsearchConfig = elasticsearchConfig;
    this.client = this.elasticsearchConfig.elasticsearchClient();
    this.objectMapper = objectMapper;
  }

  public void send(List<ControllerLog> list) {
    try {
      String indexName = "controller-log-" + LocalDateTime.now().format(FORMATTER);
      BulkRequest request = new BulkRequest();

      for (ControllerLog controllerLog : list) {
        request.add(new IndexRequest(indexName, "doc").source(objectMapper.writeValueAsBytes(controllerLog), XContentType.JSON));
      }
//      RestHighLevelClient client = new RestHighLevelClient();
      client.bulkAsync(request, RequestOptions.DEFAULT, new ActionListener<BulkResponse>() {
        @Override
        public void onResponse(BulkResponse bulkItemResponses) {
          log.debug("Send to logs to ES");
        }
        @Override
        public void onFailure(Exception e) {
          log.warn("Exception in send to logs to ES", e);
        }
      });
    } catch (Exception e) {
      log.warn("Exception is send to logs to ES", e);
    }
  }

}
