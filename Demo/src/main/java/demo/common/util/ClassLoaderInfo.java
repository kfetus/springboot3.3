package demo.common.util;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClassLoaderInfo {

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
	private final Logger logger = LogManager.getLogger(ClassLoaderInfo.class);

	public Map<String, Object> getLoadingClassInfo(String className) {

		logger.debug("========== getLoadingClassInfo:"+className);
		Map<String, Object> result = new HashMap<>();
		List<String> cl = null;
		try {
			cl = getClassLoadersOfClass(className);
		} catch (Throwable e) {
			result.put("X_ERROR", e);
			return result;
		}
		if (cl != null)
			result.put("X_CLASSLOADERS", cl);
		String resourceName = classNameToResourceName(className);
		URL url = getClass().getResource(resourceName);
		try {
			result.put("X_FILE_NAME", url.toURI().toString());
			File file = getFileFromURI(url.toURI());
			if (file.exists()) {
				result.put("X_FILE_DATE", sdf.format(new Date(file.lastModified())));
			}
		} catch (URISyntaxException e) {
		}
		return result;
	}

	public File getFileFromURI(URI uri) {
		File result = null;
		String schema = uri.getScheme();
		if ("file".equals(schema))
			result = new File(uri);
		else if ("zip".equals(schema)) {
			String uriStr = uri.getSchemeSpecificPart();
			String file = uriStr.substring(0, uriStr.indexOf('!'));
			result = new File(file);
		} else if ("jar".equals(schema)) {
			String uriStr = uri.getSchemeSpecificPart();
			String file = uriStr.substring(6, uriStr.indexOf('!'));
			result = new File(file);
		}
		return result;
	}

	public List<String> getClassLoaderInfos(String className) {
		List<String> result = new ArrayList<>();
		for (ClassLoader cl = getClass().getClassLoader(); cl != null; cl = cl.getParent())
			result.add(cl.getClass().getName());

		result.add("Bootstrap classloader");
		return result;
	}

	private List<String> getClassLoadersOfClass(String className) throws Throwable {
		Class<?> c = Class.forName(className.trim());
		List<String> result = new ArrayList<>();
		for (ClassLoader cl = c.getClassLoader(); cl != null; cl = cl.getParent())
			result.add(cl.getClass().getName());

		result.add("Bootstrap classloader");
		return result;
	}

	private String classNameToResourceName(String className) {
		String resourceName = className;
		if (!resourceName.startsWith("/"))
			resourceName = (new StringBuilder()).append("/").append(resourceName).toString();
		resourceName = resourceName.replace('.', '/');
		resourceName = (new StringBuilder()).append(resourceName).append(".class").toString();
		return resourceName;
	}
}
