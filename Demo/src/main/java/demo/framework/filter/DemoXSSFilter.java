package demo.framework.filter;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

public class DemoXSSFilter implements Filter {

	private final Logger logger = LogManager.getLogger(DemoXSSFilter.class);
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.debug("========== DemoXSSFilter.init =========="+filterConfig.getFilterName());
		Filter.super.init(filterConfig);
	}

	@Override
	public void destroy() {
		logger.debug("========== DemoXSSFilter.destroy ==========");
		Filter.super.destroy();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		logger.debug("========== DemoXSSFilter.doFilter ==========");
		chain.doFilter(new HTMLTagFilterRequestWrapper((HttpServletRequest) request), response);	
	}

}
