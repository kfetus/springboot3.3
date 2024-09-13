package demo.framework.interceptor;

import java.util.Enumeration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DemoInterceptor implements HandlerInterceptor {

	private final Logger logger = LogManager.getLogger(DemoInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		logger.debug("========== DemoInterceptor.preHandle =========="+request.getRequestURI());
        Enumeration<?> en = request.getParameterNames();
        while(en.hasMoreElements()) {
        	String paramKey = (String) en.nextElement();            	
        	logger.debug("key : " + paramKey +";value="+request.getParameter(paramKey));
        }
        
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		logger.debug("========== DemoInterceptor.postHandle =========="+modelAndView);
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		logger.debug("========== DemoInterceptor.afterCompletion ==========");
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}

}
