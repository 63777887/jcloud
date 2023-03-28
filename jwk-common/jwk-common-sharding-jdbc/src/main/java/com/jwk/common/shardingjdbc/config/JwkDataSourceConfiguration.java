package com.jwk.common.shardingjdbc.config;

import com.baomidou.dynamic.datasource.provider.AbstractDataSourceProvider;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import java.util.Map;
import javax.annotation.Resource;
import javax.sql.DataSource;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.shardingsphere.shardingjdbc.jdbc.core.connection.ShardingConnection;
import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;
import org.apache.shardingsphere.shardingjdbc.spring.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * 动态数据源配置：
 * <p>
 * 使用{@link com.baomidou.dynamic.datasource.annotation.DS}注解，切换数据源
 *
 * <code>@DS(DataSourceConfiguration.SHARDING_DATA_SOURCE_NAME)</code>
 */
@Configuration
@AutoConfigureBefore({ DynamicDataSourceAutoConfiguration.class, SpringBootConfiguration.class })
@Data
public class JwkDataSourceConfiguration {

	/**
	 * 分表数据源名称
	 */
	public static final String SHARDING_DATA_SOURCE_NAME = "mydb";

	@Resource
	private DynamicDataSourceProperties properties;

	/**
	 * shardingjdbc有四种数据源，需要根据业务注入不同的数据源
	 *
	 * <p>
	 * 1. 未使用分片, 脱敏的名称(默认): shardingDataSource;
	 * <p>
	 * 2. 主从数据源: masterSlaveDataSource;
	 * <p>
	 * 3. 脱敏数据源：encryptDataSource;
	 * <p>
	 * 4. 影子数据源：shadowDataSource
	 */
	/**
	 * 未使用分片, 脱敏的名称(默认): shardingDataSource shardingjdbc使用了主从: masterSlaveDataSource
	 * 此处如果主从策略配置在sharding属性下面，需要使用shardingDataSource
	 */
	@Lazy
	@Resource(name = "shardingDataSource")
	private DataSource shardingDataSource;

	@Bean
	public DynamicDataSourceProvider dynamicDataSourceProvider() {
		Map<String, DataSourceProperty> datasourceMap = properties.getDatasource();
		return new AbstractDataSourceProvider() {
			@SneakyThrows
			@Override
			public Map<String, DataSource> loadDataSources() {
				Map<String, DataSource> dataSourceMap = createDataSourceMap(datasourceMap);
				if (null != shardingDataSource) {
					ShardingDataSource dataSource = shardingDataSource.unwrap(ShardingDataSource.class);
					dataSourceMap.put(SHARDING_DATA_SOURCE_NAME, dataSource);
					// 打开下面的代码可以把 shardingjdbc 管理的数据源也交给动态数据源管理 (根据自己需要选择开启)
					dataSourceMap.putAll(((ShardingConnection) shardingDataSource.getConnection()).getDataSourceMap());
				}
				return dataSourceMap;
			}
		};
	}

}
