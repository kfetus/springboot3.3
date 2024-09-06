package demo.common.code;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CodeMapper {

	public List<HashMap<String,String>> selectCodeList(String codeType);
}
