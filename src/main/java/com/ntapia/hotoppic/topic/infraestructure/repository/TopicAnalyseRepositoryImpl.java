package com.ntapia.hotoppic.topic.infraestructure.repository;

import com.ntapia.hotoppic.topic.domain.TopicAnalyse;
import com.ntapia.hotoppic.topic.domain.TopicAnalyseRepository;
import org.springframework.stereotype.Component;

@Component
public class TopicAnalyseRepositoryImpl implements TopicAnalyseRepository {

  private final TopicAnalyseJpaRepository topicAnalyseJpaRepository;

  public TopicAnalyseRepositoryImpl(
      TopicAnalyseJpaRepository topicAnalyseJpaRepository) {
    this.topicAnalyseJpaRepository = topicAnalyseJpaRepository;
  }

  @Override
  public void save(TopicAnalyse topicAnalyse) {
    this.topicAnalyseJpaRepository.save(topicAnalyse);
  }
}
