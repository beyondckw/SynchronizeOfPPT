package com.newppt.android.logical;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.newppt.android.entity.MyPath;

public class FileInfo {

	/**
	 * 创建文件夹
	 * 
	 * @param savePath
	 */
	public static void CreateFile(String savePath) {
		File file = new File(savePath);

		if (!file.exists() && !file.isDirectory()) {
			file.mkdir();
		}
	}
	
	public static boolean fileExist(String savePath) {
		File file = new File(savePath);
		return file.exists();
	}

	/**
	 * 获取路径下所有JPG的信息
	 * 
	 * @param path
	 * @return
	 */
	public static List<JpgPathInfo> getJpgPathInfos(String path) {
		File pptFile = new File(path + MyPath.pptJpg);
		List<JpgPathInfo> infos = new ArrayList<JpgPathInfo>();
		JpgPathInfo info;

		String[] fileNames = pptFile.list();

		for (String name : fileNames) {
			info = new JpgPathInfo();
			info.setPptJpg(path + MyPath.pptJpg + "/" + name);
			File noteFile = new File(path + MyPath.noteJpg + "/" + name);
			if (noteFile.exists()) {
				info.setNoteJpg(path + MyPath.noteJpg + "/" + name);
			}

			infos.add(info);

		}

		return infos;
	}

	/**
	 * 根据路径删除指定的目录或文件，无论存在与否
	 *
	 * @param sPath
	 *            要删除的目录或文件
	 * @return 删除成功返回 true，否则返回 false。
	 */
	public static boolean DeleteFolder(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 判断目录或文件是否存在
		if (!file.exists()) { // 不存在返回 false
			return flag;
		} else {
			// 判断是否为文件
			if (file.isFile()) { // 为文件时调用删除文件方法
				return deleteFile(sPath);
			} else { // 为目录时调用删除目录方法
				return deleteDirectory(sPath);
			}
		}
	}

	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * 
	 * @param sPath
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	public static boolean deleteDirectory(String sPath) {
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		boolean flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} // 删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取文件修改时间
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static String getTime(String path) throws Exception {
		File file = new File(path);
		long modifiedTime = file.lastModified();
		Date date = new Date(modifiedTime);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:MM");
		String time = sdf.format(date);
		return time;
	}

	public static Map<String, Object> loadPPTInfo(String path) throws Exception {
		File file = new File(path);

		List<String> pptList = new ArrayList<String>();
		List<String> timeList = new ArrayList<String>();
		Map<String, Object> map = new HashMap<String, Object>();

		String[] fileNames = file.list();
		for (String name : fileNames) {
			if (name.endsWith(".ppt") || name.endsWith(".pptx")) {
				pptList.add(name);
				timeList.add(getTime(path + name));
			}
		}
		map.put("pptList", pptList);
		map.put("timeList", timeList);
		return map;
	}
}
