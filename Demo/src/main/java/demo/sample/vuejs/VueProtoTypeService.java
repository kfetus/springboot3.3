package demo.sample.vuejs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import demo.common.vo.UserVO;

@Service
public class VueProtoTypeService {

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
	private VueProtoTypeMapper vptMapper;
	
	public UserVO selectOneUserVo(UserVO vo) throws Exception {
		UserVO resultVo = vptMapper.selectOneUserVo(vo);
		logger.debug("@@@@@@@@@@@@@ VueProtoTypeService.selectOneUserVo resultVo=" + resultVo);
		return resultVo;
	}
	
	public UserVO selectUserInfoOne(String userNo) throws Exception {
		UserVO vo = vptMapper.selectUserInfoOne(userNo);
		return vo;
	}
}
