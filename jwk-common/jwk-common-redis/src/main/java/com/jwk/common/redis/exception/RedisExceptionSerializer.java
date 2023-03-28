package com.jwk.common.redis.exception;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.SneakyThrows;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * Redis 异常格式化
 * @date 2022/6/11
 */
public class RedisExceptionSerializer extends StdSerializer<RedisException> {

	public RedisExceptionSerializer() {
		super(RedisException.class);
	}

	@Override
	@SneakyThrows
	public void serialize(RedisException value, JsonGenerator gen, SerializerProvider provider) {
		gen.writeStartObject();
		gen.writeObjectField("code", value.getErrorCode());
		gen.writeStringField("msg", value.getMessage());
		gen.writeStringField("data", value.getMessage());
		gen.writeEndObject();
	}

}
