package com.jwk.upms.web.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jwk.common.core.excel.ExcelReq;
import com.jwk.common.core.model.RestResponse;
import com.jwk.common.core.utils.AssertUtil;
import com.jwk.common.security.annotation.Inner;
import com.jwk.common.security.annotation.UserParam;
import com.jwk.upms.base.dto.RegisterReq;
import com.jwk.upms.base.dto.UserInfo;
import com.jwk.upms.base.entity.SysUser;
import com.jwk.upms.web.dao.SysUserMapper;
import com.jwk.upms.dto.UpdatePasswordDto;
import com.jwk.upms.dto.UserDto;
import com.jwk.upms.web.service.AuthService;
import com.jwk.upms.web.service.ExcelService;
import com.jwk.upms.web.service.SysUserService;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * `sys_user` 前端控制器
 * </p>
 *
 * @author jiwk
 * @since 2021-06-03
 */
@RestController
@RequestMapping("/user")
public class SysUserController {

    @Autowired
    AuthService authService;

    @Autowired
    SysUserMapper sysUserMapper;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ExcelService excelService;

    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    /**
     * 根据用户名查找用户
     */
    @Inner
    @GetMapping(value = "/findUserByName")
    public RestResponse<UserInfo> findUserByName(@RequestParam("name") String name) {
        return RestResponse.success(authService.findUserByName(name));
    }

    /**
     * 根据手机号查找用户
     */
    @Inner
    @GetMapping(value = "/findUserByPhone")
    public RestResponse<UserInfo> findUserByPhone(@RequestParam("phone") String phone) {
        return RestResponse.success(authService.findUserByPhone(phone));
    }

    /**
     * 根据邮箱查找用户
     */
    @Inner
    @GetMapping(value = "/findUserByEmail")
    public RestResponse<UserInfo> findUserByEmail(@RequestParam("email") String mail) {
        return RestResponse.success(authService.findUserByEmail(mail));
    }

    /**
     * 根据token查找用户
     */
    @GetMapping(value = "/get")
    public RestResponse<UserInfo> get(@UserParam SysUser user) {
        return RestResponse.success(sysUserService.findUserById(user.getId()));
    }

    /**
     * 注册用户
     */
    @PostMapping(value = "/register")
    @PreAuthorize("@pms.hasPermission()")
    public RestResponse register(@RequestBody @Validated RegisterReq registerReq) {
        return RestResponse.success(authService.register(registerReq));
    }

//    /**
//     * 导入用户
//     */
//    @PostMapping(value = "/importUser", consumes = "multipart/form-data")
////    @PreAuthorize("@pms.hasPermission()")
//    public RestResponse exportUser(@RequestParam("file") MultipartFile file, @RequestPart("excelReq") ExcelReq excelReq) {
//        return RestResponse.success(excelService.importData(file,excelReq));
//    }

    /**
     * 导入用户
     */
    @PostMapping(value = "/importUser")
//    @PreAuthorize("@pms.hasPermission()")
    public RestResponse exportUser(@RequestParam("file") MultipartFile file) {
        return RestResponse.success(excelService.importData(file));
    }

    /**
     * 导入用户
     */
    @PostMapping(value = "/exportUser")
//    @PreAuthorize("@pms.hasPermission()")
    public void exportUser(HttpServletResponse response, @RequestBody ExcelReq excelReq) {
        excelService.exportData(response,excelReq);
    }

    /**
     * 更新用户信息
     */
    @PostMapping(value = "/update")
    @PreAuthorize("@pms.hasPermission()")
    public RestResponse update(@RequestBody @Validated UserDto userDto) {
        return RestResponse.success(sysUserService.updateUser(userDto));
    }

    /**
     * 更新密码
     */
    @PostMapping(value = "/updatePassword")
    public RestResponse updatePassword(@UserParam SysUser sysUser, @RequestBody @Validated UpdatePasswordDto updatePasswordDto) {
        AssertUtil.isTrue(passwordEncoder.matches(updatePasswordDto.getOldPassword(), sysUser.getPassword()), "原密码错误");
        AssertUtil.isTrue(updatePasswordDto.getNewPassword().equals(updatePasswordDto.getConfirmPassword()), "两次输入密码不一致");
        sysUser.setPassword(passwordEncoder.encode(updatePasswordDto.getNewPassword()));
        return RestResponse.success(sysUserService.updateById(sysUser));
    }

    /**
     * 根据ID查找用户信息
     */
    @GetMapping(value = "/{id}")
    public RestResponse getUserById(@PathVariable Long id) {
        AssertUtil.isTrue(id > 0L, "用户ID不能为空");
        return RestResponse.success(sysUserService.getUserById(id));
    }

    /**
     * 根据ID删除用户信息
     */
    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("@pms.hasPermission()")
    public RestResponse delete(@PathVariable Long id) {
        AssertUtil.isTrue(id > 0L, "用户ID不能为空");
        return RestResponse.success(sysUserService.deleteUser(id));
    }

    /**
     * 根据ID删除用户信息
     */
    @PostMapping(value = "/deleteBatch")
    @PreAuthorize("@pms.hasPermission()")
    public RestResponse deleteBatch(@RequestBody List<Long> userIds) {
        if (CollUtil.isEmpty(userIds)) {
            RestResponse.success();
        }
        return RestResponse.success(sysUserService.deleteBatch(userIds));
    }

    /**
     * 用户列表
     */
    @GetMapping(value = "/list")
    public RestResponse list(Page<SysUser> page, UserDto userDto) {
        return RestResponse.success(sysUserService.getUserList(page, userDto));
    }

    /**
     * 用户列表
     */
    @GetMapping(value = "/test")
    public RestResponse test() {

        return RestResponse.success(sysUserMapper.updateUser(Arrays.asList(1L, 2L, 3L, 4L)));
    }

}
