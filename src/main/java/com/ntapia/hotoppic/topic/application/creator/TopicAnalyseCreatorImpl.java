package com.ntapia.hotoppic.topic.application.creator;

import com.ntapia.hotoppic.shared.application.DomainEventPublisher;
import com.ntapia.hotoppic.shared.domain.IdGenerator;
import com.ntapia.hotoppic.topic.domain.AnalyseStatus;
import com.ntapia.hotoppic.topic.domain.Rss;
import com.ntapia.hotoppic.topic.domain.TopicAnalyse;
import com.ntapia.hotoppic.topic.domain.TopicAnalyseCreated;
import com.ntapia.hotoppic.topic.domain.TopicAnalyseRepository;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class TopicAnalyseCreatorImpl implements TopicAnalyseCreator {

  private static final String RSS_URL_LIST_REQUIRED = "RSS URL list is required";
  private static final String CREATE_TOPIC_ANALYSE_LOG = "Create topic analyse: {}";

  private final IdGenerator idGenerator;
  private final TopicAnalyseRepository topicAnalyseRepository;
  private final DomainEventPublisher domainEventPublisher;

  public TopicAnalyseCreatorImpl(IdGenerator idGenerator,
      TopicAnalyseRepository topicAnalyseRepository,
      DomainEventPublisher domainEventPublisher) {
    this.idGenerator = idGenerator;
    this.topicAnalyseRepository = topicAnalyseRepository;
    this.domainEventPublisher = domainEventPublisher;
  }

  @Transactional
  @Override
  public TopicAnalyseCreatorResponse create(TopicAnalyseCreatorRequest creatorRequest) {
    log.info(CREATE_TOPIC_ANALYSE_LOG, creatorRequest);

    validateRequest(creatorRequest);

    final String id = idGenerator.generate();
    final TopicAnalyse topicAnalyse = TopicAnalyse.create(id, AnalyseStatus.CREATED);
    mapUrlsToRss(creatorRequest).forEach(topicAnalyse::addRss);

    this.topicAnalyseRepository.save(topicAnalyse);
    this.domainEventPublisher.publish(new TopicAnalyseCreated(topicAnalyse));

    return new TopicAnalyseCreatorResponse(id);
  }

  private void validateRequest(TopicAnalyseCreatorRequest creatorRequest) {
    if (Objects.isNull(creatorRequest) || Objects.isNull(creatorRequest.getRssUrls())) {
      throw new TopicAnalyseInvalidArgumentException(RSS_URL_LIST_REQUIRED);
    }
  }

  private List<Rss> mapUrlsToRss(TopicAnalyseCreatorRequest creatorRequest) {
    return creatorRequest.getRssUrls().stream()
        .map(url -> Rss.create(null, url))
        .collect(Collectors.toList());
  }
}
