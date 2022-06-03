package com.jwk.prometheus.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jwk.prometheus.component.JwkPrometheusProperties;
import com.jwk.prometheus.component.ZookeeperProperties;
import com.jwk.prometheus.config.JwkPrometheusContext;
import com.jwk.prometheus.constant.JwkPrometheusConstants;
import com.jwk.prometheus.service.RegistryService;
import com.jwk.prometheus.utils.JwkPrometheusUtil;
import java.nio.charset.StandardCharsets;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ZookeeperRegistryServiceImpl implements RegistryService {

  private static Logger logger = LoggerFactory.getLogger(ZookeeperRegistryServiceImpl.class);

  @Autowired
  CuratorFramework curatorFramework;

  @Override
  public void registry() {
    checkConfig();
    JwkPrometheusProperties jwkPrometheusProperties = JwkPrometheusContext.getInstance()
        .getJwkPrometheusProperties();
    ZookeeperProperties zookeeper = jwkPrometheusProperties.getZookeeper();

    if (StringUtils.isBlank(zookeeper.getAddress())) {
      throw new RuntimeException("jcloud prometheus address is undefined, please check config");
    }

    if (StringUtils.isBlank(zookeeper.getNamespace())) {
      throw new RuntimeException("jcloud prometheus namespace is undefined, please check config");
    }

    try {
      logger.info("register zk starting...");
//      createZkNode(jwkPrometheusProperties);
      registerPrometheusMetrics(jwkPrometheusProperties);
    } catch (Exception e) {
      logger.error("register zk error.", e);
      throw new RuntimeException("register zk error", e);
    }
  }

  @Override
  public String support() {
    return JwkPrometheusConstants.DEFAULT_REGISTER_MODE;
  }

  private void checkConfig() {
    JwkPrometheusProperties jwkPrometheusProperties = JwkPrometheusContext.getInstance()
        .getJwkPrometheusProperties();
    ZookeeperProperties zookeeper = jwkPrometheusProperties.getZookeeper();

    if  (null == zookeeper.getAddress()){
      String zookeeper_url = System.getenv("ZOOKEEPER_URL");
      if (null == zookeeper_url){
        throw new RuntimeException("prometheus zookeeper register url is empty...");
      }
      zookeeper.setAddress(zookeeper_url);
    }

    logger.info("config init value vrvActuatorProperties :{}", JSON.toJSON(jwkPrometheusProperties));

  }

  private boolean checkNodeExists(String path) {
    try {
      Stat stat = curatorFramework.checkExists().forPath(path);

      return stat != null;
    } catch (Exception e) {
      logger.error("check zk node error...,path:{}", path, e);
    }
    return false;
  }

  private  String registerPrometheusMetrics(JwkPrometheusProperties jwkPrometheusProperties) throws Exception {
    String path = zkPath(jwkPrometheusProperties);

    JSONObject data = JwkPrometheusUtil.getPrometheusInfo(jwkPrometheusProperties);

    if (! checkNodeExists(path)) {
      String realPath = curatorFramework.create()
          .creatingParentContainersIfNeeded()
          .withMode(CreateMode.EPHEMERAL)
          .forPath(path, data.toJSONString().getBytes(StandardCharsets.UTF_8));
      logger.info("create zk node :{},data:{}", path, data);

      CuratorWatcher curatorWatcher = new CuratorWatcher() {
        @Override
        public void process(WatchedEvent event) throws Exception {
          try {
            switch (event.getType()) {
              case NodeDeleted:
                logger.warn("listen register provider node deleted path {}", realPath);
                // 等待5秒，检测节点是否存在，如果不存在，则注册
//              Thread.sleep(5 * 1000);
                Stat stat = null;
                stat = curatorFramework.checkExists().forPath(realPath);
                if (stat != null) {
                  logger.info("listen register provider node already exists path {}", realPath);
                  break;
                }
                logger.warn("listen register provider node not exists path {}", realPath);
                logger
                    .warn("listen register provider node create model[EPHEMERAL] path {}", realPath);
                curatorFramework.create().withMode(CreateMode.EPHEMERAL)
                    .forPath(realPath, data.toJSONString().getBytes(StandardCharsets.UTF_8));
                break;
              default:
                break;
            }
          } catch (Exception e) {
            logger.error("listen register provider node error,{}", e.getMessage());
          }
        }
      };

      curatorFramework.watchers().add().usingWatcher(curatorWatcher).forPath(realPath);

      //关闭客户端链接
      Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        try {
          curatorFramework.watchers().remove(curatorWatcher).forPath(realPath);
//          curatorFramework.close();
          logger.info("remove watchers...");
        } catch (Exception e) {
//					logger.error("",e);
        }
      }));
      return realPath;
    }else {
      throw new RuntimeException("zk node is exist....");
    }

  }

  private  String zkPath(JwkPrometheusProperties jwkPrometheusProperties) {
    return jwkPrometheusProperties.getZookeeper().getNamespace() + JwkPrometheusConstants.P
        + jwkPrometheusProperties.getApplication() + JwkPrometheusConstants.P
        + JwkPrometheusUtil.getId();
  }
}
