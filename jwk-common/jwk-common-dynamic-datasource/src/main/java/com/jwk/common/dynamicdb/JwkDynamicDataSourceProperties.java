package com.jwk.common.dynamicdb;

import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 配置文件
 */
@Data
@ConfigurationProperties(prefix = JwkDynamicDataSourceProperties.PREFIX)
@AutoConfigureBefore({ DynamicDataSourceAutoConfiguration.class, SpringBootConfiguration.class })
public class JwkDynamicDataSourceProperties {

	public static final String PREFIX = "jwk.datasource";

	@NestedConfigurationProperty
	private DynamicDataSourceProperties dynamic;

}
