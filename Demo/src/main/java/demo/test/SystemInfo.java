package demo.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 시스ㅔㅁ 정보 읽기
 */
public class SystemInfo {

	public static void main(String[] args) {
		System.out.println("Start");

		Process process = null;
		String str = null;

		try {
			process = new ProcessBuilder("systeminfo").start();
			//컴퓨터가 한글이 EUC-KR로 셋팅되어 있다.
			BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream(), "EUC-KR"));

			while ((str = stdOut.readLine()) != null) {
				System.out.println(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("End");
	}
}
