package com.jwk.common.prometheus.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jwk.common.prometheus.constant.JwkPrometheusConstants;
import com.jwk.common.prometheus.exception.PrometheusException;
import com.jwk.common.prometheus.exception.PrometheusExceptionCodeE;
import com.jwk.common.prometheus.properties.JwkPrometheusProperties;
import com.jwk.common.prometheus.service.RegistryService;
import com.jwk.common.prometheus.support.JwkPrometheusContext;
import com.jwk.common.prometheus.utils.JwkPrometheusUtil;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * Zookeeper注册模式实现
 * @date 2022/6/11
 */
@RequiredArgsConstructor
public class ZookeeperRegistryServiceImpl implements RegistryService {

	private static Logger logger = LoggerFactory.getLogger(ZookeeperRegistryServiceImpl.class);

	private final CuratorFramework curatorFramework;

	private final ApplicationContext applicationContext;

	@Override
	public void registry() {
		JwkPrometheusProperties jwkPrometheusProperties = JwkPrometheusContext.getInstance()
				.getJwkPrometheusProperties();
		checkConfig(jwkPrometheusProperties);

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("register zk starting...");
			}
			registerPrometheusMetrics(jwkPrometheusProperties);
		}
		catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("register zk error.", e);
			}
			throw new RuntimeException("register zk error", e);
		}
	}

	@Override
	public String support() {
		return JwkPrometheusConstants.DEFAULT_REGISTER_MODE;
	}

	private void checkConfig(JwkPrometheusProperties jwkPrometheusProperties) {

		if (StrUtil.isBlank(jwkPrometheusProperties.getApplication())) {
			String application = System.getenv("PROMETHEUS_SERVICE_NAMESPACE");
			if (StrUtil.isBlank(application)) {
				application = applicationContext.getEnvironment().getProperty("spring.application.name");
				throw new IllegalArgumentException("prometheus application is undefined, please check config");
			}
			jwkPrometheusProperties.setApplication(application);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("config init value JwkPrometheusProperties :{}", JSON.toJSON(jwkPrometheusProperties));
		}

	}

	private boolean checkNodeExists(String path) {
		try {
			Stat stat = curatorFramework.checkExists().forPath(path);

			return stat != null;
		}
		catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("check zk node error...,path:{}", path, e);
			}
		}
		return false;
	}

	private void registerPrometheusMetrics(JwkPrometheusProperties jwkPrometheusProperties) throws Exception {
		String path = zkPath(jwkPrometheusProperties);

		JSONObject data = JwkPrometheusUtil.getPrometheusInfo(jwkPrometheusProperties);

		if (!checkNodeExists(path)) {
			String realPath = curatorFramework.create().creatingParentContainersIfNeeded()
					.withMode(CreateMode.EPHEMERAL).forPath(path, data.toJSONString().getBytes(StandardCharsets.UTF_8));
			if (logger.isDebugEnabled()) {
				logger.info("create zk node :{},data:{}", path, data);
			}

			CuratorWatcher curatorWatcher = new CuratorWatcher() {
				@Override
				public void process(WatchedEvent event) throws Exception {
					try {
						switch (event.getType()) {
							case NodeDeleted:
								if (logger.isWarnEnabled()) {
									logger.warn("listen register provider node deleted path {}", realPath);
								}
								// 等待5秒，检测节点是否存在，如果不存在，则注册
								// Thread.sleep(5 * 1000);
								Stat stat = curatorFramework.checkExists().forPath(realPath);
								if (stat != null) {
									if (logger.isDebugEnabled()) {
										logger.info("listen register provider node already exists path {}", realPath);
									}
									break;
								}
								if (logger.isWarnEnabled()) {
									logger.warn("listen register provider node not exists path {}", realPath);
									logger.warn("listen register provider node create model[EPHEMERAL] path {}",
											realPath);
								}
								curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath(realPath,
										data.toJSONString().getBytes(StandardCharsets.UTF_8));
								break;
							default:
								break;
						}
					}
					catch (Exception e) {
						if (logger.isErrorEnabled()) {
							logger.error("listen register provider node error,{}", e.getMessage());
						}
					}
				}
			};

			curatorFramework.watchers().add().usingWatcher(curatorWatcher).forPath(realPath);

			// 关闭客户端链接
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				try {
					curatorFramework.watchers().remove(curatorWatcher).forPath(realPath);
					// curatorFramework.close();
					if (logger.isDebugEnabled()) {
						logger.info("remove watchers...");
					}
				}
				catch (Exception e) {
					// logger.error("",e);
				}
			}));
		}
		else {
			throw new PrometheusException(PrometheusExceptionCodeE.NodeExist);
		}

	}

	private String zkPath(JwkPrometheusProperties jwkPrometheusProperties) {
		return JwkPrometheusConstants.P + jwkPrometheusProperties.getNamespace() + JwkPrometheusConstants.P
				+ jwkPrometheusProperties.getApplication() + JwkPrometheusConstants.P + JwkPrometheusUtil.getId();
	}

}
