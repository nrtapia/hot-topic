package com.ntapia.hotoppic.topic.infraestructure.rest;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ntapia.hotoppic.HotTopicApplication;
import com.ntapia.hotoppic.TestUtil;
import com.ntapia.hotoppic.shared.infrastructure.repository.JpaRepositoryConfig;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {HotTopicApplication.class, JpaRepositoryConfig.class})
@AutoConfigureMockMvc
@TestPropertySource(
    locations = "classpath:application-integrationtest.properties")
public class TopicAnalyseControllerIntegrationTest {

  @Autowired
  private MockMvc mvc;

  @Test
  public void givenRssUrlsWhenPostAnalyseNewThenStatus200()
      throws Exception {

    final List<String> request =
        Arrays.asList(TestUtil.URL_VALID_1, TestUtil.URL_VALID_2);

    MvcResult result = mvc.perform(post(TopicAnalyseController.ENDPOINT)
        .content(TestUtil.toJson(request))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andReturn();

    TopicAnalyseController.Response response = TestUtil.toObject(
        result.getResponse().getContentAsString(), TopicAnalyseController.Response.class);

    log.info("Response: {}", response);

    assertNotNull(response);
    assertNotNull(response.getId());
  }
}
