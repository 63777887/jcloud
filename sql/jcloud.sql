DROP DATABASE IF EXISTS `jcloud`;

CREATE DATABASE  `jcloud` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

USE jcloud;


SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_api
-- ----------------------------
DROP TABLE IF EXISTS `sys_api`;
CREATE TABLE `sys_api` (
                           `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                           `api_desc` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT '接口描述',
                           `url` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT '接口URL',
                           `status` tinyint(1) DEFAULT NULL COMMENT '状态，1: 正常，2:删除',
                           `create_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'createBy',
                           `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'createTime',
                           `update_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'updateBy',
                           `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'updateTime',
                           PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='`sys_api`';

-- ----------------------------
-- Records of sys_api
-- ----------------------------
BEGIN;
INSERT INTO `sys_api` VALUES (8, '添加用户', '/user/register', 1, NULL, '2023-10-27 16:25:37', NULL, '2023-10-27 16:25:48');
INSERT INTO `sys_api` VALUES (9, '批量删除用户', '/user/deleteBatch', NULL, NULL, '2023-10-27 16:26:06', NULL, '2023-10-27 16:27:25');
INSERT INTO `sys_api` VALUES (10, '编辑用户', '/user/update', NULL, NULL, '2023-10-27 16:26:16', NULL, '2023-10-27 16:26:16');
INSERT INTO `sys_api` VALUES (11, '删除用户', '/user/delete', NULL, NULL, '2023-10-27 16:27:11', NULL, '2023-10-27 18:24:44');
INSERT INTO `sys_api` VALUES (12, '添加角色', '/sysRole/add', NULL, NULL, '2023-10-27 16:27:32', NULL, '2023-10-27 16:27:35');
INSERT INTO `sys_api` VALUES (13, '编辑角色', '/sysRole/update', NULL, NULL, '2023-10-27 16:27:44', NULL, '2023-10-27 16:27:44');
INSERT INTO `sys_api` VALUES (14, '删除角色', '/sysRole/delete', NULL, NULL, '2023-10-27 16:27:49', NULL, '2023-10-27 16:27:53');
INSERT INTO `sys_api` VALUES (15, '分配权限', '/sysRole/setRoleMenu', NULL, NULL, '2023-10-27 16:27:58', NULL, '2023-10-27 18:25:00');
INSERT INTO `sys_api` VALUES (16, '新增菜单', '/sysMenu/add', NULL, NULL, '2023-10-27 16:28:10', NULL, '2023-10-27 16:28:10');
INSERT INTO `sys_api` VALUES (17, '编辑菜单', '/sysMenu/update', NULL, NULL, '2023-10-27 16:28:19', NULL, '2023-10-27 16:28:19');
INSERT INTO `sys_api` VALUES (18, '删除菜单', '/sysMenu/delete', NULL, NULL, '2023-10-27 16:28:28', NULL, '2023-10-27 16:28:28');
INSERT INTO `sys_api` VALUES (19, 'Excel导入', '/excel/importExcel', 1, NULL, '2023-10-27 16:25:37', NULL, '2023-12-11 15:52:08');
INSERT INTO `sys_api` VALUES (20, 'Excel导出', '/excel/exportExcel', 1, NULL, '2023-10-27 16:25:37', NULL, '2023-12-11 15:52:08');
COMMIT;

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
                           `id` bigint NOT NULL AUTO_INCREMENT,
                           `log_type` tinyint(1) DEFAULT NULL COMMENT '日志类型',
                           `log_title` varchar(255) DEFAULT NULL COMMENT '日志标题',
                           `remote_addr` varchar(255) DEFAULT NULL COMMENT '操作IP地址',
                           `request_uri` varchar(255) DEFAULT NULL COMMENT '请求URI',
                           `service_id` varchar(32) DEFAULT NULL COMMENT '服务ID',
                           `user_agent` varchar(1024) DEFAULT NULL COMMENT '用户代理',
                           `method` varchar(10) DEFAULT NULL COMMENT '操作方式',
                           `params` text COMMENT '操作提交的数据',
                           `time` bigint DEFAULT NULL COMMENT '执行时间',
                           `exception` text COMMENT '异常信息',
                           `status` tinyint(1) DEFAULT NULL COMMENT '1:成功，2:失败',
                           `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'createTime',
                           `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'updateTime',
                           `create_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'createBy',
                           `update_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'updateBy',
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1734123026825367555 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of sys_log
-- ----------------------------
BEGIN;
INSERT INTO `sys_log` VALUES (1734114823119065090, 1, '登陆系统', '127.0.0.1', '/oauth2/token', 'jiwkClient', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36', 'POST', 'grant_type=%5Bpassword%5D&scope=%5BCSJS,server%5D&username=%5Bjiwk%5D&password=%5BAwert159%5D', NULL, NULL, 1, '2023-12-11 15:36:07', '2023-12-11 15:36:06', '1', NULL);
INSERT INTO `sys_log` VALUES (1734114844929445889, 2, '退出系统', '127.0.0.1', '/token/logout', 'jiwkClient', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36', 'DELETE', '', NULL, NULL, 1, '2023-12-11 15:36:12', '2023-12-11 15:36:11', '1', NULL);
INSERT INTO `sys_log` VALUES (1734114864156135425, 1, '登陆系统', '127.0.0.1', '/oauth2/token', 'jiwkClient', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36', 'POST', 'grant_type=%5Bpassword%5D&scope=%5BCSJS,server%5D&username=%5Bjiwk%5D&password=%5BAwert159%5D', NULL, NULL, 1, '2023-12-11 15:36:17', '2023-12-11 15:36:16', '1', NULL);
INSERT INTO `sys_log` VALUES (1734114952458817538, 1, '登陆系统', '127.0.0.1', '/oauth2/token', 'jiwkClient', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36', 'POST', 'grant_type=%5Bpassword%5D&scope=%5BCSJS,server%5D&username=%5Bjiwk0001%5D&password=%5BAwert159%5D', NULL, NULL, 1, '2023-12-11 15:36:38', '2023-12-11 15:36:37', '17', NULL);
INSERT INTO `sys_log` VALUES (1734117826995703810, 2, '退出系统', '127.0.0.1', '/token/logout', 'jiwkClient', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36', 'DELETE', '', NULL, NULL, 1, '2023-12-11 15:48:02', '2023-12-11 15:48:03', '1', NULL);
INSERT INTO `sys_log` VALUES (1734117840727855106, 1, '登陆系统', '127.0.0.1', '/oauth2/token', 'jiwkClient', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36', 'POST', 'grant_type=%5Bpassword%5D&scope=%5BCSJS,server%5D&username=%5Bjiwk%5D&password=%5BAwert159%5D', NULL, NULL, 1, '2023-12-11 15:48:06', '2023-12-11 15:48:06', '1', NULL);
INSERT INTO `sys_log` VALUES (1734123001059758082, 2, '退出系统', '127.0.0.1', '/token/logout', 'jiwkClient', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36', 'DELETE', '', NULL, NULL, 1, '2023-12-11 16:08:36', '2023-12-11 16:08:36', '1', NULL);
INSERT INTO `sys_log` VALUES (1734123026825367554, 1, '登陆系统', '127.0.0.1', '/oauth2/token', 'jiwkClient', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36', 'POST', 'grant_type=%5Bpassword%5D&scope=%5BCSJS,server%5D&username=%5Bjiwk%5D&password=%5BAwert159%5D', NULL, NULL, 1, '2023-12-11 16:08:43', '2023-12-11 16:08:42', '1', NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
                            `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                            `parent_id` bigint DEFAULT NULL COMMENT '父级ID',
                            `menu_name` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT '菜单名',
                            `sort` int DEFAULT NULL COMMENT '排序',
                            `permission` varchar(32) DEFAULT NULL COMMENT '权限值',
                            `path` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT '路径',
                            `icon` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT '图标',
                            `status` tinyint(1) DEFAULT '1' COMMENT '状态，1: 正常，2:删除',
                            `hidden` tinyint(1) DEFAULT '1' COMMENT '1: 展示, 2: 隐藏',
                            `tab` tinyint(1) DEFAULT '1' COMMENT '是否展示在tabs，1: 展示，2:不展示',
                            `type` tinyint(1) DEFAULT NULL COMMENT '1：菜单，2: 按钮',
                            `create_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'createBy',
                            `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'createTime',
                            `update_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'updateBy',
                            `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'updateTime',
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='`sys_menu`';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_menu` VALUES (-1, 0, '根菜单', 1, NULL, '/', NULL, 1, 2, 2, 1, NULL, '2023-10-19 18:12:45', NULL, '2023-10-19 18:15:33');
INSERT INTO `sys_menu` VALUES (1, -1, '权限管理', 1, NULL, '/acl', 'Lock', 1, 1, 2, 1, NULL, '2023-10-12 15:25:24', NULL, '2023-12-08 13:24:49');
INSERT INTO `sys_menu` VALUES (2, 1, '用户管理', 1, NULL, '/acl/user', 'User', 1, 1, 1, 1, NULL, '2023-10-12 15:25:24', NULL, '2023-12-11 13:21:35');
INSERT INTO `sys_menu` VALUES (3, 1, '角色管理', 2, NULL, '/acl/role', 'UserFilled', 1, 1, 1, 1, NULL, '2023-10-12 15:25:24', NULL, '2023-10-26 10:28:00');
INSERT INTO `sys_menu` VALUES (4, 1, '菜单管理', 3, NULL, '/acl/menu', 'Grid', 1, 1, 1, 1, NULL, '2023-10-12 15:25:24', NULL, '2023-10-26 10:40:15');
INSERT INTO `sys_menu` VALUES (7, 2, '添加用户', 1, 'sys_user_add', '/user/register', NULL, 1, 1, 1, 2, NULL, '2023-10-20 15:49:48', NULL, '2023-10-24 16:55:20');
INSERT INTO `sys_menu` VALUES (8, 2, '批量删除', 2, 'sys_delete_user_batch', '/user/deleteBatch', NULL, 1, 1, 1, 2, NULL, '2023-10-24 20:23:23', NULL, '2023-10-24 20:23:23');
INSERT INTO `sys_menu` VALUES (9, 2, '编辑用户', 1, 'sys_user_update', '/user/update', NULL, 1, 1, 1, 2, NULL, '2023-10-25 10:30:52', NULL, '2023-10-25 10:30:52');
INSERT INTO `sys_menu` VALUES (10, 2, '删除用户', 1, 'sys_user_delete', '/user/delete', NULL, 1, 1, 1, 2, NULL, '2023-10-25 10:31:23', NULL, '2023-10-27 18:25:50');
INSERT INTO `sys_menu` VALUES (11, 3, '添加角色', 1, 'sys_role_add', '/sysRole/add', NULL, 1, 1, 1, 2, NULL, '2023-10-25 10:38:57', NULL, '2023-10-25 10:38:57');
INSERT INTO `sys_menu` VALUES (12, 3, '编辑角色', 1, 'sys_role_update', '/sysRole/update', NULL, 1, 1, 1, 2, NULL, '2023-10-25 10:41:05', NULL, '2023-10-25 10:41:05');
INSERT INTO `sys_menu` VALUES (13, 3, '删除角色', 1, 'sys_role_delete', '/sysRole/delete', NULL, 1, 1, 1, 2, NULL, '2023-10-25 10:41:34', NULL, '2023-10-25 10:41:34');
INSERT INTO `sys_menu` VALUES (14, 3, '分配权限', 1, 'sys_role_update-menu', '/sysRole/setRoleMenu', NULL, 1, 1, 1, 2, NULL, '2023-10-25 10:42:51', NULL, '2023-10-27 18:25:36');
INSERT INTO `sys_menu` VALUES (15, 4, '新增菜单', 1, 'sys_menu-add', '/sysMenu/add', NULL, 1, 1, 1, 2, NULL, '2023-10-25 10:48:24', NULL, '2023-10-25 10:48:24');
INSERT INTO `sys_menu` VALUES (16, 4, '编辑菜单', 1, 'sys_menu-update', '/sysMenu/update', NULL, 1, 1, 1, 2, NULL, '2023-10-25 10:49:55', NULL, '2023-10-25 10:51:28');
INSERT INTO `sys_menu` VALUES (17, 4, '删除菜单', 1, 'sys_menu-delete', '/sysMenu/delete', NULL, 1, 1, 1, 2, NULL, '2023-10-25 10:50:28', NULL, '2023-10-25 10:50:28');
INSERT INTO `sys_menu` VALUES (18, 2, '用户导入', 1, 'sys_user_import', '/excel/importExcel', NULL, 1, 1, 1, 2, NULL, '2023-12-11 15:49:49', NULL, '2023-12-11 15:52:48');
INSERT INTO `sys_menu` VALUES (19, 2, '用户导出', 1, 'sys_user_export', '/excel/exportExcel', NULL, 1, 1, 1, 2, NULL, '2023-12-11 15:53:24', NULL, '2023-12-11 15:55:03');
COMMIT;

-- ----------------------------
-- Table structure for sys_oauth_client
-- ----------------------------
DROP TABLE IF EXISTS `sys_oauth_client`;
CREATE TABLE `sys_oauth_client` (
                                    `client_id` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL COMMENT '客户端ID',
                                    `client_name` varchar(256) DEFAULT NULL COMMENT '客户端名称',
                                    `resource_ids` varchar(256) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT '资源列表',
                                    `client_secret` varchar(256) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT '客户端密钥',
                                    `scope` varchar(256) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT '域',
                                    `authorized_grant_types` varchar(256) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT '认证类型',
                                    `web_server_redirect_uri` varchar(256) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT '重定向地址',
                                    `access_token_validity` int DEFAULT NULL COMMENT 'token 有效期',
                                    `refresh_token_validity` int DEFAULT NULL COMMENT '刷新令牌有效期',
                                    `additional_information` varchar(4096) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT '令牌扩展字段JSON',
                                    `autoapprove` varchar(256) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT '是否自动放行',
                                    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                    `create_by` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
                                    `update_by` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
                                    PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of sys_oauth_client
-- ----------------------------
BEGIN;
INSERT INTO `sys_oauth_client` VALUES ('jiwkClient', '测试客户端', NULL, '{bcrypt}$2a$10$SptM6/TuOvM68SLbY/ewHuOHVqI9mF1QlBIDXIvkJDOWd3RufCjZC', 'server,CSJS,openid,test', 'authorization_code,password,phone,refresh_token,code,client_credentials,email', 'http://www.baidu.com', 8640, 86400, NULL, NULL, '2022-11-22 19:40:50', NULL, 'jiwk', NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_org
-- ----------------------------
DROP TABLE IF EXISTS `sys_org`;
CREATE TABLE `sys_org` (
                           `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                           `parent_id` bigint DEFAULT NULL COMMENT 'orgPid',
                           `org_name` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'orgName',
                           `address` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'address',
                           `phone` varchar(13) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'phone',
                           `email` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'email',
                           `org_sort` int DEFAULT NULL COMMENT 'orgSort',
                           `level` int DEFAULT NULL COMMENT 'level',
                           `status` tinyint(1) DEFAULT NULL COMMENT 'status',
                           `create_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'createBy',
                           `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'createTime',
                           `update_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'updateBy',
                           `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'updateTime',
                           PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='`sys_org`';

-- ----------------------------
-- Records of sys_org
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
                            `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                            `role_name` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称',
                            `role_desc` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT '角色描述',
                            `code` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL COMMENT '角色标识',
                            `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态，1: 正常，2:删除',
                            `create_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'createBy',
                            `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'createTime',
                            `update_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'updateBy',
                            `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'updateTime',
                            PRIMARY KEY (`id`) USING BTREE,
                            UNIQUE KEY `code_index` (`code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='`sys_role`';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` VALUES (1, '测试角色', '测试角色\n', 'CSJS', 1, NULL, '2023-10-12 15:25:24', NULL, '2023-10-17 16:21:05');
INSERT INTO `sys_role` VALUES (2, '系统角色', '系统角色', 'server', 1, NULL, '2023-10-12 15:25:24', NULL, '2023-10-12 15:25:51');
INSERT INTO `sys_role` VALUES (3, '开放角色', '开放角色', 'openid', 1, NULL, '2023-10-12 15:25:24', NULL, '2023-10-17 14:01:58');
INSERT INTO `sys_role` VALUES (4, '测试角色001', '这是我的测试角色啊', 'test001', 1, NULL, '2023-10-17 14:57:05', NULL, '2023-10-17 16:26:08');
COMMIT;

-- ----------------------------
-- Table structure for sys_role_api
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_api`;
CREATE TABLE `sys_role_api` (
                                `id` bigint NOT NULL AUTO_INCREMENT,
                                `role_id` bigint NOT NULL COMMENT 'roleId',
                                `api_id` bigint NOT NULL COMMENT 'apiId',
                                PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='`sys_role_api`';

-- ----------------------------
-- Records of sys_role_api
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_api` VALUES (3, 2, 1);
INSERT INTO `sys_role_api` VALUES (6, 2, 15);
INSERT INTO `sys_role_api` VALUES (8, 3, 2);
INSERT INTO `sys_role_api` VALUES (20, 1, 8);
INSERT INTO `sys_role_api` VALUES (21, 1, 9);
INSERT INTO `sys_role_api` VALUES (22, 1, 10);
INSERT INTO `sys_role_api` VALUES (23, 1, 11);
INSERT INTO `sys_role_api` VALUES (24, 1, 12);
INSERT INTO `sys_role_api` VALUES (25, 1, 13);
INSERT INTO `sys_role_api` VALUES (26, 1, 14);
INSERT INTO `sys_role_api` VALUES (27, 1, 15);
INSERT INTO `sys_role_api` VALUES (28, 1, 16);
INSERT INTO `sys_role_api` VALUES (29, 1, 17);
INSERT INTO `sys_role_api` VALUES (30, 1, 18);
COMMIT;

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
                                 `id` bigint NOT NULL AUTO_INCREMENT,
                                 `role_id` bigint NOT NULL COMMENT 'roleId',
                                 `menu_id` bigint NOT NULL COMMENT 'menuId',
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=264 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='`sys_role_menu`';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_menu` VALUES (114, 1, 1);
INSERT INTO `sys_role_menu` VALUES (115, 1, 2);
INSERT INTO `sys_role_menu` VALUES (116, 1, 3);
INSERT INTO `sys_role_menu` VALUES (117, 1, 4);
INSERT INTO `sys_role_menu` VALUES (214, 4, 2);
INSERT INTO `sys_role_menu` VALUES (215, 4, 3);
INSERT INTO `sys_role_menu` VALUES (216, 4, 1);
INSERT INTO `sys_role_menu` VALUES (248, 4, 7);
INSERT INTO `sys_role_menu` VALUES (249, 4, 9);
INSERT INTO `sys_role_menu` VALUES (250, 4, 8);
INSERT INTO `sys_role_menu` VALUES (251, 1, 7);
INSERT INTO `sys_role_menu` VALUES (252, 1, 9);
INSERT INTO `sys_role_menu` VALUES (253, 1, 10);
INSERT INTO `sys_role_menu` VALUES (254, 1, 18);
INSERT INTO `sys_role_menu` VALUES (255, 1, 19);
INSERT INTO `sys_role_menu` VALUES (256, 1, 8);
INSERT INTO `sys_role_menu` VALUES (257, 1, 11);
INSERT INTO `sys_role_menu` VALUES (258, 1, 12);
INSERT INTO `sys_role_menu` VALUES (259, 1, 13);
INSERT INTO `sys_role_menu` VALUES (260, 1, 14);
INSERT INTO `sys_role_menu` VALUES (261, 1, 15);
INSERT INTO `sys_role_menu` VALUES (262, 1, 16);
INSERT INTO `sys_role_menu` VALUES (263, 1, 17);
COMMIT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
                            `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                            `org_id` bigint DEFAULT NULL COMMENT '组织机构ID',
                            `username` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
                            `nickname` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT '昵称',
                            `signature` varchar(255) DEFAULT NULL COMMENT '个性签名',
                            `password` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
                            `phone` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT '手机号',
                            `email` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT '邮箱',
                            `icon` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT '头像',
                            `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态，1: 正常，2:删除',
                            `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'createTime',
                            `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'updateTime',
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='`sys_user`';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` VALUES (1, 0, 'jiwk', '慕斯', NULL, '{bcrypt}$2a$10$pDRKzHEj30/UPG9xYO64W.OIktmiJ65zarNbHlLVkjNroHXPJnLhS', '17715803070', '63777887@qq.com', 'www.baidu.com', 1, '2023-10-12 15:05:57', '2023-11-22 18:21:57');
INSERT INTO `sys_user` VALUES (10, 0, 'jiwk001', '慕斯001', NULL, '{bcrypt}$2a$10$NMu/WUigc8UVPPIZRBnOPeWxjabMZ0qYjAJCkLvBGNZBqe4zhpETi', '17715803077', '63777888@qq.com', NULL, 1, '2023-10-16 11:31:37', '2023-10-17 15:24:29');
INSERT INTO `sys_user` VALUES (11, 0, 'test001', 'test001', NULL, '{bcrypt}$2a$10$Kunb5RMgrgdKDWIY69Nsj.M7GELXkAqgR/P0qx72ZG8mHPwF3g/Cu', '177****4537', '17728364537@qq.com', NULL, 1, '2023-10-16 14:32:21', '2023-10-16 19:05:43');
INSERT INTO `sys_user` VALUES (12, 0, 'test002', 'test002', NULL, '{bcrypt}$2a$10$xWecefjDAjvF7gYE56HYB.79cMuBijkmCjpGenGOCTGy8hoswLqsa', '17715803033', '17728364534@qq.com', NULL, 1, '2023-10-16 14:32:55', '2023-12-07 10:33:17');
INSERT INTO `sys_user` VALUES (13, 0, 'test003', 'test003', NULL, '{bcrypt}$2a$10$CuV/pFd3Zj44zaBUFJC37.CdgwMNm3ml6emb8gUbxiR5XbcjfDH1G', '177****6517', 'test003@qq.com', NULL, 1, '2023-10-17 11:21:02', '2023-10-17 11:22:54');
INSERT INTO `sys_user` VALUES (17, NULL, 'jiwk0001', '慕斯0001', NULL, '{bcrypt}$2a$10$t/Zw8/PX31zjxjlgOe6oL.52kqT78AwuUD5Bn/Z9qJzCuV34uimsa', '17722223333', '17722223333@qq.com', NULL, 1, '2023-11-29 11:39:47', '2023-11-29 11:39:47');
INSERT INTO `sys_user` VALUES (18, NULL, 'jiwk0002', '慕斯0002', NULL, '{bcrypt}$2a$10$/dBj.X/9IW4bbQORn7Aa2./tQI2i0.0hEYoWT7E3xnmeNFVwOm4Ou', '17722223334', '17722223334@qq.com', NULL, 1, '2023-11-29 16:24:05', '2023-11-29 16:24:05');
COMMIT;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
                                 `id` bigint NOT NULL AUTO_INCREMENT,
                                 `role_id` bigint NOT NULL COMMENT 'roleId',
                                 `user_id` bigint NOT NULL COMMENT 'userId',
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='`sys_user_role`';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_role` VALUES (1, 1, 1);
INSERT INTO `sys_user_role` VALUES (2, 2, 1);
INSERT INTO `sys_user_role` VALUES (4, 2, 13);
INSERT INTO `sys_user_role` VALUES (5, 1, 13);
INSERT INTO `sys_user_role` VALUES (6, 4, 10);
COMMIT;

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log` (
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `branch_id` bigint NOT NULL,
                            `xid` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL,
                            `context` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL,
                            `rollback_info` longblob NOT NULL,
                            `log_status` int NOT NULL,
                            `log_created` datetime NOT NULL,
                            `log_modified` datetime NOT NULL,
                            `ext` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL,
                            PRIMARY KEY (`id`) USING BTREE,
                            UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of undo_log
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;