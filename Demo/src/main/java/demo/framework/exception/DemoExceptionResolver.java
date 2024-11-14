package demo.framework.exception;

import java.io.IOException;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DemoExceptionResolver implements HandlerExceptionResolver {

	private final Logger logger = LogManager.getLogger(DemoExceptionResolver.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception reqException) {
		logger.debug("==========DemoExceptionResolver=" + handler.getClass().getName());

		HashMap<String, String> exceptionRetMap = new HashMap<>();

		if ( reqException instanceof BaseException) {
			exceptionRetMap.put("CODE", ((BaseException) reqException).getErrorCode());
		} else {
			exceptionRetMap.put("CODE", "9999");
		}
		exceptionRetMap.put("MSG", reqException.getMessage());
		reqException.printStackTrace();
		
		String reqAccept = request.getHeader("accept")==null?"":request.getHeader("accept");
		String reqContentType = request.getHeader("content-type")==null?"":request.getHeader("content-type");
		logger.debug("==========req accept=" + reqAccept);
		logger.debug("==========req reqContentType=" + reqContentType);
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

		try {
			if ( reqAccept.indexOf(MediaType.APPLICATION_JSON_VALUE) > -1 || reqContentType.indexOf(MediaType.APPLICATION_JSON_VALUE) > -1 ) {
				ObjectMapper mapper = new ObjectMapper();
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				response.setCharacterEncoding("utf-8");
				response.getWriter().write(mapper.writeValueAsString(exceptionRetMap));
				return new ModelAndView();
			} else {
				ModelAndView mv = new ModelAndView();
				mv.addObject("errorInfo", exceptionRetMap );
				mv.setViewName("framework/exception/baseException");
				return mv;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
