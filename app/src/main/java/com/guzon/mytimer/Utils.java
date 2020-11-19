package com.guzon.mytimer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {
    public static SimpleDateFormat format_HH = new SimpleDateFormat("HH", Locale.ENGLISH);
    public static SimpleDateFormat format_mm = new SimpleDateFormat("mm", Locale.ENGLISH);
    public static SimpleDateFormat format_HH_mm = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
    public static SimpleDateFormat format_HH_mm_ss = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
    public static SimpleDateFormat format_dd_MM_yyyy_HH_mm_ss = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
    public static SimpleDateFormat format_dd_MM_yyyy_HH_mm = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
    public static SimpleDateFormat format_dd_MM_yyyy = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    public static SimpleDateFormat format_password = new SimpleDateFormat("ddMMyy", Locale.ENGLISH);
    public static SimpleDateFormat format_MM_dd_yyyy = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
    public static SimpleDateFormat format_dd_MM = new SimpleDateFormat("dd/MM", Locale.ENGLISH);
    public static SimpleDateFormat format_yyyy_MM_dd_HH_mm_ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
    public static SimpleDateFormat format_yyyy_MM_dd_HH_mm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
    public static SimpleDateFormat format_yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    public static SimpleDateFormat format_yyyy_MM_dd_T_HH_mm_ss = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);

    public static Date addSeconds(Date date, int seconds) {
        long t = date.getTime();
        return new Date(t + (seconds * 1000));
    }

    public static Date addMinutes(Date date, int minutes) {
        return addSeconds(date, minutes * 60);
    }

    public static Date addHour(Date date, int houres) {
        return addMinutes(date, houres * 60);
    }

    public static Date addDay(Date date, int days) {
        return addHour(date, days * 24);
    }

    public static Date getDate(int year, int monthOfYear, int dayOfMonth) {
        return getDate(String.format("%02d", dayOfMonth) + "/" + String.format("%02d", monthOfYear) + "/" + String.format("%04d", year));
    }

    public static Date getDate(String date) {
        if (isNullOrEmpty(date)) {
            return null;
        }

        SimpleDateFormat[] arr = new SimpleDateFormat[]{format_dd_MM_yyyy_HH_mm_ss,
                format_dd_MM_yyyy_HH_mm,
                format_dd_MM_yyyy,
                format_yyyy_MM_dd_HH_mm_ss,
                format_yyyy_MM_dd_HH_mm,
                format_yyyy_MM_dd};

        for (SimpleDateFormat sdf : arr) {
            try {
                return sdf.parse(date);
            } catch (Exception ex) {
            }
        }

        return null;
    }

    public static boolean isNullOrEmpty(String value) {
        return value == null || value.length() == 0;
    }
}
