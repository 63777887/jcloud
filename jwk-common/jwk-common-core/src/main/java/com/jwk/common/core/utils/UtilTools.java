package com.jwk.common.core.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 * @author fengjie 2012-08-26
 * @note 常用日期时间、数据、字符处理
 */
public class UtilTools {

  /**
   * 拷贝对象属性值  忽略null值
   *
   * @param src
   * @param target
   */
  public static void copyPropertiesIgnoreNull(Object src, Object target) {
    BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
  }

  /**
   * @param source
   * @return
   */
  private static String[] getNullPropertyNames(Object source) {
    final BeanWrapper src = new BeanWrapperImpl(source);
    java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

    Set<String> emptyNames = new HashSet<>();
    for (java.beans.PropertyDescriptor pd : pds) {
      Object srcValue = src.getPropertyValue(pd.getName());
      if (srcValue == null) {
        emptyNames.add(pd.getName());
      }
    }
    String[] result = new String[emptyNames.size()];
    return emptyNames.toArray(result);
  }

  /**
   * 格式化小数点后4位
   *
   * @param value
   * @return
   */
  public static String get4DecimalString(String value) {
    if (StringUtils.isNotBlank(value)) {
      return String.format("%.4f", Double.parseDouble(value));
    }
    return "0.0000";
  }

  /**
   * BigDecimal固定取两位小数，不足补0
   *
   * @param obj
   * @return
   */
  public static String formatToNumber(BigDecimal obj) {
    DecimalFormat df = new DecimalFormat("#.00");
    if (obj.compareTo(BigDecimal.ZERO) == 0) {
      return "0.00";
    } else if (obj.compareTo(BigDecimal.ZERO) > 0 && obj.compareTo(new BigDecimal(1)) < 0) {
      return "0" + df.format(obj).toString();
    } else {
      return df.format(obj).toString();
    }
  }

  public static String fenToYuan(Long fen) {
    if (null == fen) {
      fen = 0L;
    }
    return formatToNumber(new BigDecimal(fen).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
  }

  public static String fenToYuan(BigDecimal fen) {
    return formatToNumber(fen.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
  }

}