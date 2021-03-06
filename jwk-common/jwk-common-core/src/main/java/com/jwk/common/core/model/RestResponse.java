package com.jwk.common.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jwk.common.core.utils.JacksonUtil;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 请求返回值
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
    return REST_RESPONSE_SUCCESS_CODE.equals(this.code);
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

    public static RestResponse.RestResponseBuilder createSuccessBuilder() {
      return createBuilder(REST_RESPONSE_SUCCESS_CODE);
    }

    public static RestResponse.RestResponseBuilder createSuccessBuilder(String message) {
      return createBuilder(REST_RESPONSE_SUCCESS_CODE, message);
    }

    public static RestResponse.RestResponseBuilder createFailBuilder() {
      return createBuilder(REST_RESPONSE_FIAL_CODE);
    }

    public static RestResponse.RestResponseBuilder createFailBuilder(String message) {
      return createBuilder(REST_RESPONSE_FIAL_CODE, message);
    }

    public static RestResponse.RestResponseBuilder createFailForServerBuilder(String message) {
      return createBuilder(REST_SERVER_RESPONSE_FIAL_CODE, message);
    }

    public static RestResponse.RestResponseBuilder createFailForTemplateBuilder(String message) {
      return createBuilder(REST_TEMPLATE_RESPONSE_FIAL_CODE, message);
    }

    public static RestResponse.RestResponseBuilder createBuilder(String code) {
      return createBuilder(code, "");
    }

    public static RestResponse.RestResponseBuilder createBuilder(String code, String message) {
      return new RestResponse.RestResponseBuilder(code, message);
    }

    public RestResponse.RestResponseBuilder setMessage(String message) {
      if (null == message) {
        this.response.setMsg("");
      } else {
        this.response.setMsg(message);
      }

      return this;
    }

    public <T> RestResponse.RestResponseBuilder setResult(List<T> result) {
      if (null != result) {
        this.response.setResult(result);
      }

      return this;
    }

    public <T> RestResponse.RestResponseBuilder setResult(T result) {
      this.response.setResult(result);
      return this;
    }

    public <T> RestResponse.RestResponseBuilder setResult(Collection<T> result) {
      this.response.setResult(result);
      return this;
    }

    public <KEY, VALUE> RestResponse.RestResponseBuilder setResult(Map<KEY, VALUE> result) {
      this.response.setResult(result);
      return this;
    }

    public <P, R> RestResponse.RestResponseBuilder setResult(P paginator, R[] resultList) {
      if (null != paginator && null != resultList) {
        Map<String, Object> pageData = new HashMap();
        pageData.put("paginator", paginator);
        pageData.put("resultList", resultList);
        this.response.setResult(pageData);
      }

      return this;
    }

//    public <T> RestResponse.RestResponseBuilder setResult(PageList<T> result) {
//      if (null != result) {
//        Map<String, Object> pageData = new HashMap();
//        pageData.put("resultList", result);
//        pageData.put("paginator", new Paginator(result.getPaginator()));
//        this.response.setResult(pageData);
//      }
//
//      return this;
//    }

    public RestResponse buidler() {
      return this.response;
    }

    public String toJson() {
      return JacksonUtil.objectToJackson(this.response);
    }
  }
}
