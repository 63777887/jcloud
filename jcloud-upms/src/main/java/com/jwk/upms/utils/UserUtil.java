package com.jwk.upms.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.StrUtil;
import com.jwk.common.core.constant.CharConstants;
import com.jwk.common.core.properties.MinioConfigProperties;
import com.jwk.common.security.dto.ResourceConfigAttribute;
import com.jwk.common.security.util.SecurityUtils;
import com.jwk.upms.base.dto.UserInfo;
import com.jwk.upms.base.entity.SysMenu;
import com.jwk.upms.base.entity.SysUser;
import com.jwk.upms.enums.MenuTypeE;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户工具类
 *
 * @author Jiwk
 * @version 0.1.6
 * @date 2024/3/13
 */
@UtilityClass
public class UserUtil {

    @NotNull
    public  UserInfo getUserInfo(SysUser user) {
        UserInfo userInfo = new UserInfo();
        userInfo.setSysUser(user);
        if (CollUtil.isNotEmpty(SecurityUtils.getAuthorities())){

            List<SysMenu> sysMenus = new ArrayList<>();

            SecurityUtils.getAuthorities().forEach(t->{
                sysMenus.add(((ResourceConfigAttribute) t).getSysMenu());
            });

            List<TreeNode<Long>> collect = sysMenus.stream().filter(menu -> MenuTypeE.MENU.getId().equals(menu.getType()))
                    .filter(menu -> StrUtil.isNotBlank(menu.getPath())).map(MenuUtil.getNodeFunction()).collect(Collectors.toList());
            List<SysMenu> buttons = sysMenus.stream().filter(menu -> MenuTypeE.BUTTON.getId().equals(menu.getType()))
                    .collect(Collectors.toList());
            userInfo.setButtons(buttons);
            userInfo.setSysMenu(TreeUtil.build(collect, -1L));
        }
        return userInfo;
    }

    @NotNull
    public String getRealIconAddr(SysUser user,MinioConfigProperties minioConfigProperties) {
        return minioConfigProperties.getAddress() + CharConstants.SLASH + minioConfigProperties.getBucket() + CharConstants.SLASH + user.getIcon();
    }
}
