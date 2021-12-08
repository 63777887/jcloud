package com.jwk.common.core.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Object2JsonUtil {

  public static void object2Json(Class<?> source) {
    System.out.println(
        JSON.toJSONString(source, SerializerFeature.WriteNullStringAsEmpty,
            SerializerFeature.WriteNullBooleanAsFalse, SerializerFeature.WriteNullListAsEmpty));
  }

}
