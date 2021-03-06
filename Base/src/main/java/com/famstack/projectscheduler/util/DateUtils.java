package com.famstack.projectscheduler.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import com.famstack.projectscheduler.BaseFamstackService;
import com.famstack.projectscheduler.employees.bean.FamstackDateRange;

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

	public static final String DAY_MONTH_YEAR = "dd-MMM-yyyy";
	
	public static final String DATE_MONTH_YEAR = "dd-MMM-yy";

	public static final String DATE_MONTH_YEAR_TIME = "dd-MM-yyyy HH:mm";

	
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
            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
            cal.set(Calendar.MILLISECOND, 0);
        } else if (DateTimePeriod.DAY_END == previousTimePeriod) {
            cal.add(Calendar.DAY_OF_MONTH, nextOrPrevious);
            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        } else if (DateTimePeriod.CALENDER_DAY_START == previousTimePeriod) {
            cal.add(Calendar.DAY_OF_MONTH, nextOrPrevious);
            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 12, 0, 0);
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
        long seconds = milliseconds / 1000;
        int hours = (int) (seconds / 3600);
        int minutes = (int) (seconds / 60);
        
        switch (typeInType) {
            case HOUR:
                return hours;
            case MILLISECONDS:
                return (int) milliseconds;
            case MINS:
                return minutes;
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
    	return getFirstDayOfThisMonth(new Date());
    }
    
	public static Date getFirstDayOfThisMonth(Date date) {
		Calendar currentCal = Calendar.getInstance();
        currentCal.setTime(date);
        currentCal.set(Calendar.DAY_OF_MONTH, 1);
        return currentCal.getTime();
	}
	
	public static Date getLastDayOfThisMonth(Date date){
		Calendar currentCal = Calendar.getInstance();
        currentCal.setTime(date);
        currentCal.set(Calendar.DAY_OF_MONTH, currentCal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return currentCal.getTime();
	}

    public static int getNumberOfDaysInThisMonth()
    {
        Calendar currentCal = Calendar.getInstance();
        currentCal.setTime(new Date());
        return currentCal.getActualMaximum(Calendar.DAY_OF_MONTH);

    }

    public static int getNumberOfDaysInThisMonth(Date date)
    {
        Calendar currentCal = Calendar.getInstance();
        currentCal.setTime(date);
        return currentCal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static int getNoOfDaysBetweenTwoDates(Date date1, Date date2)
    {
        Calendar dateCal1 = Calendar.getInstance();
        Calendar dateCal2 = Calendar.getInstance();
        dateCal1.setTime(date1);
        dateCal2.setTime(date2);
       return (int) ((dateCal2.getTime().getTime() - dateCal1.getTime().getTime()) / (1000 * 60 * 60 * 24));
    }

    public static int getWorkingDaysBetweenTwoDates(Date startDate, Date endDate)
    {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);

        int workDays = 0;
        if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
            return 1;
        }
        if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
            startCal.setTime(endDate);
            endCal.setTime(startDate);
        }
        do {
            if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
                && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                ++workDays;
            }
            startCal.add(Calendar.DAY_OF_MONTH, 1);
        } while (startCal.getTimeInMillis() <= endCal.getTimeInMillis()); // excluding end date

        return workDays;
    }

    public static boolean isTodayDate(Date userLastActivityDate)
    {

        Calendar startCal = Calendar.getInstance();
        startCal.setTime(userLastActivityDate);

        Calendar today = Calendar.getInstance();
        today.setTime(new Date());
        return startCal.get(Calendar.YEAR) == today.get(Calendar.YEAR)
            && startCal.get(Calendar.MONTH) == today.get(Calendar.MONTH)
            && startCal.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH);
    }

    public static String formatTime(Date date)
    {
        if (date != null) {
            return DateUtils.format(date, DateUtils.DATE_TIME_FORMAT);
        }
        return "NA";
    }

	public static boolean isCurrentMonth(Date date) {
		 Calendar startCal = Calendar.getInstance();
	        startCal.setTime(date);
	    Calendar currentDateCal = Calendar.getInstance();
	    	currentDateCal.setTime(new Date());
	        
	   return startCal.get(Calendar.MONTH) == currentDateCal.get(Calendar.MONTH);
	}

	public static String getDayString(Date date) {
		return new SimpleDateFormat("EEEE").format(date); 
	}

	public static int getWeekNumber(Date startDate) {
		 Calendar startCal = Calendar.getInstance();
	     startCal.setTime(startDate);
	     return startCal.get(Calendar.WEEK_OF_YEAR);
	}

	public static FamstackDateRange parseDateRangeString(String dateRange){
		String[] dateRanges;
        Date startDate = null;
        Date endDate = null;
        if (StringUtils.isNotBlank(dateRange)) {
            dateRanges = dateRange.split("-");

            if (dateRanges != null && dateRanges.length > 1) {
                startDate = DateUtils.tryParse(dateRanges[0].trim(), DateUtils.DATE_FORMAT_DP);
                endDate = DateUtils.tryParse(dateRanges[1].trim(), DateUtils.DATE_FORMAT_DP);
            }
        } else {
            startDate =
                DateUtils.tryParse(DateUtils.format(new Date(), DateUtils.DATE_FORMAT_DP), DateUtils.DATE_FORMAT_DP);
            endDate = startDate;
        }
        return new FamstackDateRange(startDate, endDate);
	}

	
	public static boolean isLastSundayOfMonthWeek() {
		Calendar currentDay = Calendar.getInstance();
	    Calendar lastSundayOfMonthWeek = getLastSundayOfMonthWeek(DateUtils.getNextPreviousDate(DateTimePeriod.DAY, new Date(), -8));
		 
	    return (currentDay.get(Calendar.YEAR) == lastSundayOfMonthWeek.get( Calendar.YEAR) && 
			   currentDay.get(Calendar.MONTH) == lastSundayOfMonthWeek.get( Calendar.MONTH) && 
			   currentDay.get(Calendar.DAY_OF_MONTH) == lastSundayOfMonthWeek.get(Calendar.DAY_OF_MONTH));
	}

	public static Calendar getLastSundayOfMonthWeek(Date currentDate) {
		Calendar lastSundayOfMonthWeek = Calendar.getInstance();
		lastSundayOfMonthWeek.setTime(currentDate);
		
		int numberOfTotalDays = lastSundayOfMonthWeek.getActualMaximum(Calendar.DAY_OF_MONTH);
		lastSundayOfMonthWeek.set(Calendar.DAY_OF_MONTH, numberOfTotalDays);
		if(lastSundayOfMonthWeek.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
			lastSundayOfMonthWeek.set(Calendar.DAY_OF_WEEK, 7);
			lastSundayOfMonthWeek.add(Calendar.DAY_OF_MONTH, 1);
		}
		return lastSundayOfMonthWeek;
	}
	
	public static Calendar getLastSundayOfMonthWeek() {
		Calendar lastSundayOfMonthWeek = Calendar.getInstance();
		return getLastSundayOfMonthWeek(lastSundayOfMonthWeek.getTime());
	}

	public static Date getFirstDayOfThisMonthWeek(Date startDate) {
		Calendar lastMondayOfMonthWeek = Calendar.getInstance();
		lastMondayOfMonthWeek.setTime(startDate);
		
		lastMondayOfMonthWeek.set(Calendar.DAY_OF_MONTH, 1);
		if(lastMondayOfMonthWeek.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
			lastMondayOfMonthWeek.add(Calendar.DAY_OF_MONTH, 2 - lastMondayOfMonthWeek.get(Calendar.DAY_OF_WEEK));
		}
		lastMondayOfMonthWeek.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return lastMondayOfMonthWeek.getTime();
	}
	
	
	public static void main(String[] args) {
		//System.out.println(getFirstDayOfThisMonthWeek(DateUtils.getNextPreviousDate(DateTimePeriod.DAY, new Date(), -8)));
	
		//System.out.println(getTimeDifference(TimeInType.MINS, tryParse("2020-06-25 00:51:00", "yyyy-MM-dd HH:mm:ss").getTime(), 
			//	tryParse("2020-03-25 00:51:00", "yyyy-MM-dd HH:mm:ss").getTime()));
		
		/*
		 * Date startTime =
		 * DateUtils.getNextPreviousDate(DateTimePeriod.DAY_START,
		 * DateUtils.tryParse("2012/05/04 23:30", DateUtils.DATE_TIME_FORMAT),
		 * 0);
		 * 
		 * Date endTime = DateUtils.getNextPreviousDate(DateTimePeriod.DAY_END,
		 * DateUtils.tryParse("2020/01/19 23:30", DateUtils.DATE_TIME_FORMAT),
		 * 0);
		 * 
		 * int numberOfDays = DateUtils.getWorkingDaysBetweenTwoDates(startTime,
		 * endTime); System.out.println("numberOfDays :" + numberOfDays);
		 * System.out.println(getUsersActualWorkingHours(numberOfDays,
		 * "2020-01-01", "2020-01-31", startTime, endTime));
		 
		 *
		 */
		
		System.out.println(getMonthYear(new Date()));
	}
	
	public static String getYearMonthWeekNumber(Date taskActivityStartTime) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(taskActivityStartTime);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		return "" + calendar.get(Calendar.WEEK_OF_YEAR);
	}
	
	public static List<String> getYearMonthWeekNumberBetwwenTwoDates(Date startDate, Date endDate){
		Date tempStartDate = new Date(startDate.getTime());
		List<String> yearMonthWeekNumber = new ArrayList<>();
		do{
			yearMonthWeekNumber.add(getYearMonthWeekNumber(tempStartDate));
			tempStartDate = getNextPreviousDate(DateTimePeriod.DAY, tempStartDate, 7);
		} while (tempStartDate.before(endDate));
		
		return yearMonthWeekNumber;
	}
	
	public static List<String> getYearMonthNumberBetwwenTwoDates(Date startDate, Date endDate){
		Date tempStartDate = new Date(startDate.getTime());
		List<String> yearMonthNumber = new ArrayList<>();
		do{
			yearMonthNumber.add(format(tempStartDate, "MMM-YYYY"));
			tempStartDate = getNextPreviousDate(DateTimePeriod.MONTH, tempStartDate, 1);
		} while (tempStartDate.before(endDate));
		
		return yearMonthNumber;
	}
	
	public static List<String> getYearMonthWeekNumberBetwwenTwoDatesENE(
			Date startDate, Date endDate) {
		Date tempStartDate = new Date(startDate.getTime());
		List<String> yearMonthWeekNumber = new ArrayList<>();
		Calendar cal = Calendar.getInstance();
		do{
			cal.setTime(tempStartDate);
			String weekNumber = getYearMonthWeekNumber(tempStartDate);
			
			int incrementValue = 6;
			if (cal.get(Calendar.DAY_OF_WEEK) > 2) {
				incrementValue -= cal.get(Calendar.DAY_OF_WEEK);
				incrementValue+=2;
			} else if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
				incrementValue = 0;
			}
			Date nextEndDate = getNextPreviousDate(DateTimePeriod.DAY, tempStartDate, incrementValue);
			if (nextEndDate.after(endDate)) {
				nextEndDate = endDate;
			}
			yearMonthWeekNumber.add(weekNumber);
			tempStartDate = getNextPreviousDate(DateTimePeriod.DAY, tempStartDate, incrementValue + 1);
		} while (tempStartDate.before(endDate));
		return yearMonthWeekNumber;
	}
	
	public static Map<String, String> getWeekRangeBetwwenTwoDates(Date startDate, Date endDate){
		Date tempStartDate = new Date(startDate.getTime());
		Map<String, String> weekRange = new HashMap<>();
		do{
			weekRange.put(getYearMonthWeekNumber(tempStartDate), format(tempStartDate, DATE_FORMAT) +" - " + format(getNextPreviousDate(DateTimePeriod.DAY, tempStartDate, 6), DATE_FORMAT));
			tempStartDate = getNextPreviousDate(DateTimePeriod.DAY, tempStartDate, 7);
		} while (tempStartDate.before(endDate));
		
		return weekRange;
	}

	public static Date getFirstDayOfPrevioushWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return cal.getTime();
	}

	public static Date getLastDayOfPreviousWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getFirstDayOfPrevioushWeek(date));
		cal.add(Calendar.DAY_OF_MONTH, 7);
		return cal.getTime();
	}

	public static Map<String, String> getWeekRangeBetwwenTwoDatesEndToEnd(
			Date startDate, Date endDate) {
		Date tempStartDate = new Date(startDate.getTime());
		Map<String, String> weekRange = new HashMap<>();
		Calendar cal = Calendar.getInstance();
		do{
			cal.setTime(tempStartDate);
			String weekNumber = getYearMonthWeekNumber(tempStartDate);
			
			int incrementValue = 6;
			if (cal.get(Calendar.DAY_OF_WEEK) > 2) {
				incrementValue -= cal.get(Calendar.DAY_OF_WEEK);
				incrementValue+=2;
			} else if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
				incrementValue = 0;
			}
			Date nextEndDate = getNextPreviousDate(DateTimePeriod.DAY, tempStartDate, incrementValue);
			if (nextEndDate.after(endDate)) {
				nextEndDate = endDate;
			}
			weekRange.put(weekNumber, format(tempStartDate, DATE_FORMAT) +" - " + format(nextEndDate, DATE_FORMAT));
			tempStartDate = getNextPreviousDate(DateTimePeriod.DAY, tempStartDate, incrementValue + 1);
		} while (tempStartDate.before(endDate));
		
		return weekRange;
	}

	public static Map<String, Integer> getWeekNumberWeekDayCountMap(
			Date startDate, Date endDate) {
		Date tempStartDate = new Date(startDate.getTime());
		Map<String, Integer> weekNumberWeekDayCountMap = new HashMap<>();
		Calendar cal = Calendar.getInstance();
		do{
			cal.setTime(tempStartDate);
			String weekNumber = getYearMonthWeekNumber(tempStartDate);
			
			int incrementValue = 6;
			if (cal.get(Calendar.DAY_OF_WEEK) > 2) {
				incrementValue -= cal.get(Calendar.DAY_OF_WEEK);
				incrementValue+=2;
			} else if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
				incrementValue = 0;
			}
			Date nextEndDate = getNextPreviousDate(DateTimePeriod.DAY, tempStartDate, incrementValue);
			if (nextEndDate.after(endDate)) {
				nextEndDate = endDate;
			}
			weekNumberWeekDayCountMap.put(weekNumber, getWorkingDaysBetweenTwoDates(tempStartDate, nextEndDate));
			tempStartDate = getNextPreviousDate(DateTimePeriod.DAY, tempStartDate, incrementValue + 1);
		} while (tempStartDate.before(endDate));
		
		return weekNumberWeekDayCountMap;
	}

	public static Map<String, Integer> getNumberOfworkingDaysMap(String year) {
		Map<String, Integer> numberOfworkingDaysMap = new HashMap<>();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(year));
		cal.set(Calendar.DAY_OF_MONTH, 1);
		for(int index = 0; index < 12; index++) {
			cal.set(Calendar.MONTH, index);
			Date startDate =cal.getTime();
			Date endDate = getLastDayOfThisMonth(startDate);
			numberOfworkingDaysMap.put((year+(index+1)), getWorkingDaysBetweenTwoDates(startDate, endDate));
		}
		
		return numberOfworkingDaysMap;
	}
	
	public static int getUsersActualWorkingHours(int numberOfWorkingDays, String dateOfJoinString,
			String exitDateString, Date startDate, Date endDate) {
		int inactiveStoJWorkingDays = 0;
		int inactiveEndtoExtWorkingDays = 0;
		Date dateOfJoin = DateUtils.tryParse(dateOfJoinString, DateUtils.DATE_FORMAT_CALENDER);
		Date exitDate = DateUtils.tryParse(exitDateString, DateUtils.DATE_FORMAT_CALENDER);
		
		if(exitDate != null && exitDate.before(endDate) && exitDate.after(startDate)) {
			inactiveEndtoExtWorkingDays = DateUtils.getWorkingDaysBetweenTwoDates(DateUtils.getNextPreviousDate(DateTimePeriod.DAY_END, exitDate, -1), endDate);
		} 
		
		if (dateOfJoin != null && dateOfJoin.after(startDate) && dateOfJoin.before(endDate)) {
			inactiveStoJWorkingDays =DateUtils.getWorkingDaysBetweenTwoDates(startDate, DateUtils.getNextPreviousDate(DateTimePeriod.DAY_START, dateOfJoin, -1));
		}
		System.out.println("dateOfJoin" + dateOfJoin);
		System.out.println("exitDate"+ exitDate);
		
		System.out.println("startDate"+ startDate);
		System.out.println("endDate"+ endDate);
		
		System.out.println("inactiveStoJWorkingDays"+ inactiveStoJWorkingDays);
		System.out.println("inactiveEndtoExtWorkingDays"+ inactiveEndtoExtWorkingDays);
		
		int newNumberOfWorkingDays =  (numberOfWorkingDays - inactiveEndtoExtWorkingDays - inactiveStoJWorkingDays);
		return newNumberOfWorkingDays > 0 ? newNumberOfWorkingDays : 1;
	}

	public static String getMonthYear(Date taskActivityStartTime) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(taskActivityStartTime);
		return  new SimpleDateFormat("MMM").format(cal.getTime()) + "-" + cal.get(Calendar.YEAR);
	}
}
