package demo.common.code;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/common")
public class CodeController {

	private final Logger logger = LogManager.getLogger(CodeController.class);
	
	@Autowired
	private CodeService codeService;
	
	@Value("{code.noData}")
	private String noData ;
	
    @Value("${code.success}")
    private String successCode;	

    @ResponseBody
	@RequestMapping(value="/codeList.do" , method = {RequestMethod.GET, RequestMethod.POST})
	public Map<String,Object> codeList(@RequestBody  HashMap<String,String> map) throws Exception {
    	logger.debug("@@@@@@@@@@ codeList 시작=" + map);
		Map<String , Object> retMap = new HashMap<String,Object>();
		String codeType = map.get("codeType");
		
		List<HashMap<String,String>> resultList = codeService.selectCodeList(codeType);

		if( resultList.size() == 0) {
			retMap.put("RESCODE",noData);
			retMap.put("RESMSG","데이타 없습니다.");
			retMap.put("RESULT_SIZE","0");
			return retMap;
		} else {
			retMap.put("RESCODE",successCode);
			retMap.put("RESMSG","정상적으로 처리되었습니다.");
			retMap.put("RESULT_SIZE",resultList.size());
			retMap.put("RESULT_LIST",resultList);
		}

		logger.debug("@@@@@@@@@@ codeList 종료"+ retMap);
		return retMap;
	}
}
