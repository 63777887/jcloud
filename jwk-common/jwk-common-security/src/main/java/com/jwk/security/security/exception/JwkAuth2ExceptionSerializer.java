package com.jwk.security.security.exception;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.SneakyThrows;

/**
 * OAuth2 异常格式化
 */
public class JwkAuth2ExceptionSerializer extends StdSerializer<JwkAuth2Exception> {

	public JwkAuth2ExceptionSerializer() {
		super(JwkAuth2Exception.class);
	}

	@Override
	@SneakyThrows
	public void serialize(JwkAuth2Exception value, JsonGenerator gen, SerializerProvider provider) {
		gen.writeStartObject();
		gen.writeObjectField("code", -1);
		gen.writeStringField("msg", value.getMessage());
		gen.writeStringField("data", value.getErrorCode());
		gen.writeEndObject();
	}

}
