package com.jwk.upms.web.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.jwk.common.core.enums.StatusE;
import com.jwk.common.core.exception.ServiceException;
import com.jwk.upms.base.dto.RegisterReq;
import com.jwk.upms.base.dto.UserInfo;
import com.jwk.upms.base.entity.*;
import com.jwk.upms.dto.UserImportDto;
import com.jwk.upms.enums.MenuTypeE;
import com.jwk.upms.enums.UserStatusE;
import com.jwk.upms.web.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * 远程认证
 * @date 2022/6/11
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final SysUserService sysUserService;

    private final SysUserRoleService userRoleService;

    private final SysRoleMenuService sysRoleMenuService;

    private final SysMenuService sysMenuService;

    private final SysOrgService sysOrgService;


    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    /**
     * 用户注册
     * @param user
     * @return
     */
    @Override
    @Transactional
    public Boolean register(RegisterReq user) {
        checkUserExist(user.getPhone(),user.getUsername(),user.getEmail());
        SysUser sysUser = Convert.convert(SysUser.class, user);
        sysUser.setPassword(passwordEncoder.encode(user.getPassword()));
        sysUser.setStatus(UserStatusE.Normal.getId());
        sysUser.setCreateTime(DateUtil.date());
        sysUserService.save(sysUser);
        // todo 给用户赋予角色和权限
        if (CollUtil.isNotEmpty(user.getRoles())) {
            List<SysUserRole> sysUserRoles = user.getRoles().stream().map(t -> {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUserId(sysUser.getId());
                sysUserRole.setRoleId(t);
                return sysUserRole;
            }).collect(Collectors.toList());
            userRoleService.saveBatch(sysUserRoles);
        }
        return Boolean.TRUE;
    }

    private void checkUserExist(String phone,String username,String email) {
        List<SysUser> allUser = sysUserService
                .lambdaQuery().and(tmp -> tmp.eq(SysUser::getPhone, phone).or()
                        .eq(SysUser::getUsername, username).or().eq(SysUser::getEmail, email))
                .list();
        allUser.forEach(t -> {
            if (phone.equals(t.getPhone())) {
                if (log.isWarnEnabled()){
                    log.warn("该手机号已被注册: {}",t.getPhone());
                }
                throw new ServiceException("该手机号已被注册！");
            }
            if (username.equals(t.getUsername())) {
                if (log.isWarnEnabled()) {
                    log.warn("该用户名已被注册: {}", t.getUsername());
                }
                throw new ServiceException("该用户名已被注册！");
            }
            if (email.equals(t.getEmail())) {
                if (log.isWarnEnabled()) {
                    log.warn("该邮箱已被注册: {}", t.getEmail());
                }
                throw new ServiceException("该邮箱已被注册！");
            }
        });
    }

    /**
     * 用户导入注册
     * @param datalist
     * @return
     */
    @Override
    public Boolean registerImportUsers(List<UserImportDto> datalist) {
        if (CollUtil.isEmpty(datalist)){
            return Boolean.TRUE;
        }
        List<UserImportDto> userlist = datalist.stream().filter(userImportDto -> {
            if (StrUtil.isBlank(userImportDto.getUsername())){
                if (log.isWarnEnabled()){
                    log.warn("用户名不能为空！");
                }
                return false;
            }
            if (StrUtil.isBlank(userImportDto.getPhone())){
                if (log.isWarnEnabled()){
                    log.warn("手机号不能为空！");
                }
                return false;
            }
            if (StrUtil.isBlank(userImportDto.getEmail())){
                if (log.isWarnEnabled()){
                    log.warn("邮箱不能为空！");
                }
                return false;
            }
            try {
                checkUserExist(userImportDto.getUsername(), userImportDto.getPhone(), userImportDto.getEmail());
                return true;
            } catch (Exception e) {
                return false;
            }
        }).collect(Collectors.toList());

        List<String> orgNames = userlist.stream().map(UserImportDto::getOrgName).filter(StrUtil::isNotBlank).distinct().collect(Collectors.toList());
        List<SysOrg> sysOrgs = new ArrayList<>();
        if (CollUtil.isNotEmpty(orgNames)) {
            sysOrgs = sysOrgService.lambdaQuery().in(SysOrg::getOrgName, orgNames).list();
        }
        List<SysOrg> finalSysOrgs = sysOrgs;
        List<SysUser> sysUserList = userlist.stream().map(t -> {
            SysUser sysUser = Convert.convert(SysUser.class, t);
            // todo 后续从字典中获取
            sysUser.setPassword(passwordEncoder.encode("Awert159"));
            sysUser.setStatus(UserStatusE.Normal.getId());
            sysUser.setCreateTime(DateUtil.date());
            if (StrUtil.isNotBlank(t.getOrgName())) {
                for (SysOrg sysOrg : finalSysOrgs) {
                    if (sysOrg.getOrgName().equals(t.getOrgName())) {
                        sysUser.setOrgId(sysOrg.getId());
                        break;
                    }
                }
            }
            return sysUser;
        }).collect(Collectors.toList());
        sysUserService.saveBatch(sysUserList);
        return Boolean.TRUE;
    }

    @Override
    public UserInfo findUserByName(String name) {
        SysUser user = sysUserService.lambdaQuery().eq(SysUser::getUsername, name).one();
        return getUserInfo(user);
    }

    private UserInfo getUserInfo(SysUser user) {
        if (BeanUtil.isEmpty(user)) {
            return null;
        }
        // 加载用户角色列表
        List<SysUserRole> sysUserRoles = userRoleService.lambdaQuery().eq(SysUserRole::getUserId, user.getId()).list();
        List<Long> roleIds = sysUserRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());

        List<SysMenu> sysMenus = new ArrayList<>();
        for (Long roleId : roleIds) {
            // 通过用户角色列表加载用户的资源权限列表
            List<SysRoleMenu> sysRoleMenuList = sysRoleMenuService.lambdaQuery().eq(SysRoleMenu::getRoleId, roleId).list();
            if (CollUtil.isEmpty(sysRoleMenuList)){
                continue;
            }
            List<Long> menuIds = sysRoleMenuList.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
            List<SysMenu> sysMenuList = sysMenuService.lambdaQuery().eq(SysMenu::getType, MenuTypeE.BUTTON.getId()).in(SysMenu::getId,menuIds).list();
            if (CollUtil.isNotEmpty(sysMenuList)) {
                sysMenus.addAll(sysMenuList);
            }
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setSysUser(user);
        userInfo.setButtons(sysMenus);
        return userInfo;
    }

    @Override
    public UserInfo findUserByPhone(String phone) {
        SysUser user = sysUserService.lambdaQuery().eq(SysUser::getPhone, phone).one();
        return getUserInfo(user);
    }

    @Override
    public List<SysMenu> resourceList() {
        List<SysMenu> list = sysMenuService.lambdaQuery().eq(SysMenu::getStatus, StatusE.Normal.getId()).list();
        return list;
    }

    @Override
    public Integer testSeata() {
        SysUser sysUser = new SysUser();
        sysUser.setOrgId(100L);
        sysUser.setId(4L);
        boolean update = sysUserService.lambdaUpdate().update(sysUser);
        return update ? 1 : 0;
    }

    @Override
    public UserInfo findUserByEmail(String mail) {
        SysUser user = sysUserService.lambdaQuery().eq(SysUser::getEmail, mail).one();
        return getUserInfo(user);
    }

}
