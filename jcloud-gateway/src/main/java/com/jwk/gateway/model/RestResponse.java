package com.jwk.gateway.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jwk.gateway.utils.JacksonUtil;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 接口统一返回值
 *
 * @author jiweikuan
 */
@Deprecated
public class RestResponse implements Serializable {

	public static final String REST_RESPONSE_SUCCESS_CODE = "0";

	public static final String REST_RESPONSE_FIAL_CODE = "-1";

	public static final String REST_SERVER_RESPONSE_FIAL_CODE = "1";

	public static final String REST_TEMPLATE_RESPONSE_FIAL_CODE = "2";

	@JsonProperty("code")
	private String code;

	@JsonProperty("msg")
	private String msg;

	@JsonProperty("data")
	private Object result;

	private RestResponse() {
	}

	private RestResponse(String code) {
		this.code = code;
	}

	private RestResponse(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return this.msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getResult() {
		return this.result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	@JsonIgnore
	public boolean isSuccess() {
		return "0".equals(this.code);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("RestResponse{");
		sb.append("code='").append(this.code).append('\'');
		sb.append(", msg='").append(this.msg).append('\'');
		sb.append(", result=").append(this.result);
		sb.append('}');
		return sb.toString();
	}

	@JsonIgnoreType
	public static class RestResponseBuilder {

		private final RestResponse response;

		private RestResponseBuilder(String code) {
			this.response = new RestResponse(code);
		}

		private RestResponseBuilder(String code, String message) {
			this.response = new RestResponse(code);
			this.setMessage(message);
			this.setResult((Object) "");
		}

		public static RestResponseBuilder createSuccessBuilder() {
			return createBuilder("0");
		}

		public static RestResponseBuilder createSuccessBuilder(String message) {
			return createBuilder("0", message);
		}

		public static RestResponseBuilder createFailBuilder() {
			return createBuilder("-1");
		}

		public static RestResponseBuilder createFailBuilder(String message) {
			return createBuilder("-1", message);
		}

		public static RestResponseBuilder createFailForServerBuilder(String message) {
			return createBuilder("1", message);
		}

		public static RestResponseBuilder createFailForTemplateBuilder(String message) {
			return createBuilder("2", message);
		}

		public static RestResponseBuilder createBuilder(String code) {
			return createBuilder(code, "");
		}

		public static RestResponseBuilder createBuilder(String code, String message) {
			return new RestResponseBuilder(code, message);
		}

		public RestResponseBuilder setMessage(String message) {
			if (null == message) {
				this.response.setMsg("");
			}
			else {
				this.response.setMsg(message);
			}

			return this;
		}

		public <T> RestResponseBuilder setResult(List<T> result) {
			if (null != result) {
				this.response.setResult(result);
			}

			return this;
		}

		public <T> RestResponseBuilder setResult(T result) {
			this.response.setResult(result);
			return this;
		}

		public <T> RestResponseBuilder setResult(Collection<T> result) {
			this.response.setResult(result);
			return this;
		}

		public <KEY, VALUE> RestResponseBuilder setResult(Map<KEY, VALUE> result) {
			this.response.setResult(result);
			return this;
		}

		public <P, R> RestResponseBuilder setResult(P paginator, R[] resultList) {
			if (null != paginator && null != resultList) {
				Map<String, Object> pageData = new HashMap<>();
				pageData.put("paginator", paginator);
				pageData.put("resultList", resultList);
				this.response.setResult(pageData);
			}

			return this;
		}

		// public <T> RestResponse.RestResponseBuilder setResult(PageList<T> result) {
		// if (null != result) {
		// Map<String, Object> pageData = new HashMap();
		// pageData.put("resultList", result);
		// pageData.put("paginator", new Paginator(result.getPaginator()));
		// this.response.setResult(pageData);
		// }
		//
		// return this;
		// }

		public RestResponse buidler() {
			return this.response;
		}

		public String toJson() {
			return JacksonUtil.objectToJackson(this.response);
		}

	}

}
