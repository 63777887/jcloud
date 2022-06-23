package com.jwk.shardingjdbc.algorithm;

import java.math.BigDecimal;
import java.util.Collection;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

/**
 * 精确分片
 */
public final class TableStandardShardingAlgorithm implements PreciseShardingAlgorithm<Long> {
  public static final int TABLE_COUNT = 128;
  public static final int SPOILER_NUMBER = 100;

  public TableStandardShardingAlgorithm() {
  }

  @Override
  public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
    int value = BigDecimal.valueOf((Long)shardingValue.getValue()).divide(BigDecimal.valueOf(100L)).remainder(BigDecimal.valueOf(128L)).intValue();
    StringBuilder sb = new StringBuilder(shardingValue.getLogicTableName());
    sb.append("_").append(value);
    return sb.toString();
  }
}
