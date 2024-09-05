package demo.common.util;

import java.text.SimpleDateFormat;
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
}
