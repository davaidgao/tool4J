package com.gdwii.util.time;

import java.time.format.DateTimeFormatter;

public interface DefaultDataTimeFormatters {
	DateTimeFormatter yyyy_MM_dd_HH_mm_ss = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	DateTimeFormatter yyyy_MM_dd = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");
}
