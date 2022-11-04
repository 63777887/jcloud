package com.jwk.common.Idgenerater.exception;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.SneakyThrows;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * OAuth2 异常格式化
 */
public class IdExceptionSerializer extends StdSerializer<IdGeneratorException> {

	public IdExceptionSerializer() {
		super(IdGeneratorException.class);
	}

	@Override
	@SneakyThrows
	public void serialize(IdGeneratorException value, JsonGenerator gen, SerializerProvider provider) {
		gen.writeStartObject();
		gen.writeObjectField("code", value.getErrorCode());
		gen.writeStringField("msg", value.getMessage());
		gen.writeStringField("data", value.getMessage());
		gen.writeEndObject();
	}

}
