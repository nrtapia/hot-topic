package com.ntapia.hotoppic.topic.infraestructure.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.ntapia.hotoppic.TestUtil;
import com.ntapia.hotoppic.topic.domain.AnalyseStatus;
import com.ntapia.hotoppic.topic.domain.Rss;
import com.ntapia.hotoppic.topic.domain.TopicAnalyse;
import com.ntapia.hotoppic.topic.infraestructure.repository.TopicAnalyseJpaRepository;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@DataJpaTest
public class TopicAnalyseJpaRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private TopicAnalyseJpaRepository topicAnalyseJpaRepository;

  @Test
  public void givenTwoUrlsWhenCreateTopicAnalyseThenMustPersistInDatabase() {

    final String id = TestUtil.getId();
    final TopicAnalyse topicAnalyse = TopicAnalyse.create(id, AnalyseStatus.CREATED);
    topicAnalyse.addRss(Rss.create(null, TestUtil.URL_VALID_1));
    topicAnalyse.addRss(Rss.create(null, TestUtil.URL_VALID_2));

    topicAnalyseJpaRepository.save(topicAnalyse);

    final TopicAnalyse topicAnalysePersisted = entityManager.find(TopicAnalyse.class, id);
    log.info("Persisted: {}", topicAnalysePersisted);

    assertNotNull(topicAnalysePersisted);
    assertNotNull(topicAnalysePersisted.getId());
    assertNotNull(topicAnalysePersisted.getRssList());
    assertEquals(2, topicAnalysePersisted.getRssList().size());
    assertEquals(AnalyseStatus.CREATED, topicAnalysePersisted.getAnalyseStatus());
  }

}