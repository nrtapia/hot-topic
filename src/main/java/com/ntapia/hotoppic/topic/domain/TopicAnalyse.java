package com.ntapia.hotoppic.topic.domain;

import static javax.persistence.CascadeType.ALL;

import com.ntapia.hotoppic.topic.application.creator.TopicAnalyseInvalidArgumentException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString
@Entity
public class TopicAnalyse {

  private static final String SHOULD_LEAST_TWO_RSS = "Should at least two RSS URL";
  private static final String ID_IS_REQUIRED = "Id is required";

  @Id
  private String id;

  @Enumerated(EnumType.STRING)
  private AnalyseStatus analyseStatus;

  @OneToMany(cascade = ALL, fetch = FetchType.EAGER, mappedBy = "topicAnalyse")
  private List<Rss> rssList;

  @OneToMany(cascade = ALL)
  @JoinColumn(name = "topicAnalyse_id")
  private List<HotTopic> hotTopics;

  @Version
  private Long version;

  private TopicAnalyse() {
    id = null;
    analyseStatus = null;
    rssList = null;
  }

  private TopicAnalyse(String id, AnalyseStatus analyseStatus, List<Rss> rssList) {
    this.id = id;
    this.analyseStatus = analyseStatus;
    this.rssList = rssList;
  }

  public static TopicAnalyse create(String id, AnalyseStatus analyseStatus) {
    if (StringUtils.isBlank(id)) {
      throw new TopicAnalyseInvalidArgumentException(ID_IS_REQUIRED);
    }

    return new TopicAnalyse(id, analyseStatus, new ArrayList<>());
  }

  public void addRss(Rss rss) {
    this.rssList.add(rss);
    rss.setTopicAnalyse(this);
  }

  public void addHotTopics(List<HotTopic> hotTopics){
    this.hotTopics = hotTopics;
    this.analyseStatus = AnalyseStatus.FINISHED;
  }
}
