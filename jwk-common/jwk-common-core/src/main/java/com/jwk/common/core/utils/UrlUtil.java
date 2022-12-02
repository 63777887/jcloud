package com.jwk.common.core.utils;

import java.nio.charset.StandardCharsets;
import lombok.experimental.UtilityClass;
import org.springframework.web.util.UriUtils;

/**
 * @author Jiwk
 * @date 2022/11/14
 * @version 0.1.4
 * <p>
 * url处理工具类
 */
@UtilityClass
public class UrlUtil extends UriUtils {

	/**
	 * encode
	 * @param source source
	 * @return sourced String
	 */
	public static String encode(String source) {
		return UriUtils.encode(source, StandardCharsets.UTF_8);
	}

	/**
	 * decode
	 * @param source source
	 * @return decoded String
	 */
	public static String decode(String source) {
		return UriUtils.decode(source, StandardCharsets.UTF_8);
	}

}
