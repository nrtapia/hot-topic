package com.ntapia.hotoppic.topic.application.frequency;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class TopicAnalyseNotFoundException extends RuntimeException {

  public TopicAnalyseNotFoundException(String message) {
    super(message);
  }
}
