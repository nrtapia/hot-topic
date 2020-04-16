package com.ntapia.hotoppic.topic.domain;

import com.ntapia.hotoppic.topic.application.creator.TopicAnalyseInvalidArgumentException;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;

@Getter
@EqualsAndHashCode
@ToString
@Entity
public class Rss {

  private static final String URL_REQUIRED = "RSS URL is required";
  private static final String URL_INVALID = "RSS URL is invalid";

  private static final String[] SCHEMES = {"http", "https"};

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String url;

  public Rss() {
    this.id = null;
    this.url = null;
  }

  public Rss(Long id, String url) {
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