package com.jwk.uaa.endpoint;

import cn.hutool.core.util.StrUtil;
import com.jwk.common.core.model.RestResponse;
import com.jwk.common.core.utils.AssertUtil;
import com.jwk.common.redis.annotation.JwkRateLimiter;
import com.jwk.common.security.annotation.Inner;
import com.jwk.upms.base.api.SmsRemoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author Jiwk
 * @version 0.1.4
 * <p>
 * 验证码
 * @date 2023/12/13
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/captcha")
public class JwkCaptchaEndpoint {


    private final SmsRemoteService smsRemoteService;


    /**
     * 发送短信验证码
     *
     * @param phone
     * @return
     */
    @JwkRateLimiter(value = "send_sms_code", param = "#phone", max = 3, ttl = 10, timeUnit = TimeUnit.MINUTES)
    @PostMapping("/sendSmsCode")
    @Inner(needFrom = false)
    public RestResponse sendSmsCode(String phone) {
        AssertUtil.isTrue(StrUtil.isNotBlank(phone), "手机号不能为空");
        return smsRemoteService.sendCode(phone);
    }

}
