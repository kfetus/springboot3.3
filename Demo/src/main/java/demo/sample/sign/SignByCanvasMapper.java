package demo.sample.sign;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SignByCanvasMapper {

	public List<HashMap<String, Object>> selectSignList(HashMap<String, String> map) throws Exception;
	
	public void insertSign(HashMap<String,Object> map) throws Exception;
	
	public void updateSign(HashMap<String,Object> map) throws Exception;
}
