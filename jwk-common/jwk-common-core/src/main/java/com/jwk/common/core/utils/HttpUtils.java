package com.jwk.common.core.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jwk.common.core.exception.ServiceException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 *
 */
public class HttpUtils {

	private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

	public static <T> T doGet(String url, String params, Class<T> clazz) throws ServiceException {
		JSONObject jo;
		try {
			// logger.info("url：{}", url);
			// logger.info("params：{}", params);
			String urlNameString = url + "?" + params;
			RestTemplate restTemplate = new RestTemplate();
			MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(type);
			headers.add("Accept", MediaType.APPLICATION_JSON.toString());
			HttpEntity<String> entity = new HttpEntity<>(params, headers);
			ResponseEntity<String> resp = restTemplate.exchange(urlNameString, HttpMethod.GET, entity, String.class);
			logger.info("Get请求响应结果：{}", resp);
			jo = JSON.parseObject(resp.getBody());
			if (null != jo) {
				if ("0".equals(jo.getString("code"))) {
					if (null != clazz) {
						return jo.getObject("data", clazz);
					}
				}
				else {
					throw new ServiceException(jo.getString("message"));
				}
			}
		}
		catch (ServiceException ex) {
			throw ex;
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		return null;
	}

	public static <T> T doGetHaveCookie(String url, String param, Class<T> clazz, String cookie)
			throws ServiceException {
		JSONObject jo;
		try {
			// logger.info("url：{}", url);
			// logger.info("params：{}", params);
			String urlNameString = url + "?" + param;
			RestTemplate restTemplate = new RestTemplate();
			MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(type);
			headers.set("cookie", cookie);
			headers.add("Accept", MediaType.APPLICATION_JSON.toString());
			HttpEntity<String> entity = new HttpEntity<>(param, headers);
			ResponseEntity<String> resp = restTemplate.exchange(urlNameString, HttpMethod.GET, entity, String.class);
			logger.info("Get请求响应结果：{}", resp);
			jo = JSON.parseObject(resp.getBody());
			if (null != jo) {
				if ("0".equals(jo.getString("code"))) {
					if (null != clazz) {
						return jo.getObject("data", clazz);
					}
				}
				else {
					throw new ServiceException(jo.getString("message"));
				}
			}
		}
		catch (ServiceException ex) {
			throw ex;
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		return null;
	}

	public static <T> T doPost(String url, String params, Class<T> clazz) throws ServiceException {
		JSONObject jo;
		try {
			// logger.info("url：{}", url);
			// logger.info("params：{}", params);
			RestTemplate restTemplate = new RestTemplate();
			MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(type);
			headers.add("Accept", MediaType.APPLICATION_JSON.toString());
			HttpEntity<String> entity = new HttpEntity<>(params, headers);
			ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
			jo = JSON.parseObject(resp.getBody());
			logger.info("POST请求响应结果：{}", jo);
			if (null != jo) {
				if ("0".equals(jo.getString("code"))) {
					if (null != clazz) {
						logger.info("jo:" + jo.getObject("data", clazz));
						return jo.getObject("data", clazz);
					}
				}
				else {
					throw new ServiceException(jo.getString("message"));
				}
			}
		}
		catch (ServiceException ex) {
			throw ex;
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		return null;
	}

	public static <T> T doPostWithForm(String url, MultiValueMap<String, String> params, Class<T> clazz)
			throws ServiceException {
		JSONObject jo;
		try {
			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(
					params, headers);
			ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
			jo = JSON.parseObject(resp.getBody());
			logger.info("POST请求响应结果：{}", jo);
			if (null != jo) {
				if ("0".equals(jo.getString("code"))) {
					if (null != clazz) {
						logger.info("jo:" + jo.getObject("data", clazz));
						return jo.getObject("data", clazz);
					}
				}
				else {
					throw new ServiceException(jo.getString("message"));
				}
			}
		}
		catch (ServiceException ex) {
			throw ex;
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		return null;
	}

	public static <T> T doPostHaveCookie(String url, String params, Class<T> clazz, String cookie)
			throws ServiceException {
		JSONObject jo;
		try {
			// logger.info("url：{}", url);
			// logger.info("params：{}", params);
			RestTemplate restTemplate = new RestTemplate();
			MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(type);
			headers.add("Accept", MediaType.APPLICATION_JSON.toString());
			headers.add("Cookie", cookie);
			HttpEntity<String> entity = new HttpEntity<>(params, headers);
			ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
			logger.info("POST请求响应结果：{}", resp);
			jo = JSON.parseObject(resp.getBody());
			if (null != jo) {
				if ("0".equals(jo.getString("code"))) {
					if (null != clazz) {
						return jo.getObject("data", clazz);
					}
				}
				else {
					throw new ServiceException(jo.getString("message"));
				}
			}
		}
		catch (ServiceException ex) {
			throw ex;
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw e;
		}
		return null;
	}

	public static <T> List<T> doPostList(String url, String params, Class<T> clazz) throws ServiceException {
		JSONObject jo;
		try {
			// logger.info("url：{}", url);
			// logger.info("params：{}", params);
			RestTemplate restTemplate = new RestTemplate();
			MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(type);
			headers.add("Accept", MediaType.APPLICATION_JSON.toString());
			HttpEntity<String> entity = new HttpEntity<String>(params, headers);
			ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
			logger.info("rst：{}", resp);
			jo = JSON.parseObject(resp.getBody());
			if (null != jo) {
				if ("0".equals(jo.getString("code"))) {
					if (null != clazz) {
						logger.info("resultList:" + JSON.parseArray(jo.getJSONArray("data").toJSONString(), clazz));
						return JSON.parseArray(jo.getJSONArray("data").toJSONString(), clazz);
					}
				}
				else {
					throw new ServiceException(jo.getString("message"));
				}
			}
		}
		catch (ServiceException ex) {
			throw ex;
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		return null;
	}

	public static <T> List<T> doPostListHaveCookie(String url, String params, Class<T> clazz, String cookie)
			throws ServiceException {
		JSONObject jo;
		try {
			// logger.info("url：{}", url);
			// logger.info("params：{}", params);
			RestTemplate restTemplate = new RestTemplate();
			MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(type);
			headers.add("Cookie", cookie);
			headers.add("Accept", MediaType.APPLICATION_JSON.toString());
			HttpEntity<String> entity = new HttpEntity<String>(params, headers);
			ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
			logger.info("rst：{}", resp);
			jo = JSON.parseObject(resp.getBody());
			if (null != jo) {
				if ("0".equals(jo.getString("code"))) {
					if (null != clazz) {
						logger.info("resultList:" + JSON.parseArray(jo.getJSONArray("data").toJSONString(), clazz));
						return JSON.parseArray(jo.getJSONArray("data").toJSONString(), clazz);
					}
				}
				else {
					throw new ServiceException(jo.getString("message"));
				}
			}
		}
		catch (ServiceException ex) {
			throw ex;
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		return null;
	}

	/**
	 * 因数据中心的返回机制是只有查询到记录了才返回0状态值,无记录或异常时返回-1
	 * @param url
	 * @param params
	 * @param clazz
	 * @param <T>
	 * @return
	 * @throws ServiceException
	 */
	public static <T> List<T> doPostListByDC(String url, String params, Class<T> clazz) throws ServiceException {
		JSONObject jo;
		try {
			// logger.info("url：{}", url);
			// logger.info("params：{}", params);
			RestTemplate restTemplate = new RestTemplate();
			MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(type);
			headers.add("Accept", MediaType.APPLICATION_JSON.toString());
			HttpEntity<String> entity = new HttpEntity<String>(params, headers);
			ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
			logger.info("rst：{}", resp);
			jo = JSON.parseObject(resp.getBody());
			if (null != jo) {
				if ("0".equals(jo.getString("code"))) {
					if (null != clazz) {
						JSONArray jsonArray = jo.getJSONArray("data");
						return JSON.parseArray(jsonArray.toJSONString(), clazz);
					}
				}
				else {
					if (jo.getString("message").contains("ok")) {
						if (null != clazz) {
							return JSON.parseArray(jo.getJSONArray("data").toJSONString(), clazz);
						}
					}
					else {
						throw new ServiceException(jo.getString("message"));
					}
				}
			}
		}
		catch (ServiceException ex) {
			throw ex;
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		return null;
	}

	public static <T> List<T> doPostPageList(String url, String params, Class<T> clazz) throws ServiceException {
		JSONObject jo;
		try {
			logger.info("url：{},param:{}", url, params);
			// logger.info("params：{}", params);
			RestTemplate restTemplate = new RestTemplate();
			MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(type);
			headers.add("Accept", MediaType.APPLICATION_JSON.toString());
			HttpEntity<String> entity = new HttpEntity<String>(params, headers);
			ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
			logger.info("rst：{}", resp);
			jo = JSON.parseObject(resp.getBody());
			if (null != jo) {
				if ("0".equals(jo.getString("code"))) {
					if (null != clazz) {
						JSONObject data = jo.getJSONObject("data");
						JSONArray jsonArray = data.getJSONArray("resultList");
						return JSON.parseArray(jsonArray.toJSONString(), clazz);
					}
				}
				else {
					throw new ServiceException(jo.getString("message"));
				}
			}
		}
		catch (ServiceException ex) {
			throw ex;
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		return null;
	}

	public static <T> T doPost(String url, String params, Class<T> clazz, String cookie) throws ServiceException {
		JSONObject jo;
		try {
			// logger.info("url：{}", url);
			// logger.info("params：{}", params);
			RestTemplate restTemplate = new RestTemplate();
			MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(type);
			headers.add("Accept", MediaType.APPLICATION_JSON.toString());
			headers.add("Cookie", cookie);
			HttpEntity<String> entity = new HttpEntity<>(params, headers);
			ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
			logger.info("POST请求响应结果：{}", resp);
			jo = JSON.parseObject(resp.getBody());
			if (null != jo) {
				if ("0".equals(jo.getString("code"))) {
					if (null != clazz) {
						return jo.getObject("data", clazz);
					}
				}
				else {
					throw new ServiceException(jo.getString("message"));
				}
			}
		}
		catch (ServiceException ex) {
			throw ex;
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		return null;
	}

	public static <T> List<T> doPostList(String url, String params, Class<T> clazz, String cookie)
			throws ServiceException {
		JSONObject jo;
		try {
			// logger.info("url：{}", url);
			// logger.info("params：{}", params);
			RestTemplate restTemplate = new RestTemplate();
			MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(type);
			headers.add("Accept", MediaType.APPLICATION_JSON.toString());
			headers.add("Cookie", cookie);
			HttpEntity<String> entity = new HttpEntity<String>(params, headers);
			ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
			logger.info("rst：{}", resp);
			jo = JSON.parseObject(resp.getBody());
			if (null != jo) {
				if ("0".equals(jo.getString("code"))) {
					if (null != clazz) {
						return JSON.parseArray(jo.getJSONArray("data").toJSONString(), clazz);
					}
				}
				else {
					throw new ServiceException(jo.getString("message"));
				}
			}
		}
		catch (ServiceException ex) {
			throw ex;
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		return null;
	}

	public static int doPostBackCode(String url, String params) throws ServiceException {
		JSONObject jo;
		try {
			RestTemplate restTemplate = new RestTemplate();
			MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(type);
			headers.add("Accept", MediaType.APPLICATION_JSON.toString());
			HttpEntity<String> entity = new HttpEntity<>(params, headers);
			ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
			logger.info("POST请求响应结果：{}", resp);
			jo = JSON.parseObject(resp.getBody());
			if (null != jo) {
				return Integer.parseInt(jo.getString("code"));
			}
		}
		catch (ServiceException ex) {
			throw ex;
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		return 0;
	}

	public static ResponseEntity<Object> doPostSimple(String url, String params, String cookie) {
		try {
			logger.info("url：{}", url);
			logger.info("params：{}", params);
			RestTemplate restTemplate = new RestTemplate();
			MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(type);
			headers.add("Accept", MediaType.APPLICATION_JSON.toString());
			headers.add("Cookie", cookie);
			HttpEntity<Object> entity = new HttpEntity<>(params, headers);
			ResponseEntity<Object> resp = restTemplate.exchange(url, HttpMethod.POST, entity, Object.class);
			logger.info("POST请求响应结果：{}", resp);
			return resp;
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 截取上传地址url，域名后的字符串
	 * @param url 地址
	 * @return
	 */
	public static String subStrAnnexUrl(String url) {
		if (url != null && url.indexOf("http") != -1) {
			String[] split = url.split("/");
			int i = url.indexOf(split[2]) + split[2].length();
			String substring = url.substring(i + 1);
			return substring;
		}
		return url;
	}

}