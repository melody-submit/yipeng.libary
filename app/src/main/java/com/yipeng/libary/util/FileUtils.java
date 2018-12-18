package com.yipeng.libary.util;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.storage.StorageManager;

import com.yipeng.libary.WsApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class FileUtils {

	public final static String APP_FOLDER = "kangbang";
 
	public static  String APP_ROOT_PATH = "";

	public final static long MB = 1024*1024;
	
	public final static long HALF_MB = 256*1024;
	
	public final static long _50KB = 50*1024;
	
	public final static long _200KB = 200*1024;
	
	/**
	 * 判断SD是否可以
	 * 
	 * @return
	 */
	public static boolean isSdcardExist() {
		boolean flag = false;
		try {
			flag = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
			if(flag){
				boolean canWrite = new File(Environment
						.getExternalStorageDirectory().getAbsolutePath())
						.canWrite();
				if (canWrite) {
					return true;
				}
			}
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}
	
	public static enum FolderType{
		IMAGE{
			public  String getFolder(){
				return "image"+ File.separator;
			}
		},ERROR_LOG{
			public  String getFolder(){
				return "crash_log"+ File.separator;
			}
		},VOICE_SEND{
			public  String getFolder(){
				return "voice" + File.separator+"sent"+ File.separator;
			}
		},VOICE_REC{
			public  String getFolder(){
				return "voice" + File.separator+"rev"+ File.separator;
			}
		},DOWNLOAD{
			public  String getFolder(){
				return "download"+ File.separator;
			}
		},GUIDE{
			public  String getFolder(){
				return "guide"+ File.separator;
			}
		};
		
		public  String getFolder(){
			return null;
		}
		
	}

	static Object lock = new Object();
	public static String getPath(FolderType type){
		
		
		if(StringUtils.isBlank(APP_ROOT_PATH)){
			synchronized (lock) {
				if(StringUtils.isBlank(APP_ROOT_PATH)){

					File externalStorageFile = null;
					if(!isSdcardExist()) {
						File downloadCacheDirectory = Environment.getDownloadCacheDirectory();
						if(downloadCacheDirectory.canRead()) {
							externalStorageFile = downloadCacheDirectory;
//							mFileExplorerSdcardTag = downloadCacheDirectory.getName();
						}
					} else {
						externalStorageFile = Environment.getExternalStorageDirectory();
					}
					File mSdcardFile = externalStorageFile;

					APP_ROOT_PATH = mSdcardFile.getPath() + File.separator + APP_FOLDER;
				}
			}
		}
		
		return APP_ROOT_PATH +File.separator + type.getFolder();
	}

	public static String getLocalStorgePath(){
		String path_v1 = Environment.getDataDirectory().getAbsolutePath() + File.separator + "data" + File.separator;
		//getCacheDir
		return path_v1;
	}

	public static String getCanWriteStorgePath(){
		ArrayList<String> paths = getVolumePaths();

		String path = "";
		for(String pa : paths){
			File file = new File(pa);
			if(file.isDirectory()){
				File temp= new File(pa +File.separator+"temp.t");
				try {
					if(temp.exists()){
//						showToastMsg(MainActivity.this, "可操作:"+pa);
						path = pa;
					}else if(temp.createNewFile()){
						temp.delete();
						path = pa;
//						showToastMsg(MainActivity.this, "可操作:"+pa);
					}
				} catch (IOException e) {
					e.printStackTrace();
//					showToastMsg(MainActivity.this, "不可操作:"+pa);
				}
			}else{
//				showToastMsg(MainActivity.this, "不可操作:"+pa);
			}
		}
		return path;
	}
	

	public static ArrayList<String> getVolumePaths(){
		
		ArrayList<String> paths = new ArrayList<String>();
		StorageManager storageManager = (StorageManager)WsApplication.getInstance().getSystemService(Context.STORAGE_SERVICE);
	    try {
	        Class<?>[] paramClasses = {};
	        Method getVolumePathsMethod = StorageManager.class.getMethod("getVolumePaths", paramClasses);
	        getVolumePathsMethod.setAccessible(true);
	        Object[] params = {};
	        Object invoke = getVolumePathsMethod.invoke(storageManager, params);
	        for (int i = 0; i < ((String[])invoke).length; i++) {
	        	String path =  ((String[])invoke)[i];
	        	paths.add(path);
	        }
	    } catch (NoSuchMethodException e1) {
	        e1.printStackTrace();
	    } catch (IllegalArgumentException e) {
	        e.printStackTrace();
	    } catch (IllegalAccessException e) {
	        e.printStackTrace();
	    } catch (InvocationTargetException e) {
	        e.printStackTrace();
	    }
	    return paths;
	}
	
	
	/**
	 * 创建根目录
	 * 
	 * @param path
	 *            目录路径
	 */
	public static void createDirFile(String path) {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	public static void createDirFile(File dir) {
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	public static void excuteCmd(String[] command){
//        String[] command = {"chmod", "777", dir.getPath() + "/test.apk"};

		ProcessBuilder builder = new ProcessBuilder(command);
		try {
			builder.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String readFile(String path){
		
		StringBuffer buf = new StringBuffer();
		BufferedReader bReader;
		try {
			bReader = new BufferedReader(new InputStreamReader(new FileInputStream(path))); 
			String line = bReader.readLine();
			buf.append(line);
//			buf+=line;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		return buf.toString();
	}

	/**
	 * 创建文件
	 * 
	 * @param path
	 *            文件路径
	 * @return 创建的文件
	 */
	public static File createNewFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				return null;
			}
		}
		return file;
	}

	/**
	 * 删除文件夹
	 * 
	 * @param folderPath
	 *            文件夹的路径
	 */
	public static void delFolder(String folderPath) {
		delAllFile(folderPath);
		String filePath = folderPath;
		filePath = filePath.toString();
		java.io.File myFilePath = new java.io.File(filePath);
		myFilePath.delete();
	}

	public static void delFile(String filepath) {
		File temp = new File(filepath);
		if (temp.isFile()) {
			temp.delete();
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param path
	 *            文件的路径
	 */
	public static void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);
				delFolder(path + "/" + tempList[i]);
			}
		}
	}

	/**
	 * 获取文件的Uri
	 * 
	 * @param path
	 *            文件的路径
	 * @return
	 */
	public static Uri getUriFromFile(String path) {
		File file = new File(path);
		return Uri.fromFile(file);
	}

	public static long getFileSize(File file) {
		long size = -1;
		if (file != null && file.isFile()) {
//			FileInputStream fis;
//			try {
//				fis = new FileInputStream(file);
//				FileChannel fc = fis.getChannel();
//				fc = fis.getChannel();
//				size = fc.size();
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
			size = file.length();
		}
		return size;
	}
	
	public static long getFileModified(File file) {
		long time = -1;
		if (file != null && file.isFile()) {
			time = file.lastModified();
		}
		return time;
	}

	/**
	 * 换算文件大小
	 * 
	 * @param size
	 * @return
	 */
	public static String formatFileSize(long size) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "未知大小";
		if (size < 1024) {
			fileSizeString = df.format((double) size) + "B";
		} else if (size < 1048576) {
			fileSizeString = df.format((double) size / 1024) + "K";
		} else if (size < 1073741824) {
			fileSizeString = df.format((double) size / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) size / 1073741824) + "G";
		}
		return fileSizeString;
	}
}
