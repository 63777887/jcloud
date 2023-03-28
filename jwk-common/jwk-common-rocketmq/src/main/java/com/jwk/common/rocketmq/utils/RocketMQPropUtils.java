package com.jwk.common.rocketmq.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jwk.common.rocketmq.JwkRocketMQProperties;
import java.util.HashMap;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * 获取配置信息
 * @date 2022/6/11
 */
public class RocketMQPropUtils {

	public static HashMap concurrentHashMap = new HashMap<>();

	@Autowired
	JwkRocketMQProperties jwkRocketMQProperties;

	public static String getProperty(String var1) {
		return String.valueOf(concurrentHashMap.get(var1));
	}

	@PostConstruct
	void init() {
		concurrentHashMap = JSONObject.parseObject(JSON.toJSONString(jwkRocketMQProperties), HashMap.class);
	}

}
