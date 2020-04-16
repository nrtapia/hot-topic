package com.ntapia.hotoppic.shared.application;

import com.ntapia.hotoppic.topic.domain.TopicAnalyseCreated;

public interface DomainEventPublisher {

  void publish(TopicAnalyseCreated topicAnalyseCreated);

}
