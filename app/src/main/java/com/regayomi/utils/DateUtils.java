package com.regayomi.utils;

import java.util.Calendar;

public class DateUtils {

    /**
     * Creates calendar that contains only the date part without the time part.
     */
    public static Calendar createCalendarWithoutTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

}
