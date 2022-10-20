package com.jwk.common.shardingjdbc.algorithm;

import java.math.BigInteger;
import java.util.Collection;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

/**
 * 精确分片
 */
public class JwkDBPreciseAlgorithm implements PreciseShardingAlgorithm<Long> {

	@Override
	public String doSharding(Collection<String> dbNames, PreciseShardingValue<Long> preciseShardingValue) {
		Long idValue = preciseShardingValue.getValue();
		String id = preciseShardingValue.getColumnName();
		BigInteger temp = BigInteger.valueOf(idValue).mod(BigInteger.valueOf(2)).add(BigInteger.ONE);
		String dbName = "m" + temp.intValue();
		if (dbNames.contains(dbName)) {
			return dbName;
		}
		throw new UnsupportedOperationException("database " + dbName + " is not support, please check your config");
	}

}
