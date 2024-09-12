package demo.common.menu;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SiteMenuMapper {

	public List<HashMap<String,String>> selectMenuList(HashMap<String,String> map);
}
