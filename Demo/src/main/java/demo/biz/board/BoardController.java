package demo.biz.board;

import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import demo.common.SystemConstant;

@Controller
@RequestMapping("/board")
public class BoardController {

	private final Logger logger = LogManager.getLogger(BoardController.class);
	
	@Autowired
	private BoardService boardService;
	
    @Value("${code.success}")
    private String successCode;	
	
    /**
     * 게시물 조회. postgreSQL JDBC 드라이버는 int일 경우 '' 이 안 붙고, String 일 경우는 붙는다. 쿼리에 바인딩하는 파라미터를 Map으로 던지려면 String,Object로 받아야 처리가 된다
     * @param map
     * @return
     * @throws Exception
     */
	@RequestMapping(value="/boardList.do" , method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView boardList(@RequestParam HashMap<String,Object> map) throws Exception {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("biz/board/boardList");
		
		logger.debug("@@@@@@@@@@ boardList START="+map);
		
		String category = String.valueOf(map.get("category"));
		String categoryNm = "기본";
		if (StringUtils.hasText(category) && "NOTICE".equals(categoryNm)) {
			categoryNm = "공지";
		}
		mv.addObject("categoryNm", categoryNm);
		
		int totalCnt = boardService.selectBoardListCnt(map);
		
		int nowPage = 1;
		int calcuNowPage = 0;
		int pageListCnt = SystemConstant.DEFAULT_PAGE_LIST_COUNT;
		int startIdx = 0;
		
		if (totalCnt == 0) {
			mv.addObject("resultSize", totalCnt);
			mv.addObject("resultTotalCnt", totalCnt);
			return mv;
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

			mv.addObject("rescode", successCode);
			mv.addObject("resmsg", "");
			mv.addObject("resultList", resultList);
			mv.addObject("resultTotalCnt", totalCnt);
		}
		mv.addObject("nowPage", nowPage);
		mv.addObject("pageListCnt", pageListCnt);

		logger.debug("@@@@@@@@@@ boardList END="+mv);

		return mv;
	}
	
	@PostMapping("/boardEdit.do")
	public ModelAndView boardEdit(@RequestParam HashMap<String,Object> map) throws Exception {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("biz/board/boardEdit");
		
		logger.debug("@@@@@@@@@@ boardEdit START="+map);

		mv.addObject("RESCODE", successCode);
		mv.addObject("RESMSG", "");

		logger.debug("@@@@@@@@@@ boardEdit END="+mv);
		return mv;
	}
	
	
}
