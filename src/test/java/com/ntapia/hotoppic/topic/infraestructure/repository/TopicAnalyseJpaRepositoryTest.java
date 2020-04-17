package com.ntapia.hotoppic.topic.infraestructure.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import com.ntapia.hotoppic.TestUtil;
import com.ntapia.hotoppic.topic.domain.AnalyseStatus;
import com.ntapia.hotoppic.topic.domain.Rss;
import com.ntapia.hotoppic.topic.domain.RssItem;
import com.ntapia.hotoppic.topic.domain.TopicAnalyse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

  @Autowired
  private RssItemJpaRepository rssItemJpaRepository;

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

  @Test
  public void givenRssWhenUpdateTopicItemsThenMustPersistInDatabase() {

    final String id = TestUtil.getId();
    final TopicAnalyse topicAnalyse = TopicAnalyse.create(id, AnalyseStatus.CREATED);
    Rss rss1 = Rss.create(null, TestUtil.URL_VALID_1);
    rss1.setRssItems(new ArrayList<>());
    topicAnalyse.addRss(rss1);
    TopicAnalyse persisted = topicAnalyseJpaRepository.save(topicAnalyse);

    assertNotNull(persisted.getRssList());
    assertFalse(persisted.getRssList().isEmpty());

    Rss rss = persisted.getRssList().get(0);

    final List<RssItem> rssItems = Arrays.asList(
        RssItem.create(null, "title1", "link1", rss),
        RssItem.create(null, "title2", "link2", rss)
    );

    rssItemJpaRepository.saveAll(rssItems);
    List<RssItem> list = rssItemJpaRepository.findAll();
    assertNotNull(list);
    assertEquals(2, list.size());
  }

}