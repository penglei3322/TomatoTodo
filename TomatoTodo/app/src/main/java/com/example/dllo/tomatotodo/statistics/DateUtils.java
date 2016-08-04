package com.example.dllo.tomatotodo.statistics;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by dllo on 16/8/3.
 */
public class DateUtils {

    //根据点击次数返回指定月份的天数
    public static Date getLastMonthDay(Date date, int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -i);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        date = calendar.getTime();
        return date;
    }

    public static Date getNextDayThirtyDay(Date date, int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -i + 1);
        date = calendar.getTime();
        return date;
    }

    public static Date getNextDay(Date date, int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -i);
        date = calendar.getTime();
        return date;
    }

    //获取指定月份的天数
    public static int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    //由long型时间获取星期几
    public static int getDay(Long startTime) {
        //前面的lSysTime是秒数，先乘1000得到毫秒数，再转为java.util.Date类型
        Date dt = new Date(startTime);
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        return cal.get(Calendar.DAY_OF_WEEK) - 1;
    }

    //根据点击temp获取当前月
    public static String getLastMonth(int temp) {
        Calendar cal = Calendar.getInstance();
        cal.add(cal.MONTH, -(temp - 1));
        SimpleDateFormat dft = new SimpleDateFormat("yyyyMM");
        String preMonth = dft.format(cal.getTime());
        return preMonth;
    }



}
