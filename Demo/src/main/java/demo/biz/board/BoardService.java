package demo.biz.board;

import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
	
	public HashMap<String,Object> selectBoardFileOne(HashMap<String,String> map) throws Exception {
		HashMap<String,Object> result = boardMapper.selectBoardFileOne(map);
		return result;
	}
	
	public int insertBoard(HashMap<String,Object> map) throws Exception {
		
		boardMapper.insertBoardOne(map);
		String boardSeq = String.valueOf(map.get("boardSeq"));
		int result = Integer.parseInt(boardSeq);

		MultipartFile multiFiles = (MultipartFile)map.get("multiFiles");
		if(multiFiles != null) {
			HashMap<String, Object> fileMap = new HashMap<String, Object>();

			String originalFilename = multiFiles.getOriginalFilename();
			int pointIndex = originalFilename.lastIndexOf(".");
			String fileExtension = originalFilename.substring(pointIndex + 1);
			String fileName = originalFilename.substring(0, pointIndex);
			long fileSize = multiFiles.getSize(); // 파일 사이즈
			logger.debug("fileName= " + fileName);
			logger.debug("fileExtension= " + fileExtension);
			logger.debug("fileSize= " + fileSize);
			
			fileMap.put("boardSeq", result);
			fileMap.put("fileName", fileName);
			fileMap.put("fileData", multiFiles.getBytes());
			fileMap.put("fileSize", fileSize);
			fileMap.put("fileExtension", fileExtension);
			
			boardMapper.insertBoardFile(fileMap);
		}
		return result;
	}
}
