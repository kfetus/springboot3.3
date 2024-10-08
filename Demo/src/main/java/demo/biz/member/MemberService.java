package demo.biz.member;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import demo.common.login.LoginMapper;
import demo.common.util.crypto.Sha256Crypto;
import demo.common.vo.UserVO;

@Transactional
@Service
public class MemberService {

	private final Logger logger = LogManager.getLogger(MemberService.class);
	
	@Autowired
	private LoginMapper loginMapper;
	
	@Autowired
	private MemberMapper memberMapper;
	
	public int checkDupIdOne(UserVO vo) throws Exception {
		logger.debug("@@@@@@@@@@@@@ checkDupIdOne =" + vo);
		int cnt = loginMapper.checkDupIdOne(vo);
		return cnt;
	}
	
	public int insertUserInfoOne(UserVO vo) throws Exception {
		logger.debug("@@@@@@@@@@@@@ insertUserInfoOne data=" + vo.toString());
		
		String salt = Sha256Crypto.getSalt();
		vo.setSalt(salt);
		vo.setUserPass(Sha256Crypto.encSah256(vo.getUserPass(), salt));
		
		loginMapper.insertLoginInfoOne(vo);
		if( !StringUtils.hasText(vo.getRegId()) ) {
			vo.setRegId("2");
		}
		logger.debug("@@@@@@@@@@@@@ insertUserInfoOne login insert ");
		int result = memberMapper.insertUserInfoOne(vo);
		logger.debug("@@@@@@@@@@@@@ insertUserInfoOne user insert ");
		return result;
	}
	
	public UserVO selectUserInfoOne(String userNo) throws Exception {
		UserVO vo = memberMapper.selectUserInfoOne(userNo);
		return vo;
	}

	public int updateUserInfoOne(UserVO vo) throws Exception {
		logger.debug("@@@@@@@@@@@@@ updateUserInfoOne map=" + vo.toString());
		
		String salt = Sha256Crypto.getSalt();
		vo.setSalt(salt);
		vo.setUserPass(Sha256Crypto.encSah256(vo.getUserPass(), salt));
		loginMapper.updateLoginInfoOne(vo);
		int result = memberMapper.updateUserInfoOne(vo);
		return result;
	}
	
	public int deleteUserInfoOne(int userNo) throws Exception {
		logger.debug("@@@@@@@@@@@@@ deleteUserInfoOne key=" + userNo);
		int result = memberMapper.deleteUserInfoOne(userNo);
		return result;
	}
}