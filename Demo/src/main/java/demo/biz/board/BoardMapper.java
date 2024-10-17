package demo.biz.board;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardMapper {
	
	public int selectBoardListCnt(HashMap<String,Object> map);
	
	public List<HashMap<String,String>> selectBoardList(HashMap<String,Object> map);

	public HashMap<String,String> selectBoardOne(String seq);
	
	public List<HashMap<String,String>> selectBoardOneCommemtList(String seq);
	
	public HashMap<String,Object> selectBoardFileOne(HashMap<String,String> map);
	
	public int insertBoardOne(HashMap<String,Object> map);
	
	public int insertBoardFile(HashMap<String,Object> map);
	
	public int deleteBoardOne(String seq);
	
	public int deleteBoardFile(String seq);
}
