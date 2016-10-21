package com.gdwii.util.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalQuery;
import java.util.Date;

import com.gdwii.util.text.ParseRuntimeException;

public abstract class DateTimeParser {
	public static LocalDateTime parseDateTime(String dateTime){
		return DefaultDataTimeFormatters.yyyy_MM_dd_HH_mm_ss.parse(dateTime, LocalDateTime::from);
	}

	public static LocalDate parseDate(String date){
		return DefaultDataTimeFormatters.yyyy_MM_dd.parse(date, LocalDate::from);
	}
	
	public static LocalDate parseDateFormyyyyMMdd(String date){
		return DefaultDataTimeFormatters.yyyyMMdd.parse(date, LocalDate::from);
	}

	public static LocalDateTime parseDateTime(String dateTime, String pattern){
		return DateTimeFormatter.ofPattern(pattern).parse(dateTime, LocalDateTime::from);
	}
	
	public static LocalDate parseDate(String date, String pattern){
		return DateTimeFormatter.ofPattern(pattern).parse(date, LocalDate::from);
	}

	public static <T> T parse(String date, String pattern, TemporalQuery<T> query){
		return DateTimeFormatter.ofPattern(pattern).parse(date, query);
	}
	
	public static Date parseDateStrictly(String date, String pattern){
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		dateFormat.setLenient(false);
		try {
			return dateFormat.parse(date);
		} catch (ParseException e) {
			throw new ParseRuntimeException(e);
		}
	}
}
