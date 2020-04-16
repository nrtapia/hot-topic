package com.ntapia.hotoppic.topic.application.extractor;

import com.ntapia.hotoppic.topic.domain.Rss;
import java.util.List;
import lombok.Value;

@Value
public class TopicAnalyseExtractorRequest {

  private final List<Rss> rssList;

}
