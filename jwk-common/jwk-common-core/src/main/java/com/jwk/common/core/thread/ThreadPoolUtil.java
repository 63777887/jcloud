package com.jwk.common.core.thread;

import org.ehcache.impl.internal.util.ThreadFactoryUtil;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 常用线程池
 *
 * @author Jiwk
 * @version 0.1.6
 * @date 2023/12/13
 */
public class ThreadPoolUtil {

  /**
   * 通用线程池方法
   * @param keepAliveTime – 存活时长，默认s
   * @param factoryName – 工厂名称
   * @param businessName – 业务名称
   * @return ExecutorService 线程池操作对象
   */
  public static ThreadPoolExecutor newCommonThreadPool(long keepAliveTime,
                                                       String factoryName,
                                                       String businessName) {
    return new ThreadPoolExecutor(
            ThreadParamConstant.CORE_SIZE,
            ThreadParamConstant.MAX_SIZE,
            keepAliveTime,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(ThreadParamConstant.QUEUE_MAX_NUM),
            new JwkThreadDefaultFactory(factoryName, businessName));
  }

  /**
   * 通用线程池方法
   * @param keepAliveTime – 存活时长，默认s
   * @param factoryName – 工厂名称
   * @param businessName – 业务名称
   * @param handler – 队列满了如何处理
   * @return ExecutorService 线程池操作对象
   */
  public static ThreadPoolExecutor newCommonThreadPool(long keepAliveTime,
                                                       String factoryName,
                                                       String businessName,
                                                       RejectedExecutionHandler handler) {
    return new ThreadPoolExecutor(
            ThreadParamConstant.CORE_SIZE,
            ThreadParamConstant.MAX_SIZE,
            keepAliveTime,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(ThreadParamConstant.QUEUE_MAX_NUM),
            new JwkThreadDefaultFactory(factoryName, businessName),
            handler);
  }

  /**
   * 单例线程池方法
   * @param factoryName – 工厂名称
   * @param businessName – 业务名称
   * @return ExecutorService 线程池操作对象
   */
  public static ThreadPoolExecutor newSingleThreadPool(String factoryName,
                                                       String businessName) {
    return new ThreadPoolExecutor(
            1,
            1,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(ThreadParamConstant.QUEUE_MAX_NUM),
            new JwkThreadDefaultFactory(factoryName, businessName));
  }
}
