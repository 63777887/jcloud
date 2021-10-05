package com.jwk.common.core.utils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;

/**
 * Created by Simple on 2017/11/7.
 */
public class DateHelper {

  /**
   * @param date
   * @return
   */

  public static final String FORMAT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

  public static Date getDate(Date date) {
    try {
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      String dateString = format.format(date);
      return format.parse(dateString);
    } catch (ParseException ex) {
    }
    return date;
  }

  /**
   * 最大天数
   *
   * @param date
   * @return
   */
  public static Integer getMaxDayOfMonth(Long date) {
    String time = date.toString().substring(0, 6);
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
    Date parsepDate = null;
    try {
      parsepDate = df.parse(time);
    } catch (ParseException e) {
      throw new RuntimeException("最大天数失败：" + date);
    }
    cal.setTime(parsepDate);
    return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
  }

  /**
   * 将字符串日期转成Date
   *
   * @param strDate
   * @return
   */
  public static Date getDateByStrDate(String strDate) {
    return getDateByStrDate(strDate, "yyyy-MM-dd");
  }

  public static Date getDateByStrDate(String strDate, String pattern) {
    try {
      SimpleDateFormat fmt = new SimpleDateFormat(pattern);
      return fmt.parse(strDate);
    } catch (ParseException ex) {
      return null;
    }
  }

  /**
   * 将DATE转换为
   *
   * @param date
   * @return
   */
  public static int getCurrentTime(Date date) {
    int timeMills = 0;
    if (date == null) {
      return timeMills;
    }
    timeMills = Integer.parseInt(String.valueOf(date.getTime() / 1000));
    return timeMills;
  }

  /**
   * 将14长度的日期值进行格式化 示例: 20180515123518 ==> 2018-05-15 12:35:18
   *
   * @param bigDate
   * @return
   */
  public static String getStringDate(Long bigDate) {
    return get14StringDate(bigDate);
  }


