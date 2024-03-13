package com.jwk.upms.base.api;

import com.jwk.common.core.constant.JwkSecurityConstants;
import com.jwk.common.core.constant.ServerNameConstants;
import com.jwk.common.core.model.RestResponse;
import com.jwk.upms.base.dto.RemoveTokenDto;
import com.jwk.upms.base.dto.SysOauthClientDto;
import com.jwk.upms.base.dto.UserInfo;
import com.jwk.upms.base.entity.SysMenu;
import com.jwk.upms.base.entity.SysSetting;
import com.jwk.upms.base.entity.SysUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * Upms接口
 * @date 2022/6/11
 */
@FeignClient(name = ServerNameConstants.SERVER_UMPS, contextId = "authService")
public interface UpmsRemoteService {

    /**
     * 根据姓名找用户
     *
     * @param name
     * @return
     */
    @GetMapping(value = "/user/findUserByName", headers = JwkSecurityConstants.HEADER_FROM_IN)
    RestResponse<UserInfo> findUserByName(@RequestParam("name") String name);

    /**
     * 根据手机找用户
     *
     * @param phone
     * @return
     */
    @GetMapping(value = "/user/findUserByPhone", headers = JwkSecurityConstants.HEADER_FROM_IN)
    RestResponse<UserInfo> findUserByPhone(@RequestParam("phone") String phone);

    /**
     * 根据邮箱找用户
     *
     * @param email
     * @return
     */
    @GetMapping(value = "/user/findUserByEmail", headers = JwkSecurityConstants.HEADER_FROM_IN)
    RestResponse<UserInfo> findUserByEmail(@RequestParam("email") String email);

    /**
     * 获取资源列表
     *
     * @return
     */
    @GetMapping(value = "/sysMenu/loadUserAuthoritiesByRole", headers = JwkSecurityConstants.HEADER_FROM_IN)
    RestResponse<List<SysMenu>> loadUserAuthoritiesByRole(@RequestParam("roleCodeList") List<String> roleCode);

    /**
     * 通过clientId 查询客户端信息
     *
     * @param clientId 用户名
     * @return
     */
    @GetMapping(value = "/oauthClient/getClientDetailsById/{clientId}", headers = JwkSecurityConstants.HEADER_FROM_IN)
    RestResponse<SysOauthClientDto> getClientDetailsById(@PathVariable("clientId") String clientId);

    @GetMapping("/inner/admin/testSeata")
    RestResponse<Integer> testSeata();

    @GetMapping(value = "/sysSetting/getSysSetting", headers = JwkSecurityConstants.HEADER_FROM_IN)
    RestResponse<List<SysSetting>> getSysSetting(@RequestParam(value = "orgId", required = false) Long orgId, @RequestParam("paramKey") String paramKey, @RequestParam("paramType") Byte paramType);

    @PostMapping(value = "/system/removeToken", headers = JwkSecurityConstants.HEADER_FROM_IN)
    RestResponse<List<SysSetting>> removeToken(@RequestBody RemoveTokenDto removeTokenDto);

}
