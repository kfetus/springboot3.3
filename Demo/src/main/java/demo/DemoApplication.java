package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
	//springboot에서 내장 톰켓으로 실행될때만 사용. 실 WAS에 구동될때는 ServletInitializer만 호출 됨
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
