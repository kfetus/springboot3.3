package demo.sample.vuejs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import demo.common.login.SessionManager;
import demo.common.util.StringUtil;
import demo.common.util.crypto.Sha256Crypto;
import demo.common.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * VueJS3 서버측 샘플
 */
@Controller
@ResponseBody
public class VueProtoTypeController {

	private final Logger logger = LogManager.getLogger(this.getClass());
	
    @Value("${code.success}")
    private String successCode;

	@Value("${code.validation.null}")
	private String validationNullCode;
    
	@Value("${code.biz.noUser}")
	private String bizNoUser;
	
	@Autowired
	private SessionManager sessionManager;

	@Autowired
	private JwtComponent jwt;
	
	@Autowired
	private VueProtoTypeService vptService;

	@PostMapping(value = "/restLogin.do")
	public Map<String,Object> restLogin(@RequestBody  UserVO vo, HttpServletRequest req, HttpServletResponse res) throws Exception {
		logger.debug("@@@@@@@@@@@ restLogin 시작="+vo.toString());
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

		UserVO vo2 = vptService.selectOneUserVo(vo);
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
//				vo.setUserIp((null != req.getHeader("X-FORWARDED-FOR")) ? req.getHeader("X-FORWARDED-FOR") : req.getRemoteAddr());
				vo2.setUserPass("");
				vo2.setSalt("");
				
				vo2.setUserId(StringUtil.asteriskName(vo2.getUserId()));//ID 변환.
				vo2.setUserName(StringUtil.asteriskName(vo2.getUserName()));//이름 변환.
				vo2.setHpNo(StringUtil.asteriskHP(vo2.getHpNo()));//HP 변환
				
				retMap.put("userInfo", vo2);
				sessionManager.createUserInfo(req, vo2);
				
				//JWT 관련 심플 셋팅. 30분 expire Time 설정
				String token = jwt.makeToken(vo2, jwt.HEADER_KEY);
				
				//이 값을 DB나 Redis에 PK 기준으로 저장해서 차후 Token 갱신때 비교해야 함. 현재는 생략함.  
				String refreshToken = jwt.makeRefreshToken(vo2, jwt.HEADER_KEY);
				
				retMap.put("token", token);
				retMap.put("refreshToken", refreshToken);
			} else {
				retMap.put("RESCODE",bizNoUser);
				retMap.put("RESMSG","사용자가 없습니다.");//패스워드가 없습니다가 맞으나 너무 많은 정보를 줄 필요가 없음. 해킹 우려
				return retMap;
			}
		}

		logger.debug("@@@@@@@@@@@ restLogin 종료"+sessionManager.getUserInfo(req));
		return retMap;
	}
	
	
	@PostMapping(value = "/restLogOut.do")
	public Map<String,Object> restLogOut(@RequestBody Map<String, Object> map, HttpServletRequest req) throws Exception {
		logger.debug("@@@@@@@@@@@ restLogOut 시작="+map);
		Map<String , Object> retMap = new HashMap<String,Object>();

		sessionManager.removeUserInfo(req);
		
		retMap.put("RESCODE",successCode);
		retMap.put("RESMSG","정상적으로 처리되었습니다.");
		logger.debug("@@@@@@@@@@@ restLogOut 종료"+retMap);
		return retMap;
	}
	
	/**
	 * 로그인 여부 체크
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@PostMapping(value = "/checkUser.do")
	public Map<String,Object> checkUser(HttpServletRequest req) throws Exception {
		logger.debug("@@@@@@@@@@@ checkUser 시작=");
		
		Map<String , Object> retMap = new HashMap<String,Object>();
		retMap.put("RESCODE",successCode);
		retMap.put("RESMSG","정상적으로 처리되었습니다.");
				
		UserVO vo = (UserVO)sessionManager.getUserInfo(req);
		if( vo != null) {
			logger.debug("@@@@@@@@@@@ 로그인 사용자 정보:"+vo.toString());
			retMap.put("loginYn","Y");
		} else {
			retMap.put("loginYn","N");
		}
		retMap.put("userInfo", vo);
		logger.debug("@@@@@@@@@@@ checkUser 종료");
		return retMap;
	}
	
	@PostMapping(value = "/userInfoOne.do")
	public Map<String,Object> selectUserInfoOne(@RequestBody  HashMap<String,String> map, HttpServletRequest req) throws Exception {
		logger.debug("@@@@@@@@@@@ selectBoardOne 시작=" + map);
		Map<String , Object> retMap = new HashMap<String,Object>();

		UserVO loginVo = sessionManager.getUserInfo(req);
		if (loginVo == null) {
			retMap.put("RESCODE",bizNoUser);
			retMap.put("RESMSG","로그인 정보가 없습니다.");
			return retMap;
		}
		
		UserVO vo = vptService.selectUserInfoOne(String.valueOf(loginVo.getUserNo()));
		vo.setUserId(StringUtil.asteriskName(vo.getUserId()));//ID 변환.
		vo.setUserName(StringUtil.asteriskName(vo.getUserName()));//이름 변환.
		vo.setHpNo(StringUtil.asteriskHP(vo.getHpNo()));//HP 변환
		
		retMap.put("RESCODE",successCode);
		retMap.put("RESMSG","정상적으로 처리되었습니다");
		retMap.put("RESULT_DATA",vo);

		logger.debug("@@@@@@@@@@@ selectBoardOne 종료"+retMap);
		return retMap;
	}
	
	
	@PostMapping(value = "/scheduleCalender.do")
	public Map<String,Object> scheduleCalender(@RequestBody  HashMap<String,String> map, HttpServletRequest req) throws Exception {
		logger.debug("@@@@@@@@@@@ scheduleCalender 시작=" + map);
		
		Map<String , Object> retMap = new HashMap<String,Object>();

		UserVO loginVo = sessionManager.getUserInfo(req);
		if (loginVo == null) {
			retMap.put("RESCODE",bizNoUser);
			retMap.put("RESMSG","로그인 정보가 없습니다.");
			return retMap;
		}		
		
		HashMap<String,String> paramMap = new HashMap<String,String>();
		
		String yyyymmdd = map.get("yyyymmdd");

		if( !StringUtils.hasText(yyyymmdd)) {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			Date now = new Date();
			yyyymmdd = format.format(now);
		} else {
			yyyymmdd = yyyymmdd.replaceAll("-", "").substring(0, 6) + "01";
		}

		paramMap.put("userNo", String.valueOf(loginVo.getUserNo()));
		paramMap.put("yyyymmdd", yyyymmdd);

		List<HashMap<String,String>> resultList = vptService.selectScheduleList(paramMap);

		retMap.put("RESCODE",successCode);
		retMap.put("RESMSG","정상적으로 처리되었습니다");
		retMap.put("RESULT_LIST",resultList);
		retMap.put("YYYYMMDD",yyyymmdd.substring(0, 4)+"-"+yyyymmdd.substring(4, 6));

		logger.debug("@@@@@@@@@@@ scheduleCalender 종료"+ retMap);
		return retMap;
	}

	
	@PostMapping(value = "/scheduleOneDay.do")
	public Map<String,Object> selectScheduleOneDayList(@RequestBody  HashMap<String,String> map, HttpServletRequest req) throws Exception {
		logger.debug("@@@@@@@@@@@ selectScheduleOneDayList 시작=" + map);
		
		Map<String , Object> retMap = new HashMap<String,Object>();

		UserVO loginVo = sessionManager.getUserInfo(req);
		if (loginVo == null) {
			retMap.put("RESCODE",bizNoUser);
			retMap.put("RESMSG","로그인 정보가 없습니다.");
			return retMap;
		}		
		
		String yyyymmdd = map.get("yyyymmdd");

		if( !StringUtils.hasText(yyyymmdd)) {
			retMap.put("RESCODE",validationNullCode);
			retMap.put("RESMSG","조회하려는 날짜를 선택해 주세요.");
			return retMap;
		}

		HashMap<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("yyyymmdd", yyyymmdd);
		paramMap.put("userNo", String.valueOf(loginVo.getUserNo()));
		
		List<HashMap<String,String>> resultList = vptService.selectScheduleOneDay(paramMap);

		retMap.put("RESCODE",successCode);
		retMap.put("RESMSG","정상적으로 처리되었습니다");
		retMap.put("RESULT_LIST",resultList);

		logger.debug("@@@@@@@@@@@ selectScheduleOneDayList 종료"+ retMap);
		return retMap;
	}

	
	@PostMapping(value = "/scheduleUpdate.do")
	public Map<String,Object> scheduleInsert(@RequestBody  HashMap<String,String> map, HttpServletRequest req) throws Exception {
		logger.debug("@@@@@@@@@@@ scheduleUpdate 시작=" + map);
		
		Map<String , Object> retMap = new HashMap<String,Object>();

		UserVO loginVo = sessionManager.getUserInfo(req);
 
		if (loginVo == null) {
			retMap.put("RESCODE",bizNoUser);
			retMap.put("RESMSG","로그인 정보가 없습니다.");
			return retMap;
		}		
		
		String yyyymmdd = map.get("yyyymmdd");
		String strList = map.get("scheduleList");

		if( !StringUtils.hasText(yyyymmdd)) {
			retMap.put("RESCODE",validationNullCode);
			retMap.put("RESMSG","변경하려는 날짜를 선택해 주세요.");
			return retMap;
		}

		ObjectMapper om = new ObjectMapper();
		List<HashMap<String,String>> paramList = om.readValue(strList, new TypeReference<List<HashMap<String,String>>>() {});
		logger.debug("@@@@@@@@@@@ scheduleUpdate =" + paramList.size());
		logger.debug("@@@@@@@@@@@ scheduleUpdate =" + paramList);
		
		if(paramList.size() == 0) {
			retMap.put("RESCODE",validationNullCode);
			retMap.put("RESMSG","추가하려는 스케쥴을 입력하세요.");
			return retMap;
		}
		
		for ( int i = 0 ; i < paramList.size();i++) {
			paramList.get(i).put("userNo", String.valueOf(loginVo.getUserNo()));
		}

		HashMap<String , Object> paramMap = new HashMap<String,Object>();
		paramMap.put("paramList", paramList);
		paramMap.put("yyyymmdd", yyyymmdd);
		paramMap.put("userNo", loginVo.getUserNo());
		
		int result = vptService.insertSchedule(paramMap);

		retMap.put("RESCODE",successCode);
		retMap.put("RESMSG","정상적으로 처리되었습니다");
		retMap.put("RESULT_DATA",result);

		logger.debug("@@@@@@@@@@@ scheduleUpdate 종료"+ retMap);
		return retMap;
	}
}
