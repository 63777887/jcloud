package com.jwk.uaa.grant.captcha;

import com.jwk.common.core.constant.JwkSecurityConstants;
import com.jwk.common.security.support.base.OAuth2ResourceOwnerBaseAuthenticationConverter;
import com.jwk.common.security.util.SecurityUtils;
import com.jwk.uaa.constant.JwkOAuth2ParameterNames;
import com.jwk.common.core.utils.SmsUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * 资源所有者邮箱令牌授予者
 * @date 2022/6/11
 */
public class SmsAuthenticationGranter
		extends OAuth2ResourceOwnerBaseAuthenticationConverter<SmsAuthenticationToken> {

	private StringRedisTemplate stringRedisTemplate;

	public SmsAuthenticationGranter(StringRedisTemplate stringRedisTemplate) {
		super();
		this.stringRedisTemplate = stringRedisTemplate;
	}

	@Override
	public boolean support(String grantType) {
		return JwkOAuth2ParameterNames.SMS.equals(grantType);
	}

	@Override
	public SmsAuthenticationToken buildToken(Authentication clientPrincipal, Set<String> requestedScopes,
											 Map<String, Object> additionalParameters) {
		return new SmsAuthenticationToken(new AuthorizationGrantType(JwkOAuth2ParameterNames.EMAIL), clientPrincipal,
				requestedScopes, additionalParameters);
	}

	/**
	 * 校验扩展参数 密码模式密码必须不为空
	 * @param request 参数列表
	 */
	@Override
	public void checkParams(HttpServletRequest request) {
		MultiValueMap<String, String> parameters = SecurityUtils.getParameters(request);
		// phone (REQUIRED)
		String phone = parameters.getFirst(JwkOAuth2ParameterNames.PHONE_PARAMETER_NAME);
		if (!StringUtils.hasText(phone)
				|| parameters.get(JwkOAuth2ParameterNames.PHONE_PARAMETER_NAME).size() != 1) {
			SecurityUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, JwkOAuth2ParameterNames.PHONE_PARAMETER_NAME,
					JwkSecurityConstants.ACCESS_TOKEN_REQUEST_ERROR_URI);
		}

		// captcha (REQUIRED)
		String captcha = parameters.getFirst(JwkOAuth2ParameterNames.CAPTCHA);
		if (!StringUtils.hasText(captcha) || parameters.get(JwkOAuth2ParameterNames.CAPTCHA).size() != 1) {
			SecurityUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, JwkOAuth2ParameterNames.CAPTCHA,
					JwkSecurityConstants.ACCESS_TOKEN_REQUEST_ERROR_URI);
		}

		// 校验验证码，从redis中获取手机号对应的验证码进行比对
		String captchaKey = SmsUtil.getCaptchaKey(phone);
		String captchaCache = stringRedisTemplate.opsForValue().get(captchaKey);
		// 取出后立马清除验证码
		stringRedisTemplate.delete(captchaKey);
		if (!captcha.equals(captchaCache)) {
			SecurityUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, JwkOAuth2ParameterNames.CAPTCHA,
					JwkSecurityConstants.ACCESS_TOKEN_REQUEST_ERROR_URI);
		}
	}

}
