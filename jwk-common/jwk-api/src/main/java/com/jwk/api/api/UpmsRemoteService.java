package com.jwk.api.api;

import com.jwk.api.constant.ServerNameConstants;
import com.jwk.api.dto.SysApiDto;
import com.jwk.api.dto.SysOauthClientDto;
import com.jwk.api.dto.UserInfo;
import com.jwk.common.core.constant.JwkSecurityConstants;
import com.jwk.common.core.model.InnerResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * Upms接口
 */
@FeignClient(name = ServerNameConstants.SERVER_UMPS, contextId = "authService")
public interface UpmsRemoteService {

	/**
	 * 根据姓名找用户
	 * @param name
	 * @return
	 */
	@GetMapping(value = "/user/findUserByName", headers = JwkSecurityConstants.HEADER_FROM_IN)
	InnerResponse<UserInfo> findUserByName(@RequestParam("name") String name);

	/**
	 * 根据手机找用户
	 * @param phone
	 * @return
	 */
	@GetMapping(value = "/user/findUserByPhone", headers = JwkSecurityConstants.HEADER_FROM_IN)
	InnerResponse<UserInfo> findUserByPhone(@RequestParam("phone") String phone);

	/**
	 * 根据邮箱找用户
	 * @param email
	 * @return
	 */
	@GetMapping(value = "/user/findUserByEmail", headers = JwkSecurityConstants.HEADER_FROM_IN)
	InnerResponse<UserInfo> findUserByEmail(@RequestParam("email") String email);

	/**
	 * 获取资源列表
	 * @return
	 */
	@GetMapping(value = "/sysApi/loadUserAuthoritiesByRole", headers = JwkSecurityConstants.HEADER_FROM_IN)
	InnerResponse<List<SysApiDto>> loadUserAuthoritiesByRole(@RequestParam("roleCode") String roleCode);

	/**
	 * 通过clientId 查询客户端信息
	 * @param clientId 用户名
	 * @return
	 */
	@GetMapping(value = "/oauthClient/getClientDetailsById/{clientId}", headers = JwkSecurityConstants.HEADER_FROM_IN)
	InnerResponse<SysOauthClientDto> getClientDetailsById(@PathVariable("clientId") String clientId);


	@GetMapping("/inner/admin/testSeata")
	InnerResponse<Integer> testSeata();

}
