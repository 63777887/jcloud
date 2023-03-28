package com.jwk.common.shardingjdbc.algorithm;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

/**
 * 精确分片
 */
public class DBStandardShardingAlgorithm implements PreciseShardingAlgorithm<Long> {

	public static final int TABLE_SIZE_PER_DB = 32;

	public DBStandardShardingAlgorithm() {
	}

	@Override
	public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
		int value = BigDecimal.valueOf(shardingValue.getValue()).divide(BigDecimal.valueOf(100L))
				.remainder(BigDecimal.valueOf(128L)).divide(BigDecimal.valueOf(32L)).intValue();
		Iterator var4 = availableTargetNames.iterator();

		String targetName;
		do {
			if (!var4.hasNext()) {
				return null;
			}

			targetName = (String) var4.next();
		}
		while (!targetName.endsWith("_" + value));

		return targetName;
	}

}
