package com.ntapia.hotoppic.topic.application.frequency;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TopicFrequencyFinderResponse {

  private List<HotTopicDto> hotTopics;
  private List<ItemDto> items;

  @Builder
  @Data
  public static class HotTopicDto {

    private String topic;
    private Integer count;
  }

  @Builder
  @Data
  public static class ItemDto {

    private String title;
    private String link;
  }
}
