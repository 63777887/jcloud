package com.jwk.common.core.utils;

import java.nio.charset.StandardCharsets;
import lombok.experimental.UtilityClass;
import org.springframework.util.Base64Utils;

import java.nio.charset.Charset;

/**
 * @author Jiwk
 * @date 2022/11/14
 * @version 0.1.4
 * <p>
 * Base64工具
 */
@UtilityClass
public class Base64Util extends Base64Utils {

	/**
	 * 编码
	 * @param value 字符串
	 * @return {String}
	 */
	public static String encode(String value) {
		return Base64Util.encode(value, StandardCharsets.UTF_8);
	}

	/**
	 * 编码
	 * @param value 字符串
	 * @param charset 字符集
	 * @return {String}
	 */
	public static String encode(String value, Charset charset) {
		byte[] val = value.getBytes(charset);
		return new String(Base64Utils.encode(val), charset);
	}

	/**
	 * 编码URL安全
	 * @param value 字符串
	 * @return {String}
	 */
	public static String encodeUrlSafe(String value) {
		return Base64Util.encodeUrlSafe(value, StandardCharsets.UTF_8);
	}

	/**
	 * 编码URL安全
	 * @param value 字符串
	 * @param charset 字符集
	 * @return {String}
	 */
	public static String encodeUrlSafe(String value, Charset charset) {
		byte[] val = value.getBytes(charset);
		return new String(Base64Utils.encodeUrlSafe(val), charset);
	}

	/**
	 * 解码
	 * @param value 字符串
	 * @return {String}
	 */
	public static String decode(String value) {
		return Base64Util.decode(value, StandardCharsets.UTF_8);
	}

	/**
	 * 解码
	 * @param value 字符串
	 * @param charset 字符集
	 * @return {String}
	 */
	public static String decode(String value, Charset charset) {
		byte[] val = value.getBytes(charset);
		byte[] decodedValue = Base64Utils.decode(val);
		return new String(decodedValue, charset);
	}

	/**
	 * 解码URL安全
	 * @param value 字符串
	 * @return {String}
	 */
	public static String decodeUrlSafe(String value) {
		return Base64Util.decodeUrlSafe(value, StandardCharsets.UTF_8);
	}

	/**
	 * 解码URL安全
	 * @param value 字符串
	 * @param charset 字符集
	 * @return {String}
	 */
	public static String decodeUrlSafe(String value, Charset charset) {
		byte[] val = value.getBytes(charset);
		byte[] decodedValue = Base64Utils.decodeUrlSafe(val);
		return new String(decodedValue, charset);
	}

}
