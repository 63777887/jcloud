package com.jwk.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayParamFlowItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwk.gateway.filter.PasswordDecoderFilter;
import com.jwk.gateway.filter.RequestGlobalFilter;
import com.jwk.gateway.handler.GlobalExceptionHandler;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * 网关配置
 * @date 2022/6/11
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(GatewayConfigProperties.class)
public class GatewayConfiguration {

	private final List<ViewResolver> viewResolvers;

	private final ServerCodecConfigurer serverCodecConfigurer;

	public GatewayConfiguration(ObjectProvider<List<ViewResolver>> viewResolversProvider,
			ServerCodecConfigurer serverCodecConfigurer) {
		this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
		this.serverCodecConfigurer = serverCodecConfigurer;
	}

	@Bean
	public PasswordDecoderFilter passwordDecoderFilter(GatewayConfigProperties configProperties) {
		return new PasswordDecoderFilter(configProperties);
	}

	@Bean
	public RequestGlobalFilter jwkRequestGlobalFilter() {
		return new RequestGlobalFilter();
	}

	@Bean
	public GlobalExceptionHandler globalExceptionHandler(ObjectMapper objectMapper) {
		return new GlobalExceptionHandler(objectMapper);
	}

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler() {
		// Register the block exception handler for Spring Cloud Gateway.
		return new SentinelGatewayBlockExceptionHandler(viewResolvers, serverCodecConfigurer);
	}

	@PostConstruct
	public void doInit() {
		initCustomizedApis();
		initGatewayRules();
	}

	private void initCustomizedApis() {
		Set<ApiDefinition> definitions = new HashSet<>();
		ApiDefinition api1 = new ApiDefinition("some_customized_api")
				.setPredicateItems(new HashSet<ApiPredicateItem>() {
					{
						add(new ApiPathPredicateItem().setPattern("/ahas"));
						add(new ApiPathPredicateItem().setPattern("/product/**")
								.setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
					}
				});
		ApiDefinition api2 = new ApiDefinition("another_customized_api")
				.setPredicateItems(new HashSet<ApiPredicateItem>() {
					{
						add(new ApiPathPredicateItem().setPattern("/**")
								.setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
					}
				});
		definitions.add(api1);
		definitions.add(api2);
		GatewayApiDefinitionManager.loadApiDefinitions(definitions);
	}

	private void initGatewayRules() {
		Set<GatewayFlowRule> rules = new HashSet<>();
		rules.add(new GatewayFlowRule("aliyun_route").setCount(10).setIntervalSec(1));
		rules.add(new GatewayFlowRule("aliyun_route").setCount(2).setIntervalSec(2).setBurst(2).setParamItem(
				new GatewayParamFlowItem().setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_CLIENT_IP)));
		rules.add(new GatewayFlowRule("httpbin_route").setCount(10).setIntervalSec(1)
				.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_RATE_LIMITER).setMaxQueueingTimeoutMs(600)
				.setParamItem(new GatewayParamFlowItem()
						.setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_HEADER)
						.setFieldName("X-Sentinel-Flag")));
		rules.add(new GatewayFlowRule("httpbin_route").setCount(1).setIntervalSec(1)
				.setParamItem(new GatewayParamFlowItem()
						.setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_URL_PARAM).setFieldName("pa")));
		rules.add(new GatewayFlowRule("httpbin_route").setCount(2).setIntervalSec(30)
				.setParamItem(new GatewayParamFlowItem()
						.setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_URL_PARAM).setFieldName("type")
						.setPattern("warn").setMatchStrategy(SentinelGatewayConstants.PARAM_MATCH_STRATEGY_CONTAINS)));

		rules.add(new GatewayFlowRule("some_customized_api")
				.setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_CUSTOM_API_NAME).setCount(5).setIntervalSec(1)
				.setParamItem(new GatewayParamFlowItem()
						.setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_URL_PARAM).setFieldName("pn")));
		GatewayRuleManager.loadRules(rules);
	}

}
