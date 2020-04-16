package com.ntapia.hotoppic.shared.infrastructure.event;

import java.util.concurrent.Executors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;

@Configuration
public class AsynchronousSpringEventsConfig {

  @Value("${events.poolSize}")
  private int poolSize;

  @Bean(name = "applicationEventMulticaster")
  public ApplicationEventMulticaster simpleApplicationEventMulticaster() {
    final SimpleApplicationEventMulticaster simpleApplicationEventMulticaster
        = new SimpleApplicationEventMulticaster();

    simpleApplicationEventMulticaster.setTaskExecutor(Executors.newFixedThreadPool(poolSize));

    return simpleApplicationEventMulticaster;
  }

}