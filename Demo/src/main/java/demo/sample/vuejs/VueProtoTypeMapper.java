package demo.sample.vuejs;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import demo.common.vo.UserVO;

@Mapper
public interface VueProtoTypeMapper {

	public UserVO selectOneUserVo(UserVO vo);
	
	public UserVO selectUserInfoOne(String userNo);
	
	public List<HashMap<String,String>> selectScheduleList(HashMap<String,String> paramMap);
	
	public List<HashMap<String,String>> selectScheduleOneDay(HashMap<String,String> paramMap);
	
	public int deletescheduleDay(HashMap<String,Object> paramMap);
	
	public int insertSchedule(List<HashMap<String,String>> list);

}
