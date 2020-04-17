package com.ntapia.hotoppic.topic.domain;

import java.util.List;
import java.util.Optional;

public interface TopicAnalyseRepository {

  TopicAnalyse save(TopicAnalyse topicAnalyse);

  Optional<TopicAnalyse> findById(String id);

  void updateRssItems(Long rssId, List<RssItem> rssItems);

  void saveAllRssItems(List<RssItem> rssItems);

  List<RssItem> findItemsByTopicAndLimit(int hotTopicCount);
}
