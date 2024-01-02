package com.jwk.upms.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.jwk.common.core.properties.SmsConfigProperties;
import com.jwk.common.core.thread.ThreadPoolUtil;
import com.jwk.common.core.utils.SmsUtil;
import com.jwk.upms.web.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 短信服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService {

    private final ValueOperations valueOperations;

    private final SmsConfigProperties smsConfigProperties;
    /**
     * 发送短信的单例线程池
     */
    private static ExecutorService sendSmsThreadPool = ThreadPoolUtil.newSingleThreadPool("JcloudUpms","SendSms");

    @Override
    public String sendCode(String phone) {
        String code = SmsUtil.generateCaptchaCode();
        if (log.isDebugEnabled()){
            log.debug("发送短信验证码：{}", code);
        }
        valueOperations.set(SmsUtil.getCaptchaKey(phone), code, smsConfigProperties.getExpireTime(), TimeUnit.MINUTES);
        if (smsConfigProperties.getEnable()){
            // 发送短信
            sendSmsThreadPool.execute(()->{
                SendSmsResponse sendSmsResponse = SmsUtil.sendLoginSms(phone, code);
                log.info("sendSmsResponse: {}", JSON.toJSONString(sendSmsResponse));
            });
            return Boolean.TRUE.toString();
        }else {
            return code;
        }
    }
}
