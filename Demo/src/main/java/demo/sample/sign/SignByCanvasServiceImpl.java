package demo.sample.sign;

import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SignByCanvasServiceImpl {

	private final Logger logger = LogManager.getLogger(SignByCanvasServiceImpl.class);
	
	@Autowired
	private SignByCanvasMapper signByCanvasMapper;	
	
	public List<HashMap<String, Object>> selectSignList(HashMap<String, String> map) throws Exception {
		logger.debug("@@@@@@@@@@@@@ SignByCanvasServiceImpl.selectSignList"+map);
		return signByCanvasMapper.selectSignList(map);
	}

	public void insertSign(HashMap<String, Object> map) throws Exception {
		logger.debug("@@@@@@@@@@@@@ SignByCanvasServiceImpl.insertSign"+map);
		signByCanvasMapper.insertSign(map);
	}	

	public void updateSign(HashMap<String, Object> map) throws Exception {
		logger.debug("@@@@@@@@@@@@@ SignByCanvasServiceImpl.updateSign"+map);
		signByCanvasMapper.updateSign(map);


	}	

}
