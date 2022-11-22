package com.mapnote.mapnoteserver.log.config;

import com.mapnote.mapnoteserver.log.ControllerLog;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

@Configuration
public class ControllerLogConfig {

  @Bean
  public EmitterProcessor<ControllerLog> controllerLogEmitterProcessor() {
    return EmitterProcessor.create();
  }

  @Bean
  public FluxSink<ControllerLog> controllerLogSink(EmitterProcessor<ControllerLog> controllerLogEmitterProcessor) {
    return controllerLogEmitterProcessor.sink(FluxSink.OverflowStrategy.DROP);
  }

  @Bean
  public Flux<ControllerLog> controllerLogFlux(EmitterProcessor<ControllerLog> controllerLogEmitterProcessor) {
    return controllerLogEmitterProcessor.publishOn(Schedulers.elastic());
  }

}
