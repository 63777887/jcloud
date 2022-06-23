package com.jwk.uaa.service;

import javax.sql.DataSource;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.server.handler.ResponseStatusExceptionHandler;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * {@link org.springframework.security.oauth2.provider.client.JdbcClientDetailsService}
 */
@Component
public class JwkClientDetailsService extends JdbcClientDetailsService {

	public JwkClientDetailsService(DataSource dataSource) {
		super(dataSource);
//		//	配置JdbcClientDetailsService，用于查询Client信息
//		//	设置默认根据client_id查询Client语句
//		setSelectClientDetailsSql(SecurityConstants.DEFAULT_SELECT_STATEMENT);
//		//	设置默认的查询所有Client语句
//		setFindClientDetailsSql(SecurityConstants.DEFAULT_FIND_STATEMENT);
	}

//	/**
//	 * 重写原生方法支持redis缓存
//	 * @param clientId
//	 * @return
//	 */
//	@Override
//	@SneakyThrows
//	@Cacheable(value = CacheConstants.CLIENT_DETAILS_KEY, key = "#clientId", unless = "#result == null")
//	public ClientDetails loadClientByClientId(String clientId) {
//		return super.loadClientByClientId(clientId);
//	}

}
