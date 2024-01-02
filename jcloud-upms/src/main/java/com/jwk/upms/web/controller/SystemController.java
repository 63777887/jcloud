package com.jwk.upms.web.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jwk.common.core.model.RestResponse;
import com.jwk.common.security.annotation.Inner;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jiwk
 * @since 2023-12-05
 */
@RestController
@RequestMapping("/system")
@Slf4j
public class SystemController {

    @Autowired
    RedisTemplate redisTemplate;

    // 不需要加入redis命令统计的命令
    private final List<String> excludeCommandList = Arrays.asList("ping", "info", "type", "scan");

    @GetMapping("/redis/info")
    @Inner(needFrom = false)
    public RestResponse redisInfo() {
        JSONObject jsonObject = new JSONObject();
        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        Properties info = getRedisInfo(connection);
        HashMap<String, Integer> commandMap = getCommandMap(connection);
        Map<String, Integer> keyCount = getKeyCount(connection);
        jsonObject.put("command", commandMap);
        jsonObject.put("info", info);
        jsonObject.put("keys", keyCount);
        return RestResponse.success(jsonObject);
    }

    @NotNull
    private static Properties getRedisInfo(RedisConnection connection) {
        Properties info = connection.info();
        boolean isCluster = connection instanceof RedisClusterConnection;
        info.setProperty("isCluster", isCluster ? "true" : "false");
        return info;
    }

    @NotNull
    private HashMap<String, Integer> getCommandMap(RedisConnection connection) {
        Properties commandstats = connection.info("commandstats");
        HashMap<String, Integer> commandMap = new HashMap<>();
        // 遍历Commandstats字段的属性
        for (Map.Entry<Object, Object> entry : commandstats.entrySet()) {
            String key = (String) entry.getKey();
            if (key.startsWith("cmdstat_")) {
                String command = key.substring("cmdstat_".length());
                if (excludeCommandList.contains(command)) {
                    continue;
                }
                String stats = (String) entry.getValue();
                String[] statsArr = stats.split(",");
                for (String stat : statsArr) {
                    String[] statKeyValue = stat.split("=");
                    String statKey = statKeyValue[0].trim();
                    String statValue = statKeyValue[1].trim();
                    if (statKey.equals("calls")) {
                        commandMap.put(command, Integer.valueOf(statValue));
                    }
                }
            }
        }
        return commandMap;
    }

    @NotNull
    private Map<String, Integer> getKeyCount(RedisConnection connection) {
        Map<String, Integer> keyCountMap = new HashMap<>();
        try (Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().match("*").count(1000).build())) {
            while (cursor.hasNext()) {
                byte[] keyBytes = cursor.next();
                String key = new String(keyBytes);
                String keyType = String.valueOf(connection.type(key.getBytes())).toLowerCase();
                if (null != keyCountMap.get(keyType)) {
                    keyCountMap.put(keyType, keyCountMap.get(keyType) + 1);
                } else {
                    keyCountMap.put(keyType, 0);
                }
            }
        }

        return keyCountMap;
    }

}
