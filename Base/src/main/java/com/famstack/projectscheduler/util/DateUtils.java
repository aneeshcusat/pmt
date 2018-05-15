package com.famstack.projectscheduler.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.logging.log4j.Logger;

import com.famstack.projectscheduler.BaseFamstackService;

/**
 * The Class DateUtils.
 * 
 * @author Aneeshkumar
 * @version 1.0
 */
public final class DateUtils extends BaseFamstackService
{

    public static final String DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm";

    public static final String DATE_FORMAT = "yyyy/MM/dd";

    public static final String DATE_FORMAT_DP = "MM/dd/yyyy";

    public static final String DATE_FORMAT_CALENDER = "yyyy-MM-dd";

    public static final String DATE_TIME_FORMAT_CALENDER = "yyyy-MM-dd'T'HH:mm:ss";

    /** The logger. */
    private static Logger logger = getStaticLogger(DateUtils.class);

    /**
     * Try parse.
     * 
     * @param dateAsString the date as string
     * @param datePattern the date pattern
     * @return the date
     */
    public static Date tryParse(String dateAsString, String datePattern)
    {
        if (StringUtils.isNotBlank(dateAsString)) {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat(datePattern);
                return formatter.parse(dateAsString);
            } catch (ParseException e) {
                logger.error(e.getMessage());
            }
        }
        return null;
    }

    /**
     * Format.
     * 
     * @param date the date
     * @param datePattern the date pattern
     * @return the string
     */
    public static String format(Date date, String datePattern)
    {
        if (date != null) {
            SimpleDateFormat formatter = new SimpleDateFormat(datePattern);
            return formatter.format(date);
        }
        return null;
    }

    /**
     * Gets the next previous date.
     * 
     * @param previousTimePeriod the previous time period
     * @param date the date
     * @param nextOrPrevious the next or previous
     * @return the next previous date
     */
    public static Date getNextPreviousDate(DateTimePeriod previousTimePeriod, Date date, int nextOrPrevious)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (DateTimePeriod.WEEK == previousTimePeriod) {
            cal.add(Calendar.WEEK_OF_YEAR, nextOrPrevious);
        } else if (DateTimePeriod.YEAR == previousTimePeriod) {
            cal.add(Calendar.YEAR, nextOrPrevious);
        } else if (DateTimePeriod.MONTH == previousTimePeriod) {
            cal.add(Calendar.MONTH, nextOrPrevious);
        } else if (DateTimePeriod.DAY == previousTimePeriod) {
            cal.add(Calendar.DAY_OF_MONTH, nextOrPrevious);
        } else if (DateTimePeriod.HOUR == previousTimePeriod) {
            cal.add(Calendar.HOUR, nextOrPrevious);
        } else if (DateTimePeriod.MINUTE == previousTimePeriod) {
            cal.add(Calendar.MINUTE, nextOrPrevious);
        } else if (DateTimePeriod.DAY_START == previousTimePeriod) {
            cal.add(Calendar.DAY_OF_MONTH, nextOrPrevious);
            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0);
        } else if (DateTimePeriod.DAY_END == previousTimePeriod) {
            cal.add(Calendar.DAY_OF_MONTH, nextOrPrevious);
            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 23, 59);
        } else if (DateTimePeriod.CALENDER_DAY_START == previousTimePeriod) {
            cal.add(Calendar.DAY_OF_MONTH, nextOrPrevious);
            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 12, 0);
        }
        return cal.getTime();
    }

    /**
     * Gets the previous date.
     * 
     * @param previousTimePeriod the previous time period
     * @return the previous date
     */
    public static Date getPreviousDate(DateTimePeriod previousTimePeriod)
    {
        return getNextPreviousDate(previousTimePeriod, new Date(), -1);
    }

    public static String getDisplayDate(Timestamp timestamp)
    {
        if (timestamp != null) {
            SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME_FORMAT);
            return formatter.format(new Date(timestamp.getTime()));
        }
        return null;
    }

    public static int getTimeDifference(TimeInType typeInType, long latestTime, long previousTime)
    {
        long milliseconds = latestTime - previousTime;
        int seconds = (int) milliseconds / 1000;
        int hours = seconds / 3600;
        int minutes = seconds / 60;

        switch (typeInType) {
            case HOUR:
                return hours;
            case MILLISECONDS:
                return (int) milliseconds;
            case MINS:
                return minutes;
            case SECONDS:
                return seconds;
            default:
                break;
        }

        return 0;
    }

    public static Date getFirstDayOfThisWeek()
    {
        Calendar currentCal = Calendar.getInstance();
        currentCal.setTime(new Date());
        currentCal.set(Calendar.DAY_OF_WEEK, 1);
        return currentCal.getTime();

    }

    public static Date getFirstDayOfThisMonth()
    {
        Calendar currentCal = Calendar.getInstance();
        currentCal.setTime(new Date());
        currentCal.set(Calendar.DAY_OF_MONTH, 1);
        return currentCal.getTime();

    }

    public static int getNumberOfDaysInThisMonth()
    {
        Calendar currentCal = Calendar.getInstance();
        currentCal.setTime(new Date());
        return currentCal.getActualMaximum(Calendar.DAY_OF_MONTH);

    }

    public static void main(String[] args)
    {
        System.out.println(getNumberOfDaysBetweenTwoDates(new Date(),
            getNextPreviousDate(DateTimePeriod.DAY, new Date(), +6)));
    }

    public static int getNumberOfDaysInThisMonth(Date date)
    {
        Calendar currentCal = Calendar.getInstance();
        currentCal.setTime(date);
        return currentCal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static int getNumberOfDaysBetweenTwoDates(Date date1, Date date2)
    {
        Calendar dateCal1 = Calendar.getInstance();
        Calendar dateCal2 = Calendar.getInstance();
        dateCal1.setTime(date1);
        dateCal2.setTime(date2);
        dateCal1.set(dateCal1.get(Calendar.YEAR), dateCal1.get(Calendar.MONTH), dateCal1.get(Calendar.DAY_OF_MONTH), 0,
            0);
        dateCal2.set(dateCal2.get(Calendar.YEAR), dateCal2.get(Calendar.MONTH), dateCal2.get(Calendar.DAY_OF_MONTH), 0,
            0);

        return (int) ((dateCal2.getTime().getTime() - dateCal1.getTime().getTime()) / (1000 * 60 * 60 * 24));
    }

}
