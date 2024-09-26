package demo.common.login;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import demo.common.util.StringUtil;
import demo.common.util.crypto.Sha256Crypto;
import demo.common.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/login")
public class LoginController {

	private final Logger logger = LogManager.getLogger(LoginController.class);
	
	@Autowired
	private SessionManager sessionManager;	

	@Autowired
	private LoginService loginService;

	@Value("${code.validation.null}")
	private String validationNullCode;

	@Value("${code.biz.noUser}")
	private String bizNoUser;

	@Value("${code.success}")
	private String successCode;
	
	
	@ResponseBody
	@PostMapping(value="/userLogin.do")
	public Map<String, Object> userLogin(Model model, @RequestBody HashMap<String,String> map,HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		HashMap<String,Object> retMap = new HashMap<>();
		
		if( !StringUtils.hasText(map.get("loginId"))) {
			retMap.put("RESCODE",validationNullCode);
			retMap.put("RESMSG","아이디가 없습니다.");
			return retMap;
		}
		if (!StringUtils.hasText( map.get("loginPass")) ) {
			retMap.put("RESCODE",validationNullCode);
			retMap.put("RESMSG","패스워드가 없습니다.");
			return retMap;
		}

		UserVO vo = new UserVO();
		vo.setUserId(map.get("loginId"));
		vo.setUserPass(map.get("loginPass"));
		
		UserVO vo2 = loginService.selectOneUserVo(vo);
		logger.debug("@@@@@@@@@@@ restLogin after="+vo2);
		
		if( vo2 == null) {
			retMap.put("RESCODE",bizNoUser);
			retMap.put("RESMSG","사용자가 없습니다.");
			return retMap;
		} else {
			String sha256pass = Sha256Crypto.encSah256(vo.getUserPass(), vo2.getSalt());
			if(vo2.getUserPass().equals(sha256pass)) {
				retMap.put("RESCODE",successCode);
				retMap.put("RESMSG","정상적으로 처리되었습니다.");
				vo2.setUserPass("");
				vo2.setSalt("");
				
				vo2.setUserId(StringUtil.asteriskName(vo2.getUserId()));//ID 변환.
				vo2.setUserName(StringUtil.asteriskName(vo2.getUserName()));//이름 변환.
				vo2.setHpNo(StringUtil.asteriskHP(vo2.getHpNo()));//HP 변환
				
				retMap.put("userInfo", vo2);
				sessionManager.createUserInfo(req, vo2);
				
				try {//로그인 히스토리는 에러가 나도 무시하기.
					insertLoginHist(vo2.getUserNo()+"",req);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				retMap.put("RESCODE",bizNoUser);
				retMap.put("RESMSG","사용자가 없습니다.");//패스워드가 없습니다가 맞으나 너무 많은 정보를 줄 필요가 없음. 해킹 우려
				return retMap;
			}
		}		
		
//		String viewName = "welcome";
//		HashMap<String,String> result = loginService.selectOneUserVo(map);
//		if( result != null) {
//			viewName = "redirect:/board/boardList.do";
//		}
//		
//		logger.debug("@@@@@@@@@@ userLogin START="+map);
//		return viewName;
		return retMap;
	}
	
	public int insertLoginHist(String userNo,HttpServletRequest req) throws Exception {
		HashMap<String,String> param = new HashMap<>();
		param.put("userNo", userNo);
		param.put("userIp", getClientIp(req));
		param.put("loginBrowser", getClientBrowser(req));
		param.put("loginDevice", getClientAccessDevice(req));

		logger.debug("@@@@@@@@@@ insertLoginHist="+param);
		int result = loginService.insertLoginHist(param);
		return result;
	}
	
	
	public String getClientIp(HttpServletRequest req) {
		String clientIp = req.getHeader("X-Forwarded-For");
		logger.debug("@@@@@@@@@@ X-FORWARDED-FOR=" + clientIp);
		if (clientIp == null) {
			clientIp = req.getHeader("Proxy-Client-IP");
			logger.debug("@@@@@@@@@@ Proxy-Client-IP=" + clientIp);
		}
		if (clientIp == null) {
			clientIp = req.getHeader("WL-Proxy-Client-IP");
			logger.debug("@@@@@@@@@@ WL-Proxy-Client-IP=" + clientIp);
		}
		if (clientIp == null) {
			clientIp = req.getHeader("HTTP_CLIENT_IP");
			logger.debug("@@@@@@@@@@ HTTP_CLIENT_IP=" + clientIp);
		}
		if (clientIp == null) {
			clientIp = req.getHeader("HTTP_X_FORWARDED_FOR");
			logger.debug("@@@@@@@@@@ HTTP_X_FORWARDED_FOR=" + clientIp);
		}
		if (clientIp == null) {
			clientIp = req.getRemoteAddr();
			logger.debug("@@@@@@@@@@ getRemoteAddr="+clientIp);
		}
		logger.debug("@@@@@@@@@@ client IP="+clientIp);
		
		return clientIp;
	}
	
	public String getClientBrowser(HttpServletRequest req) {
		String agent = req.getHeader("User-Agent");
		String cBrowser = null;
		if (agent != null) {
			if (agent.indexOf("Trident") > -1) {
				cBrowser = "MSIE";
			} else if (agent.indexOf("Edg") > -1) {
				cBrowser = "Edge";
			} else if (agent.indexOf("Chrome") > -1) {
				cBrowser = "Chrome";
			} else if (agent.indexOf("Opera") > -1) {
				cBrowser = "Opera";
			} else if (agent.indexOf("iPhone") > -1 && agent.indexOf("Mobile") > -1) {
				cBrowser = "iPhone";
			} else if (agent.indexOf("Android") > -1 && agent.indexOf("Mobile") > -1) {
				cBrowser = "Android";
			}
		}
		return cBrowser;
	}
	
	public String getClientAccessDevice(HttpServletRequest req) {
		String mobileType[] = {"iphone","ipod","android","windows ce","blackberry","symbian","windows phone","webos","opera mini","opera mobi","polaris","iemobile","lgtelecom","nokia","sonyericsson","lg","samsung"};
		String aDevice = "";
		
		for(String loopData : mobileType){
			if ( req.getHeader("User-Agent").toLowerCase().indexOf(loopData) != -1) {
				aDevice = "MOBILE";
				break;
			} else {
				aDevice = "PC";
			}
		}
		return aDevice;
		
	}	
}
