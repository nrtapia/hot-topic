package com.ntapia.hotoppic.topic.application.frequency;

import com.ntapia.hotoppic.topic.application.frequency.TopicFrequencyFinderResponse.HotTopicDto;
import com.ntapia.hotoppic.topic.application.frequency.TopicFrequencyFinderResponse.ItemDto;
import com.ntapia.hotoppic.topic.domain.HotTopic;
import com.ntapia.hotoppic.topic.domain.RssItem;
import com.ntapia.hotoppic.topic.domain.TopicAnalyse;
import com.ntapia.hotoppic.topic.domain.TopicAnalyseRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TopicFrequencyFinderImpl implements TopicFrequencyFinder {

  private static final String TOPIC_ANALYSE_NOT_FOUND_MESSAGE = "Topic analyse not found";
  private static final String FIND_TOPIC_FREQUENCY_LOG = "Find topic frequency id: {}";

  @Value("${extractor.hotTopic.count}")
  private int hotTopicCount;

  private final TopicAnalyseRepository topicAnalyseRepository;

  public TopicFrequencyFinderImpl(
      TopicAnalyseRepository topicAnalyseRepository) {
    this.topicAnalyseRepository = topicAnalyseRepository;
  }

  @Override
  public TopicFrequencyFinderResponse find(
      TopicFrequencyFinderRequest topicFrequencyFinderRequest) {
    log.info(FIND_TOPIC_FREQUENCY_LOG, topicFrequencyFinderRequest.getId());

    List<HotTopic> hotTopics = getHotTopics(topicFrequencyFinderRequest);
    List<RssItem> rssItems = getHotRssItems();

    List<HotTopicDto> topicsDto = mapToTopicsDto(hotTopics);
    List<ItemDto> itemsDto = mapToItemsDto(rssItems);

    return TopicFrequencyFinderResponse.builder()
        .hotTopics(topicsDto)
        .items(itemsDto)
        .build();
  }

  private List<RssItem> getHotRssItems() {
    return topicAnalyseRepository.findItemsByTopicAndLimit(hotTopicCount);
  }

  private List<HotTopic> getHotTopics(TopicFrequencyFinderRequest topicFrequencyFinderRequest) {
    return topicAnalyseRepository
        .findById(topicFrequencyFinderRequest.getId())
        .map(TopicAnalyse::getHotTopics)
        .orElseThrow(() -> new TopicAnalyseNotFoundException(TOPIC_ANALYSE_NOT_FOUND_MESSAGE));
  }

  private List<ItemDto> mapToItemsDto(List<RssItem> rssItems) {
    return rssItems.stream()
        .map(rssItem -> ItemDto.builder()
            .title(rssItem.getTitle())
            .link(rssItem.getLink())
            .build()
        ).collect(Collectors.toList());
  }

  private List<HotTopicDto> mapToTopicsDto(List<HotTopic> hotTopics) {
    return hotTopics.stream()
        .map(hotTopic -> HotTopicDto.builder()
            .topic(hotTopic.getTopic())
            .count(hotTopic.getCount())
            .build()
        )
        .collect(Collectors.toList());
  }
}
