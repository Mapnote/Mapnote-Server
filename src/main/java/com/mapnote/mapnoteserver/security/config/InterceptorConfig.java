package com.mapnote.mapnoteserver.security.config;

import com.mapnote.mapnoteserver.log.ControllerLogInterceptor;
import com.mapnote.mapnoteserver.security.aop.CurrentUserArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

  private final CurrentUserArgumentResolver currentUserArgumentResolver;
  private final ControllerLogInterceptor controllerLogInterceptor;

  public InterceptorConfig(
      CurrentUserArgumentResolver currentUserArgumentResolver,
      ControllerLogInterceptor controllerLogInterceptor) {
    this.currentUserArgumentResolver = currentUserArgumentResolver;
    this.controllerLogInterceptor = controllerLogInterceptor;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(controllerLogInterceptor)
        .addPathPatterns("/**");
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(currentUserArgumentResolver);
  }
}
