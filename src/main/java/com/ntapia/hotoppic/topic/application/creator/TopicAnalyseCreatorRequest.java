package com.ntapia.hotoppic.topic.application.creator;

import java.util.List;
import lombok.Value;

@Value
public class TopicAnalyseCreatorRequest {

  private final List<String> rssUrls;

}