  /**
   * 获取当天
   *
   * @return
   */
  public static long getToday() {
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Date());
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    return getLongDate(cal.getTime());
  }


  /**
   * 将14位长度的日期值进行格式化 示例: 20180515123518 ==> 2018-05-15 12:35:18
   *
   * @param bigDate
   * @return
   */
  public static String get14StringDate(Long bigDate) {
    if (null == bigDate) {
      return "";
    }
    String date = bigDate.toString();
    if (date.length() != 14) {
      return "";
    }
    return date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8) + " "
        + date.substring(8, 10) + ":" + date.substring(10, 12) + ":" + date.substring(12, 14);
  }

  /**
   * 将14位长度的日期值进行格式化 示例: 20180515123518 ==> 2018.05.15 12:35:18
   *
   * @param bigDate
   * @return
   */
  public static String get14StringDate1(Long bigDate) {
    if (null == bigDate) {
      return "";
    }
    String date = bigDate.toString();
    if (date.length() != 14) {
      return "";
    }
    return date.substring(0, 4) + "." + date.substring(4, 6) + "." + date.substring(6, 8) + " "
        + date.substring(8, 10) + ":" + date.substring(10, 12) + ":" + date.substring(12, 14);
  }

  /**
   * 将12位长度的日期值进行格式化 示例: 20180515123518 ==> 2018-05-15 12:35
   *
   * @param bigDate
   * @return
   */
  public static String get12StringDate(Long bigDate) {
    if (null == bigDate) {
      return "";
    }
    String date = bigDate.toString();
    if (date.length() < 12) {
      return "";
    }
    return date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8) + " "
        + date.substring(8, 10) + ":" + date.substring(10, 12);
  }

  /**
   * 将14位长度的日期值进行格式化 示例: 20180515123518 ==> 2018-05-15
   *
   * @param bigDate
   * @return
   */
  public static String get8StringDate(Long bigDate) {
    if (null != bigDate && bigDate > 0) {
      String date = bigDate.toString();
      if (date.length() < 8) {
        return "";
      }
      return date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
    }
    return "";
  }

  /**
   * 将6位长度的日期值进行格式化 示例: 201805 ==> 2018年05月
   *
   * @param bigDate
   * @return
   */
  public static String get6StringDate(Integer bigDate) {
    if (null != bigDate && bigDate > 0) {
      String date = bigDate.toString();
      if (date.length() < 6) {
        return "";
      }
      return date.substring(0, 4) + "-" + date.substring(4, 6);
    }
    return "";
  }

  /**
   * @param startDate
   * @param endDate
   * @return
   */
  public static String getPeriodStringDate(Long startDate, Long endDate) {
    if (null != startDate && startDate > 0 && null != endDate && endDate > 0) {
      String start = startDate.toString();
      String end = endDate.toString();
      if (start.length() < 8 || end.length() < 8) {
        return "";
      }
      return start.substring(0, 4) + "-" + start.substring(4, 6) + "-" + start.substring(6, 8) + "至"
          + end.substring(0, 4) + "-" + end.substring(4, 6) + "-" + end.substring(6, 8);
    }
    return "";
  }

  /**
   * 将日期生成14位长度的日期值
   *
   * @param date
   * @return
   */
  public static Long getLongDate(Date date) {
    SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
    return Long.valueOf(fmt.format(date));
  }


  /**
   * 将日期生成8位长度的日期值
   *
   * @param date
   * @return
   */
  public static Long get8LongDate(Date date) {
    SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
    return Long.valueOf(fmt.format(date));
  }

  /**
   * 将日期生成14位长度的日期值
   *
   * @param date
   * @return
   */
  public static Long getLongDate(String date) {
    if (StringUtils.isNotBlank(date)) {
      try {
        date = date.trim();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (date.length() == 10) {
          fmt = new SimpleDateFormat("yyyy-MM-dd");
        }
        String str = new SimpleDateFormat("yyyyMMddHHmmss").format(fmt.parse(date));
        return Long.valueOf(str);
      } catch (ParseException ex) {
        return 0L;
      }
    } else {
      return 0L;
    }
  }

  public static Long getLongDateZeroTime(Date date) {
    SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd000000");
    return Long.valueOf(fmt.format(date));
  }

  /**
   * 将日期生成6位长度的日期值
   *
   * @param date
   * @return
   */
  public static Integer get6LongDate(String date) {
    if (StringUtils.isNotBlank(date)) {
      try {
        date = date.trim();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (date.length() == 7) {
          fmt = new SimpleDateFormat("yyyy-MM");
        }
        String str = new SimpleDateFormat("yyyyMM").format(fmt.parse(date));
        return Integer.valueOf(str);
      } catch (ParseException ex) {
        return 0;
      }
    } else {
      return 0;
    }
  }

  /**
   * 将2020-03日期生成14位长度的日期值
   *
   * @param date
   * @return
   */
  public static Long getLongDateFrom7(String date) {
    if (StringUtils.isNotBlank(date)) {
      try {
        date = date.trim();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (date.length() == 7) {
          fmt = new SimpleDateFormat("yyyy-MM");
        }
        String str = new SimpleDateFormat("yyyyMMddHHmmss").format(fmt.parse(date));
        return Long.valueOf(str);
      } catch (ParseException ex) {
        return 0L;
      }
    } else {
      return 0L;
    }
  }

  /**
   * 将20200322日期生成14位长度的日期值20200322235959
   *
   * @param date
   * @return
   */
  public static Long getLongFrom8(Long date) {
    if (null != date && date.toString().length() == 8) {
      return date * 1000000 + 235959;
    } else {
      return 0L;
    }
  }


  /**
   * @param date
   * @return
   */
  public static Date getDateFromLongDate(Long date) {
    try {
      return new SimpleDateFormat("yyyyMMddHHmmss").parse(date.toString());
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * 日期增加月份数后的新日期（按实际日期天数返回）
   */
  public static Date getNewDate(Date date, Integer monthNumbers) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.MONTH, monthNumbers);
    return cal.getTime();
  }

  /**
   * 日期增加月份数后的新日期（只计算月头）
   *
   * @param date
   * @return
   */
  public static final Date addMonthNumbersOfDateMonthHead(Date date, int monthNumbers) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.MONTH, monthNumbers);
    int value = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
    cal.set(Calendar.DAY_OF_MONTH, value);
    return cal.getTime();
  }

  /**
   * 日期增加月份数后的新日期（只计算月尾）
   *
   * @param date
   * @return
   */
  public static final Date addMonthNumbersOfDateMonthTail(Date date, int monthNumbers) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.MONTH, monthNumbers);
    int value = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    cal.set(Calendar.DAY_OF_MONTH, value);
    return cal.getTime();
  }

  /**
   * 日期string转14位long日期
   *
   * @param dateStr
   * @return
   */
  public static Long getLongDateFromStr(String dateStr) {
    if (StringUtils.isBlank(dateStr)) {
      return null;
    }
    return getLongDateFromStr(dateStr, "yyyyMMddHHmmss");
  }

  /**
   * 日期string转14位long日期
   *
   * @param dateStr
   * @param pattern
   * @return
   */
  public static Long getLongDateFromStr(String dateStr, String pattern) {
    if (StringUtils.isBlank(dateStr)) {
      return null;
    }
    SimpleDateFormat fmt = new SimpleDateFormat(pattern);
    Date date;
    try {
      date = fmt.parse(dateStr);
    } catch (ParseException e) {
      throw new RuntimeException(pattern + "日期转换失败：" + dateStr);
    }
    return getLongDate(date);
  }

  /**
   * 日期string转14位long日期
   *
   * @param dateStr
   * @return
   */
  public static Long getTomorrow(String dateStr, int day) {
    DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = null;
    try {
      date = format2.parse(dateStr);
      Calendar calendar = new GregorianCalendar();
      calendar.setTime(date);
      calendar.add(Calendar.DATE, day);
      date = calendar.getTime();
      SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
      Long.valueOf(fmt.format(date));
      return Long.valueOf(fmt.format(date));
    } catch (ParseException e) {
      return 0L;
    }
  }

  /**
   * 计算两个日期之间相差多少分钟
   *
   * @param date1 格式:20180515123518
   * @param date2 格式:20180515123518
   * @return
   */
  public static long getDiffMins(Long date1, Long date2) {
    try {
      SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      Date begin = dfs.parse(get14StringDate(date1));
      Date end = dfs.parse(get14StringDate(date2));
      //除以1000是为了转换成秒
      long between = (end.getTime() - begin.getTime()) / 1000;
      long min = between / 60;
      return min;
    } catch (ParseException ex) {
      return 0;
    }
  }

  /**
   * 根据日期格式，获取日期字符串
   *
   * @return
   */
  public static String nowDateToString(String format) {
    Date date = nowDate();
    SimpleDateFormat dateFormat = new SimpleDateFormat(format);
    try {
      return dateFormat.format(date);
    } catch (Exception ex) {
      ex.printStackTrace();
      return "";
    }
  }

  /**
   * 获取当前日期
   *
   * @return
   */
  public static Date nowDate() {
    return new Date();
  }

  /**
   * 2.     * 计算两个日期之间相差的月数 3.     * @param date1 4.     * @param date2 5.     * @return 6.
   */
  public static int getMonths(Date date1, Date date2) {
    int iMonth = 0;
    int flag = 0;
    try {
      Calendar objCalendarDate1 = Calendar.getInstance();
      objCalendarDate1.setTime(date1);
      Calendar objCalendarDate2 = Calendar.getInstance();
      objCalendarDate2.setTime(date2);
      if (objCalendarDate2.equals(objCalendarDate1)) {
        return 0;
      }
      if (objCalendarDate1.after(objCalendarDate2)) {
        Calendar temp = objCalendarDate1;
        objCalendarDate1 = objCalendarDate2;
        objCalendarDate2 = temp;
      }
      if (objCalendarDate2.get(Calendar.DAY_OF_MONTH) < objCalendarDate1
          .get(Calendar.DAY_OF_MONTH)) {
        flag = 1;
      }

      if (objCalendarDate2.get(Calendar.YEAR) > objCalendarDate1.get(Calendar.YEAR)) {
        iMonth = ((objCalendarDate2.get(Calendar.YEAR) - objCalendarDate1.get(Calendar.YEAR))
            * 12 + objCalendarDate2.get(Calendar.MONTH) - flag)
            - objCalendarDate1.get(Calendar.MONTH);
      } else {
        iMonth = objCalendarDate2.get(Calendar.MONTH)
            - objCalendarDate1.get(Calendar.MONTH) - flag;
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return iMonth;
  }

  /**
   * 获取两个日期相差的月数
   *
   * @param d1 较大的日期
   * @param d2 较小的日期
   * @return 如果d1>d2,返回月数差,否则返回0
   */
  public static int getMonthDiff(Date d1, Date d2) {
    Calendar c1 = Calendar.getInstance();
    Calendar c2 = Calendar.getInstance();
    c1.setTime(d1);
    c2.setTime(d2);

    if (c1.getTimeInMillis() < c2.getTimeInMillis()) {
      return 0;
    }

    int year1 = c1.get(Calendar.YEAR);
    int year2 = c2.get(Calendar.YEAR);
    int month1 = c1.get(Calendar.MONTH);
    int month2 = c2.get(Calendar.MONTH);
    int day1 = c1.get(Calendar.DAY_OF_MONTH);
    int day2 = c2.get(Calendar.DAY_OF_MONTH);

    // 获取年的差值
    int yearInterval = year1 - year2;

    // 获取年数差值(不足一年时处理)
    int str1 = Integer.parseInt("1" + getLongDate(d1).toString().substring(4, 8));
    int str2 = Integer.parseInt("1" + getLongDate(d2).toString().substring(4, 8));
    if (str1 < str2) {
      yearInterval--;
    }

    // 获取月数差值(不足一月时处理)
    int monthInterval = (month1 + 12) - month2;
    if (day1 < day2) {
      monthInterval--;
    }

    // 天数相同则相加一天
    if (day1 >= day2) {
      monthInterval++;
    }

    if (monthInterval == 12) {
      yearInterval++;
    }

    monthInterval %= 12;
    return yearInterval * 12 + monthInterval;
  }

  /**
   * 获取指定几天后的日期
   *
   * @param date
   * @param day
   * @return
   */
  public static long getOtherDay(Long date, int day) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(DateHelper.getDateFromLongDate(date));
    cal.add(Calendar.DATE, day);
    return getLongDate(cal.getTime());
  }

  /**
   * 得到几天前的时间
   *
   * @param date
   * @param day
   * @return
   */
  public static long getDateBefore(Long date, int day) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(DateHelper.getDateFromLongDate(date));
    cal.add(Calendar.DATE, -day);
    return getLongDate(cal.getTime());
  }

  /**
   * 获取一年的最后一天
   *
   * @param year
   * @return
   */
  public static long getLastDayOfYear(int year) {
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.YEAR, year);
    cal.set(Calendar.DAY_OF_YEAR, cal.getActualMaximum(Calendar.DAY_OF_YEAR));
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    return getLongDate(cal.getTime());
  }

  /**
   * @param year
   * @return
   */
  public static Long getOneDayOfYear(int year) {
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    if (year != cal.get(Calendar.YEAR)) {
      cal.set(Calendar.YEAR, year);
      cal.set(Calendar.DAY_OF_YEAR, 1);
    }
    return getLongDate(cal.getTime());
  }

  /**
   * date2比date1多的天数
   *
   * @param startDate
   * @param endDate
   * @return
   */
  public static int differentDays(Long startDate, Long endDate) {
    SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    try {
      Date date1 = dfs.parse(get14StringDate(startDate));
      Date date2 = dfs.parse(get14StringDate(endDate));
      Calendar cal1 = Calendar.getInstance();
      cal1.setTime(date1);

      Calendar cal2 = Calendar.getInstance();
      cal2.setTime(date2);
      int day1 = cal1.get(Calendar.DAY_OF_YEAR);
      int day2 = cal2.get(Calendar.DAY_OF_YEAR);

      int year1 = cal1.get(Calendar.YEAR);
      int year2 = cal2.get(Calendar.YEAR);
      if (year1 != year2)   //同一年
      {
        int timeDistance = 0;
        for (int i = year1; i < year2; i++) {
          if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0)    //闰年
          {
            timeDistance += 366;
          } else    //不是闰年
          {
            timeDistance += 365;
          }
        }

        return timeDistance + (day2 - day1) + 1;
      } else    //不同年
      {
//            System.out.println("判断day2 - day1 : " + (day2-day1));
        return day2 - day1 + 1;
      }
    } catch (ParseException e) {
      return 0;
    }
  }

  /**
   * @param date
   * @return
   */
  public static int getDay(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar.get(Calendar.DATE);
  }

  /**
   * 返回日期的月份，1-12,即yyyy-MM-dd中的MM
   *
   * @param date
   * @return
   */
  public static int getMonth(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar.get(Calendar.MONTH) + 1;
  }

  /**
   * 返回日期的月份，1-12,即yyyy-MM-dd中的MM
   *
   * @param validatetime
   * @return
   */
  public static int getCurrentMonthDay(Long validatetime) {
    try {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
      Date now = sdf.parse(validatetime.toString());
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(now);
      return calendar.get(Calendar.DATE);
    } catch (ParseException ex) {
      return 0;
    }
  }

  /**
   * 返回日期的年,即yyyy-MM-dd中的yyyy
   *
   * @param date Date
   * @return int
   */
  public static int getYear(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar.get(Calendar.YEAR);
  }

  /**
   * @param year
   * @param month
   * @return
   */
  public static int getDaysOfMonth(int year, int month) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(year, month - 1, 1);
    return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
  }

  /**
   * 判断该日期是否是该月的最后一天
   *
   * @param date 需要判断的日期
   * @return
   */
  public static boolean isLastDayOfMonth(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar.get(Calendar.DAY_OF_MONTH) == calendar
        .getActualMaximum(Calendar.DAY_OF_MONTH);

  }

  /**
   * 判断该日期是否是该月的第一天
   *
   * @param date 需要判断的日期
   * @return
   */
  public static boolean isFirstDayOfMonth(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar.get(Calendar.DAY_OF_MONTH) == 1;
  }

  /**
   * 计算两个日期相差的月数
   *
   * @param startDate
   * @param endDate
   * @return
   */
  public static int calDiffMonth(String startDate, String endDate) {
    return calDiffMonth(startDate, endDate, "yyyy-MM-dd");
  }

  public static int calDiffMonth(String startDate, String endDate, String pattern) {
    int result = 0;
    try {
      Date start = getDateByStrDate(startDate, pattern);
      Date end = getDateByStrDate(endDate, pattern);

      int startYear = getYear(start);
      int startMonth = getMonth(start);
      int startDay = getDay(start);
      int endYear = getYear(end);
      int endMonth = getMonth(end);
      int endDay = getDay(end);

      //1月17  大于 2月28
      if (startDay > endDay) {
        //也满足一月
        if (endDay == getDaysOfMonth(getYear(new Date()), 2)) {
          result = (endYear - startYear) * 12 + endMonth - startMonth;
        } else {
          result = (endYear - startYear) * 12 + endMonth - startMonth - 1;
        }
      } else {
        result = (endYear - startYear) * 12 + endMonth - startMonth;
      }
      if (startDay - endDay == 1 || (isFirstDayOfMonth(start)) && isLastDayOfMonth(end)) {
        result = result + 1;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  /**
   * 获取当前年月日
   *
   * @param date
   * @return
   */
  public static String getYearMonthDay(Date date) {
    SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
    return fmt.format(date);
  }

  /**
   * 获取昨天的0时0分0秒
   *
   * @param today
   * @return
   */
  public static long yesterdayZero(Date today) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(today);
    calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    return getLongDate(calendar.getTime());
  }

  /**
   * 获取昨天的23时59分59秒
   *
   * @param today
   * @return
   */
  public static long yesterdayAll(Date today) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(today);
    calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
    calendar.set(Calendar.HOUR_OF_DAY, 23);
    calendar.set(Calendar.MINUTE, 59);
    calendar.set(Calendar.SECOND, 59);
    return getLongDate(calendar.getTime());
  }

  /**
   * 获取明天的0时0分0秒
   *
   * @param today
   * @return
   */
  public static Date tomorrow(Date today) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(today);
    calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    return calendar.getTime();
  }

  /**
   * 获取当天的0时0分0秒
   *
   * @param today
   * @return
   */
  public static Date today(Date today) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(today);
    calendar.set(Calendar.DATE, calendar.get(Calendar.DATE));
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    return calendar.getTime();
  }

  /**
   * 获取后天的0时0分0秒
   *
   * @param today
   * @return
   */
  public static Date afterTomorrow(Date today) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(today);
    calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 2);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    return calendar.getTime();
  }

  /**
   * 获取某天的时间
   *
   * @param index 为正表示当前时间加天数，为负表示当前时间减天数
   * @return String
   */
  public static Date getTimeDay(int index) {
    TimeZone tz = TimeZone.getTimeZone("Asia/Nanjing");
    TimeZone.setDefault(tz);
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.DAY_OF_MONTH, index);
    Date time = calendar.getTime();
    return time;
  }

  /**
   * 导入Excel文件日期解析成天数后转换成固定类型
   *
   * @param value
   * @return
   */
  public static String importByExcelForDate(String value) {//value就是它的天数
    String currentCellValue = "";
    if (value != null && !"".equals(value)) {
      Calendar calendar = new GregorianCalendar(1900, 0, -1);
      Date d = calendar.getTime();
      Date dd = DateUtils.addDays(d, Integer.parseInt(value));
      DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
      currentCellValue = formater.format(dd).replace("-", "");
    }
    return currentCellValue;
  }

  /**
   * 判断导入Excel文件不是是日期格式
   *
   * @param cell
   * @return
   */
  public static boolean isNotCellDateFormatted(Cell cell) {
    if (isCellDateFormatted(cell)) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * 获取当前月份的总天数
   *
   * @param date
   * @return
   */
  public static final int getDaysOfMonth(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    //得到一个月最最后一天日期(31/30/29/28)
    int value = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    return value;
  }

  /**
   * 判断导入Excel文件是否是日期格式
   *
   * @param cell
   * @return
   */
  public static boolean isCellDateFormatted(Cell cell) {
    if (cell == null) {
      return false;
    }
    boolean bDate = false;
    double d = cell.getNumericCellValue();
    if (isValidExcelDate(d)) {
      CellStyle style = cell.getCellStyle();
      if (style == null) {
        return false;
      }
      int i = style.getDataFormat();
      String f = style.getDataFormatString();
      bDate = isADateFormat(i, f);
    }
    return bDate;
  }

  public static boolean isADateFormat(int formatIndex, String formatString) {
    if (isInternalDateFormat(formatIndex)) {
      return true;
    }
    if ((formatString == null) || (formatString.length() == 0)) {
      return false;
    }
    String fs = formatString;
    fs = fs.replaceAll("[\"|\']", "").replaceAll("[年|月|日|时|分|秒|毫秒|微秒]", "");
    fs = fs.replaceAll("\\\\-", "-");
    fs = fs.replaceAll("\\\\,", ",");
    fs = fs.replaceAll("\\\\.", ".");
    fs = fs.replaceAll("\\\\ ", " ");
    fs = fs.replaceAll(";@", "");
    fs = fs.replaceAll("^\\[\\$\\-.*?\\]", "");
    fs = fs.replaceAll("^\\[[a-zA-Z]+\\]", "");
    return (fs.matches("^[yYmMdDhHsS\\-/,. :]+[ampAMP/]*$"));
  }

  /**
   * @param date
   * @return
   */
  public static String getDateFormatNoM(Date date) {
    try {
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy/M/d");
      String timeStr = formatter.format(date);
      return timeStr;
    } catch (Exception e) {
      return null;
    }
  }

  public static boolean isInternalDateFormat(int format) {
    switch (format) {
      case 14:
      case 15:
      case 16:
      case 17:
      case 18:
      case 19:
      case 20:
      case 21:
      case 22:
      case 45:
      case 46:
      case 47:
        return true;
      case 23:
      case 24:
      case 25:
      case 26:
      case 27:
      case 28:
      case 29:
      case 30:
      case 31:
      case 32:
      case 33:
      case 34:
      case 35:
      case 36:
      case 37:
      case 38:
      case 39:
      case 40:
      case 41:
      case 42:
      case 43:
      case 44:
    }
    return false;
  }

  public static boolean isValidExcelDate(double value) {
    return (value > -4.940656458412465E-324D);
  }

  public static Long getnewDate(Long validatetime, int renewalsdata) {
    try {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
      Date now = sdf.parse(validatetime.toString());
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(now);
      calendar.add(Calendar.MONTH, renewalsdata);
      return getLongDate(sdf.format(calendar.getTime()));
    } catch (ParseException ex) {
      return null;
    }


  }

  /**
   * 获取两个月份的跨度，如2018-08-29到2018-10-02算三个月
   */
  public static int getMonthsOfAge(Long startDate, Long endDate) {
    if (startDate > endDate) {
      Long temp = startDate;
      startDate = endDate;
      endDate = temp;
    }
    int result = 0;
    int startYear = Integer.parseInt(startDate.toString().substring(0, 4));
    int startMonth = Integer.parseInt(startDate.toString().substring(4, 6));
    int endYear = Integer.parseInt(endDate.toString().substring(0, 4));
    int endMonth = Integer.parseInt(endDate.toString().substring(4, 6));
    //1月17  大于 2月28
    if (startYear < endYear) {
      result = (endYear - startYear) * 12 + endMonth - startMonth;
    } else {
      //也满足一月
      if (startYear == endYear) {
        result = endMonth - startMonth;
      }
    }
    return result + 1;
  }

  /**
   * @Description: 获取指定日期下个月的第一天
   * @Param: [longDate]
   * @return: java.lang.Long
   * @Date: 2018/8/15
   */
  public static Long getNextYear(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.YEAR, 1);
    calendar.set(Calendar.MONTH, 0);
    calendar.set(Calendar.DAY_OF_MONTH, 1);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    return getLongDate(calendar.getTime());
  }

  /**
   * 根据时间 和时间格式 校验是否正确
   *
   * @param legalLen 合法的长度
   * @param sDate    校验的日期
   * @param format   校验的格式
   * @return 格式是否正确
   */
  public static boolean isLegalDate(int legalLen, String sDate, String format) {
    if ((sDate == null) || (sDate.length() != legalLen)) {
      return false;
    }
    DateFormat formatter = new SimpleDateFormat(format);
    try {
      Date date = formatter.parse(sDate);
      return sDate.equals(formatter.format(date));
    } catch (Exception e) {
      return false;
    }
  }
}