package com.ntapia.hotoppic.topic.domain;

import com.ntapia.hotoppic.topic.application.creator.TopicAnalyseInvalidArgumentException;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;

@Getter
@Setter
@EqualsAndHashCode
@ToString(exclude = "topicAnalyse")
@Entity
public class Rss {

  private static final String URL_REQUIRED = "RSS URL is required";
  private static final String URL_INVALID = "RSS URL is invalid";

  private static final String[] SCHEMES = {"http", "https"};

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String url;

  @ManyToOne(fetch = FetchType.LAZY)
  private TopicAnalyse topicAnalyse;

  @OneToMany(
      mappedBy = "rss",
      cascade = CascadeType.ALL,
      orphanRemoval = true
  )
  private List<RssItem> rssItems;

  @Version
  private Long version;

  public Rss() {
    this.id = null;
    this.url = null;
  }

  private Rss(Long id, String url) {
    this.id = id;
    this.url = url;
  }

  public static Rss create(Long id, String url) {
    if (StringUtils.isBlank(url)) {
      throw new TopicAnalyseInvalidArgumentException(URL_REQUIRED);
    }

    UrlValidator urlValidator = new UrlValidator(SCHEMES);
    if (!urlValidator.isValid(url)) {
      throw new TopicAnalyseInvalidArgumentException(URL_INVALID);
    }

    return new Rss(id, url);
  }

}
