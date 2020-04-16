package com.ntapia.hotoppic.topic.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

@Data
@Entity
public class FeedItem {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String link;

  public FeedItem() {
    this.id = null;
    this.title = null;
    this.link = null;
  }

  public FeedItem(Long id, String title, String link) {
    this.id = id;
    this.title = title;
    this.link = link;
  }

  public static FeedItem create(Long id, String title, String link) {
    return new FeedItem(null, title, link);
  }
}
