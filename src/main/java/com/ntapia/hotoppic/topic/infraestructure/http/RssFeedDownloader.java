package com.ntapia.hotoppic.topic.infraestructure.http;

import com.ntapia.hotoppic.topic.domain.RssItem;
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
  public Rss call() {
    log.info(DOWNLOAD_RSS_LOG, this.rss.getUrl());

    try {
      this.rss.setRssItems(readFeed());
      return this.rss;

    } catch (Exception e) {
      log.error("Error to read RSS: {}", this.rss.getUrl(), e);
      return this.rss;
    }
  }

  private List<RssItem> readFeed() throws IOException, FeedException {
    URL feedSource = new URL(this.rss.getUrl());
    SyndFeedInput input = new SyndFeedInput(false);
    SyndFeed feed = input.build(new XmlReader(feedSource));

    List<RssItem> rssItems = new ArrayList<>();

    feed.getEntries().forEach(entry -> {
      SyndEntryImpl syndEntry = (SyndEntryImpl) entry;
      rssItems.add(RssItem.create(null, syndEntry.getTitle(), syndEntry.getLink(), rss));
    });

    return rssItems;
  }
}
