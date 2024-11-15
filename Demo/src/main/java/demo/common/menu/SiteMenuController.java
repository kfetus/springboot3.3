package demo.common.menu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
//@RequestMapping("/common")
public class SiteMenuController {

	private final Logger logger = LogManager.getLogger(SiteMenuController.class);
	
	@Autowired
	private SiteMenuService siteMenuService;
	
    @Value("${code.success}")
    private String successCode;	
    
    @ResponseBody
	@PostMapping(value = "/common/siteMenuList.do")
	public Map<String,Object> siteMenuList(@RequestBody  HashMap<String,String> map) throws Exception {
		logger.debug("@@@@@@@@@@@ siteMenuList 시작=" + map);
		Map<String , Object> retMap = new HashMap<String,Object>();
		
		List<HashMap<String,String>> resultList = siteMenuService.selectMenuList(map);

		retMap.put("RESCODE",successCode);
		retMap.put("RESMSG","정상적으로 처리되었습니다.");
		retMap.put("RESULT_SIZE",resultList.size());
		retMap.put("RESULT_LIST",resultList);

		logger.debug("@@@@@@@@@@@ siteMenuList 종료"+ retMap);
		return retMap;
	}	

    @ResponseBody
    @PostMapping(value = "/menuList.do")
	public Map<String,Object> menuList(@RequestBody  HashMap<String,String> map) throws Exception {
    	logger.debug("@@@@@@@@@@@ menuList 시작=" + map);
		Map<String , Object> retMap = new HashMap<String,Object>();
		
		List<HashMap<String,String>> resultList = siteMenuService.selectMenuList(map);

		retMap.put("RESCODE",successCode);
		retMap.put("RESMSG","정상적으로 처리되었습니다.");
		retMap.put("RESULT_SIZE",resultList.size());
		retMap.put("RESULT_LIST",resultList);

		logger.debug("@@@@@@@@@@@ menuList 종료"+ retMap);
		return retMap;
	}
}
