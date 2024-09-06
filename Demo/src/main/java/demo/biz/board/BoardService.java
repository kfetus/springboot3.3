package demo.biz.board;

import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService {

	private final Logger logger = LogManager.getLogger(BoardService.class);
	
	@Autowired
	private BoardMapper boardMapper;
	
	public int selectBoardListCnt(HashMap<String,Object> map) throws Exception {
		logger.debug("@@@@@@@@@@@@@ selectBoardListCnt param data=" + map);
		int result = boardMapper.selectBoardListCnt(map);
		return result;
	}
	
	public List<HashMap<String,String>> selectBoardList(HashMap<String,Object> map) throws Exception {
		logger.debug("@@@@@@@@@@@@@ selectBoardList param data=" + map);
		List<HashMap<String,String>> result = boardMapper.selectBoardList(map);
		return result;
	}
	
	public HashMap<String,String> selectBoardOne(String seq) throws Exception {
		HashMap<String,String> result = boardMapper.selectBoardOne(seq);
		return result;
	}
	
	public List<HashMap<String,String>> selectBoardOneCommemtList(String seq) throws Exception {
		List<HashMap<String,String>> result = boardMapper.selectBoardOneCommemtList(seq);
		return result;
	}
}
