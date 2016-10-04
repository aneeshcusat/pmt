package com.famstack.projectscheduler.util;

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
public final class DateUtils extends BaseFamstackService {

	public static final String DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm";

	/** The logger. */
	private static Logger logger = getStaticLogger(DateUtils.class);

	/**
	 * Try parse.
	 *
	 * @param dateAsString
	 *            the date as string
	 * @param datePattern
	 *            the date pattern
	 * @return the date
	 */
	public static Date tryParse(String dateAsString, String datePattern) {
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
	 * @param date
	 *            the date
	 * @param datePattern
	 *            the date pattern
	 * @return the string
	 */
	public static String format(Date date, String datePattern) {
		if (date != null) {
			SimpleDateFormat formatter = new SimpleDateFormat(datePattern);
			return formatter.format(date);
		}
		return null;
	}

	/**
	 * Gets the next previous date.
	 *
	 * @param previousTimePeriod
	 *            the previous time period
	 * @param date
	 *            the date
	 * @param nextOrPrevious
	 *            the next or previous
	 * @return the next previous date
	 */
	public static Date getNextPreviousDate(DateTimePeriod previousTimePeriod, Date date, int nextOrPrevious) {
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
		}
		return cal.getTime();
	}

	/**
	 * Gets the previous date.
	 *
	 * @param previousTimePeriod
	 *            the previous time period
	 * @return the previous date
	 */
	public static Date getPreviousDate(DateTimePeriod previousTimePeriod) {
		return getNextPreviousDate(previousTimePeriod, new Date(), -1);
	}
}
