package com.ntapia.hotoppic.topic.application.creator;


import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import com.ntapia.hotoppic.TestUtil;
import com.ntapia.hotoppic.shared.application.DomainEventPublisher;
import com.ntapia.hotoppic.shared.domain.IdGenerator;
import com.ntapia.hotoppic.topic.domain.AnalyseStatus;
import com.ntapia.hotoppic.topic.domain.Rss;
import com.ntapia.hotoppic.topic.domain.TopicAnalyse;
import com.ntapia.hotoppic.topic.domain.TopicAnalyseCreated;
import com.ntapia.hotoppic.topic.domain.TopicAnalyseRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TopicAnalyseCreatorImplTest {

  private static final String ID_VALID = TestUtil.getId();

  @Mock
  private IdGenerator idGenerator;

  @Mock
  private DomainEventPublisher domainEventPublisher;

  @Mock
  private TopicAnalyseRepository topicAnalyseRepository;

  @InjectMocks
  private TopicAnalyseCreatorImpl creator;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test(expected = TopicAnalyseInvalidArgumentException.class)
  public void givenNullRequestWhenCreateTopicAnalyseThenThrowException() {
    creator.create(null);
  }

  @Test(expected = TopicAnalyseInvalidArgumentException.class)
  public void givenEmptyRssUrlWhenCreateTopicAnalyseThenThrowException() {
    TopicAnalyseCreatorRequest creatorRequest = new TopicAnalyseCreatorRequest(null);

    creator.create(creatorRequest);

    then(idGenerator).should(never()).generate();
  }

  @Test(expected = TopicAnalyseInvalidArgumentException.class)
  public void givenNoRssUrlWhenCreateTopicAnalyseThenThrowException() {
    TopicAnalyseCreatorRequest creatorRequest
        = new TopicAnalyseCreatorRequest(Collections.emptyList());

    creator.create(creatorRequest);

    then(idGenerator).should(times(1)).generate();
  }

  @Test(expected = TopicAnalyseInvalidArgumentException.class)
  public void givenOneRssUrlWhenCreateTopicAnalyseThenThrowException() {
    TopicAnalyseCreatorRequest creatorRequest
        = new TopicAnalyseCreatorRequest(Collections.singletonList("url1"));

    creator.create(creatorRequest);

    then(idGenerator).should(times(1)).generate();
  }

  @Test(expected = TopicAnalyseInvalidArgumentException.class)
  public void givenTwoInvalidUrlWhenCreateTopicAnalyseThenThrowException() {
    TopicAnalyseCreatorRequest creatorRequest
        = new TopicAnalyseCreatorRequest(Arrays.asList("url1", "url2"));

    creator.create(creatorRequest);

    then(idGenerator).should(times(1)).generate();
  }


  @Test
  public void givenTwoUrlsWhenCreateTopicAnalyseThenMustPersistAndPublishEvent() {
    given(idGenerator.generate()).willReturn(ID_VALID);

    final TopicAnalyseCreatorRequest creatorRequest
        = new TopicAnalyseCreatorRequest(
        Arrays.asList(TestUtil.URL_VALID_1, TestUtil.URL_VALID_2));

    final List<Rss> rssList = creatorRequest.getRssUrls().stream()
        .map(url -> Rss.create(null, url))
        .collect(Collectors.toList());

    final TopicAnalyse topicAnalyse = TopicAnalyse.create(ID_VALID, AnalyseStatus.CREATED, rssList);
    willDoNothing().given(topicAnalyseRepository).save(topicAnalyse);

    final TopicAnalyseCreated topicAnalyseCreated = new TopicAnalyseCreated(topicAnalyse);
    willDoNothing().given(domainEventPublisher).publish(topicAnalyseCreated);

    creator.create(creatorRequest);

    then(idGenerator).should(times(1)).generate();
    then(topicAnalyseRepository).should(times(1)).save(topicAnalyse);
    then(domainEventPublisher).should(times(1)).publish(topicAnalyseCreated);
  }

}