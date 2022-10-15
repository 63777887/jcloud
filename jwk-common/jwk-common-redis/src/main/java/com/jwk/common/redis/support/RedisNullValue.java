package com.jwk.common.redis.support;

import java.io.Serializable;
import lombok.Data;

@Data
class RedisNullValue implements Serializable {

	private static final long serialVersionUID = 1L;

	public static RedisNullValue REDISNULLVALUE = new RedisNullValue();

}
