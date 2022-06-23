package com.jwk.prometheus.ext;

import static feign.Util.checkNotNull;

import com.alibaba.cloud.sentinel.feign.SentinelContractHolder;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.jwk.common.core.model.InnerResponse;
import com.jwk.prometheus.utils.JwkMetricUtils;
import feign.Feign;
import feign.InvocationHandlerFactory;
import feign.MethodMetadata;
import feign.Target;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 支持自动降级注入 重写 {@link com.jwk.common.cloud.fegin.ext.SentinelInvocationHandler}
 */
@Slf4j
public class PrometheusSentinelInvocationHandler implements InvocationHandler {

  public static final String EQUALS = "equals";

  public static final String HASH_CODE = "hashCode";

  public static final String TO_STRING = "toString";

  private final Target<?> target;

  private final Map<Method, InvocationHandlerFactory.MethodHandler> dispatch;

  private FallbackFactory<?> fallbackFactory;

  private Map<Method, Method> fallbackMethodMap;

  PrometheusSentinelInvocationHandler(Target<?> target,
      Map<Method, InvocationHandlerFactory.MethodHandler> dispatch,
      FallbackFactory<?> fallbackFactory) {
    this.target = checkNotNull(target, "target");
    this.dispatch = checkNotNull(dispatch, "dispatch");
    this.fallbackFactory = fallbackFactory;
    this.fallbackMethodMap = toFallbackMethod(dispatch);
  }

  PrometheusSentinelInvocationHandler(Target<?> target,
      Map<Method, InvocationHandlerFactory.MethodHandler> dispatch) {
    this.target = checkNotNull(target, "target");
    this.dispatch = checkNotNull(dispatch, "dispatch");
  }

  @Override
  public Object invoke(final Object proxy, final Method method, final Object[] args)
      throws Throwable {
    if (EQUALS.equals(method.getName())) {
      try {
        Object otherHandler =
            args.length > 0 && args[0] != null ? Proxy.getInvocationHandler(args[0]) : null;
        return equals(otherHandler);
      } catch (IllegalArgumentException e) {
        return false;
      }
    } else if (HASH_CODE.equals(method.getName())) {
      return hashCode();
    } else if (TO_STRING.equals(method.getName())) {
      return toString();
    }

    Object result;
    InvocationHandlerFactory.MethodHandler methodHandler = this.dispatch.get(method);
    // only handle by HardCodedTarget
    if (target instanceof Target.HardCodedTarget) {
      Target.HardCodedTarget<?> hardCodedTarget = (Target.HardCodedTarget) target;
      MethodMetadata methodMetadata = SentinelContractHolder.METADATA_MAP
          .get(hardCodedTarget.type().getName() + Feign.configKey(hardCodedTarget.type(), method));
      // resource default is HttpMethod:protocol://url
      long invokeBegin = 0;
      if (methodMetadata == null) {
        invokeBegin = System.currentTimeMillis();
        result = methodHandler.invoke(args);
        pushPrometheus(method, invokeBegin, Boolean.TRUE);
      } else {
        //	resourceName = 		GET:http://jwk-gateway/jwk-security/inner/test
        String resourceName =
            methodMetadata.template().method().toUpperCase() + ':' + hardCodedTarget.url()
                + methodMetadata.template().path();
        Entry entry = null;
        try {
          ContextUtil.enter(resourceName);
          entry = SphU.entry(resourceName, EntryType.OUT, 1, args);
          invokeBegin = System.currentTimeMillis();
          result = methodHandler.invoke(args);
          pushPrometheus(method, invokeBegin, Boolean.TRUE);
        } catch (Throwable ex) {
          pushPrometheus(method, invokeBegin, Boolean.FALSE);
          // fallback handle
          if (!BlockException.isBlockException(ex)) {
            Tracer.trace(ex);
          }
          if (fallbackFactory != null) {
            try {
              return fallbackMethodMap.get(method).invoke(fallbackFactory.create(ex), args);
            } catch (IllegalAccessException e) {
              // shouldn't happen as method is public due to being an
              // interface
              throw new AssertionError(e);
            } catch (InvocationTargetException e) {
              throw new AssertionError(e.getCause());
            }
          } else {
            // 若是业务统一类型 执行自动降级返回R
            if (InnerResponse.class == method.getReturnType()) {
              log.error("feign 服务间调用异常", ex);
              return InnerResponse.error().setMsg(ex.getLocalizedMessage());
            } else {
              throw ex;
            }
          }
        } finally {
          if (entry != null) {
            entry.exit(1, args);
          }
          ContextUtil.exit();
        }
      }
    } else {
      // other target type using default strategy
      result = methodHandler.invoke(args);
    }

    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof com.alibaba.cloud.sentinel.feign.SentinelInvocationHandler) {
      PrometheusSentinelInvocationHandler other = (PrometheusSentinelInvocationHandler) obj;
      return target.equals(other.target);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return target.hashCode();
  }

  @Override
  public String toString() {
    return target.toString();
  }

  static Map<Method, Method> toFallbackMethod(
      Map<Method, InvocationHandlerFactory.MethodHandler> dispatch) {
    Map<Method, Method> result = new LinkedHashMap<>();
    for (Method method : dispatch.keySet()) {
      method.setAccessible(true);
      result.put(method, method);
    }
    return result;
  }

  private void pushPrometheus(Method method, long invokeBegin, Boolean isSuccess) {
    long invokeEnd = System.currentTimeMillis();
    String methodName = method.getName();
    JwkMetricUtils.getCounter(methodName,isSuccess).increment();
    long invokeTime = invokeEnd - invokeBegin;
    JwkMetricUtils.getTimer(methodName,isSuccess).record(invokeTime, TimeUnit.MILLISECONDS);
  }

}
