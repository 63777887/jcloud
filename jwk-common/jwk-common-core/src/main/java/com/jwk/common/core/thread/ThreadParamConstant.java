package com.jwk.common.core.thread;

 /**
   * 线程池参数常量类
   *
   * @author Jiwk
   * @date 2023/12/13
   * @version 0.1.6
   */
public class ThreadParamConstant {

    /**
     * 线程池通用核心线程数
     */
    public static final int CORE_SIZE =
            Runtime.getRuntime().availableProcessors();

    /**
     * 线程池通用最大线程数
     */
    public static final int MAX_SIZE = CORE_SIZE << 1;

    /**
     * 队列最大数量
     */
    public static final int QUEUE_MAX_NUM = 5000;

    /**
     * 通用线程存活时长
     */
    public static final long COMMON_KEEP_ALIVE_TIME = 60L;

}
