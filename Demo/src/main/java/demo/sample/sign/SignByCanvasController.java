package demo.sample.sign;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/sign")
public class SignByCanvasController {

	private final Logger logger = LogManager.getLogger(SignByCanvasController.class);
	
	@Autowired
	private SignByCanvasServiceImpl signByCanvasService;
	
	@GetMapping("/canvasForm.do")
	public ModelAndView canvasForm(@RequestParam Map<String,Object> map) throws Exception {
		logger.debug("@@@@@@@@@@ START canvasForm @@@@@@@@@@");
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("pageTitle", "사인 테스트" );
		mv.setViewName("sample/signForm");
		logger.debug("@@@@@@@@@@ END canvasForm @@@@@@@@@@");
		return mv;
	}

	
	@ResponseBody
	@PostMapping("/canvasInsert.do")
	public Map<String, Object> canvasInsert(@RequestBody HashMap<String,Object> map) throws Exception {
		logger.debug("@@@@@@@@@@ START canvasInsert @@@@@@@@@@"+map);
		
		String signKey = System.currentTimeMillis()/1000+ (String)map.get("regId");
		logger.debug("@@@@@@ signKey @@@@@@"+signKey);
		map.put("signKey", signKey);
		
		signByCanvasService.insertSign(map);
		
		Map<String, Object> jsonModel = new HashMap<>();
		jsonModel.put("RESCODE", "0000");
		jsonModel.put("RESMSG", "정상적으로 처리되었습니다");
		jsonModel.put("SIGN_KEY", signKey);

		logger.debug("@@@@@@@@@@ END canvasInsert @@@@@@@@@@");
		return jsonModel;
	}

	
}
