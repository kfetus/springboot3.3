package demo.framework;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.support.MultipartFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import demo.framework.filter.HjXSSFilter;
import demo.framework.interceptor.HjInterceptor;

@Configuration
public class DemoWebConfig implements WebMvcConfigurer {
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new HjInterceptor())
				.excludePathPatterns("/css/**", "/images/**", "/js/**");
	}

// encoding filter
//	@Bean
//	public FilterRegistrationBean<CharacterEncodingFilter> EncodingFilterRegistrationBean() {
//	    FilterRegistrationBean<CharacterEncodingFilter> filterRegBean = new FilterRegistrationBean<>();
//	    CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();	    
//	    characterEncodingFilter.setEncoding("UTF-8");
//	    characterEncodingFilter.setForceEncoding(true);
//	    filterRegBean.setFilter(characterEncodingFilter);
//	    filterRegBean.setOrder(1);
//	    filterRegBean.addUrlPatterns("/*");
//	    return filterRegBean;
//	}
	
	/**
	 * 파일 업로드 필터. 아래 XSS 필터와 조합되므로 필수임.
	 * 파일 업로드 url은 반드시 upload/~~~.do 로 시작해야 함
	 * @return
	 */
    @Bean
    FilterRegistrationBean<MultipartFilter> MultipartFilterRegistrationBean() {
        FilterRegistrationBean<MultipartFilter> filterRegBean = new FilterRegistrationBean<>();
        filterRegBean.setFilter(new MultipartFilter());
        filterRegBean.setOrder(2);
        filterRegBean.addUrlPatterns("/upload/*");
        return filterRegBean;
    }

	/**
	 * XSS 필터
	 * @return
	 */
    @Bean
    FilterRegistrationBean<HjXSSFilter> filterRegistrationBean() {
        FilterRegistrationBean<HjXSSFilter> filterRegBean = new FilterRegistrationBean<>();
        filterRegBean.setFilter(new HjXSSFilter());
        filterRegBean.setOrder(3);
        filterRegBean.addUrlPatterns("*.do");
        return filterRegBean;
    }

    /**
     * application.properties 에서 datasource 가져옴
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean
    SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception{
        SqlSessionFactoryBean ssfBean = new SqlSessionFactoryBean();
        ssfBean.setDataSource(dataSource);
        Resource[] arrResource = new PathMatchingResourcePatternResolver().getResources("mapper/**/*Mapper.xml");//classpath:mapper/**/*Mapper.xml 와 동일함
        ssfBean.setMapperLocations(arrResource);
        ssfBean.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
        ssfBean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
        return ssfBean.getObject();
    }    
    
}