package demo.common.login;


import org.springframework.stereotype.Component;

import demo.common.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Component
public class SessionManager {

	private static final String USER_INFO = "USER_INFO";

	public void createUserInfo(HttpServletRequest req, UserVO vo) {
		HttpSession session =  req.getSession();
		session.removeAttribute(USER_INFO);
		session.setAttribute(USER_INFO, vo);
		session.setMaxInactiveInterval(600);//10분
	}

	public UserVO getUserInfo(HttpServletRequest req) {
		HttpSession session =  req.getSession();
		UserVO vo = (UserVO)session.getAttribute(USER_INFO);
		return vo;
	}

	public void removeUserInfo(HttpServletRequest req) {
		HttpSession session =  req.getSession();
		session.removeAttribute(USER_INFO);
		session.invalidate();
	}
	
}
