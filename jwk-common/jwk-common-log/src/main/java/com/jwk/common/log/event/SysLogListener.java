package com.jwk.common.log.event;

import com.jwk.upms.base.api.LogRemoteService;
import com.jwk.upms.base.dto.SysLogDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

/**
 * @author Jiwk
 * @version 0.1.6
 * <p>
 * 异步监听日志事件
 * @date 2023/12/11
 */
@Slf4j
@RequiredArgsConstructor
public class SysLogListener {

    private final LogRemoteService remoteLogService;

    @SneakyThrows
    @Async
    @Order
    @EventListener(SysLogEvent.class)
    public void saveSysLog(SysLogEvent event) {
        SysLogDto source = (SysLogDto) event.getSource();
        remoteLogService.saveLog(source);
    }

}
