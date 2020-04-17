package com.ntapia.hotoppic.topic.infraestructure.event;

import com.ntapia.hotoppic.shared.domain.DomainEvent;
import com.ntapia.hotoppic.topic.application.extractor.TopicAnalyseExtractor;
import com.ntapia.hotoppic.topic.application.extractor.TopicAnalyseExtractorRequest;
import com.ntapia.hotoppic.topic.domain.TopicAnalyseCreated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TopicAnalyseCreatedListener {

  private static final String LISTENER_TOPIC_ANALYSE_CREATED_LOG
      = "Listener topic analyse created id: {}";

  private final TopicAnalyseExtractor topicAnalyseExtractor;

  public TopicAnalyseCreatedListener(
      TopicAnalyseExtractor topicAnalyseExtractor) {
    this.topicAnalyseExtractor = topicAnalyseExtractor;
  }

  @EventListener
  public void handle(final DomainEvent<TopicAnalyseCreated> domainEvent) {
    log.info(LISTENER_TOPIC_ANALYSE_CREATED_LOG,
        domainEvent.getData().getTopicAnalyse().getId());

    this.topicAnalyseExtractor.extractAndAnalyse(
        new TopicAnalyseExtractorRequest(domainEvent.getData().getTopicAnalyse()));
  }
}
