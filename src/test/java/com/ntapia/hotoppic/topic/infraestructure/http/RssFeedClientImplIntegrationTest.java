package com.ntapia.hotoppic.topic.infraestructure.http;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import com.ntapia.hotoppic.TestUtil;
import com.ntapia.hotoppic.topic.domain.Rss;
import com.ntapia.hotoppic.topic.domain.RssFeedClient;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class RssFeedClientImplIntegrationTest {

  @Test
  public void givenTwoFeedUrlsWhenCallRssClientThenMustReturnFeedItems()
      throws ExecutionException, InterruptedException {

    RssFeedClient rssFeedClient = new RssFeedClientImpl(3);

    long begin = System.currentTimeMillis();
    List<Rss> rssList = rssFeedClient.findFeedItemsAndCompose(
        Arrays.asList(
            Rss.create(1L, TestUtil.URL_VALID_1),
            Rss.create(2L, TestUtil.URL_VALID_2)
        )
    );
    log.info("time: {}", System.currentTimeMillis() - begin);

    assertNotNull(rssList);
    assertEquals(2, rssList.size());

    assertNotNull(rssList.get(0).getFeedItems());
    assertFalse(rssList.get(0).getFeedItems().isEmpty());

    assertNotNull(rssList.get(1).getFeedItems());
    assertFalse(rssList.get(1).getFeedItems().isEmpty());

    rssList.forEach(rss -> log.info("URL: {} items: {}", rss.getUrl(), rss.getFeedItems().size()));
  }
}