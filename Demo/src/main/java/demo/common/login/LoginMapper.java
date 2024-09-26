package demo.common.login;

import org.apache.ibatis.annotations.Mapper;

import demo.common.vo.UserVO;

@Mapper
public interface LoginMapper {

	public UserVO selectOneUserVo(UserVO vo);
}
