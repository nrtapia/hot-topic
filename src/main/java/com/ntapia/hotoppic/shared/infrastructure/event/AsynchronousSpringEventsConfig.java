package com.ntapia.hotoppic.shared.infrastructure.event;

import java.util.concurrent.Executors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

@Configuration
public class AsynchronousSpringEventsConfig {

  private static final String THREAD_NAME_PREFIX = "events-";

  @Value("${events.poolSize}")
  private int poolSize;

  @Bean(name = "applicationEventMulticaster")
  public ApplicationEventMulticaster simpleApplicationEventMulticaster() {
    final SimpleApplicationEventMulticaster simpleApplicationEventMulticaster
        = new SimpleApplicationEventMulticaster();

    CustomizableThreadFactory threadFactory = new CustomizableThreadFactory(THREAD_NAME_PREFIX);

    simpleApplicationEventMulticaster
        .setTaskExecutor(Executors.newFixedThreadPool(poolSize, threadFactory));

    return simpleApplicationEventMulticaster;
  }

}