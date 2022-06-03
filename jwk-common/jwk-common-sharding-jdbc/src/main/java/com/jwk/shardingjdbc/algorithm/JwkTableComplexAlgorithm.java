package com.jwk.shardingjdbc.algorithm;

import com.google.common.collect.Range;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;

/**
 * 通过复合分片键进行演示，覆盖Precise，Range，List三种类型的ShardingValue。
 * 项目中应根据实际情况实现：
 * 1精确分片PreciseShardingAlgorithm、
 * 2范围分片RangeShardingAlgorithm
 * 3复合分片ComplexKeysShardingAlgorithm
 * 4非SQL解析分片HintShardingAlgorithm
 */
public class JwkTableComplexAlgorithm implements ComplexKeysShardingAlgorithm<Long> {


  @Override
  public Collection<String> doSharding(Collection<String> collection,
      ComplexKeysShardingValue<Long> complexKeysShardingValue) {

    ArrayList<String> result = new ArrayList<>();

    // 根据值的范围算，根据自己的分片规则选择范围分片，还是值分片
    Range<Long> ageRange = complexKeysShardingValue.getColumnNameAndRangeValuesMap().get("age");
    // 根据值算
    Collection<Long> ages = complexKeysShardingValue.getColumnNameAndShardingValuesMap().get("age");

    Long lowerEndpoint = ageRange.lowerEndpoint();
    Long upperEndpoint = ageRange.upperEndpoint();
    for (Long age : ages) {
      BigInteger temp = BigInteger.valueOf(age).mod(BigInteger.valueOf(2)).add(BigInteger.ONE);
      result.add(complexKeysShardingValue.getLogicTableName()+"_"+temp);
    }

    return result;
  }
}
