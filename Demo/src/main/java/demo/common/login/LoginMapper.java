package demo.common.login;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import demo.common.vo.UserVO;

@Mapper
public interface LoginMapper {

	public UserVO selectOneUserVo(UserVO vo);
	
	public int insertLoginHist(HashMap<String,String> param);
}
