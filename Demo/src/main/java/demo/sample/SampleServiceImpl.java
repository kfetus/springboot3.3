package demo.sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SampleServiceImpl {

	private final Logger logger = LogManager.getLogger(SampleController.class);

	@Autowired
	private SampleMapper sampleMapper;
	
	public List<HashMap<String,String>> selectList(HashMap<String,String> map) throws Exception {
		List<HashMap<String,String>> result = sampleMapper.selectList(map);
		logger.debug("@@@@@@@@@@@@@ SampleServiceImpl.selectList result=" + result);
		return result;
	}

	/**
	 * 파일 업로드 테스트
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int insertFileUploadTest(HashMap<String, Object> map) throws Exception {
		logger.debug("@@@@@@@@@@@ insertUploadTest 시작="+map);
		
		MultipartFile multiFiles = (MultipartFile)map.get("multiFiles");
		if(multiFiles != null) {
			map.put("blobData", multiFiles.getBytes());
		}		
		
		int count = sampleMapper.insertFileUploadTest(map);
		logger.debug("@@@@@@@@@@@ insertUploadTest 종료="+count);
		
		return count;
	}
	
	
	/**
	 * 대용량 배열 DATA INSERT
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public int insertUploadTestList(List<HashMap<String, String>> list) throws Exception {
		logger.debug("@@@@@@@@@@@ insertUploadTestList 시작="+list.size());
		int count = 0;
		List<HashMap<String, String>> tempList = new ArrayList<HashMap<String, String>>();
		for ( int index = 1 ; index < list.size()+1; index++) {
			tempList.add(list.get(index-1));
			if(index % 10000 == 0) {
				count += sampleMapper.insertListUploadTest(tempList);
				tempList.clear();
				logger.debug("@@@@@@@@@@@ count="+count);
			}
		}
		if(tempList.size() > 0) {
			count += sampleMapper.insertListUploadTest(tempList);
			tempList.clear();
			logger.debug("@@@@@@@@@@@ count="+count);
		}
		
		return count;
	}
}
