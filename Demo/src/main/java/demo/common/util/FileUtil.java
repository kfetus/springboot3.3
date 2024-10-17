package demo.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class FileUtil {

	private static List<String> fielExtensionList = new ArrayList<>(Arrays.asList("png","jpg","xlsx","pdf"));
	
	
	public static boolean checkUploadFileExtension(MultipartFile multiFile) {
		
		String originalFilename = multiFile.getOriginalFilename();
		int pointIndex = originalFilename.lastIndexOf(".");
		//확장자가 없을 경우
		if(pointIndex < 0) {
			return false;
		}
		//확장자가 허용되지 않을 경우
		String fileExtension = originalFilename.substring(pointIndex + 1).toLowerCase();
		if( !fielExtensionList.contains(fileExtension) ){
			return false;	
		}
	    return true;
	}
}
