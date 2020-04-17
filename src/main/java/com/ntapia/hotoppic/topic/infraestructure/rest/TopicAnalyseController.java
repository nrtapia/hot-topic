package com.ntapia.hotoppic.topic.infraestructure.rest;

import com.ntapia.hotoppic.topic.application.creator.TopicAnalyseCreator;
import com.ntapia.hotoppic.topic.application.creator.TopicAnalyseCreatorRequest;
import com.ntapia.hotoppic.topic.application.creator.TopicAnalyseCreatorResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TopicAnalyseController {

  static final String ENDPOINT = "/analyse/new";

  private static final String ANALYSE_REQUEST_LOG = "Analyse request: {}";

  private final TopicAnalyseCreator topicAnalyseCreator;

  public TopicAnalyseController(
      TopicAnalyseCreator topicAnalyseCreator) {
    this.topicAnalyseCreator = topicAnalyseCreator;
  }

  @PostMapping(ENDPOINT)
  public Response post(@RequestBody List<String> request) {
    log.info(ANALYSE_REQUEST_LOG, request);

    return process(request);
  }

  private Response process(List<String> request) {
    final TopicAnalyseCreatorResponse creatorResponse =
        this.topicAnalyseCreator.create(new TopicAnalyseCreatorRequest(request));
    return new Response(creatorResponse.getId());
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  static class Response {

    String id;
  }

}
