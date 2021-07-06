package com.jwk.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtil {
  private static Logger logger = LoggerFactory.getLogger(DateUtil.class);
  public static final String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
  public static final String FORMAT_YYYYMM = "yyyyMM";
  public static final String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
  public static final String FORMAT_YYYY_MM_DD_HH_MM = "yyyyMMddHHmm";
  public static final String FORMAT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
  public static final String FORMAT_YYYYMMDD = "yyyyMMdd";
  public static final String FORMAT_YYYYMMDDHH = "yyyyMMddHH";
  public static final String FORMAT_YYYY_MM = "yyyy-MM";

  public DateUtil() {
  }

  public static long parseTime(String time) {
    try {
      SimpleDateFormat yyyyMMddHHmmssSDF = new SimpleDateFormat("yyyyMMddHHmmss");
      return yyyyMMddHHmmssSDF.parse(time).getTime();
    } catch (Exception var2) {
      logger.error("转换时间异常:time=" + time, var2);
      return System.currentTimeMillis();
    }
  }

  public static String convertBeginDate(String beginDate) {
    if (beginDate == null) {
      return null;
    } else {
      SimpleDateFormat sdf;
      if (beginDate.contains("-") && beginDate.contains(":")) {
        try {
          sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          beginDate = sdf.format(sdf.parse(beginDate));
        } catch (Exception var2) {
          logger.error(var2.getMessage(), var2);
        }

        return beginDate.replace("-", "").replace(":", "").replace(" ", "");
      } else {
        try {
          sdf = new SimpleDateFormat("yyyy-MM-dd");
          beginDate = sdf.format(sdf.parse(beginDate));
        } catch (Exception var3) {
          logger.error(var3.getMessage(), var3);
        }

        return beginDate.replace("-", "") + "000000";
      }
    }
  }

  public static String convertEndDate(String endDate) {
    if (endDate == null) {
      return null;
    } else {
      SimpleDateFormat sdf;
      if (endDate.contains("-") && endDate.contains(":")) {
        try {
          sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          endDate = sdf.format(sdf.parse(endDate));
        } catch (Exception var2) {
          logger.error(var2.getMessage(), var2);
        }

        return endDate.replace("-", "").replace(":", "").replace(" ", "");
      } else {
        try {
          sdf = new SimpleDateFormat("yyyy-MM-dd");
          endDate = sdf.format(sdf.parse(endDate));
        } catch (Exception var3) {
          logger.error(var3.getMessage(), var3);
        }

        return endDate.replace("-", "") + "235959";
      }
    }
  }

  public static Date nowDate() {
    return new Date();
  }

  public static String formatDateToStringYYMMDD(Date date) {
    return formatDateToString(date, "yyyy-MM-dd");
  }

  public static String formatDateToStringYYMMDDHHMM(Date date) {
    return formatDateToString(date, "yyyyMMddHHmm");
  }

  public static String formatDateToStringYYMMDDHHmmss(Date date) {
    return formatDateToString(date, "yyyy-MM-dd HH:mm:ss");
  }

  public static String formatDateToStringYYMM(Date date) {
    return formatDateToString(date, "yyyyMM");
  }

  public static String formatDateToStringYYYYMMDD(Date date) {
    return formatDateToString(date, "yyyyMMdd");
  }

  public static String formatDateToStringYY_MM(Date date) {
    return formatDateToString(date, "yyyy-MM");
  }

  public static String formatDateToStringYY_MM_DD(Date date) {
    return formatDateToString(date, "yyyy-MM-dd");
  }

  public static String formatDateToStringYYYYMMDDHHMMSS(Date date) {
    return formatDateToString(date, "yyyyMMddHHmmss");
  }

  public static String formatDateToString(Date date, String format) {
    if (date == null) {
      return "";
    } else {
      SimpleDateFormat dateFormat = new SimpleDateFormat(format);

      try {
        return dateFormat.format(date);
      } catch (Exception var4) {
        var4.printStackTrace();
        return "";
      }
    }
  }

  public static String nowDateToStringYYMMDDHHmmss() {
    Date date = nowDate();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    try {
      return dateFormat.format(date);
    } catch (Exception var3) {
      var3.printStackTrace();
      return "";
    }
  }

  public static String nowDateToStringYYMM() {
    Date date = nowDate();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");

    try {
      return dateFormat.format(date);
    } catch (Exception var3) {
      var3.printStackTrace();
      return "";
    }
  }

  public static String getCurrentYearFirstMonth() {
    Calendar cla = Calendar.getInstance();
    return cla.get(1) + "01";
  }

  public static Date parseStringToDateYYMMDD(String value) {
    return parseStringToDate(value, "yyyy-MM-dd");
  }

  public static Date parseStringToDateYYYYMMDD(String value) {
    return parseStringToDate(value, "yyyyMMdd");
  }

  public static Date parseStringToDateYYYYMM(String value) {
    return parseStringToDate(value, "yyyy-MM");
  }

  public static Date parseStringToDateYYYYMMDDHHMMSS(String value) {
    return parseStringToDate(value, "yyyyMMddHHmmss");
  }

  public static Date parseStringToDateYYMM(String value) {
    return parseStringToDate(value, "yyyy-MM");
  }

  public static Date parseStringToDateYYMMDDHHmmss(String value) {
    return parseStringToDate(value, "yyyy-MM-dd HH:mm:ss");
  }

  public static Date parseStringToDate(String value, String format) {
    if (StringUtils.isEmpty(value)) {
      return null;
    } else {
      SimpleDateFormat dateFormat = new SimpleDateFormat(format);

      try {
        return dateFormat.parse(value);
      } catch (Exception var4) {
        var4.printStackTrace();
        return null;
      }
    }
  }

  public static Date parseNowDateYYMMDD() {
    String value = formatDateToStringYYMMDD(nowDate());
    return parseStringToDateYYMMDD(value);
  }

  public static Date addDateDay(Date date, int day) {
    if (date == null) {
      return null;
    } else {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      calendar.add(6, day);
      return calendar.getTime();
    }
  }

  public static String getDayOfWeek(Date date, int day) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.set(5, calendar.get(5) + day);
    String dayOfWeek = String.valueOf(calendar.get(7) - 1);
    return dayOfWeek;
  }

  public static String getMonthOfYear(Date date, int day) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.set(5, calendar.get(5) + day);
    String month = String.valueOf(calendar.get(2) + 1);
    return month;
  }

  public static Date addDateMonth(Date date, int month) {
    if (date == null) {
      return null;
    } else {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      calendar.add(2, month);
      return calendar.getTime();
    }
  }

  public static int getCurrentHour() {
    Calendar calendar = Calendar.getInstance();
    return calendar.get(11);
  }

  public static boolean compareDay(Date fromDate, Date toDate, int day) {
    return fromDate != null && toDate != null ? addDateDay(fromDate, day).after(toDate) : false;
  }

  public static Date createTokenExpireDate(int day) {
    return addDateDay(nowDate(), day);
  }

  public static int comperDate(Date fromDate, Date toDate, String flag) {
    SimpleDateFormat f = null;
    if ("0".equals(flag)) {
      f = new SimpleDateFormat("yyyyMMdd");
    } else if ("1".equals(flag)) {
      f = new SimpleDateFormat("yyyyMMddHH");
    }

    int fromDateNumber = Integer.parseInt(f.format(fromDate).toString());
    int toDateNumber = Integer.parseInt(f.format(toDate).toString());
    System.out.println("fromDateNumber+==" + fromDateNumber + "toDateNumber===" + toDateNumber);
    int retInt = fromDateNumber - toDateNumber;
    return retInt;
  }

  public static String getDateInterval(Date date, int billingType) {
    String retStr = "";
    String year = formatDateToStringYY_MM(date).substring(0, 4);
    int month = Integer.parseInt(getMonthOfYear(new Date(), 2));
    Calendar calendar = Calendar.getInstance();
    int maxDay;
    if (billingType == 1) {
      calendar.set(Integer.parseInt(year), month - 1, 1);
      maxDay = calendar.getActualMaximum(5);
      retStr = formatDateToStringYY_MM(date) + "-01," + formatDateToStringYY_MM(date) + "-" + maxDay;
    } else if (billingType == 3) {
      if (month >= 1 && month <= 3) {
        calendar.set(Integer.parseInt(year), 2, 1);
        maxDay = calendar.getActualMaximum(5);
        retStr = year + "-01-01," + year + "-03-" + maxDay;
      } else if (month >= 4 && month <= 6) {
        calendar.set(Integer.parseInt(year), 5, 1);
        maxDay = calendar.getActualMaximum(5);
        retStr = year + "-04-01," + year + "-06-" + maxDay;
      } else if (month >= 7 && month <= 9) {
        calendar.set(Integer.parseInt(year), 8, 1);
        maxDay = calendar.getActualMaximum(5);
        retStr = year + "-07-01," + year + "-09-" + maxDay;
      } else if (month >= 10 && month <= 12) {
        calendar.set(Integer.parseInt(year), 11, 1);
        maxDay = calendar.getActualMaximum(5);
        retStr = year + "-10-01," + year + "-12-" + maxDay;
      }
    } else if (billingType == 6) {
      if (month >= 1 && month <= 6) {
        calendar.set(Integer.parseInt(year), 5, 1);
        maxDay = calendar.getActualMaximum(5);
        retStr = year + "-01-01," + year + "-06-" + maxDay;
      } else if (month >= 6 && month <= 12) {
        calendar.set(Integer.parseInt(year), 11, 1);
        maxDay = calendar.getActualMaximum(5);
        retStr = year + "-07-01," + year + "-12-" + maxDay;
      }
    } else if (billingType == 12 && month >= 1 && month <= 12) {
      calendar.set(Integer.parseInt(year), 11, 1);
      maxDay = calendar.getActualMaximum(5);
      retStr = year + "-01-01," + year + "-12-" + maxDay;
    }

    return retStr;
  }

  public static void main(String[] arg) {
    System.out.println(parseStringToDate("2016-01", "yyyy-MM"));
  }

  public static final Date lastDayOfMonth(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    int value = cal.getActualMaximum(5);
    cal.set(5, value);
    return cal.getTime();
  }

  public static final boolean isLastDayOfMonth(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    return cal.get(5) == cal.getActualMaximum(5);
  }

  public static final boolean isFirstDayOfMonth(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    return cal.get(5) == cal.getActualMinimum(5);
  }

  public static final Date addMonthNumbersOfDateMonthHead(Date date, int monthNumbers) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(2, monthNumbers);
    int value = cal.getActualMinimum(5);
    cal.set(5, value);
    return cal.getTime();
  }

  public static final Date addMonthNumbersOfDateMonthTail(Date date, int monthNumbers) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(2, monthNumbers);
    int value = cal.getActualMaximum(5);
    cal.set(5, value);
    return cal.getTime();
  }

  public static final int daysBetween(Date early, Date late) {
    Calendar calst = Calendar.getInstance();
    Calendar caled = Calendar.getInstance();
    calst.setTime(early);
    caled.setTime(late);
    calst.set(11, 0);
    calst.set(12, 0);
    calst.set(13, 0);
    caled.set(11, 0);
    caled.set(12, 0);
    caled.set(13, 0);
    int days = ((int)(caled.getTime().getTime() / 1000L) - (int)(calst.getTime().getTime() / 1000L)) / 3600 / 24;
    return days;
  }

  public static final int getDaysOfMonth(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    int value = cal.getActualMaximum(5);
    return value;
  }
}
