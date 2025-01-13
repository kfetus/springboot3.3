package demo.common.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import demo.framework.exception.BaseException;

public class FileUtil {

	private static final List<String> fielExtensionList = new ArrayList<>(Arrays.asList("png","jpg","xlsx","pdf"));
	
	/**
	 * 업로드용 파일 확장자 체크
	 * @param multiFile
	 * @return
	 */
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
	
	/**
	 * NIO 방식 파일 복사
	 * 
	 * @param destDir
	 * @param sourceFile
	 * @param createDir
	 * @throws Exception
	 */
	public static void fileCopy(String destDir, File sourceFile, boolean createDir) throws BaseException {

		if (!sourceFile.isFile()) {
			throw new BaseException("7777", "복사하는 대상이 파일이 아닙니다.");
		}

		File newFile = new File(destDir+File.separator+sourceFile.getName());
		if (createDir) {
			if (!newFile.exists()) {
				newFile.mkdirs();
			}
		} else {
			if ( !(newFile.exists() && newFile.isDirectory()) ) {
				throw new BaseException("7777", "복사하려는 대상 폴더가 없습니다");
			}
		}
		
		if ( !(newFile.canRead() && newFile.canWrite()) ) {
			throw new BaseException("7777", "폴더에 대한 읽기 또는 쓰기 권한이 없습니다");
		}
		
		try {
			// 4. 파일 복사
			Files.copy(sourceFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
			throw new BaseException("7777", "파일 복사중 에러가 발생했습니다.");
		}
	}
}
