package com.ntapia.hotoppic.topic.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import lombok.Data;

@Data
@Entity
public class RssItem {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false, length = 1000)
  private String link;

  private Integer rank;

  @ManyToOne(fetch = FetchType.LAZY)
  private Rss rss;

  @Version
  private Long version;

  public RssItem() {
    this.id = null;
    this.title = null;
    this.link = null;
  }

  private RssItem(Long id, String title, String link, Rss rss) {
    this.id = id;
    this.title = title;
    this.link = link;
    this.rss = rss;
    this.rank = 0;
  }

  public static RssItem create(Long id, String title, String link, Rss rss) {
    return new RssItem(id, title, link, rss);
  }
}
