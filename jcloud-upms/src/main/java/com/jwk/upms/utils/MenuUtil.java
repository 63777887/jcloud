package com.jwk.upms.utils;

import cn.hutool.core.lang.tree.TreeNode;
import com.jwk.upms.base.entity.SysMenu;
import lombok.experimental.UtilityClass;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * 菜单工具类
 *
 * @author Jiwk
 * @version 0.1.6
 * @date 2024/3/13
 */
@UtilityClass
public class MenuUtil {

    @NotNull
    public Function<SysMenu, TreeNode<Long>> getNodeFunction() {
        return menu -> {
            TreeNode<Long> node = new TreeNode<>();
            node.setId(menu.getId());
            node.setName(menu.getMenuName());
            node.setParentId(menu.getParentId());
            node.setWeight(menu.getSort());
            // 扩展属性
            Map<String, Object> extra = new HashMap<>();
            extra.put("icon", menu.getIcon());
            extra.put("path", menu.getPath());
            extra.put("menuName", menu.getMenuName());
            extra.put("sort", menu.getSort());
            extra.put("hidden", menu.getHidden());
            extra.put("tab", menu.getTab());
            node.setExtra(extra);
            return node;
        };
    }

}
