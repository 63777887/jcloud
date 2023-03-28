package com.jwk.gateway.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.jwk.gateway.model.ConfigRouteDefinition;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.yaml.snakeyaml.Yaml;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * 动态变更网关路由
 * @date 2022/6/11
 */
public class NacosRouteDefinitionRepository implements RouteDefinitionRepository {

	private static final Logger logger = LoggerFactory.getLogger(NacosRouteDefinitionRepository.class);

	NacosConfigManager nacosConfigManager;

	private final ApplicationEventPublisher publisher;

	public NacosRouteDefinitionRepository(ApplicationEventPublisher publisher,
			NacosConfigProperties nacosConfigProperties) {
		this.publisher = publisher;
		this.nacosConfigManager = new NacosConfigManager(nacosConfigProperties);
		logger.info("[gateway-platform] NacosDataId: {},NacosGroupId: {}", GatewayRoutConfig.getNacosDataId(),
				GatewayRoutConfig.getNacosGroupId());
		this.addListener();
	}

	@Override
	public Flux<RouteDefinition> getRouteDefinitions() {
		try {
			String content = nacosConfigManager.getConfigService().getConfig(GatewayRoutConfig.getNacosDataId(),
					GatewayRoutConfig.getNacosGroupId(), GatewayRoutConfig.getDefaultReadTimeout());
			logger.info("[gateway-platform]获取网关当前配置:{}", content);
			List<RouteDefinition> routeDefinitions = getListByStr(content);
			return Flux.fromIterable(routeDefinitions);
		}
		catch (NacosException e) {
			logger.error("getRouteDefinitions by nacos error", e);
		}
		return Flux.fromIterable(CollUtil.newArrayList());
	}

	/**
	 * 添加Nacos监听
	 */
	private void addListener() {
		try {
			nacosConfigManager.getConfigService().addListener(GatewayRoutConfig.getNacosDataId(),
					GatewayRoutConfig.getNacosGroupId(), new Listener() {
						@Override
						public Executor getExecutor() {
							return null;
						}

						@Override
						public void receiveConfigInfo(String configInfo) {
							publisher.publishEvent(new RefreshRoutesEvent(this));
						}
					});
		}
		catch (NacosException e) {
			logger.error("nacos-addListener-error", e);
		}
	}

	@Override
	public Mono<Void> save(Mono<RouteDefinition> route) {
		return null;
	}

	@Override
	public Mono<Void> delete(Mono<String> routeId) {
		return null;
	}

	/**
	 * 解析路由
	 * @param content
	 * @return
	 */
	private List<RouteDefinition> getListByStr(String content) {
		if (StrUtil.isNotEmpty(content)) {
			Yaml yaml = new Yaml();
			ConfigRouteDefinition configRouteDefinition = yaml.loadAs(content, ConfigRouteDefinition.class);
			return configRouteDefinition.getRoutes();
		}
		return Collections.emptyList();
	}

	/**
	 * 解析路由
	 * @param content
	 * @return
	 */
	// private List<RouteDefinition> getListByStr(String content) {
	// if (StringUtils.isNotEmpty(content)) {
	// return JSONObject.parseArray(content, RouteDefinition.class);
	// }
	// return new ArrayList<>(0);
	// }

}