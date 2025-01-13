package demo.common.util;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Locale;

public class DateUtil {

	/**
	 * 오늘 날짜 
	 * @return yyyymmdd
	 */
	public static String getToday() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance(Locale.KOREA); 
		return sdf.format(calendar.getTime());
	}

	/**
	 * Parameter에 따른 오늘 날짜 형식 리턴
	 * @param format="yyyy-MM-dd"
	 * @return
	 */
	public static String getFormatedToday(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Calendar calendar = Calendar.getInstance(Locale.KOREA); 
		return sdf.format(calendar.getTime());
	}

	/**
	 * 요일을 숫자로 리턴
	 * @return
	 */
	public static int getDayOfWeekTodayNum() {
		LocalDate date = LocalDate.now();
//		System.out.println(date);
		DayOfWeek dow = date.getDayOfWeek();
//		System.out.println(dow.getValue());//숫자형
//		System.out.println(dow.getDisplayName(TextStyle.FULL, Locale.KOREAN));//텍스트형
		return dow.getValue();
	}

	/**
	 * 요일을 한글 Text로 리턴
	 * @return
	 */
	public static String getDayOfWeekTodayStr(Locale locale) {
		LocalDate date = LocalDate.now();
		DayOfWeek dow = date.getDayOfWeek();
		return dow.getDisplayName(TextStyle.FULL, locale);
	}

}