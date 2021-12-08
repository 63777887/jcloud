package com.jwk.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.Message;
import com.alibaba.otter.canal.protocol.exception.CanalClientException;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Canal 链接帮助类
 *
 * @author fanxuankai
 */
@Slf4j
public class DefaultCanalConnectorHelper implements CanalConnectotHelper{

    /**
     * 默认重试次数
     */
    private static final int DEFAULT_RETRY_COUNT = 20;
    private CanalConnector canalConnector;
    @Resource
    private CanalProperties canalProperties;

//    @PostConstruct
    @Override
    public void init() {
        String instance = canalProperties.getInstance();
        CanalProperties.Cluster cluster = canalProperties.getCluster();
        if (cluster != null && StringUtils.hasText(cluster.getNodes())) {
            canalConnector = CanalConnectors.newClusterConnector(cluster.getNodes(),
                    instance, canalProperties.getUsername(), canalProperties.getPassword());
        } else {
            canalConnector =
                    CanalConnectors.newSingleConnector(
                            new InetSocketAddress(canalProperties.getSingleNode().getHostname(),
                                    canalProperties.getSingleNode().getPort()), instance,
                            canalProperties.getUsername(),
                            canalProperties.getPassword());
        }
        if (canalConnector == null) {
            throw new IllegalArgumentException("请检查 Canal 链接配置");
        }
        connect();
    }

    /**
     * 链接
     */
    @Override
    public void connect() {
        int retry = 0;
        // 异常后重试
        while (retry++ < DEFAULT_RETRY_COUNT) {
            try {
                canalConnector.connect();
                canalConnector.subscribe(canalProperties.getFilter());
                canalConnector.rollback();
                return;
            } catch (CanalClientException e) {
                log.error("链接失败", e);
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException ex) {
                    log.error("中断异常", ex);
                }
            }
        }
        throw new RuntimeException("链接失败");
    }

    @Override
    public void disconnect() {
        canalConnector.unsubscribe();
        canalConnector.disconnect();
        canalConnector = null;
    }

//    public void ack(long batchId) {
//        canalConnector.ack(batchId);
//    }
//
//    public void rollback(long batchId) {
//        canalConnector.rollback(batchId);
//    }
//
//    public Message getWithoutAck() {
//        return canalConnector.getWithoutAck(canalProperties.getBatchSize());
//    }

}
