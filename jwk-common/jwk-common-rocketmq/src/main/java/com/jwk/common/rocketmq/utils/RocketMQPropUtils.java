package com.jwk.common.rocketmq.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jwk.common.rocketmq.JwkRocketMQProperties;
import java.util.HashMap;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 获取配置信息
 */
public class RocketMQPropUtils {

	@Autowired
	JwkRocketMQProperties jwkRocketMQProperties;

	public static HashMap concurrentHashMap = new HashMap<>();

	@PostConstruct
	void init() {
		concurrentHashMap = JSONObject.parseObject(JSON.toJSONString(jwkRocketMQProperties), HashMap.class);
	}

	public static String getProperty(String var1) {
		return String.valueOf(concurrentHashMap.get(var1));
	}

}
