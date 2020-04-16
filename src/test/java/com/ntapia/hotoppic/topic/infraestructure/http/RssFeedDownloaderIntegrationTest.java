package com.ntapia.hotoppic.topic.infraestructure.http;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import com.ntapia.hotoppic.TestUtil;
import com.ntapia.hotoppic.topic.domain.FeedItem;
import com.ntapia.hotoppic.topic.domain.Rss;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class RssFeedDownloaderIntegrationTest {

  @Test
  public void givenOneFeedUrlsWhenDownloadRssThenMustBuildFeedItems()
      throws ExecutionException, InterruptedException {

    ExecutorService pool = Executors.newSingleThreadExecutor();
    Future<Rss> future = pool
        .submit(new RssFeedDownloader(Rss.create(1L, TestUtil.URL_VALID_1)));

    Rss rss = future.get();
    assertNotNull(rss.getFeedItems());
    assertFalse(rss.getFeedItems().isEmpty());
  }

}