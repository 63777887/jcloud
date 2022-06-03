package com.jwk.shardingjdbc.algorithm;

import java.util.Collection;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;

/**
 * 范围分片
 */
public class JwkDBRangeAlgorithm implements RangeShardingAlgorithm<Long> {


  @Override
  public Collection<String> doSharding(Collection<String> collection,
      RangeShardingValue<Long> rangeShardingValue) {
    return collection;
  }
}
