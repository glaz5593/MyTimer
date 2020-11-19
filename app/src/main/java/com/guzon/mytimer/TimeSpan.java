package com.guzon.mytimer;

import java.util.Date;

/**
 * Created by mosheg on 04/11/2015.
 */
public class TimeSpan {
    public float totalYears;
    public long totalDays;
    public long totalHours;
    public long totalMilliseconds;
    public long totalMinutes;
    public long totalSeconds;

    public TimeSpan(Date early, Date later) {
        totalMilliseconds = (later.getTime() - early.getTime());
        totalSeconds = (later.getTime() - early.getTime()) / 1000;
        totalMinutes = (later.getTime() - early.getTime()) / (60000);
        totalHours = (later.getTime() - early.getTime()) / (3600000);
        totalDays = (later.getTime() - early.getTime()) / (86400000);
        totalYears = (float) totalDays / 365;
    }

    public static TimeSpan fromNow(Date early) {
        return new TimeSpan(early, new Date());
    }

    public static TimeSpan toNow(Date later) {
        return new TimeSpan(new Date(), later);
    }

    public static boolean isToday(Date date) {
        TimeSpan timeSpan = fromNow(date);
        return timeSpan.totalDays < 1 && timeSpan.totalDays > -1;
    }

    public static boolean isFuture(Date date) {
        return fromNow(date).totalSeconds < 0;
    }
}

