package com.ntapia.hotoppic.topic.infraestructure.rest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ntapia.hotoppic.TestUtil;
import com.ntapia.hotoppic.topic.application.creator.TopicAnalyseCreator;
import com.ntapia.hotoppic.topic.application.creator.TopicAnalyseCreatorRequest;
import com.ntapia.hotoppic.topic.application.creator.TopicAnalyseCreatorResponse;
import com.ntapia.hotoppic.topic.application.creator.TopicAnalyseInvalidArgumentException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@RunWith(SpringRunner.class)
@WebMvcTest(TopicAnalyseController.class)
public class TopicAnalyseControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private TopicAnalyseCreator topicAnalyseCreator;

  @Test
  public void givenOneRssUrlWhenPostAnalyseNewThenReturnBadRequest() throws Exception {
    List<String> request = Arrays.asList(TestUtil.URL_VALID_1);

    final TopicAnalyseCreatorRequest createRequest = new TopicAnalyseCreatorRequest(request);

    when(this.topicAnalyseCreator.create(createRequest))
        .thenThrow(new TopicAnalyseInvalidArgumentException("Bad Request"));

    MvcResult result = mvc.perform(
        post(TopicAnalyseController.ENDPOINT)
            .content(TestUtil.toJson(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andReturn();

    Exception exception = result.getResolvedException();
    assertThat(exception, is(instanceOf(TopicAnalyseInvalidArgumentException.class)));

    verify(this.topicAnalyseCreator, times(1)).create(createRequest);
  }

  @Test
  public void givenNoRssUrlWhenPostAnalyseNewThenReturnBadRequest() throws Exception {
    List<String> request = new ArrayList<>();

    final TopicAnalyseCreatorRequest createRequest = new TopicAnalyseCreatorRequest(request);

    when(this.topicAnalyseCreator.create(createRequest))
        .thenThrow(new TopicAnalyseInvalidArgumentException("Bad Request"));

    MvcResult result = mvc.perform(
        post(TopicAnalyseController.ENDPOINT)
            .content(TestUtil.toJson(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andReturn();

    Exception exception = result.getResolvedException();
    assertThat(exception, is(instanceOf(TopicAnalyseInvalidArgumentException.class)));

    verify(this.topicAnalyseCreator, times(1)).create(createRequest);
  }

  @Test
  public void givenTwoRssUrlWhenPostAnalyseNewThenReturnStatusOkAndUniqueIdentifier()
      throws Exception {

    List<String> request = Arrays.asList(TestUtil.URL_VALID_1, TestUtil.URL_VALID_2);

    UUID uuid = UUID.randomUUID();
    final String expectedIdValue = uuid.toString();

    final TopicAnalyseCreatorRequest createRequest = new TopicAnalyseCreatorRequest(request);

    final TopicAnalyseCreatorResponse createResponse = new TopicAnalyseCreatorResponse(
        expectedIdValue);

    when(this.topicAnalyseCreator.create(createRequest)).thenReturn(createResponse);

    mvc.perform(
        post(TopicAnalyseController.ENDPOINT)
            .content(TestUtil.toJson(request))
            .characterEncoding("utf-8")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(expectedIdValue))
        .andDo(print());

    verify(this.topicAnalyseCreator, times(1)).create(createRequest);
  }
}