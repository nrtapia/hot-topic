package com.ntapia.hotoppic.topic.infraestructure.repository;

import com.ntapia.hotoppic.topic.domain.RssItem;
import com.ntapia.hotoppic.topic.domain.TopicAnalyse;
import com.ntapia.hotoppic.topic.domain.TopicAnalyseRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class TopicAnalyseRepositoryImpl implements TopicAnalyseRepository {

  private final TopicAnalyseJpaRepository topicAnalyseJpaRepository;
  private final RssItemJpaRepository rssItemJpaRepository;

  public TopicAnalyseRepositoryImpl(
      TopicAnalyseJpaRepository topicAnalyseJpaRepository,
      RssItemJpaRepository rssItemJpaRepository) {
    this.topicAnalyseJpaRepository = topicAnalyseJpaRepository;
    this.rssItemJpaRepository = rssItemJpaRepository;
  }

  @Override
  public TopicAnalyse save(TopicAnalyse topicAnalyse) {
    return this.topicAnalyseJpaRepository.save(topicAnalyse);
  }

  @Override
  public Optional<TopicAnalyse> findById(String id) {
    return topicAnalyseJpaRepository.findById(id);
  }

  @Override
  public void updateRssItems(Long rssId, List<RssItem> rssItems) {
    topicAnalyseJpaRepository.updateRssItems(rssId, rssItems);
  }

  @Override
  public void saveAllRssItems(List<RssItem> rssItems) {
    rssItemJpaRepository.saveAll(rssItems);
  }

  @Override
  public List<RssItem> findItemsByTopicAndLimit(int hotTopicCount) {

    return rssItemJpaRepository.findAll(
        PageRequest.of(0, hotTopicCount,
            Sort.by(Sort.Order.desc("rank"), Sort.Order.asc("title"))
        )
    ).stream().collect(Collectors.toList());
  }
}
