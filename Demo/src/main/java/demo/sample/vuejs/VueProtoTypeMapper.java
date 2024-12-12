package demo.sample.vuejs;

import org.apache.ibatis.annotations.Mapper;

import demo.common.vo.UserVO;

@Mapper
public interface VueProtoTypeMapper {

	public UserVO selectOneUserVo(UserVO vo);
}
