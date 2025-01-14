package demo.biz.board;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import demo.common.code.CodeService;
import demo.common.login.SessionManager;
import demo.common.util.FileUtil;
import demo.common.vo.UserVO;
import demo.framework.exception.BaseException;
import demo.framework.system.SystemConstant;
import jakarta.servlet.http.HttpServletRequest;
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
	
    @Value("${code.biz.noAuthority}")
    private String noAuthority;
    
    @Value("${code.biz.defaultException}")
    private String defaultException;
    
    @Value("${code.login.infoNullCODE}")
    private String infoNullCODE;
    
    @Value("${code.biz.nomalError}")
    private String nomalError;
	
	@Value("${code.validation.null}")
	private String validationNullCode;
	
	@Autowired
	private SessionManager sessionManager;	
	
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
		//수정과 화면을 같이 쓰기 위해.
//		} else {
//			throw new BaseException(validationNullCode,"필수값이 누락되었습니다.");
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

	@ResponseBody
	@PostMapping(value = "/insertBoardOne.do")
	public Map<String, Object> insertBoardOne(@RequestParam(value="srcTitle") String srcTitle,
			@RequestParam(value="bodyText", required=false) String bodyText,
			@RequestParam(value="category", required=false) String category,
			@RequestPart(value="multiFiles", required=false) MultipartFile multiFiles, HttpServletRequest req) throws Exception {
		logger.debug("@@@@@@@@@@@ insertBoardOne 시작 @@@@@@@@@@@"+srcTitle);
		Map<String, Object> retMap = new HashMap<String, Object>();
		
		if(multiFiles != null) {
			boolean checkState = FileUtil.checkUploadFileExtension(multiFiles);
			if(!checkState) {
				retMap.put("RESCODE", nomalError);
				retMap.put("RESMSG", "잘못된 파일을 업로드 하였습니다."+multiFiles.getOriginalFilename());
				logger.debug("@@@@@@@@@@@ insertBoardOne 에러발생=" + retMap);
				return retMap;
			}
		}
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("title",srcTitle);
		map.put("bodyText",bodyText);
		map.put("category",category);
		logger.debug("@@@@@@@@@@@ insertBoardOne 시작map=" + map);

		UserVO vo = sessionManager.getUserInfo(req);
		if (vo == null) {
			retMap.put("RESCODE", infoNullCODE);
			retMap.put("RESMSG", "로그인 정보가 없습니다.");
			logger.debug("@@@@@@@@@@@ insertBoardOne 에러발생=" + retMap);
			return retMap;
		} else {
			map.put("userNo", String.valueOf(vo.getUserNo()));
		}
		map.put("multiFiles", multiFiles);
		
		
		int result = boardService.insertBoard(map);

		retMap.put("RESCODE", successCode);
		retMap.put("RESMSG", "");
		retMap.put("RESULT_CNT", result);

		logger.debug("@@@@@@@@@@@ insertBoardOne 종료" + retMap);
		return retMap;
	}

	@ResponseBody
	@PostMapping("/boardDeleteOne.do")
	public Map<String, Object> boardDeleteOne(@RequestBody HashMap<String,String> map, HttpServletRequest req) throws Exception {
		logger.debug("@@@@@@@@@@ boardDeleteOne START="+map);
		Map<String, Object> retMap = new HashMap<String, Object>();

		UserVO vo = sessionManager.getUserInfo(req);
		if (vo == null) {
			retMap.put("RESCODE", infoNullCODE);
			retMap.put("RESMSG", "로그인 정보가 없습니다.");
			return retMap;
		} else {
			map.put("userNo", String.valueOf(vo.getUserNo()));
		}
		
		String seq = map.get("seq");
		
		if(!StringUtils.hasText(seq)) {
			retMap.put("RESCODE",validationNullCode);
			retMap.put("RESMSG","필수값이 누락되었습니다.");
			return retMap;
		}
		HashMap<String, String> resultData = boardService.selectBoardOne(seq);
		
		logger.debug("@@@@@@@@@@@ deleteBoardOne resultData=" + resultData);
		
		String boardOwnerNo = String.valueOf(resultData.get("CNG_USER_NO"));
		int tempUserNo = Integer.parseInt(boardOwnerNo);
		if (tempUserNo != vo.getUserNo()) {
			retMap.put("RESCODE", noAuthority);
			retMap.put("RESMSG", "삭제 권한이 없습니다.");
			return retMap;
		}
		
		int result = boardService.deleteBoard(seq);
		
		if(result == 1) {
			retMap.put("RESCODE", successCode);
			retMap.put("RESMSG", "");
		} else {
			retMap.put("RESCODE", defaultException);
			retMap.put("RESMSG", "삭제에 실패하였습니다.");
		}
		
		logger.debug("@@@@@@@@@@ boardDeleteOne END="+retMap);
		return retMap;
	}

	
	@PostMapping("/boardUpdate.do")
	public String boardUpdate(@RequestParam HashMap<String,String> map, HttpServletRequest req) throws Exception {
		logger.debug("@@@@@@@@@@ boardUpdate START="+map);

		UserVO vo = sessionManager.getUserInfo(req);
		if (vo == null) {
			throw new BaseException(infoNullCODE,"로그인 정보가 없습니다.");
		} else {
			map.put("cngUserNo", String.valueOf(vo.getUserNo()));
		}
		
		String seq = map.get("seq");
		if(!StringUtils.hasText(seq)) {
			throw new BaseException(validationNullCode,"필수값이 누락되었습니다.");
		}
		
		HashMap<String, String> resultData = boardService.selectBoardOne(seq);
		
		logger.debug("@@@@@@@@@@@ deleteBoardOne resultData=" + resultData);
		
		String boardOwnerNo = String.valueOf(resultData.get("CNG_USER_NO"));
		int tempUserNo = Integer.parseInt(boardOwnerNo);
		if (tempUserNo != vo.getUserNo()) {
			throw new BaseException(noAuthority,"삭제 권한이 없습니다.");
		}
		map.put("title", map.get("srcTitle"));
		boardService.updateBoardOne(map);

		logger.debug("@@@@@@@@@@ boardUpdate END=");
		return "redirect:/board/boardList.do";
	}

	
	@ResponseBody
	@PostMapping("/insertBoardCommentOne.do")
	public Map<String, String> insertBoardCommentOne(@RequestBody HashMap<String,Object> map, HttpServletRequest req) throws Exception {
		logger.debug("@@@@@@@@@@ insertBoardCommentOne START="+map);
		Map<String, String> retMap = new HashMap<>();

		UserVO vo = sessionManager.getUserInfo(req);
		if (vo == null) {
			retMap.put("RESCODE", infoNullCODE);
			retMap.put("RESMSG", "로그인 정보가 없습니다.");
			return retMap;
		} else {
			map.put("userNo", String.valueOf(vo.getUserNo()));
		}
		
		String originBoardSeq = (String)map.get("originBoardSeq");
		
		if(!StringUtils.hasText(originBoardSeq)) {
			retMap.put("RESCODE",validationNullCode);
			retMap.put("RESMSG","필수값이 누락되었습니다.");
			return retMap;
		}
		String bodyText = (String)map.get("bodyText");
		if(!StringUtils.hasText(bodyText)) {
			retMap.put("RESCODE",validationNullCode);
			retMap.put("RESMSG","필수값이 누락되었습니다.");
			return retMap;
		}
		String title = (String)map.get("title");
		if(!StringUtils.hasText(title)) {
			retMap.put("RESCODE",validationNullCode);
			retMap.put("RESMSG","필수값이 누락되었습니다.");
			return retMap;
		}
		
		int result = boardService.insertBoard(map);
		
		if(result > 1) {
			retMap.put("RESCODE", successCode);
			retMap.put("RESMSG", "");
		} else {
			retMap.put("RESCODE", defaultException);
			retMap.put("RESMSG", "등록에 실패하였습니다.");
		}
		
		logger.debug("@@@@@@@@@@ insertBoardCommentOne END="+retMap);
		return retMap;
	}
	
	
}