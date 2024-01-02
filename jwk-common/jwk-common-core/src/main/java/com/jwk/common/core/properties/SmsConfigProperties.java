package com.jwk.common.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 短信配置
 *
 * @author Jiwk
 * @version 0.1.6
 * @date 2023/12/13
 */
@Data
@ConfigurationProperties("jwk.sms")
public class SmsConfigProperties {

    /**
     * 是否开启发送短信
     */
    private Boolean enable = true;
    /**
     * 验证码默认过期时间
     */
    private Long expireTime = 5L;
    /**
     * 短信后台key
     */
    private String accessKeyId;
    /**
     * 短信后台Secret
     */
    private String accessKeySecret;
    /**
     * 服务地址 请参考 https://api.aliyun.com/product/Dysmsapi
     */
    private String endpoint="dysmsapi.aliyuncs.com";
    /**
     * 短信默认签名
     */
    private String signName;
    /**
     * 登陆模版
     */
    private List<TemplateCode> template;

    @Data
    public static class TemplateCode{
        /**
         * 短信类型
         */
        private String type;
        /**
         * 模版code
         */
        private String code;
    }
}
