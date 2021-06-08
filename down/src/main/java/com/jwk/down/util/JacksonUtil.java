//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.jwk.down.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.TimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JacksonUtil {
  private static Logger logger = LoggerFactory.getLogger(JacksonUtil.class);
  private static final ObjectMapper jacksonMapper = new ObjectMapper();

  public JacksonUtil() {
  }

  public static String objectToJackson(Object obj) {
    try {
      return jacksonMapper.writeValueAsString(obj);
    } catch (JsonProcessingException var2) {
      logger.error("对象转换Json失败", var2);
      return null;
    }
  }

  public static String objectToFormateJackson(Object obj) {
    try {
      return jacksonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    } catch (JsonProcessingException var2) {
      logger.error("对象转换Json失败", var2);
      return null;
    }
  }

  public static <T> T jacksonToCollection(String src, Class<?> collectionClass, Class<?>... valueType) {
    JavaType javaType = jacksonMapper.getTypeFactory().constructParametricType(collectionClass, valueType);

    try {
      return jacksonMapper.readValue(src, javaType);
    } catch (IOException var5) {
      logger.error("json转换集合对象失败", var5);
      return null;
    }
  }

  public static <T> List<T> jacksonToArray(String src, Class<T> valueType) throws IOException {
    JavaType javaType = jacksonMapper.getTypeFactory().constructParametricType(List.class, new Class[]{valueType});

    try {
      return (List)jacksonMapper.readValue(src, javaType);
    } catch (IOException var4) {
      logger.error("json转换List对象失败", var4);
      throw var4;
    }
  }

  public static <T> T jacksonToObject(String src, Class<T> valueType) {
    try {
      return jacksonMapper.readValue(src, valueType);
    } catch (IOException var3) {
      logger.error("json转换对象失败", var3);
      return null;
    }
  }

  public static ObjectMapper getObjectMapper() {
    return jacksonMapper;
  }

  static {
    jacksonMapper.setSerializationInclusion(Include.NON_NULL);
    jacksonMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
  }
}
