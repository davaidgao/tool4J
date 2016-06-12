package com.gdwii.util.time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class DateTimeUtil {
	private static final DateTimeFormatter yyyy_MM_dd_HH_mm_ss = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private static final DateTimeFormatter yyyy_MM_dd = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public static String format(LocalDateTime dateTime){
		return yyyy_MM_dd_HH_mm_ss.format(dateTime);
	}

	public static String format(LocalDate date){
		return yyyy_MM_dd.format(date);
	}
	
	public static LocalDateTime parseDateTime(String dateTime){
		return yyyy_MM_dd_HH_mm_ss.parse(dateTime, LocalDateTime::from);
	}

	public static LocalDate parseDate(String dateTime){
		return yyyy_MM_dd.parse(dateTime, LocalDate::from);
	}
}
