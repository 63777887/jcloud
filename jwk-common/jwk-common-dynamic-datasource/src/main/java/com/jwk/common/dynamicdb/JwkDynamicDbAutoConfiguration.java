package com.jwk.common.dynamicdb;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 自动配置类
 */
@AutoConfigureBefore({ DynamicDataSourceAutoConfiguration.class, SpringBootConfiguration.class })
@EnableConfigurationProperties(JwkDynamicDataSourceProperties.class)
public class JwkDynamicDbAutoConfiguration {

	@Resource
	private DynamicDataSourceProperties dynamicDataSourceProperties;

	@Resource
	private JwkDynamicDataSourceProperties jwkDynamicDataSourceProperties;

	@Resource
	private DataSourceProperties dataSourceProperties;

	/**
	 * 将动态数据源设置为首选的 当spring存在多个数据源时, 自动注入的是首选的对象 设置为主要的数据源之后，就可以支持shardingjdbc原生的配置方式了
	 */
	@Primary
	@Bean
	@ConditionalOnMissingBean
	public DataSource dataSource() {
		DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource();
		dataSource.setPrimary(dynamicDataSourceProperties.getPrimary());
		dataSource.setStrict(dynamicDataSourceProperties.getStrict());
		dataSource.setStrategy(dynamicDataSourceProperties.getStrategy());
		dataSource.setP6spy(dynamicDataSourceProperties.getP6spy());
		dataSource.setSeata(dynamicDataSourceProperties.getSeata());
		// 加载spring-datasource配置
		if (null != dataSourceProperties) {
			DataSourceProperty dataSourceProperty = new DataSourceProperty();
			BeanUtil.copyProperties(dataSourceProperties, dataSourceProperty);
			dynamicDataSourceProperties.getDatasource().put("master", dataSourceProperty);
		}
		// 加载jcloud配置
		DynamicDataSourceProperties dynamic = jwkDynamicDataSourceProperties.getDynamic();
		if (null != dynamic) {
			this.dynamicDataSourceProperties.getDatasource().putAll(dynamic.getDatasource());
		}
		return dataSource;
	}

}
