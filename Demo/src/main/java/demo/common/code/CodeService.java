package demo.common.code;

import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CodeService {
	
	private final Logger logger = LogManager.getLogger(CodeService.class);

	@Autowired
	private CodeMapper codeMapper;

	public List<HashMap<String,String>> selectCodeList(String codeType) throws Exception {
		logger.debug("@@@@@@@@@@ selectCodeList data=" + codeType);
		List<HashMap<String,String>> result = codeMapper.selectCodeList(codeType);
		return result;
	}
}
