package demo.sample;


import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SampleMapper {
	
	public List<HashMap<String,String>> selectList(HashMap<String,String> map);
	
	public int insertFileUploadTest(HashMap<String,Object> map);
	
	public int insertListUploadTest(List<HashMap<String, String>> list);
	
}
