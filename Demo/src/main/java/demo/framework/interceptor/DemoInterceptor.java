package demo.framework.interceptor;

import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import demo.common.menu.SiteMenuService;
//import demo.sample.vuejs.JwtComponent;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DemoInterceptor implements HandlerInterceptor {

	private final Logger logger = LogManager.getLogger(DemoInterceptor.class);
	
	@Autowired
	private SiteMenuService siteMenuService;
	
//	@Autowired
//	private JwtComponent jwt;	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
        StopWatch swatch = new StopWatch();
        swatch.start();
		logger.debug("========== DemoInterceptor.preHandle =========="+request.getRequestURI());
		//이 값은 spring에서 request를 받을때 정상 요청인지 에러 요청인지 판단을 주로 하는데 thymeleaf에서 Layout을 사용하면 에러가 발생해도 값이 INCLUDE로 와서 처리가 불가함.
		logger.debug("========== DemoInterceptor.preHandle getDispatcherType()=========="+request.getDispatcherType());
		
		
        //jwt 관련 샘플 보기. url 별로 셋팅해서 체크해야 하는데 그냥 샘플이라 있으면 하고 아니면 말고임.
//        String jwtToken = request.getHeader(jwt.HEADER_KEY);
//        logger.debug("request.getHeader(jwt.HEADER_KEY)="+jwtToken);
//        if(jwtToken != null) {
//        	logger.debug("jwt.verifyJWT(jwtToken)="+jwt.verifyJWT(jwtToken));
//        }
        
/*
        logger.debug("========== request.getRemoteAddr()=>"+request.getRemoteAddr());
        Enumeration<?> en1 = request.getHeaderNames();
        while(en1.hasMoreElements()) {
        	String paramKey = (String) en1.nextElement();            	
        	logger.debug("header key : " + paramKey +";value="+request.getHeader(paramKey));
        }
        

        Enumeration<?> en = request.getParameterNames();
        while(en.hasMoreElements()) {
        	String paramKey = (String) en.nextElement();            	
        	logger.debug("key : " + paramKey +";value="+request.getParameter(paramKey));
        }
*/        
        request.setAttribute("_timeCheck", swatch);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		logger.debug("========== DemoInterceptor.postHandle ==========");
		
		StopWatch swatch = ((StopWatch)request.getAttribute("_timeCheck"));
		swatch.stop();
		logger.debug("========== 실행 시간 ==========" + swatch.prettyPrint());
		request.removeAttribute("_timeCheck");
		
		//권한 및 사이트에 따른 메뉴 구조 로딩해서 셋팅하기. 공통에서 처리 할 부분
		HashMap<String,String> map = new HashMap<>();
		map.put("sysGroup", "KWW");//임시로.. 나중에 환경설정이나 프로퍼티에서 받아서 처리하기
		List<HashMap<String,String>> F_menuList = siteMenuService.selectMenuList(map);
		logger.debug("========== DemoInterceptor="+F_menuList);
		request.setAttribute("F_menuList", F_menuList);
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	/**
	 * view(현재 Thymeleaf)에서 에러 발생 시 로그 쌓기. view 에러가 밠생하면 처리 불가능함.
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		logger.debug("========== DemoInterceptor.afterCompletion ==========");
        logger.debug("exception=>"+ex);
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}

}
