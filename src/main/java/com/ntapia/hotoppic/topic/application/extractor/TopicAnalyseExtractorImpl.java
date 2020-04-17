package com.ntapia.hotoppic.topic.application.extractor;

import com.ntapia.hotoppic.topic.domain.HotTopic;
import com.ntapia.hotoppic.topic.domain.Rss;
import com.ntapia.hotoppic.topic.domain.RssFeedClient;
import com.ntapia.hotoppic.topic.domain.TopicAnalyse;
import com.ntapia.hotoppic.topic.domain.TopicAnalyseRepository;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class TopicAnalyseExtractorImpl implements TopicAnalyseExtractor {

  private static final String SPLITTER_CHARACTER = "\\s+";
  private static final String EXTRACT_RSS_TOPICS_LOG = "Extract RSS topics from: {}";
  private static final String PERSIST_HOT_TOPICS_LOG = "Persist hot topics for id: {}";
  private static final String HOT_TOPICS_PERSISTED_LOG = "Hot topics persisted id: {}";
  private static final String COUNTS_TO_HOT_TOPICS_LOG = "Rank hot topics and update";
  private static final String START_TO_ANALYSE_LOG = "Start to analyse feeds";
  private static final String PERSIST_RSS_FEED_LOG = "Persist RSS items list";
  private static final String ANALYSE_ELAPSED_TIME_LOG = "Analyse id: {} elapsed time: {} seconds";

  private final RssFeedClient rssFeedClient;

  private final TopicAnalyseRepository topicAnalyseRepository;

  @Value("${extractor.skip.words}")
  private String stopWords;
  @Value("${extractor.hotTopic.count}")
  private int hotTopicCount;

  public TopicAnalyseExtractorImpl(RssFeedClient rssFeedClient,
      TopicAnalyseRepository topicAnalyseRepository) {
    this.rssFeedClient = rssFeedClient;
    this.topicAnalyseRepository = topicAnalyseRepository;
  }

  @Transactional
  @Override
  public void extractAndAnalyse(TopicAnalyseExtractorRequest topicAnalyseExtractorRequest) {
    log.info(EXTRACT_RSS_TOPICS_LOG, topicAnalyseExtractorRequest);
    final long begin = System.nanoTime();

    final TopicAnalyse topicAnalyse = topicAnalyseExtractorRequest.getTopicAnalyse();
    final List<Rss> rssList = getRssTopics(topicAnalyse);
    final List<HotTopic> hotTopics = mapToHotTopicsAndSort(calculateHotTopics(rssList));
    updateAnalyse(topicAnalyse, hotTopics);
    persistAndRankRssItems(rssList, hotTopics);

    log.info(ANALYSE_ELAPSED_TIME_LOG, topicAnalyse.getId(),
        TimeUnit.SECONDS.convert(System.nanoTime() - begin, TimeUnit.NANOSECONDS));
  }

  private List<HotTopic> mapToHotTopicsAndSort(Map<String, Integer> counter) {
    log.info(COUNTS_TO_HOT_TOPICS_LOG);

    return counter.entrySet().stream()
        .map(entry -> HotTopic.create(null, entry.getKey(), entry.getValue()))
        .sorted(Comparator.reverseOrder())
        .limit(hotTopicCount)
        .collect(Collectors.toList());
  }

  private Map<String, Integer> calculateHotTopics(List<Rss> rssList) {
    log.info(START_TO_ANALYSE_LOG);

    return rssList.stream()
        .flatMap(rss -> rss.getRssItems().stream())
        .map(feedItem -> Arrays.asList(feedItem.getTitle().split(SPLITTER_CHARACTER)))
        .flatMap(Collection::stream)
        .map(String::toLowerCase)
        .filter(word -> StringUtils.isAlpha(word) && !stopWords.contains(word))
        .collect(Collectors.toConcurrentMap(word -> word, word -> 1, Integer::sum));
  }

  protected void persistAndRankRssItems(List<Rss> rssList, List<HotTopic> hotTopics) {
    log.info(PERSIST_RSS_FEED_LOG);

    rssList.stream()
        .map(Rss::getRssItems)
        .flatMap(Collection::stream)
        .forEach(rssItem -> {

          Integer rank = hotTopics.stream()
              .filter(hotTopic -> rssItem.getTitle().toLowerCase().contains(hotTopic.getTopic()))
              .mapToInt(HotTopic::getCount)
              .sum();

          rssItem.setRank(rank);
        });

    rssList.forEach(rss -> topicAnalyseRepository.saveAllRssItems(rss.getRssItems()));
  }

  private void updateAnalyse(TopicAnalyse topicAnalyse, List<HotTopic> hotTopics) {
    log.info(PERSIST_HOT_TOPICS_LOG, topicAnalyse.getId());

    Optional<TopicAnalyse> optionalTopicAnalyse =
        topicAnalyseRepository.findById(topicAnalyse.getId());

    if (optionalTopicAnalyse.isPresent()) {
      TopicAnalyse topicAnalysePersisted = optionalTopicAnalyse.get();
      topicAnalysePersisted.addHotTopics(hotTopics);
      topicAnalyseRepository.save(topicAnalysePersisted);
      log.info(HOT_TOPICS_PERSISTED_LOG, topicAnalyse.getId());
    }
  }

  private List<Rss> getRssTopics(TopicAnalyse topicAnalyse) {
    return rssFeedClient.findFeedItemsAndCompose(topicAnalyse.getRssList());
  }
}
