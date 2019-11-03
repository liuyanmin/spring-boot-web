package com.lym.springboot.web.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalUnit;
import java.util.Date;

/**
 * @ClassName TimeUtil
 * @Description 基于 JDK 8 time包的时间工具类
 * @Author LYM
 * @Date 2018/9/6 9:51
 * @Version 1.0
 */
public class TimeUtil {

    /**
     * 获取默认时间格式: yyyy-MM-dd HH:mm:ss
     */
    private static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER = TimeFormat.LONG_DATE_PATTERN_LINE.formatter;

    private TimeUtil(){

    }

    /**
     * String 转时间
     * @param timeStr
     * @return
     */
    public static LocalDateTime parseDateTime(String timeStr) {
        return LocalDateTime.parse(timeStr, DEFAULT_DATETIME_FORMATTER);
    }

    /**
     * String 转 LocalDateTime
     * @param timeStr
     * @param format  时间格式
     * @return
     */
    public static LocalDateTime parseDateTime(String timeStr, TimeFormat format) {
        return LocalDateTime.parse(timeStr, format.formatter);
    }

    /**
     * String 转 LocalDate
     * @param dateStr
     * @param format
     * @return
     */
    public static LocalDate parseLocalDate(String dateStr, TimeFormat format) {
        return LocalDate.parse(dateStr, format.formatter);
    }

    /**
     * String 转 Date
     * @param timeStr
     * @param format
     * @return
     * @throws ParseException
     */
    public static Date parseDate(String timeStr, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(timeStr);
    }

    /**
     * String 转 LocalTime
     * @param timeStr
     * @param format
     * @return
     */
    public static LocalTime parseLocalTime(String timeStr, TimeFormat format) {
        return LocalTime.parse(timeStr, format.formatter);
    }

    /**
     * 时间转 String
     * @param time
     * @return
     */
    public static String formatDateTime(LocalDateTime time) {
        return DEFAULT_DATETIME_FORMATTER.format(time);
    }

    /**
     * LocalDateTime 转 String
     * @param time
     * @param format 时间格式
     * @return
     */
    public static String formatDateTime(LocalDateTime time, TimeFormat format) {
        return format.formatter.format(time);
    }

    /**
     * LocalDate 转 String
     * @param date
     * @param format
     * @return
     */
    public static String formatLocalDate(LocalDate date, TimeFormat format) {
        return format.formatter.format(date);
    }

    /**
     * LocalTime 转 String
     * @param localTime
     * @param format
     * @return
     */
    public static String formatLocalTime(LocalTime localTime, TimeFormat format) {
        return format.formatter.format(localTime);
    }

    /**
     * Date 转 String
     * @param date
     * @param format
     * @return
     */
    public static String formatDate(Date date, TimeFormat format) {
        return format.formatter.format(convertDateToLDT(date));
    }

    /**
     * 获取当前时间，默认格式: yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getCurrentDatetime() {
        return DEFAULT_DATETIME_FORMATTER.format(LocalDateTime.now());
    }

    /**
     * 获取当前时间
     * @param format 时间格式
     * @return
     */
    public static String getCurrentDatetime(TimeFormat format) {
        return format.formatter.format(LocalDateTime.now());
    }

    /**
     * 获取当前时间
     * @return LocalDateTime
     */
    public static LocalDateTime getCurrentLocalDateTime() {
        return LocalDateTime.now(Clock.system(ZoneId.of("Asia/Shanghai")));
    }

