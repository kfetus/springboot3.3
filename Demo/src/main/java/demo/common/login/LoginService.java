package demo.common.login;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import demo.common.vo.UserVO;

@Service
public class LoginService {

	private final Logger logger = LogManager.getLogger(LoginService.class);
	
	@Autowired
	private LoginMapper loginMapper;
	
	public UserVO selectOneUserVo(UserVO vo) throws Exception {
		logger.debug("@@@@@@@@@@@@@ selectLoginInfo =" + vo);
		UserVO result = loginMapper.selectOneUserVo(vo);
		return result;
	}
	
	public int insertLoginHist(HashMap<String,String> param) throws Exception {
		logger.debug("@@@@@@@@@@@@@ insertLoginHist =" + param);
		int result = loginMapper.insertLoginHist(param);
		return result;
	}

}
