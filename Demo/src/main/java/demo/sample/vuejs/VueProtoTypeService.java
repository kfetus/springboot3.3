package demo.sample.vuejs;

import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	
	public List<HashMap<String,String>> selectScheduleList(HashMap<String,String> paramMap) throws Exception {
		logger.debug("@@@@@@@@@@@@@ selectScheduleList paramMap=" + paramMap);
		List<HashMap<String,String>> result = vptMapper.selectScheduleList(paramMap);
		return result;
	}
	
	public List<HashMap<String,String>> selectScheduleOneDay(HashMap<String,String> paramMap) throws Exception {
		logger.debug("@@@@@@@@@@@@@ selectScheduleOne paramMap=" + paramMap);
		List<HashMap<String,String>> result = vptMapper.selectScheduleOneDay(paramMap);
		return result;
	}

	@Transactional
	public int insertSchedule( HashMap<String,Object> map ) throws Exception {
		logger.debug("@@@@@@@@@@@@@ scheduleUpdate map=" + map);
		
		vptMapper.deletescheduleDay(map);
		@SuppressWarnings("unchecked")
		List<HashMap<String,String>> paramList = (List<HashMap<String,String>>) map.get("paramList");
		int result = vptMapper.insertSchedule(paramList);
		
		return result;
		
	}	
	
}
