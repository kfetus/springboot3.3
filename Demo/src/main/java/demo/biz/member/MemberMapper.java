package demo.biz.member;

import org.apache.ibatis.annotations.Mapper;

import demo.common.vo.UserVO;

@Mapper
public interface MemberMapper {

	public UserVO selectUserInfoOne(String userNo);

	public int insertUserInfoOne(UserVO vo);
	
	public int updateUserInfoOne(UserVO vo);

	public int deleteUserInfoOne(int userNo);
}
