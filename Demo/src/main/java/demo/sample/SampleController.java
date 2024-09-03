package demo.sample;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import demo.framework.exception.BaseException;
//import demo.common.StringUtil;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class SampleController {

	private final Logger logger = LogManager.getLogger(SampleController.class);
	
	@Autowired
	private SampleServiceImpl sampleService;
	
	@GetMapping("/sample.do")
	public String sample(Model model) {
		model.addAttribute("data1", "가나다라마바사12");
		logger.debug("@@@@@@@@@@ 11debug");
		logger.info("@@@@@@@@@@ info");
		return "sample/sample";
	}
	
	@GetMapping("/sample1.do")
	public ModelAndView sample1(ModelAndView model) {
		logger.debug("@@@@@@@@@@ debug sample1");
		logger.info("@@@@@@@@@@ info sample1");
		model.addObject("data1", "가나다라마바사12sample1");
		model.setViewName("sample/sample1");
		return model;
	}

	/**
	 * url에 들어온 url 그대로 view로 매핑. 파라미터들 그대로 전달
	 * @param map
	 * @param subPath
	 * @param viewName
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/urlToView/{subPath}/{viewName}")
	public ModelAndView subPathUrlToHtml( @RequestParam HashMap<String,String> map, @PathVariable String subPath,
			@PathVariable String viewName) throws Exception {
		logger.debug("@@@@@@@@@@ subPathUrlToHtml  @@@@@@@@@@"+map);
		ModelAndView mv = new ModelAndView();
		String viewFileName = viewName;
		if(viewName.indexOf(".") != -1) {
			viewFileName = viewName.substring(0, viewName.indexOf("."));
		}
		if(subPath.equals("exception")) {
			subPath = "framework/"+subPath;
		}
		mv.setViewName(subPath+"/"+viewFileName);
		return mv;
	}
	
	@RequestMapping("/sampleList.do")
	public ModelAndView sampleList(@RequestParam HashMap<String,String> map) throws Exception {
		
		logger.debug("@@@@@@@@@@ sampleList  @@@@@@@@@@"+map);
		List<HashMap<String,String>> list = sampleService.selectList(map);
		logger.debug("@@@@@@@@@@ sampleList  @@@@@@@@@@"+list);

/*		절대로 서버에서 바꿔서 리턴하면 안됨. 변경해서 리턴하면 컴파일된 jsp를 브라우저는 그대로 실행하게 되므로 XSS에 취약해짐. 필요하면 클라이언트에서 해당 값 replace해야 함(EX:에디터 내용들)
 * 		th:text 로 한다면 기본적으로 escape 을 thymeleaf가 제공하므로 서버에서 변경해서 내려도 됨. XSS가 적용된 상태로 내리면 thymeleaf의 th:utext로 하면 escaping을 해서 보여줌 
		for(int i = 0 ; i < list.size();i++) {
			HashMap<String, String> row = list.get(i);
			String bodyText = row.get("DESCRIP");
			logger.debug("@@@@@@@@@@@ boardList bodyText=" + bodyText);
			bodyText = StringUtil.unEscapeXSS(bodyText);
			logger.debug("@@@@@@@@@@@ boardList after bodyText=" + bodyText);
			row.put("DESCRIP", bodyText);
		}
*/
		if( "Y".equals(map.get("charCol")) ) {
			throw new BaseException("TESTTTTTT","9999");
		}
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("list", list );
		mv.setViewName("sample/sampleList");
		return mv;

	}
	
	@ResponseBody
	@PostMapping("/ajaxSampleList.do")
	public Map<String, Object> ajaxSampleList(@RequestBody HashMap<String, String> map) throws Exception {
		logger.debug("@@@@@@@@@@ ajaxSampleList 시작=" + map);
		Map<String, Object> retMap = new HashMap<String, Object>();
		
		if( "Y".equals(map.get("charCol")) ) {
			throw new BaseException("TESTTTTTT","9999");
		}
		
		List<HashMap<String,String>> list = sampleService.selectList(map);
		
		retMap.put("RESCODE", "0000");
		retMap.put("RESMSG", "정상적으로 처리되었습니다");
		retMap.put("RESULT_SIZE", list.size());
		retMap.put("RESULT_LIST", list);

		logger.debug("@@@@@@@@@@@ ajaxSampleList 종료" + retMap);
		return retMap;
	}
	
	@ResponseBody
	@PostMapping(value = "/upload/ajaxSampleUpload.do")
	public Map<String, Object> ajaxSampleUpload(@RequestParam(required=false) String varcharCol, @RequestParam(required=false) String intCol, 
			@RequestParam(required=false) String dateCol, @RequestParam(required=false) String charCol, 
			@RequestParam(required=false) String descrip, @RequestParam(required=false) String blobName, 
			@RequestPart(value="multiFiles",required = false)  MultipartFile multiFiles, HttpServletRequest req) throws Exception {
		logger.debug("@@@@@@@@@@@ ajaxSampleUpload 시작 @@@@@@@@@@@");
		Map<String, Object> retMap = new HashMap<String, Object>();
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("varcharCol",varcharCol);
		map.put("intCol",intCol);
		map.put("dateCol",dateCol);
		map.put("charCol",charCol);
		map.put("descrip",descrip);
		map.put("blobName",blobName);
		
		map.put("multiFiles", multiFiles);
		logger.debug("@@@@@@@@@@@ insertBoardOne 시작map=" + map);
		
		int result = sampleService.insertFileUploadTest(map);
		retMap.put("RESCODE", "0000");
		retMap.put("RESMSG", "게시물을 등록하였습니다.");
		retMap.put("RESULT_CNT", result);

		logger.debug("@@@@@@@@@@@ ajaxSampleUpload 종료" + retMap);
		return retMap;
	}	

}
