package demo.common.menu;

import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class SiteMenuService {

	private final Logger logger = LogManager.getLogger(SiteMenuService.class);

	@Autowired
	private SiteMenuMapper siteMenuMapper;
	
	//파라미터별로 캐싱
	@Cacheable("systemCache")
	public List<HashMap<String,String>> selectMenuList(HashMap<String,String> map) throws Exception {
		logger.debug("@@@@@@@@@@@ selectMenuList Service 시작=" + map);
		List<HashMap<String,String>> result = siteMenuMapper.selectMenuList(map);
		return result;
	}
}
