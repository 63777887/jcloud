package com.jwk.common.core.factory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.lang.Nullable;

/**
 * @author Jiwk
 * @date 2022/7/13
 * @version 0.1.0
 * <p>
 * 读取自定义 yaml 文件工厂类
 */
public class JwkYamlPropertySourceFactory implements PropertySourceFactory {

	@Override
	public PropertySource<?> createPropertySource(@Nullable String name, EncodedResource resource) throws IOException {
		Properties propertiesFromYaml = loadYamlIntoProperties(resource);
		String sourceName = name != null ? name : resource.getResource().getFilename();
		return new PropertiesPropertySource(sourceName, propertiesFromYaml);
	}

	private Properties loadYamlIntoProperties(EncodedResource resource) throws FileNotFoundException {
		try {
			YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
			factory.setResources(resource.getResource());
			factory.afterPropertiesSet();
			return factory.getObject();
		}
		catch (IllegalStateException e) {
			Throwable cause = e.getCause();
			if (cause instanceof FileNotFoundException) {
				throw (FileNotFoundException) e.getCause();
			}
			throw e;
		}
	}

}
