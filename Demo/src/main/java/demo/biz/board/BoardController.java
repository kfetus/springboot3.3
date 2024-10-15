package demo.biz.board;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mariadb.jdbc.MariaDbBlob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import demo.common.code.CodeService;
import demo.framework.system.SystemConstant;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/board")
public class BoardController {

	private final Logger logger = LogManager.getLogger(BoardController.class);
	
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private CodeService codeService;
	
    @Value("${code.success}")
    private String successCode;	
	
    /**
     * 게시물 조회. postgreSQL JDBC 드라이버는 int일 경우 '' 이 안 붙고, String 일 경우는 붙는다. 쿼리에 바인딩하는 파라미터를 Map으로 던지려면 String,Object로 받아야 처리가 된다
     * @param map
     * @return
     * @throws Exception
     */
	@RequestMapping(value="/boardList.do" , method = {RequestMethod.GET, RequestMethod.POST})
	public String boardList(Model model, @RequestParam HashMap<String,Object> map) throws Exception {
		String viewName = "biz/board/boardList";
		
		logger.debug("@@@@@@@@@@ boardList START="+map);
		logger.debug("@@@@@@@@@@ boardList START="+model);
		model.mergeAttributes(map);
		
		logger.debug("@@@@@@@@@@ boardList START="+model);
		
		String category = String.valueOf(map.get("category"));
		String categoryNm = "기본";
		if (StringUtils.hasText(category) && "NOTICE".equals(categoryNm)) {
			categoryNm = "공지";
		}
		model.addAttribute("categoryNm", categoryNm);
		
		List<HashMap<String,String>> codeList = codeService.selectCodeList("BOARD");
		model.addAttribute("codeList", codeList);
		
		int totalCnt = boardService.selectBoardListCnt(map);
		
		int nowPage = 1;
		int calcuNowPage = 0;
		int pageListCnt = SystemConstant.DEFAULT_PAGE_LIST_COUNT;
		int startIdx = 0;
		
		if (totalCnt == 0) {
			model.addAttribute("resultSize", totalCnt);
			model.addAttribute("resultTotalCnt", totalCnt);
			return viewName;
		} else {
			
			if( !ObjectUtils.isEmpty(map.get("nowPage")) && StringUtils.hasText(String.valueOf(map.get("nowPage")))) {
				calcuNowPage = Integer.parseInt(String.valueOf(map.get("nowPage")));
				nowPage = calcuNowPage;
				if(calcuNowPage > 0) {
					calcuNowPage = calcuNowPage -1;
				}
			}
			if( !ObjectUtils.isEmpty(map.get("pageListCnt")) && StringUtils.hasText(String.valueOf(map.get("pageListCnt")))) {
				pageListCnt = Integer.parseInt(String.valueOf(map.get("pageListCnt")));
			}
			
			startIdx = calcuNowPage * pageListCnt;
			map.put("startIdx", startIdx);
			map.put("pageListCnt", pageListCnt);
			//postgreSQL JDBC 드라이버는 int일 경우 '' 이 안 붙고, String 일 경우는 붙는다. 쿼리에 바인딩하는 파라미터를 Map으로 던지려면 String,Object로 받아야 처리가 된다
			List<HashMap<String, String>> resultList = boardService.selectBoardList(map);

			model.addAttribute("rescode", successCode);
			model.addAttribute("resmsg", "");
			model.addAttribute("resultList", resultList);
			model.addAttribute("resultTotalCnt", totalCnt);
		}
		model.addAttribute("nowPage", nowPage);
		model.addAttribute("pageListCnt", pageListCnt);
		logger.debug("@@@@@@@@@@ boardList END="+model);

		return viewName;
	}
	
	@PostMapping("/boardEdit.do")
	public String boardEdit(Model model, @RequestParam HashMap<String,String> map) throws Exception {
		
		logger.debug("@@@@@@@@@@ boardEdit START="+map);
		
		List<HashMap<String,String>> codeList = codeService.selectCodeList("BOARD");
		model.addAttribute("codeList", codeList);
		
		String seq = map.get("seq");
		if(StringUtils.hasText(seq)) {
			model.addAttribute("paramSeq", seq);
			logger.debug("seq = "+seq);
			HashMap<String,String> boardData = boardService.selectBoardOne(seq);
			model.addAttribute("category",boardData.get("CATEGORY"));
			model.addAttribute("boardData",boardData);
			
			List<HashMap<String,String>> list = boardService.selectBoardOneCommemtList(seq);
			model.addAttribute("commentList",list);
		}

		logger.debug("@@@@@@@@@@ boardEdit END="+model);
		return "biz/board/boardEdit";
	}

	
	/**
	 * 
	 * @설명 : DB BLOB 파일 다운로드
	 * @param map
	 * @param response
	 * @throws Exception
	 */
	@GetMapping(value = "/blobFiledown.do")
	public void blobFiledown(@RequestParam HashMap<String, String> map, HttpServletResponse response) throws Exception {
		logger.debug("@@@@@@@@@@@ blobFiledown 시작=" + map);

		HashMap<String, Object> resultData = boardService.selectBoardFileOne(map);
		logger.debug("@@@@@@@@@@@ selectBoardFileOne DB 조회 결과=" + resultData);

		MariaDbBlob mb = (MariaDbBlob)resultData.get("FILE_DATA");
		byte[] fileData = mb.getBytes(1, (int) mb.length());
	    String fileName = (String)resultData.get("FILE_NAME");
	    
	    
        response.setContentType("application/octet-stream");//MediaType.APPLICATION_OCTET_STREAM_VALUE
        response.setContentLength(fileData.length);
        response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(fileName,"UTF-8")+"\";");//HttpHeaders.CONTENT_DISPOSITION
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.getOutputStream().write(fileData);
        response.getOutputStream().flush();
        response.getOutputStream().close();
	}
	
}
