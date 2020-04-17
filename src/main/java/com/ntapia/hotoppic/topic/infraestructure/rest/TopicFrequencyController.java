package com.ntapia.hotoppic.topic.infraestructure.rest;

import com.ntapia.hotoppic.topic.application.frequency.TopicFrequencyFinder;
import com.ntapia.hotoppic.topic.application.frequency.TopicFrequencyFinderRequest;
import com.ntapia.hotoppic.topic.application.frequency.TopicFrequencyFinderResponse;
import com.ntapia.hotoppic.topic.application.frequency.TopicFrequencyFinderResponse.HotTopicDto;
import com.ntapia.hotoppic.topic.application.frequency.TopicFrequencyFinderResponse.ItemDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TopicFrequencyController {

  static final String ENDPOINT = "/frequency/{id}";

  private final TopicFrequencyFinder topicFrequencyFinder;

  public TopicFrequencyController(
      TopicFrequencyFinder topicFrequencyFinder) {
    this.topicFrequencyFinder = topicFrequencyFinder;
  }

  @GetMapping(ENDPOINT)
  public Response get(@PathVariable String id) {

    final TopicFrequencyFinderResponse finderResponse =
        topicFrequencyFinder.find(new TopicFrequencyFinderRequest(id));

    return Response.builder()
        .hotTopics(mapToHotTopic(finderResponse.getHotTopics()))
        .news(mapToNews(finderResponse.getItems()))
        .build();
  }

  private List<News> mapToNews(List<ItemDto> items) {
    return items.stream()
        .map(itemDto ->
            News.builder()
                .title(itemDto.getTitle())
                .link(itemDto.getLink())
                .build()
        ).collect(Collectors.toList());
  }

  private List<HotTopic> mapToHotTopic(List<HotTopicDto> hotTopics) {
    return hotTopics.stream()
        .map(hotTopicDto ->
            HotTopic.builder()
                .topic(hotTopicDto.getTopic())
                .count(hotTopicDto.getCount())
                .build())
        .collect(Collectors.toList());
  }

  @Builder
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  static class Response {

    private List<HotTopic> hotTopics;
    private List<News> news;
  }

  @Builder
  @Data
  static class HotTopic {

    private String topic;
    private Integer count;
  }

  @Builder
  @Data
  static class News {

    private String title;
    private String link;
  }
}
