package demo.framework.system;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SystemController {

	private final Logger logger = LogManager.getLogger(SystemController.class);
	
	/**
	 * welcome page 이동
	 * @param model
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/" , method = {RequestMethod.GET, RequestMethod.POST})
	public String welcomePage(Model model, @RequestParam HashMap<String,Object> map) throws Exception {
		logger.debug("@@@@@@@@@@ welcomePage @@@@@@@@@@");
		return "welcome";
	}

}
