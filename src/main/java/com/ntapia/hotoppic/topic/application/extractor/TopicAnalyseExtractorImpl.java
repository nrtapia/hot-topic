package com.ntapia.hotoppic.topic.application.extractor;

import com.ntapia.hotoppic.topic.domain.Rss;
import com.ntapia.hotoppic.topic.domain.RssFeedClient;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TopicAnalyseExtractorImpl implements TopicAnalyseExtractor {

  private static final String EXTRACT_RSS_TOPICS_LOG = "Extract RSS topics from: {}";

  private final RssFeedClient rssFeedClient;

  public TopicAnalyseExtractorImpl(RssFeedClient rssFeedClient) {
    this.rssFeedClient = rssFeedClient;
  }

  @Override
  public void extract(TopicAnalyseExtractorRequest topicAnalyseExtractorRequest) {
    log.info(EXTRACT_RSS_TOPICS_LOG, topicAnalyseExtractorRequest);

    List<Rss> rssList = rssFeedClient
        .findFeedItemsAndCompose(topicAnalyseExtractorRequest.getRssList());


  }
}
