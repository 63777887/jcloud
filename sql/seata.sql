DROP DATABASE IF EXISTS `seata`;

CREATE DATABASE  `seata` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

USE seata;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for branch_table
-- ----------------------------
DROP TABLE IF EXISTS `branch_table`;
CREATE TABLE `branch_table` (
                                `branch_id` bigint NOT NULL,
                                `xid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                `transaction_id` bigint DEFAULT NULL,
                                `resource_group_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                                `resource_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                                `branch_type` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                                `status` tinyint DEFAULT NULL,
                                `client_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                                `application_data` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                                `gmt_create` datetime(6) DEFAULT NULL,
                                `gmt_modified` datetime(6) DEFAULT NULL,
                                PRIMARY KEY (`branch_id`) USING BTREE,
                                KEY `idx_xid` (`xid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of branch_table
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for distributed_lock
-- ----------------------------
DROP TABLE IF EXISTS `distributed_lock`;
CREATE TABLE `distributed_lock` (
                                    `lock_key` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                    `lock_value` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                    `expire` bigint DEFAULT NULL,
                                    PRIMARY KEY (`lock_key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of distributed_lock
-- ----------------------------
BEGIN;
INSERT INTO `distributed_lock` VALUES ('AsyncCommitting', '192.168.1.2:7091', 1660665636041);
INSERT INTO `distributed_lock` VALUES ('RetryCommitting', '192.168.1.2:7091', 1660665636415);
INSERT INTO `distributed_lock` VALUES ('RetryRollbacking', ' ', 0);
INSERT INTO `distributed_lock` VALUES ('TxTimeoutCheck', ' ', 0);
INSERT INTO `distributed_lock` VALUES ('UndologDelete', ' ', 0);
COMMIT;

-- ----------------------------
-- Table structure for global_table
-- ----------------------------
DROP TABLE IF EXISTS `global_table`;
CREATE TABLE `global_table` (
                                `xid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                `transaction_id` bigint DEFAULT NULL,
                                `status` tinyint NOT NULL,
                                `application_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                                `transaction_service_group` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                                `transaction_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                                `timeout` int DEFAULT NULL,
                                `begin_time` bigint DEFAULT NULL,
                                `application_data` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                                `gmt_create` datetime DEFAULT NULL,
                                `gmt_modified` datetime DEFAULT NULL,
                                PRIMARY KEY (`xid`) USING BTREE,
                                KEY `idx_status_gmt_modified` (`status`,`gmt_modified`) USING BTREE,
                                KEY `idx_transaction_id` (`transaction_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of global_table
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for lock_table
-- ----------------------------
DROP TABLE IF EXISTS `lock_table`;
CREATE TABLE `lock_table` (
                              `row_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                              `xid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                              `transaction_id` bigint DEFAULT NULL,
                              `branch_id` bigint NOT NULL,
                              `resource_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                              `table_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                              `pk` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                              `status` tinyint NOT NULL DEFAULT '0' COMMENT '0:locked ,1:rollbacking',
                              `gmt_create` datetime DEFAULT NULL,
                              `gmt_modified` datetime DEFAULT NULL,
                              PRIMARY KEY (`row_key`) USING BTREE,
                              KEY `idx_status` (`status`) USING BTREE,
                              KEY `idx_branch_id` (`branch_id`) USING BTREE,
                              KEY `idx_xid_and_branch_id` (`xid`,`branch_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of lock_table
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
