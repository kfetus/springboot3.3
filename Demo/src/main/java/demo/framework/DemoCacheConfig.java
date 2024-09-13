package demo.framework;

import java.util.List;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class DemoCacheConfig {
	
    @Bean
    CacheManager cacheManager() throws Exception {
    	
    	ConcurrentMapCacheManager cm = new ConcurrentMapCacheManager();
    	cm.setCacheNames(List.of("systemCache","testCache"));
        return cm;
    }
}