package com.gdwii.util.time;

import java.time.temporal.TemporalAccessor;

public abstract class DateTimeFormatter {
	public static String format(TemporalAccessor temporal){
		return DefaultDataTimeFormatters.yyyy_MM_dd_HH_mm_ss.format(temporal);
	}

	public static String formatToyyyy_MM_dd(TemporalAccessor temporal){
		return DefaultDataTimeFormatters.yyyy_MM_dd.format(temporal);
	}
	
	public static String formatToyyMMdd(TemporalAccessor temporal){
		return DefaultDataTimeFormatters.yyyyMMdd.format(temporal);
	}
	
	public static String format(TemporalAccessor temporal, String pattern){
		return java.time.format.DateTimeFormatter.ofPattern(pattern).format(temporal);
	}
}
