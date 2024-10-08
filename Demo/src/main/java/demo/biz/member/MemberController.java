package demo.biz.member;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import demo.common.login.SessionManager;
import demo.common.util.HttpUtil;
import demo.common.util.StringUtil;
import demo.common.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/member")
public class MemberController {

	private final Logger logger = LogManager.getLogger(MemberController.class);
	
	@Value("${code.validation.null}")
	private String validationNullCode;

	@Value("${code.success}")
	private String successCode;	

	@Value("${code.login.infoNullCODE}")
	private String loginNullErrorCode;	
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private SessionManager sessionManager;	
	
	@GetMapping("/joinUsForm.do")
	public String joinUsForm(@RequestParam HashMap<String,Object> map) throws Exception {
		logger.debug("@@@@@@@@@@ joinUsForm START="+map);
		String viewName = "biz/member/joinForm";
		
		logger.debug("@@@@@@@@@@ joinUsForm END");
		return viewName;
	}

	@ResponseBody
	@PostMapping("/checkDupId.do")
	public Map<String, Object>  checkDupId(@RequestBody UserVO vo) throws Exception {
		logger.debug("@@@@@@@@@@ checkDupId START="+vo.toString());

		Map<String, Object> retMap = new HashMap<>();
		
		if( !StringUtils.hasText(vo.getUserId())) {
			retMap.put("RESCODE",validationNullCode);
			retMap.put("RESMSG","아이디가 없습니다.");
			return retMap;
		}
		
		int result = memberService.checkDupIdOne(vo);
		
		retMap.put("RESCODE",successCode);
		retMap.put("RESMSG","정상적으로 처리되었습니다");
		retMap.put("RESULT_STATE",result == 0 ? "FALSE" : "TRUE");		
		
		logger.debug("@@@@@@@@@@ checkDupId END");
		return retMap;
	}

	/**
	 * 회원 가입
	 * @param vo
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@PostMapping(value = "/joinUser.do")
	public Map<String,Object> insertUserInfoOne(@RequestBody UserVO vo, HttpServletRequest req) throws Exception {
		logger.debug("@@@@@@@@@@@ insertUserInfoOne 시작=" + vo.toString());
		Map<String , Object> retMap = new HashMap<String,Object>();
		
		if( !StringUtils.hasText(vo.getUserId())) {
			retMap.put("RESCODE",validationNullCode);
			retMap.put("RESMSG","아이디가 없습니다.");
			return retMap;
		}
		if (!StringUtils.hasText(vo.getUserPass()) ) {
			retMap.put("RESCODE",validationNullCode);
			retMap.put("RESMSG","패스워드가 없습니다.");
			return retMap;
		}
		if (!StringUtils.hasText(vo.getUserName()) ) {
			retMap.put("RESCODE",validationNullCode);
			retMap.put("RESMSG","이름이 없습니다.");
			return retMap;
		}

//		if (!StringUtils.hasText(vo.getHpNo()) ) {
//			retMap.put("RESCODE",validationNullCode);
//			retMap.put("RESMSG","휴대전화 정보가 없습니다.");
//			return retMap;
//		}

		vo.setUserIp(HttpUtil.getClientIp(req));
		logger.debug("@@@@@@@@@@@ insertUserInfoOne insert data=" + vo.toString());
		int result = memberService.insertUserInfoOne(vo);

		retMap.put("RESCODE",successCode);
		retMap.put("RESMSG","정상적으로 처리되었습니다");
		retMap.put("RESULT_CNT",result);

		logger.debug("@@@@@@@@@@@ insertBoardOne 종료"+retMap);
		return retMap;
	}

	/**
	 * 회원 정보 조회
	 * @param map
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@PostMapping(value = "/userInfoOne.do")
	public Map<String,Object> selectUserInfoOne(@RequestBody  HashMap<String,String> map, HttpServletRequest req) throws Exception {
		logger.debug("@@@@@@@@@@@ selectBoardOne 시작=" + map);
		Map<String , Object> retMap = new HashMap<String,Object>();

		UserVO loginVo = sessionManager.getUserInfo(req);
		if (loginVo == null) {
			retMap.put("RESCODE",loginNullErrorCode);
			retMap.put("RESMSG","로그인 정보가 없습니다.");
			return retMap;
		}
		
		UserVO vo = memberService.selectUserInfoOne(String.valueOf(loginVo.getUserNo()));
		vo.setUserId(StringUtil.asteriskName(vo.getUserId()));//ID 변환.
		vo.setUserName(StringUtil.asteriskName(vo.getUserName()));//이름 변환.
		vo.setHpNo(StringUtil.asteriskHP(vo.getHpNo()));//HP 변환
		
		retMap.put("RESCODE",successCode);
		retMap.put("RESMSG","정상적으로 처리되었습니다");
		retMap.put("RESULT_DATA",vo);

		logger.debug("@@@@@@@@@@@ selectBoardOne 종료"+retMap);
		return retMap;
	}

	/**
	 * 회원 정보 수정
	 * @param vo
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@PostMapping(value = "/updateUserInfoOne.do")
	public Map<String,Object> updateUserInfoOne(@RequestBody UserVO vo, HttpServletRequest req) throws Exception {
		logger.debug("@@@@@@@@@@@ updateUserInfoOne 시작=" + vo.toString());
		Map<String , Object> retMap = new HashMap<String,Object>();
		
		UserVO loginVo = sessionManager.getUserInfo(req);
		if (loginVo == null) {
			retMap.put("RESCODE",loginNullErrorCode);
			retMap.put("RESMSG","로그인 정보가 없습니다.");
			return retMap;
		}
		vo.setUserNo(loginVo.getUserNo());
		vo.setUserId(loginVo.getUserId());
		vo.setUserIp(HttpUtil.getClientIp(req));
		int result = memberService.updateUserInfoOne(vo);

		retMap.put("RESCODE",successCode);
		retMap.put("RESMSG","정상적으로 처리되었습니다");
		retMap.put("RESULT_CNT",result);

		logger.debug("@@@@@@@@@@@ updateBoardOne 종료"+retMap);
		return retMap;
	}
	
	/**
	 * 회원 탈퇴 시 회원 정보 삭제
	 * @param map
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@PostMapping(value = "/deleteUserInfoOne.do")
	public Map<String,Object> deleteUserInfoOne(@RequestBody  HashMap<String,String> map, HttpServletRequest req) throws Exception {
		logger.debug("@@@@@@@@@@@ deleteUserInfoOne 시작=" + map);
		Map<String , Object> retMap = new HashMap<String,Object>();
		
		UserVO vo = sessionManager.getUserInfo(req);
		if (vo == null) {
			retMap.put("RESCODE",loginNullErrorCode);
			retMap.put("RESMSG","로그인 정보가 없습니다.");
			return retMap;
		}

		int result = memberService.deleteUserInfoOne(vo.getUserNo());

		retMap.put("RESCODE",successCode);
		retMap.put("RESMSG","정상적으로 처리되었습니다");
		retMap.put("RESULT_CNT",result);

		logger.debug("@@@@@@@@@@@ updateBoardOne 종료"+retMap);
		return retMap;
	}
}