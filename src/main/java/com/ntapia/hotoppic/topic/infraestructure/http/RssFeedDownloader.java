package com.ntapia.hotoppic.topic.infraestructure.http;

import com.ntapia.hotoppic.topic.domain.FeedItem;
import com.ntapia.hotoppic.topic.domain.Rss;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class RssFeedDownloader implements Callable<Rss> {

  private static final String DOWNLOAD_RSS_LOG = "Download RSS items from: {}";

  private final Rss rss;

  public RssFeedDownloader(Rss rss) {
    this.rss = rss;
  }

  @Override
  public Rss call() throws Exception {
    log.info(DOWNLOAD_RSS_LOG, this.rss.getUrl());

    try {
      this.rss.setFeedItems(readFeed());
      return this.rss;

    } catch (Exception e) {
      log.error("Error to read RSS: {}", this.rss.getUrl(), e);
      return this.rss;
    }
  }

  private List<FeedItem> readFeed() throws IOException, FeedException {
    URL feedSource = new URL(this.rss.getUrl());
    SyndFeedInput input = new SyndFeedInput(false);
    SyndFeed feed = input.build(new XmlReader(feedSource));

    List<FeedItem> feedItems = new ArrayList<>();

    feed.getEntries().forEach(entry -> {
      SyndEntryImpl syndEntry = (SyndEntryImpl) entry;
      feedItems.add(FeedItem.create(null, syndEntry.getTitle(), syndEntry.getLink()));
    });

    return feedItems;
  }
}
