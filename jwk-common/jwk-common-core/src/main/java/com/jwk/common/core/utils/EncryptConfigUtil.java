package com.jwk.common.core.utils;

import com.jwk.common.core.model.EncodeParamDto;
import java.util.ArrayList;
import java.util.List;
import org.jasypt.util.text.BasicTextEncryptor;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * jasypt-spring-boot-starter 生成密文的工具代码
 */
public class EncryptConfigUtil {

	/**
	 * 配置文件加密
	 * @param salt
	 * @param params
	 * @return
	 */
	private static List<EncodeParamDto> encodeParamList(String salt, List<EncodeParamDto> params) {
		List<EncodeParamDto> result = new ArrayList<>();
		for (EncodeParamDto param : params) {
			param.setValue(encryptParam(salt, param.getValue()));
			result.add(param);
		}
		return result;
	}

	/**
	 * 配置文件加密
	 * @param salt
	 * @param param
	 * @return
	 */
	private static String encryptParam(String salt, String param) {
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(salt);
		return textEncryptor.encrypt(param);
	}

	/**
	 * 配置文件解密
	 * @param salt
	 * @param param
	 * @return
	 */
	private static String decryptParam(String salt, String param) {
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(salt);
		return textEncryptor.decrypt(param);
	}

}