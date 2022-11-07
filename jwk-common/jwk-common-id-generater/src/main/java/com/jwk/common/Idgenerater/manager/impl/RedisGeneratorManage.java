package com.jwk.common.Idgenerater.manager.impl;

import com.jwk.common.Idgenerater.exception.IdExceptionCodeE;
import com.jwk.common.Idgenerater.exception.IdGeneratorException;
import com.jwk.common.Idgenerater.manager.IdGeneratorManage;
import com.jwk.common.Idgenerater.properties.IdGeneraterProperties;
import com.jwk.common.redis.exception.RedisException;
import com.jwk.common.redis.exception.RedisExceptionCodeE;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * ID生成器（集群版）
 *
 * @author chenlong
 * @date 2022-06-06 14:46:44
 */
@Component
@Slf4j
public class RedisGeneratorManage implements IdGeneratorManage {

  @Autowired
  private RedissonClient redissonClient;
  @Autowired
  private RedisTemplate redisTemplate;
  @Autowired
  private IdGeneraterProperties idGeneraterProperties;

  List<String> initIdListCache = new ArrayList<>();


  /**
   * 生成1个id
   *
   * @param slotId
   * @return
   */
  @Override
  public long generate(int slotId) throws IdGeneratorException, RedisException {
    List<Long> ids = genIds(slotId, 1);
    return ids.size() > 0 ? ids.get(0) : -1;
  }

  /**
   * 生成size个id
   *
   * @param slotId
   * @param size
   * @return
   * @throws IdGeneratorException
   */
  @Override
  public List<Long> generate(int slotId, int size) throws IdGeneratorException, RedisException {
    if (size > idGeneraterProperties.maxSize) {
      IdGeneratorException exception = new IdGeneratorException(IdExceptionCodeE.SizeLimit);
      if (log.isErrorEnabled()) {
        log.error("generate error:{}", exception.toString());
      }
      throw exception;
    }

    return genIds(slotId, size);
  }

  /**
   * 路由规则
   * @param type
   * @return
   * @throws IdGeneratorException
   */
  @Override
  public int slotId(long type) throws IdGeneratorException {
    if (type > 0) {
      return (int) (type % idGeneraterProperties.slotCount);
    } else {
      IdGeneratorException exception = new IdGeneratorException(IdExceptionCodeE.IllegalType);
      if (log.isErrorEnabled()){
        log.error("slotId error:{}", exception.toString());
      }
      throw exception;
    }
  }

  private List<Long> genIds(int slotId, int size) throws IdGeneratorException, RedisException {
    // 获取分布式锁
    String idLockKey = getIdLockKey(slotId);
    RLock lock = redissonClient.getLock(idLockKey);
    try {
      // 加锁
      if (lock.isLocked()) {
        throw new RedisException(RedisExceptionCodeE.LockIsExist);
      }
      boolean tryLock;
      try {
        tryLock = lock.tryLock(3, TimeUnit.SECONDS);
      } catch (InterruptedException e) {
        if (log.isErrorEnabled()) {
          log.error("获取锁失败，key为：{}", idLockKey);
        }
        throw new RedisException(RedisExceptionCodeE.GetLockError);
      }
      if (tryLock) {
        // 返回结果
        List<Long> list = new ArrayList<Long>();

        if (size <= 0) {
          return list;
        }
        String idTypeKey = getIdTypeKey(slotId);

        //是否需要初始化
        if (!initIdListCache.contains(idTypeKey)) {
          redisTemplate.opsForValue().setIfAbsent(idTypeKey,idGeneraterProperties.slotInitialValue);
          initIdListCache.add(idTypeKey);
        }

        // redis:curId+size
        long currentCurId = redisTemplate.opsForValue().increment(idTypeKey, size);

        if (log.isDebugEnabled()) {
          log.debug("currentCurId:{}", currentCurId);
        }

        // 获取该槽位的起始值
        long initValue = idGeneraterProperties.slotInitialValue;
        if (log.isDebugEnabled()) {
          log.debug("initValue:{}", initValue);
        }

        // 小于起始值，认为redis值非法
        if (currentCurId <= initValue) {

            // 恢复初始值
            currentCurId = initValue;
          redisTemplate.opsForValue().set(idTypeKey, currentCurId);
        }

        // 批量获取
        if (size > 1) {

          // 先前的curId
          long oldCurId = currentCurId - size;

          // 组装返回值
          for (long id = oldCurId + 1; id <= currentCurId; id++) {
            list.add(id);
          }

        } else {
          // 组装返回值
          list.add(currentCurId);

        }

        return list;
      } else {
        if (log.isErrorEnabled()) {
          log.error("未获取到锁，key为：{}" + idLockKey);
        }
        throw new RedisException(RedisExceptionCodeE.GetLockFail);
      }
    } finally {
      if (null != lock && lock.isLocked()
          && lock.isHeldByCurrentThread()) {
        lock.unlock();
      }
    }

  }

  private String getIdLockKey(int slotId) {
    return "IDGEN_LOCK:" + slotId;
  }

  private String getIdTypeKey(int slotId) {
    return "IDGEN:"+slotId;
  }

}

