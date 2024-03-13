package com.jwk.upms.base.utils;

import com.jwk.common.core.utils.WebUtils;
import com.jwk.upms.base.constants.OAuth2Constant;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;

/**
 * Token工具类
 *
 * @author Jiwk
 * @version 0.1.6
 * @date 2024/2/4
 */
@UtilityClass
public class TokenUtil {
    public String buildKey(String type, String id) {
        return String.format("%s:%s:%s", OAuth2Constant.TOKEN, type, id);
    }

    public String buildRecordKey(String type, Long userId) {
        return String.format("%s:%s:%s:%d", OAuth2Constant.TOKEN, OAuth2Constant.RECORD, type, userId);
    }


    public String getToken() {
        if (WebUtils.getRequest().isPresent()) {
            return WebUtils.getRequest().get().getHeader(HttpHeaders.AUTHORIZATION);
        }
        return "";
    }
}