    /**
     * 获取指定日期的毫秒
     * @param time
     * @return
     */
    public static Long getMilliByTime(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 获取指定日期的毫秒
     * @param time
     * @return
     */
    public static Long getMilliByTime(Date time) {
        return getMilliByTime(convertDateToLDT(time));
    }

    /**
     * 获取指定日期的秒
     * @param time
     * @return
     */
    public static Long getSecondsByTime(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }

    /**
     * 获取指定日期的秒
     * @param time
     * @return
     */
    public static Long getSecondsByTime(Date time) {
        return getSecondsByTime(convertDateToLDT(time));
    }

    /**
     * 获取当前时间的毫秒值
     * @return
     */
    public static Long getCurrMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前时间的秒值
     * @return
     */
    public static Long getCurrSeconds() {
        long millis = System.currentTimeMillis();
        return millis / 1000;
    }

    /**
     * 获取一天的开始时间 2018-09-06 00:00
     * @param time
     * @return
     */
    public static LocalDateTime getDayStart(LocalDateTime time) {
        return time.withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }

    /**
     * 获取一天的开始时间 2018-09-06 00:00
     */
    public static LocalDateTime getDayStart(Date time) {
        return getDayStart(convertDateToLDT(time));
    }

    /**
     * 获取一天的结束时间 2018-09-06 23:59:59.999999999
     * @param time
     * @return
     */
    public static LocalDateTime getDayEnd(LocalDateTime time) {
        return time.withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(999999999);
    }

    /**
     * 获取一天的结束时间 2018-09-06 23:59:59.999999999
     */
    public static LocalDateTime getDayEnd(Date time) {
        return getDayEnd(convertDateToLDT(time));
    }

    /**
     * 获取月的开始时间 2018-09-01 00:00
     * @param time
     * @return
     */
    public static LocalDateTime getMonthStart(LocalDateTime time) {
        return time.withDayOfMonth(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }

    /**
     * 获取月的开始时间 2018-09-01 00:00
     */
    public static LocalDateTime getMonthStart(Date date) {
        return getMonthStart(convertDateToLDT(date));
    }

    /**
     * 获取月的结束时间 2018-09-30 23:59:59.999999999
     * @param time
     * @return
     */
    public static LocalDateTime getMonthEnd(LocalDateTime time) {
        return time.withDayOfMonth(getActualMaximum(time))
                .withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(999999999);
    }

    /**
     * 获取月的结束时间 2018-09-30 23:59:59.999999999
     */
    public static LocalDateTime getMonthEnd(Date date) {
        return getMonthEnd(convertDateToLDT(date));
    }

    /**
     * 获取年的开始时间 2018-01-01 00:00
     * @param time
     * @return
     */
    public static LocalDateTime getYearStart(LocalDateTime time) {
        return time.withMonth(1)
                .withDayOfMonth(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }

    /**
     * 获取年的开始时间 2018-01-01 00:00
     */
    public static LocalDateTime getYearStart(Date date) {
        return getYearStart(convertDateToLDT(date));
    }

    /**
     * 获取年的结束时间 2018-12-31 23:59:59.999999999
     * @param time
     * @return
     */
    public static LocalDateTime getYearEnd(LocalDateTime time) {
        return time.withMonth(12)
                .withDayOfMonth(31)
                .withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(999999999);
    }

    /**
     * 获取年的结束时间 2018-12-31 23:59:59.999999999
     */
    public static LocalDateTime getYearEnd(Date date) {
        return getYearEnd(convertDateToLDT(date));
    }

    /**
     * 获取指定月有多少天数
     * @param date
     * @return
     */
    public static int getActualMaximum(Date date) {
        return getActualMaximum(convertDateToLDT(date));
    }

    /**
     * 获取指定月有多少天数
     * @param localDateTime
     * @return
     */
    public static int getActualMaximum(LocalDateTime localDateTime) {
        return localDateTime.getMonth().length(localDateTime.toLocalDate().isLeapYear());
    }

    /**
     * 根据日期获得星期
     * @param date
     * @return 1:星期一；2:星期二；3:星期三；4:星期四；5:星期五；6:星期六；7:星期日；
     */
    public static int getWeekOfDate(Date date) {
        return convertDateToLDT(date).getDayOfWeek().getValue();
    }

    /**
     * 根据日期获得星期描述
     * @param date
     * @return 1:星期一；2:星期二；3:星期三；4:星期四；5:星期五；6:星期六；7:星期日；
     */
    public static String getWeekOfDateChinese(Date date) {
        String[] weekChineseArr = new String[]{"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};
        int week = getWeekOfDate(date);
        return weekChineseArr[week - 1];
    }

    /**
     * 获取指定日期的年份数
     * @param date
     * @return
     */
    public static int getYear(Date date) {
        return convertDateToLDT(date).getYear();
    }

    /**
     * 获取指定日期的月份数
     * @param date
     * @return
     */
    public static int getMonth(Date date) {
        return convertDateToLDT(date).getMonthValue();
    }

    /**
     * 返回年月，格式: yyyyMM
     * @param year
     * @param month
     * @return
     */
    public static int getYearAndMonth(int year, int month) {
        String monthStr = String.valueOf(month);
        if (month < 10) {
            monthStr = "0" + month;
        }
        return Integer.valueOf(year + monthStr);
    }

    /**
     * 获取指定日期当月的天数
     * @param date
     * @return
     */
    public static int getDayOfMonth(Date date) {
        return convertDateToLDT(date).getDayOfMonth();
    }

    /**
     * 获取指定日期当年的天数
     * @param date
     * @return
     */
    public static int getDayOfYear(Date date) {
        return convertDateToLDT(date).getDayOfYear();
    }

    /**
     * 获取 LocalTime 的 时/分/秒/毫秒
     * @param time
     * @param field
     * @return
     */
    public static int getTimeOfDay(LocalTime time, ChronoUnit field) {
        if (field == ChronoUnit.HOURS) {
            return time.getHour();
        } else if (field == ChronoUnit.MINUTES) {
            return time.getHour() * 60 + time.getMinute();
        } else if (field == ChronoUnit.SECONDS) {
            return time.getHour() * 3600 + time.getMinute() * 60 + time.getSecond();
        } else if (field == ChronoUnit.MILLIS) {
            return (time.getHour() * 3600 + time.getMinute() * 60 + time.getSecond()) * 1000 + time.getNano()/1000000;
        }
        return -1;
    }

    /**
     * 获取 Date 的 时/分/秒/毫秒
     * @param date
     * @param field
     * @return
     */
    public static int getTimeOfDay(Date date, ChronoUnit field) {
        return getTimeOfDay(convertLocalTimeToDate(date), field);
    }

    /**
     * Date转 LocalDateTime
     * @param date
     * @return
     */
    public static LocalDateTime convertDateToLDT(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * LocalDateTime转 Date
     * @param time
     * @return
     */
    public static Date convertLDTToDate(LocalDateTime time) {
        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Date 转 LocalDate
     * @param date
     * @return LocalDate
     */
    public static LocalDate convertDateToLocalDate(Date date) {
        return convertDateToLDT(date).toLocalDate();
    }

    /**
     * LocalDate 转 Date
     * @param localDate
     * @return
     */
    public static Date convertLocalDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Date 转 LocalTime
     * @param date
     * @return
     */
    public static LocalTime convertLocalTimeToDate(Date date) {
        return convertDateToLDT(date).toLocalTime();
    }

    /**
     * 秒转 LocalDateTime
     * @param second 秒
     * @return
     */
    public static LocalDateTime convertSecond2LocalDateTime(long second) {
        return Instant.ofEpochSecond(second).atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime();
    }

    /**
     * 毫秒转 LocalDateTime
     * @param milli 毫秒
     * @return
     */
    public static LocalDateTime convertMilli2LocalDateTime(long milli) {
        return Instant.ofEpochMilli(milli).atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime();
    }

    /**
     * 秒转 LocalDate
     * @param second 秒
     * @return
     */
    public static LocalDate convertSecond2LocalDate(long second) {
        return Instant.ofEpochSecond(second).atZone(ZoneId.of("Asia/Shanghai")).toLocalDate();
    }

    /**
     * 毫秒转 LocalDate
     * @param milli 毫秒
     * @return
     */
    public static LocalDate convertMilli2LocalDate(long milli) {
        return Instant.ofEpochMilli(milli).atZone(ZoneId.of("Asia/Shanghai")).toLocalDate();
    }

    /**
     * 秒转 LocalDate
     * @param second 秒
     * @return
     */
    public static Date convertSecond2Date(long second) {
        LocalDateTime localDateTime = Instant.ofEpochSecond(second).atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime();
        return convertLDTToDate(localDateTime);
    }

    /**
     * 毫秒转 LocalDate
     * @param milli 毫秒
     * @return
     */
    public static Date convertMilli2Date(long milli) {
        LocalDateTime localDateTime = Instant.ofEpochMilli(milli).atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime();
        return convertLDTToDate(localDateTime);
    }

    /**
     * 日期加上一个数,根据field不同加不同值,field为 ChronoUnit.*
     * @param time
     * @param number
     * @param field ChronoUnit.YEARS/ChronoUnit.MONTHS/ChronoUnit.DAYS/ChronoUnit.HOURS/ChronoUnit.MINUTES/ChronoUnit.SECONDS/ChronoUnit.WEEKS
     * @return
     */
    public static LocalDateTime plus(LocalDateTime time, long number, TemporalUnit field) {
        return time.plus(number, field);
    }

    /**
     * 日期加上一个数,根据field不同加不同值,field为 ChronoUnit.*
     */
    public static LocalDateTime plus(Date time, long number, TemporalUnit field) {
        return plus(convertDateToLDT(time), number, field);
    }

    /**
     * 日期加上一个数,根据field不同加不同值,field为 ChronoUnit.*
     */
    public static LocalDate plus(LocalDate date, long number, TemporalUnit field) {
        return date.plus(number, field);
    }

    /**
     * 日期加上一个数,根据field不同加不同值,field为 ChronoUnit.*
     */
    public static LocalTime plus(LocalTime time, long number, TemporalUnit field) {
        return time.plus(number, field);
    }

    /**
     * 日期减去一个数,根据field不同减不同值,field参数为 ChronoUnit.*
     * @param time
     * @param number
     * @param field ChronoUnit.YEARS/ChronoUnit.MONTHS/ChronoUnit.DAYS/ChronoUnit.HOURS/ChronoUnit.MINUTES/ChronoUnit.SECONDS/ChronoUnit.WEEKS
     * @return
     */
    public static LocalDateTime minu(LocalDateTime time, long number, TemporalUnit field){
        return time.minus(number, field);
    }

    /**
     * 日期减去一个数,根据field不同减不同值,field参数为 ChronoUnit.*
     */
    public static LocalDateTime minu(Date time, long number, TemporalUnit field){
        return minu(convertDateToLDT(time), number, field);
    }

    public static LocalDate minu(LocalDate date, long number, TemporalUnit field){
        return date.minus(number, field);
    }

    public static LocalTime minu(LocalTime time, long number, TemporalUnit field){
        return time.minus(number, field);
    }

    /**
     * 日期是否在指定范围内
     * @param startDate 起始日期
     * @param endDate 终止日期
     * @param date 待比较日期
     * @return
     */
    public static boolean between(Date startDate, Date endDate, Date date) {
        boolean b1 = isBefore(startDate, date);
        boolean b2 = isAfter(endDate, date);
        return b1 && b2;
    }

    public static boolean between(LocalDate startDate, LocalDate endDate, LocalDate date) {
        Date date1 = TimeUtil.convertLocalDateToDate(startDate);
        Date date2 = TimeUtil.convertLocalDateToDate(endDate);
        Date date3 = TimeUtil.convertLocalDateToDate(date);
        return between(date1, date2, date3);
    }

    public static boolean between(LocalDateTime startTime, LocalDateTime endTime, LocalDateTime time) {
        Date startDate = TimeUtil.convertLDTToDate(startTime);
        Date endDate = TimeUtil.convertLDTToDate(endTime);
        Date date = TimeUtil.convertLDTToDate(time);
        return between(startDate, endDate, date);
    }

    /**
     * 日期是否在指定范围内，闭区间
     * @param startDate
     * @param endDate
     * @param date
     * @return
     */
    public static boolean betweenEqual(Date startDate, Date endDate, Date date) {
        boolean b1 = isBefore(startDate, date) || isEqual(startDate, date);
        boolean b2 = isAfter(endDate, date) || isEqual(endDate, date);
        return b1 && b2;
    }

    public static boolean betweenEqual(LocalDateTime startTime, LocalDateTime endTime, LocalDateTime dateTime) {
        boolean b1 = isBefore(startTime, dateTime) || isEqual(startTime, dateTime);
        boolean b2 = isAfter(endTime, dateTime) || isEqual(endTime, dateTime);
        return b1 && b2;
    }

    /**
     * 日期是否在指定范围内
     * @param startTime 起始时间
     * @param endTime 终止时间
     * @param time 待比较时间
     * @return
     */
    public static boolean between(LocalTime startTime, LocalTime endTime, LocalTime time) {
        boolean b1 = isBefore(startTime, time);
        boolean b2 = isAfter(endTime, time);
        return b1 && b2;
    }

    /**
     * 日期是否在指定范围内，闭区间
     * @param startTime
     * @param endTime
     * @param time
     * @return
     */
    public static boolean betweenEqual(LocalTime startTime, LocalTime endTime, LocalTime time) {
        boolean b1 = isBefore(startTime, time) || isEqual(startTime, time);
        boolean b2 = isAfter(endTime, time) || isEqual(endTime, time);
        return b1 && b2;
    }

    /**
     * 获取两个日期的差  field参数为ChronoUnit.*
     * @param startTime
     * @param endTime
     * @param field  单位(年月日时分秒)
     * @return
     */
    public static long betweenTwoTime(LocalDateTime startTime, LocalDateTime endTime, ChronoUnit field) {
        Period period = Period.between(LocalDate.from(startTime), LocalDate.from(endTime));
        if (field == ChronoUnit.YEARS) {
            return period.getYears();
        }
        if (field == ChronoUnit.MONTHS) {
            return period.getYears() * 12L + period.getMonths();
        }
        return field.between(startTime, endTime);
    }

    /**
     * 获取两个日期的差，不考虑时间先后顺序
     * @param startTime
     * @param endTime
     * @param field  单位(年月日时分秒)
     * @return
     */
    public static long betweenAbsTwoTime(LocalDateTime startTime, LocalDateTime endTime, ChronoUnit field) {
        return Math.abs(betweenTwoTime(startTime, endTime, field));
    }

    public static long betweenTwoTime(LocalDate startDate, LocalDate endDate, ChronoUnit field) {
        LocalTime nowTime = LocalTime.now();
        LocalDateTime startLocalDateTime = LocalDateTime.of(startDate, nowTime);
        LocalDateTime endLocalDateTime = LocalDateTime.of(endDate, nowTime);
        return betweenTwoTime(startLocalDateTime, endLocalDateTime, field);
    }


    public static long betweenAbsTwoTime(LocalDate startDate, LocalDate endDate, ChronoUnit field) {
        return Math.abs(betweenTwoTime(startDate, endDate, field));
    }

    /**
     * 获取两个日期的差  field参数为ChronoUnit.*
     * @param startDate
     * @param endDate
     * @param field  单位(年月日时分秒)
     * @return
     */
    public static long betweenTwoTime(Date startDate, Date endDate, ChronoUnit field) {
        LocalDateTime startTime = convertDateToLDT(startDate);
        LocalDateTime endTime = convertDateToLDT(endDate);
        return betweenTwoTime(startTime, endTime, field);
    }

    /**
     * 获取两个日期的差，不考虑时间先后顺序
     * @param startDate
     * @param endDate
     * @param field  单位(年月日时分秒)
     * @return
     */
    public static long betweenAbsTwoTime(Date startDate, Date endDate, ChronoUnit field) {
        return Math.abs(betweenTwoTime(startDate, endDate, field));
    }

    /**
     * 比较两个时间 LocalDateTime 大小
     * @param time1
     * @param time2
     * @return 1:第一个比第二个大；0：第一个与第二个相同；-1：第一个比第二个小
     */
    public static int compareTwoTime(LocalDateTime time1, LocalDateTime time2) {
        if (time1.isAfter(time2)) {
            return 1;
        } else if (time1.isBefore(time2)) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * 比较两个时间 LocalDateTime 大小
     * @param time1
     * @param time2
     * @return 1:第一个比第二个大；0：第一个与第二个相同；-1：第一个比第二个小
     */
    public static int compareTwoTime(Date time1, Date time2) {
        return compareTwoTime(convertDateToLDT(time1), convertDateToLDT(time2));
    }

    /**
     * 比较两个日期 LocalDate 大小
     * @param date1
     * @param date2
     * @return 1:第一个比第二个大；0：第一个与第二个相同；-1：第一个比第二个小
     */
    public static int compareTwoDate(LocalDate date1, LocalDate date2) {
        if (date1.isAfter(date2)) {
            return 1;
        } else if (date1.isBefore(date2)) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * 比较两个 LocalTime 大小
     * @param time1
     * @param time2
     * @param field ChronoUnit.YEARS/ChronoUnit.MONTHS/ChronoUnit.DAYS/ChronoUnit.HOURS/ChronoUnit.MINUTES/ChronoUnit.SECONDS/ChronoUnit.WEEKS
     * @return
     */
    public static long compareTwoTime(LocalTime time1, LocalTime time2, ChronoUnit field) {
        return field.between(time1, time2);
    }

    /**
     * 比较两个 LocalTime 大小
     * @param timeStr1
     * @param timeStr2
     * @param format TimeFormat.SHORT_DATE_PATTERN_LINE
     * @param field ChronoUnit.YEARS/ChronoUnit.MONTHS/ChronoUnit.DAYS/ChronoUnit.HOURS/ChronoUnit.MINUTES/ChronoUnit.SECONDS/ChronoUnit.WEEKS
     * @return
     */
    public static long compareTwoTime(String timeStr1, String timeStr2, TimeFormat format, ChronoUnit field) {
        LocalTime startTime = parseLocalTime(timeStr1, format);
        LocalTime endTime = parseLocalTime(timeStr2, format);
        return compareTwoTime(startTime, endTime, field);
    }

    /**
     * localDateTime1 是否在 localDateTime2 之前
     * @param localDateTime1
     * @param localDateTime2
     * @return
     */
    public static boolean isBefore(LocalDateTime localDateTime1, LocalDateTime localDateTime2) {
        return localDateTime1.isBefore(localDateTime2);
    }

    /**
     * localDate1 是否在 localDate2 之前
     * @param localDate1
     * @param localDate2
     * @return
     */
    public static boolean isBefore(LocalDate localDate1, LocalDate localDate2) {
        return localDate1.isBefore(localDate2);
    }

    /**
     * localTime1 是否在 localTime2 之前
     * @param localTime1
     * @param localTime2
     * @return
     */
    public static boolean isBefore(LocalTime localTime1, LocalTime localTime2) {
        return localTime1.isBefore(localTime2);
    }

    /**
     * date1 是否在 date2 之前
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isBefore(Date date1, Date date2) {
        return date1.before(date2);
    }

    /**
     * localDateTime1 是否在 localDateTime2 之后
     * @param localDateTime1
     * @param localDateTime2
     * @return
     */
    public static boolean isAfter(LocalDateTime localDateTime1, LocalDateTime localDateTime2) {
        return localDateTime1.isAfter(localDateTime2);
    }

    /**
     * localDate1 是否在 localDate2 之后
     * @param localDate1
     * @param localDate2
     * @return
     */
    public static boolean isAfter(LocalDate localDate1, LocalDate localDate2) {
        return localDate1.isAfter(localDate2);
    }

    /**
     * localTime1 是否在 localTime2 之后
     * @param localTime1
     * @param localTime2
     * @return
     */
    public static boolean isAfter(LocalTime localTime1, LocalTime localTime2) {
        return localTime1.isAfter(localTime2);
    }

    /**
     * date1 是否在 date2 之后
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isAfter(Date date1, Date date2) {
        return date1.after(date2);
    }

    /**
     * localDateTime1 是否等于 localDateTime2
     * @param localDateTime1
     * @param localDateTime2
     * @return
     */
    public static boolean isEqual(LocalDateTime localDateTime1, LocalDateTime localDateTime2) {
        return localDateTime1.isEqual(localDateTime2);
    }

    /**
     * localDate1 是否等于 localDate2
     * @param localDate1
     * @param localDate2
     * @return
     */
    public static boolean isEqual(LocalDate localDate1, LocalDate localDate2) {
        return localDate1.isEqual(localDate2);
    }

    /**
     * date1 是否等于 date2
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isEqual(Date date1, Date date2) {
        return date1.equals(date2);
    }

    /**
     * startLocalTime 是否等于 endLocalTime
     * @param startLocalTime
     * @param endLocalTime
     * @return
     */
    public static boolean isEqual(LocalTime startLocalTime, LocalTime endLocalTime) {
        LocalDateTime startLocalDateTime = LocalDateTime.of(LocalDate.now(), startLocalTime);
        LocalDateTime endLocalDateTime = LocalDateTime.of(LocalDate.now(), endLocalTime);
        return startLocalDateTime.isEqual(endLocalDateTime);
    }

    /**
     * 比较两个 LocalDateTime 是否同一天
     * @param begin
     * @param end
     * @return
     */
    public static boolean isTheSameDay(LocalDateTime begin, LocalDateTime end) {
        return begin.toLocalDate().equals(end.toLocalDate());
    }

    /**
     * 比较两个 Date 是否同一天
     * @param begin
     * @param end
     * @return
     */
    public static boolean isTheSameDay(Date begin, Date end) {
        return isTheSameDay(convertDateToLDT(begin), convertDateToLDT(end));
    }

    /**
     * 比较两个 LocalDateTime 是否同一月
     * @param begin
     * @param end
     * @return
     */
    public static boolean isTheSameMonth(LocalDateTime begin, LocalDateTime end) {
        String beginStr = formatDateTime(begin, TimeFormat.YEAR_MONTH_PATTERN_LINE);
        String endStr = formatDateTime(end, TimeFormat.YEAR_MONTH_PATTERN_LINE);
        return beginStr.equals(endStr);
    }

    /**
     * 比较两个 Date 是否同一月
     * @param begin
     * @param end
     * @return
     */
    public static boolean isTheSameMonth(Date begin, Date end) {
        return isTheSameMonth(convertDateToLDT(begin), convertDateToLDT(end));
    }

    /**
     * 比较两个 LocalDateTime 是否同一年
     * @param begin
     * @param end
     * @return
     */
    public static boolean isTheSameYear(LocalDateTime begin, LocalDateTime end) {
        return begin.getYear() == end.getYear();
    }

    /**
     * 比较两个 Date 是否同一年
     * @param begin
     * @param end
     * @return
     */
    public static boolean isTheSameYear(Date begin, Date end) {
        return isTheSameYear(convertDateToLDT(begin), convertDateToLDT(end));
    }

    /**
     * 判断当前时间是否在时间范围内
     * @param startTime
     * @param endTime
     * @return
     */
    public static boolean isTimeInRange(LocalDateTime startTime, LocalDateTime endTime) {
        LocalDateTime now = getCurrentLocalDateTime();
        return (startTime.isBefore(now) && endTime.isAfter(now)) || startTime.isEqual(now) || endTime.isEqual(now);
    }

    /**
     * 判断当前时间是否在时间范围内
     * @param startDate
     * @param endDate
     * @return
     */
    public static boolean isTimeInRange(LocalDate startDate, LocalDate endDate) {
        LocalDate now = LocalDate.now();
        return (startDate.isBefore(now) && endDate.isAfter(now)) || startDate.isEqual(now) || endDate.isEqual(now);
    }

    /**
     * 判断当前时间是否在时间范围内
     * @param startTime
     * @param endTime
     * @return
     */
    public static boolean isTimeInRange(Date startTime, Date endTime) {
        return isTimeInRange(convertDateToLDT(startTime), convertDateToLDT(endTime));
    }

    /**
     * 判断是否是闰年
     * @param date
     * @return
     */
    public static boolean isLeapYear(Date date) {
        return isLeapYear(convertDateToLDT(date));
    }

    /**
     * 判断是否是闰年
     * @param date
     * @return
     */
    public static boolean isLeapYear(LocalDate date) {
        return date.isLeapYear();
    }

    /**
     * 判断是否是闰年
     * @param date
     * @return
     */
    public static boolean isLeapYear(LocalDateTime date) {
        return isLeapYear(date.toLocalDate());
    }

    /**
     * 获取下周日期
     * @param adjuster 星期常量, DayOfWeek.MONDAY,DayOfWeek.TUESDAY,DayOfWeek.WEDNESDAY,DayOfWeek.THURSDAY,DayOfWeek.FRIDAY,DayOfWeek.SATURDAY,DayOfWeek.SUNDAY,
     */
    public static LocalDate getNextWeek(TemporalAdjuster adjuster) {
        // 获取今日星期几，1-星期一 2-星期二 ...
        int nowWeek = LocalDateTime.now().getDayOfWeek().getValue();
        int diff = 1;
        if (adjuster == DayOfWeek.MONDAY) {
            diff = 7 - nowWeek + 1;
        } else if (adjuster == DayOfWeek.TUESDAY) {
            diff = 7 - nowWeek + 2;
        } else if (adjuster == DayOfWeek.WEDNESDAY) {
            diff = 7 - nowWeek + 3;
        } else if (adjuster == DayOfWeek.THURSDAY) {
            diff = 7 - nowWeek + 4;
        } else if (adjuster == DayOfWeek.FRIDAY) {
            diff = 7 - nowWeek + 5;
        } else if (adjuster == DayOfWeek.SATURDAY) {
            diff = 7 - nowWeek + 6;
        } else if (adjuster == DayOfWeek.SUNDAY) {
            diff = 7 - nowWeek + 7;
        }
        LocalDate nextWeekLocalDate = plus(LocalDate.now(), diff, ChronoUnit.DAYS);
        return nextWeekLocalDate;
    }

    /**
     * 获取下个月最后一天
     * @return
     */
    public static LocalDate getNextMonthEnd() {
        LocalDateTime localDateTime = TimeUtil.plus(LocalDateTime.now(), 1, ChronoUnit.MONTHS);
        localDateTime = getMonthEnd(localDateTime);
        return localDateTime.toLocalDate();
    }

    /**
     * 时间格式
     */
    public enum TimeFormat {
        /**
         * 年月
         */
        YEAR_MONTH_PATTERN_LINE("yyyy-MM"),
        YEAR_MONTH_PATTERN_SLASH("yyyy/MM"),
        YEAR_MONTH_PATTERN_DOUBLE_SLASH("yyyy\\MM"),
        YEAR_MONTH_PATTERN_NONE("yyyyMM"),
        YEAR_MONTH_PATTERN_CHINESE("yyyy年MM月"),

        /**
         * 短时间格式
         */
        SHORT_DATE_PATTERN_LINE("yyyy-MM-dd"),
        SHORT_DATE_PATTERN_SLASH("yyyy/MM/dd"),
        SHORT_DATE_PATTERN_DOUBLE_SLASH("yyyy\\MM\\dd"),
        SHORT_DATE_PATTERN_NONE("yyyyMMdd"),
        SHORT_DATE_PATTERN_CHINESE("yyyy年MM月dd日"),

        /**
         * 长时间格式
         */
        LONG_DATE_PATTERN_LINE("yyyy-MM-dd HH:mm:ss"),
        LONG_DATE_PATTERN_SLASH("yyyy/MM/dd HH:mm:ss"),
        LONG_DATE_PATTERN_DOUBLE_SLASH("yyyy\\MM\\dd HH:mm:ss"),
        LONG_DATE_PATTERN_NONE("yyyyMMdd HH:mm:ss"),
        LONG_DATE_PATTERN_CHINESE("yyyy年MM月dd日 HH时mm分ss秒"),
        MIDDLE_DATE_PATTERN_LINE("yyyy-MM-dd HH:mm"),

        /**
         * 长时间格式 带毫秒
         */
        LONG_DATE_PATTERN_WITH_MILSEC_LINE("yyyy-MM-dd HH:mm:ss.SSS"),
        LONG_DATE_PATTERN_WITH_MILSEC("yyyyMMddHHmmssSSS"),
        LONG_DATE_PATTERN_WITH_MILSEC_SLASH("yyyy/MM/dd HH:mm:ss.SSS"),
        LONG_DATE_PATTERN_WITH_MILSEC_DOUBLE_SLASH("yyyy\\MM\\dd HH:mm:ss.SSS"),
        LONG_DATE_PATTERN_WITH_MILSEC_NONE("yyyyMMdd HH:mm:ss.SSS"),
        LONG_DATE_PATTERN_WITH_MILSEC_CHINESE("yyyy年MM月dd日 HH时mm分ss秒SSS毫秒"),

        /**
         * 小时、分钟、秒、毫秒
         */
        HOUR_MINUTE_PATTERN_COLON("HH:mm"),
        HOUR_MINUTE_PATTERN_NONE("HHmm"),
        HOUR_MINUTE_SECOND_PATTERN_COLON("HH:mm:ss"),
        HOUR_MINUTE_SECOND_PATTERN_NONE("HHmmss"),
        HOUR_MINUTE_SECOND_MILLI_PATTERN_COLON("HH:mm:ss.SSS");

        private transient DateTimeFormatter formatter;

        TimeFormat(String pattern) {
            formatter = DateTimeFormatter.ofPattern(pattern);
        }
    }

}
