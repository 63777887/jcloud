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
                           `parent_id` bigint DEFAULT NULL COMMENT 'apiPid',
                           `api_desc` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'apiName',
                           `url` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'url',
                           `level` int DEFAULT NULL COMMENT 'level',
                           `status` tinyint(1) DEFAULT NULL COMMENT 'status',
                           `create_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'createBy',
                           `create_time` bigint DEFAULT NULL COMMENT 'createTime',
                           `update_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'updateBy',
                           `update_time` bigint DEFAULT NULL COMMENT 'updateTime',
                           `category_id` bigint DEFAULT NULL,
                           PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='`sys_api`';

-- ----------------------------
-- Records of sys_api
-- ----------------------------
BEGIN;
INSERT INTO `sys_api` VALUES (1, NULL, NULL, '/user/**', 1, 1, NULL, NULL, NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (2, NULL, NULL, '/sysApi/**', 1, 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_api` VALUES (3, NULL, NULL, '/sysRole/**', 1, 1, NULL, NULL, NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (4, NULL, NULL, '/test', 1, 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_api` VALUES (5, NULL, NULL, '/web/test2', 1, 1, NULL, NULL, NULL, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_api_category
-- ----------------------------
DROP TABLE IF EXISTS `sys_api_category`;
CREATE TABLE `sys_api_category` (
                                    `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                                    `create_time` bigint DEFAULT NULL COMMENT 'createTime',
                                    `name` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'name',
                                    `sort` int DEFAULT NULL COMMENT 'sort',
                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='`sys_api_category`';

-- ----------------------------
-- Records of sys_api_category
-- ----------------------------
BEGIN;
INSERT INTO `sys_api_category` VALUES (1, NULL, '用户管理', 1);
COMMIT;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
                            `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                            `parent_id` bigint DEFAULT NULL COMMENT 'menuPid',
                            `menu_name` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'menuName',
                            `sort` int DEFAULT NULL COMMENT 'menuSort',
                            `url` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'url',
                            `icon` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'icon',
                            `level` int DEFAULT NULL COMMENT 'level',
                            `status` tinyint(1) DEFAULT NULL COMMENT 'status',
                            `hidden` tinyint(1) DEFAULT NULL COMMENT 'hidden',
                            `view_import` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'viewImport',
                            `create_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'createBy',
                            `create_time` bigint DEFAULT NULL COMMENT 'createTime',
                            `update_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'updateBy',
                            `update_time` bigint DEFAULT NULL COMMENT 'updateTime',
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='`sys_menu`';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
BEGIN;
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
                                    `authorities` varchar(256) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT '角色列表',
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
INSERT INTO `sys_oauth_client` VALUES ('jiwkClient', '测试客户端', NULL, '{bcrypt}$2a$10$SptM6/TuOvM68SLbY/ewHuOHVqI9mF1QlBIDXIvkJDOWd3RufCjZC', 'server', 'authorization_code,password,phone,refresh_token,code,client_credentials,email', 'http://www.baidu.com', NULL, 864000, 864000, NULL, NULL, '2022-11-22 19:40:50', NULL, 'jiwk', NULL);
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
                           `create_time` bigint DEFAULT NULL COMMENT 'createTime',
                           `update_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'updateBy',
                           `update_time` bigint DEFAULT NULL COMMENT 'updateTime',
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
                            `role_name` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'roleName',
                            `role_desc` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'roleDesc',
                            `code` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'roleCode',
                            `sort` int DEFAULT NULL COMMENT 'roleSort',
                            `status` tinyint(1) DEFAULT NULL COMMENT 'status',
                            `create_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'createBy',
                            `create_time` bigint DEFAULT NULL COMMENT 'createTime',
                            `update_by` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'updateBy',
                            `update_time` bigint DEFAULT NULL COMMENT 'updateTime',
                            PRIMARY KEY (`id`) USING BTREE,
                            UNIQUE KEY `code_index` (`code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='`sys_role`';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` VALUES (1, '测试角色', NULL, 'CSJS', 1, 1, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role` VALUES (2, '系统角色', NULL, 'server', 2, 1, NULL, NULL, NULL, NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='`sys_role_api`';

-- ----------------------------
-- Records of sys_role_api
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_api` VALUES (1, 1, 1);
INSERT INTO `sys_role_api` VALUES (3, 2, 2);
INSERT INTO `sys_role_api` VALUES (4, 1, 4);
INSERT INTO `sys_role_api` VALUES (5, 1, 3);
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='`sys_role_menu`';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
                            `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                            `org_id` bigint DEFAULT NULL COMMENT 'orgId',
                            `username` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'username',
                            `password` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'password',
                            `phone` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'phone',
                            `email` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'email',
                            `icon` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'icon',
                            `status` tinyint(1) DEFAULT NULL COMMENT 'enabled',
                            `create_time` bigint DEFAULT NULL COMMENT 'createTime',
                            `update_time` bigint DEFAULT NULL COMMENT 'updateTime',
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='`sys_user`';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` VALUES (1, 0, 'jiwk', '{bcrypt}$2a$10$JU52vGa6QoDemcEvY4BazuP/HzAyOCd/Os8bC6O5ObTq2zMDwBLEG', '17715803070', '63777887@qq.com', NULL, 1, 20210604232609, NULL);
INSERT INTO `sys_user` VALUES (2, NULL, 'test001', '', '', '', '', -1, NULL, NULL);
INSERT INTO `sys_user` VALUES (4, 2, 'jiwk1', '{bcrypt}$2a$10$fCQ47BQJeqmzs8WhzmAat.VMdHmkDc9IFBzLTvjy9dQ7MPZzm1ITq', '17715803071', '63777888@qq.com', NULL, 1, 20210810140442, NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='`sys_user_role`';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_role` VALUES (1, 1, 1);
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
