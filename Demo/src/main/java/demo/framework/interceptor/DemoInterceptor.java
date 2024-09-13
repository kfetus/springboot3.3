package demo.framework.interceptor;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import demo.common.menu.SiteMenuService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DemoInterceptor implements HandlerInterceptor {

	private final Logger logger = LogManager.getLogger(DemoInterceptor.class);
	
	@Autowired
	private SiteMenuService siteMenuService;
	
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
		logger.debug("========== DemoInterceptor.postHandle ==========");
		
		//권한 및 사이트에 따른 메뉴 구조 로딩해서 셋팅하기. 공통에서 처리 할 부분
		HashMap<String,String> map = new HashMap<>();
		map.put("sysGroup", "KWW");//임시로.. 나중에 환경설정이나 프로퍼티에서 받아서 처리하기
		List<HashMap<String,String>> F_menuList = siteMenuService.selectMenuList(map);
		logger.debug("========== DemoInterceptor="+F_menuList);
		request.setAttribute("F_menuList", F_menuList);
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		logger.debug("========== DemoInterceptor.afterCompletion ==========");
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}

}
