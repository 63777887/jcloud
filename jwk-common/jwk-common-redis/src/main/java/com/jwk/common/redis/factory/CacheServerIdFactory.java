package com.jwk.common.redis.factory;

import java.util.function.Supplier;

/**
 * @author Jiwk
 * @date 2022/11/9
 * @version 0.1.3
 * <p>
 * 为当前服务创建一个唯一id
 */
public interface CacheServerIdFactory extends Supplier<Object> {

}