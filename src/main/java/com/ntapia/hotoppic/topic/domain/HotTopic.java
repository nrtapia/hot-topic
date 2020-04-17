package com.ntapia.hotoppic.topic.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode
public class HotTopic implements Comparable<HotTopic> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(length = 40)
  private String topic;
  private Integer count;
  @Version
  private Long version;

  public HotTopic() {
    this.id = null;
    this.topic = null;
    this.count = 0;
  }

  private HotTopic(Long id, String topic, Integer count) {
    this.id = id;
    this.topic = topic;
    this.count = count;
  }

  public static HotTopic create(Long id, String topic, Integer count) {
    return new HotTopic(id, topic, count);
  }

  @Override
  public int compareTo(HotTopic other) {
    return count.compareTo(other.getCount());
  }
}
