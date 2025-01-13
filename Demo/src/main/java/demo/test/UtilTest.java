package demo.test;

import java.io.File;
import java.util.Locale;

import demo.common.util.DateUtil;
import demo.common.util.StringUtil;
//import demo.common.util.FileUtil;

public class UtilTest {

	public static void main(String[] args) {
		System.out.println("======== START ");
		
		try {
			File file = new File("d://메모.txt");
//			FileUtil.fileCopy("d://test", file, true);

			System.out.println( DateUtil.getToday() );
			System.out.println( DateUtil.getFormatedToday("yyyy-MM-dd") );
			System.out.println( DateUtil.getDayOfWeekTodayNum() );
			System.out.println( DateUtil.getDayOfWeekTodayStr(Locale.KOREAN) );
			System.out.println( DateUtil.getDayOfWeekTodayStr(Locale.ENGLISH) );
			System.out.println( StringUtil.asteriskSsn("1234567891230") );
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("======== END ");
	}

}
