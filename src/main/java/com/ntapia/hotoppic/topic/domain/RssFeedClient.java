package com.ntapia.hotoppic.topic.domain;

import java.util.List;

public interface RssFeedClient {

  List<Rss> findFeedItemsAndCompose(List<Rss> rssList);

}
