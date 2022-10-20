package com.jwk.common.shardingjdbc.algorithm;

import java.math.BigInteger;
import java.util.Collection;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

/**
 * 精确分片
 */
public class JwkTablePreciseAlgorithm implements PreciseShardingAlgorithm<Long> {

	@Override
	public String doSharding(Collection<String> tableNames, PreciseShardingValue<Long> preciseShardingValue) {
		Long idValue = preciseShardingValue.getValue();
		String id = preciseShardingValue.getColumnName();
		BigInteger temp = BigInteger.valueOf(idValue).mod(BigInteger.valueOf(2)).add(BigInteger.ONE);
		String tableName = preciseShardingValue.getLogicTableName() + "_" + temp.intValue();
		if (tableNames.contains(tableName)) {
			return tableName;
		}
		throw new UnsupportedOperationException("route " + tableName + " is not support, please check your config");
	}

}
