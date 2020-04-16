package com.ntapia.hotoppic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestUtil {

  private static ObjectMapper mapper = new ObjectMapper();


  public static final String URL_VALID_1
      = "https://news.google.com/news?cf=all&hl=en&pz=1&ned=us&output=rss";

  public static final String URL_VALID_2
      = "https://www.genbeta.com/tag/rss";

  static {
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
  }

  private TestUtil() {
  }

  public static String getId() {
    return UUID.randomUUID().toString();
  }

  public static String toJson(Object object) {
    try {
      return mapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      log.error("Error to convert object to Json: {}", object, e);
      throw new IllegalArgumentException(e.getMessage(), e);
    }
  }

  public static <T> T toObject(String json, Class<T> valueType) {
    try {
      return mapper.readValue(json, valueType);
    } catch (IOException e) {
      log.error("Error to convert Json to Object: {}", json, e);
      throw new IllegalArgumentException(e.getMessage(), e);
    }
  }
}