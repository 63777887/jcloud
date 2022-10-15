package com.jwk.common.prometheus.config;

import java.util.Map;
import java.util.TreeMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 预设配置初始值
 */
public class MeteorEnvironmentPostProcessor implements EnvironmentPostProcessor {

	/**
	 *
	 * 调整优化环境变量，对于框架会默认覆盖一些环境变量，此时我们需要在processor中执行 我们不再需要使用单独的yml文件来解决此问题。原则：
	 * 1）所有设置为系统属性的，初衷为"对系统管理员可见"、"对外部接入组件可见"（比如starter或者日志组件等）
	 * 2）对设置为lastSource，表示"当用户没有通过yml"配置选项时的默认值--担保策略。
	 **/

	/**
	 * 可以被application覆盖的配置，此处作为默认担保
	 */
	private static final Map<String, Object> lastSource = new TreeMap<>();

	/**
	 * 框架限定，不能被覆盖或者外部指定无效的配置
	 */
	private static final Map<String, Object> firstSource = new TreeMap<>();

	private static final String ACTUATOR_FIRST = "ACTUATOR_FIRST";

	private static final String ACTUATOR_LAST = "ACTUATOR_DEFAULT";

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {

		MutablePropertySources propertySources = environment.getPropertySources();
		// 已装载，则中断
		if (propertySources.contains(ACTUATOR_FIRST) || propertySources.contains(ACTUATOR_LAST)) {
			return;
		}

		resolveManagement();
		MapPropertySource lastPropertySource = new MapPropertySource(ACTUATOR_FIRST, lastSource);
		propertySources.addLast(lastPropertySource);

		MapPropertySource firstPropertySource = new MapPropertySource(ACTUATOR_LAST, firstSource);
		propertySources.addFirst(firstPropertySource);
	}

	private void resolveManagement() {
		// 默认全部打开
		lastSource.put("management.endpoints.enabled-by-default", true);
		lastSource.put("management.endpoints.jmx.exposure.include", "*");

		lastSource.put("management.endpoints.web.exposure.include", "*");
		lastSource.put("management.endpoints.web.exposure.exclude", "");
		lastSource.put("management.endpoints.web.base-path", "/jwk");

		lastSource.put("management.endpoint.health.enabled", true);
		// lastSource.put("management.endpoint.health.show-details","never");
		//
		// firstSource.put("management.endpoint.info.enabled",true);
		// firstSource.put("management.endpoint.mappings.enabled",true);
		// firstSource.put("management.endpoint.metrics.enabled",true);
		// firstSource.put("management.endpoint.env.enabled",true);
		// firstSource.put("management.endpoint.configprops.enabled",true);
		// firstSource.put("management.endpoint.beans.enabled",true);
		// firstSource.put("management.endpoint.httptrace.enabled",true);
	}

}