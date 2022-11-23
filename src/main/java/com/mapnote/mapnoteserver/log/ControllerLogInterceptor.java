package com.mapnote.mapnoteserver.log;

import java.time.ZonedDateTime;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import reactor.core.publisher.FluxSink;

@Component
public class ControllerLogInterceptor extends HandlerInterceptorAdapter {

  private static final Logger log = LoggerFactory.getLogger(ControllerLogInterceptor.class);

  private final FluxSink<ControllerLog> controllerLogFluxSink;

  public ControllerLogInterceptor(
      FluxSink<ControllerLog> controllerLogFluxSink) {
    this.controllerLogFluxSink = controllerLogFluxSink;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    ControllerLog controllerLog = new ControllerLog(
        request.getMethod(),
        (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE),
        ZonedDateTime.now()
    );

    log.info(controllerLog.toString());
    controllerLogFluxSink.next(controllerLog);
    return super.preHandle(request, response, handler);
  }
}
