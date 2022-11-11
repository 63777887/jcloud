package com.jwk.common.prometheus.utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jwk.common.core.utils.JwkSpringUtil;
import com.jwk.common.prometheus.properties.JwkPrometheusProperties;
import java.util.UUID;
import org.springframework.core.env.Environment;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * Prometheus工具类
 */
public class JwkPrometheusUtil {

	public static String getId() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	public static JSONObject getPrometheusInfo(JwkPrometheusProperties jwkPrometheusProperties) {
		String jsonString = JSON.toJSONString(jwkPrometheusProperties);

		JSONObject data = JSONObject.parseObject(jsonString);
		JSONObject object = new JSONObject();
		Environment environment = JwkSpringUtil.getBean(Environment.class);
		object.put("path", environment.getProperty("management.endpoints.web.base-path"));
		String port = environment.getProperty("management.server.port");
		if (StrUtil.isBlank(port)) {
			port = environment.getProperty("server.port");
		}
		object.put("port", port);
		object.put("name", jwkPrometheusProperties.getApplication());
		data.put("prometheusInfo", object);
		return data;
	}

}
