package com.mapnote.mapnoteserver.log;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

@Component
public class Slf4jControllerLogProcessor {

  private static final Logger log = LoggerFactory.getLogger(Slf4jControllerLogProcessor.class);

  private final Flux<ControllerLog> controllerLogFlux;
  private Disposable disposable;

  public Slf4jControllerLogProcessor(
      Flux<ControllerLog> controllerLogFlux) {
    this.controllerLogFlux = controllerLogFlux;
  }

  @PostConstruct
  public void init() {
    disposable = controllerLogFlux.subscribe(it -> log.info(it.toString()));
  }

  @PreDestroy
  public void destroy() {
    if(disposable != null) disposable.dispose();
  }


}
