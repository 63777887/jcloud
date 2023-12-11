package com.jwk.upms.base.dto;

import cn.hutool.core.lang.tree.Tree;
import com.jwk.upms.base.entity.SysMenu;
import com.jwk.upms.base.entity.SysUser;
import lombok.Data;

import java.util.List;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * 用户详情
 * @date 2022/6/11
 */
@Data
public class UserInfo {

	SysUser sysUser;

	List<Tree<Long>> sysMenu;

	List<SysMenu> buttons;

}
