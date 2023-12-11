package com.jwk.common.log.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author Jiwk
 * @version 0.1.6
 * <p>
 * 系统日志事件
 * @date 2023/12/11
 */
public class SysLogEvent extends ApplicationEvent {

    public SysLogEvent(Object source) {
        super(source);
    }

}
