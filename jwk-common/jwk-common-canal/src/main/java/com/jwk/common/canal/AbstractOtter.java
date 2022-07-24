package com.jwk.common.canal;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.alibaba.otter.canal.protocol.CanalEntry.EventType.*;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * Otter 客户端抽象类
 */
@Slf4j
public abstract class AbstractOtter implements Otter {

    /**
     * 过滤的事件类型
     */
    private static final List<CanalEntry.EventType> EVENT_TYPES = Arrays.asList(INSERT, DELETE, UPDATE, ERASE);
    @Resource
    private CanalProperties canalProperties;

    private DefaultCanalConnectorHelper defaultCanalConnectorHelper;
    private volatile boolean running;

    @Override
    public void stop() {
        this.running = false;
    }

    @Override
    public void start() {
        if (running) {
            return;
        }
        running = true;
        try {
            while (running) {
                Stopwatch sw = Stopwatch.createStarted();
                // 获取指定数量的数据
                Message message = null;
                sw.stop();
                message.setEntries(filter(message.getEntries()));
                long batchId = message.getId();
                if (batchId != -1) {
                    if (canalProperties.isShowEventLog() && !message.getEntries().isEmpty()) {
                        log.info("Get batchId: {} time: {}ms", batchId, sw.elapsed(TimeUnit.MILLISECONDS));
                    }
                    onMessage(message);
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(canalProperties.getIntervalMillis());
                } catch (InterruptedException e) {
                    log.error(e.getLocalizedMessage(), e);
                }
            }
        } finally {
            defaultCanalConnectorHelper.disconnect();
            log.info("Stop get data");
        }
    }

    /**
     * 处理
     *
     * @param message 信息
     */
    protected abstract void onMessage(Message message);

    /**
     * 只消费增、删、改、删表事件，其它事件暂不支持且会被忽略
     *
     * @param entries CanalEntry.Entry
     */
    private List<CanalEntry.Entry> filter(List<CanalEntry.Entry> entries) {
        if (CollectionUtils.isEmpty(entries)) {
            return Collections.emptyList();
        }
        return entries.stream()
                .filter(entry -> entry.getEntryType() != CanalEntry.EntryType.TRANSACTIONBEGIN)
                .filter(entry -> entry.getEntryType() != CanalEntry.EntryType.TRANSACTIONEND)
                .filter(entry -> EVENT_TYPES.contains(entry.getHeader().getEventType()))
                .collect(Collectors.toList());
    }

}
