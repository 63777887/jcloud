package com.jwk.common.core.utils;

import com.jwk.common.core.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 *
 */
public class AssertUtil {

  private static final Logger logger = LoggerFactory.getLogger(AssertUtil.class);

  public static void isTrue(boolean expression, String message) throws ServiceException {
    if (!expression) {
      logger.info(message);
      throw new ServiceException(message);
    }
  }
}