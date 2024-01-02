package com.jwk.common.core.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

 /**
   * 线程池工厂
   *
   * @author Jiwk
   * @date 2023/12/13
   * @version 0.1.6
   */
public class JwkThreadDefaultFactory implements ThreadFactory {

    /**
     * 线程池名称前缀
     */
    private StringBuffer namePrefix = new StringBuffer();

    /**
     * 线程数量的计数器
     */
    private final AtomicInteger nextId = new AtomicInteger(1);

    /**
     * 线程池构造方法
     *
     * @param factoryName  工厂名称
     * @param businessName 业务名称
     */
    public JwkThreadDefaultFactory(String factoryName, String businessName) {
        namePrefix.append("pool-").append(factoryName).append("-").append(businessName).append("-Worker-");
    }

    @Override
    public Thread newThread(Runnable runnable) {
        return new Thread(null, runnable, namePrefix.toString() + nextId.getAndIncrement(), 0);
    }

}
