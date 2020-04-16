package com.ntapia.hotoppic.shared.infrastructure.event;

import com.ntapia.hotoppic.shared.application.DomainEventPublisher;
import com.ntapia.hotoppic.shared.domain.DomainEvent;
import com.ntapia.hotoppic.topic.domain.TopicAnalyseCreated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DomainEventPublisherImpl implements DomainEventPublisher {

  private static final String PUBLISH_TOPIC_ANALYSE_CREATED_LOG = "Publish topic analyse created id: {}";
  private final ApplicationEventPublisher applicationEventPublisher;

  public DomainEventPublisherImpl(
      ApplicationEventPublisher applicationEventPublisher) {
    this.applicationEventPublisher = applicationEventPublisher;
  }

  @Override
  public void publish(TopicAnalyseCreated topicAnalyseCreated) {
    log.info(PUBLISH_TOPIC_ANALYSE_CREATED_LOG, topicAnalyseCreated.getTopicAnalyse().getId());

    applicationEventPublisher.publishEvent(new DomainEvent<>(this, topicAnalyseCreated));
  }
}
