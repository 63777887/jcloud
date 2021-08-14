package com.jwk.common.utils;

import com.jwk.common.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by gubinxian
 *
 * @author Administrator
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