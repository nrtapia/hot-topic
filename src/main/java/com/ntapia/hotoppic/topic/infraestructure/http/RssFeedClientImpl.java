package com.ntapia.hotoppic.topic.infraestructure.http;

import com.ntapia.hotoppic.topic.domain.Rss;
import com.ntapia.hotoppic.topic.domain.RssFeedClient;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RssFeedClientImpl implements RssFeedClient {

  private static final String INITIALIZE_THREAD_POOL_LOG = "Initialize thread pool size: {}";
  private static final String THREAD_NAME_PREFIX = "rss-client-";
  private final ExecutorCompletionService<Rss> completionService;

  public RssFeedClientImpl(@Value("${rssFeedClient.poolSize}") int poolSize) {
    log.info(INITIALIZE_THREAD_POOL_LOG, poolSize);

    CustomizableThreadFactory threadFactory = new CustomizableThreadFactory(THREAD_NAME_PREFIX);

    this.completionService = new ExecutorCompletionService<>(
        Executors.newFixedThreadPool(poolSize, threadFactory));
  }

  @Override
  public List<Rss> findFeedItemsAndCompose(List<Rss> rssList) {
    rssList.forEach(rss -> completionService.submit(new RssFeedDownloader(rss)));

    List<Rss> response = new ArrayList<>();
    Iterator<Rss> iterator = rssList.iterator();
    while(iterator.hasNext()) {
      iterator.next();
      try {
        final Future<Rss> future = completionService.take();
        final Rss rssResponse = future.get();
        response.add(rssResponse);

      } catch (ExecutionException | InterruptedException e) {
        log.warn("Error while downloading RSS", e);
        Thread.currentThread().interrupt();
      }
    }

    return response;
  }
}
