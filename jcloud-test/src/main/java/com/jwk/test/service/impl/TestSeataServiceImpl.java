package com.jwk.test.service.impl;

import com.jwk.api.api.UpmsRemoteService;
import com.jwk.test.mapper.UserMapper;
import com.jwk.test.service.TestSeataService;
//import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Jiwk
 * @date 2022/8/15
 * @version 0.1.0
 * <p>
 * 测试Seata
 */
@Service
public class TestSeataServiceImpl implements TestSeataService {

	@Autowired
	UserMapper userMapper;

	@Autowired
	UpmsRemoteService upmsRemoteService;

	@Override
	// @GlobalTransactional(rollbackFor = Exception.class)
	public int testSeata() {
		 upmsRemoteService.testSeata();
//		int i = userMapper.addOrginId();
		// int i1 = 3/0;
		return 0;
	}

}
