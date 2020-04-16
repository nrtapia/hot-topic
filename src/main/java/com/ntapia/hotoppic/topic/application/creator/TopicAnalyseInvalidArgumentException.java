package com.ntapia.hotoppic.topic.application.creator;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class TopicAnalyseInvalidArgumentException extends RuntimeException {

  public TopicAnalyseInvalidArgumentException(String message) {
    super(message);
  }
}
