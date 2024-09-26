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
}
