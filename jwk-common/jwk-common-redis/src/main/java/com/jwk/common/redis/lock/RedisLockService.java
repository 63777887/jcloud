package com.jwk.common.redis.lock;

import com.jwk.common.redis.enums.RedisExceptionCodeE;
import com.jwk.common.redis.exception.RedisException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;


@Slf4j
@RequiredArgsConstructor
public class RedisLockService {

    private final RedissonClient redissonClient;

    public <T> T executeWithLock(String key, int waitTime, int leaseTime, TimeUnit unit, LockAspect<T> lockAspect) throws Throwable {
        RLock rLock = redissonClient.getLock(key);
        if (rLock.isLocked()) {
            throw new RedisException(RedisExceptionCodeE.LockIsExist);
        }
        try {
            boolean tryLock = rLock.tryLock(waitTime, leaseTime, unit);
            if (!tryLock) {
                throw new RedisException(RedisExceptionCodeE.GetLockFail);
            }
            return lockAspect.proceed();
        } catch (InterruptedException e) {
            if (log.isErrorEnabled()) {
                log.error("获取锁失败，key为：{}", key);
            }
            throw new RedisException(RedisExceptionCodeE.GetLockError);
        } finally {
            if (rLock.isLocked() && rLock.isHeldByCurrentThread()) {
                rLock.unlock();
            }
        }
    }

    public <T> T executeWithLock(String key, int waitTime, TimeUnit unit, LockAspect<T> lockAspect) throws Throwable {
        return executeWithLock(key, waitTime, -1, unit, lockAspect);
    }

    public <T> T executeWithLock(String key, LockAspect<T> lockAspect) throws Throwable {
        return executeWithLock(key, -1, -1, TimeUnit.MILLISECONDS, lockAspect);
    }

    @FunctionalInterface
    public interface LockAspect<T> {

        /**
         * Gets a result.
         *
         * @return a result
         */
        T proceed() throws Throwable;
    }
}