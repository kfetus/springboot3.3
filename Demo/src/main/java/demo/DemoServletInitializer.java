package demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class DemoServletInitializer extends SpringBootServletInitializer {

	private final Logger logger = LogManager.getLogger(DemoServletInitializer.class);
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		logger.debug("========== ServletInitializer"+application);
		
		
//		ClassPathResource res = new ClassPathResource("mybatis-config.xml");
//		logger.debug("========== ServletInitializer"+res.getPath());
		return application.sources(DemoApplication.class);
	}

}
