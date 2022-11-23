package com.mapnote.mapnoteserver.log;

import java.time.Duration;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Component
public class EsControllerLogProcessor {

  private static final Logger log = LoggerFactory.getLogger(EsControllerLogProcessor.class);

  private final Flux<ControllerLog> controllerLogFlux;
  private final EsLogSender esLogSender;

  private Disposable disposable;

  public EsControllerLogProcessor(
      Flux<ControllerLog> controllerLogFlux, EsLogSender esLogSender) {
    this.controllerLogFlux = controllerLogFlux;
    this.esLogSender = esLogSender;
  }

  @PostConstruct
  public void init() {
    disposable = controllerLogFlux.bufferTimeout(1000, Duration.ofSeconds(60), Schedulers.elastic())
        .filter(it -> !CollectionUtils.isEmpty(it))
        .subscribe(esLogSender::send);
  }

  @PreDestroy
  public void destroy() {
    if (disposable != null) {
      disposable.dispose();
    }
  }

}
