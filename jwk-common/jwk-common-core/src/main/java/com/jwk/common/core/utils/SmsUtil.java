package com.jwk.common.core.utils;

import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.jwk.common.core.properties.SmsConfigProperties;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static com.aliyun.teautil.Common.assertAsString;

@Slf4j
@UtilityClass
public class SmsUtil {

    private final String signName = "JCloud";


    /**
     * 获取短信验证码的key
     *
     * @param phone
     * @return
     */
    public String getCaptchaKey(String phone) {
        return "sms:captcha:" + phone;
    }

    /**
     * 获取短信验证码
     *
     * @return
     */
    public String generateCaptchaCode() {
        return new RandomGenerator("0123456789", 6).generate();
    }

    /**
     * 创建短信客户端
     *
     * @return
     * @throws Exception
     */
    public Client createClient() throws Exception {
        SmsConfigProperties smsConfigProperties = SpringUtil.getBean(SmsConfigProperties.class);
        Config config = new Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(smsConfigProperties.getAccessKeyId())
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(smsConfigProperties.getAccessKeySecret());
        // Endpoint 请参考 https://api.aliyun.com/product/Dysmsapi
        config.endpoint = smsConfigProperties.getEndpoint();
        return new Client(config);
    }

    /**
     * 发送参数可选短信
     *
     * @param phone
     * @param signName
     * @param runtimeOptions
     * @return
     * @throws Exception
     */
    public SendSmsResponse sendSmsWithOptions(String phone, String signName, RuntimeOptions runtimeOptions) {
        return doSendSms(() -> {
            Client client = createClient();
            SendSmsRequest sendSmsRequest = new SendSmsRequest()
                    .setPhoneNumbers(phone)
                    .setSignName(signName);

            // 复制代码运行请自行打印 API 的返回值
            return client.sendSmsWithOptions(sendSmsRequest, runtimeOptions);
        });
    }

    /**
     * 发送短信
     *
     * @param phone
     * @param code
     * @return
     * @throws Exception
     */
    public SendSmsResponse sendLoginSms(String phone, String code) {
        return doSendSms(() -> {
            SmsConfigProperties smsConfigProperties = SpringUtil.getBean(SmsConfigProperties.class);

            Client client = createClient();
            SendSmsRequest sendSmsRequest = new SendSmsRequest()
                    .setPhoneNumbers(phone);
            sendSmsRequest.setSignName(signName);
            if (StrUtil.isNotBlank(smsConfigProperties.getSignName())) {
                sendSmsRequest.setSignName(smsConfigProperties.getSignName());
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", code);
            sendSmsRequest.setTemplateParam(jsonObject.toJSONString());
            Optional<SmsConfigProperties.TemplateCode> optional = smsConfigProperties.getTemplate().stream().filter(t -> t.getType().equals("login")).findFirst();
            AssertUtil.isTrue(optional.isPresent(),"短信配置信息异常，请检查配置文件");
            sendSmsRequest.setTemplateCode(optional.get().getCode());
            // 复制代码运行请自行打印 API 的返回值
            return client.sendSms(sendSmsRequest);
        });
    }

    private SendSmsResponse doSendSms(SmsSupplierThrows supplier) {
        try {
            return supplier.doSendSms();
        } catch (TeaException error) {
            if (log.isErrorEnabled()) {
                log.error("sendSms error: {}", error.getMessage());
            }
            assertAsString(error.message);
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            if (log.isErrorEnabled()) {
                log.error("sendSms error: {}", error.getMessage());
            }
            assertAsString(error.message);
        }
        return null;
    }

    @FunctionalInterface
    public interface SmsSupplierThrows {

        /**
         * Gets a result.
         *
         * @return a result
         */
        SendSmsResponse doSendSms() throws Exception;
    }
}
