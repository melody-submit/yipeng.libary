package com.yipeng.libary.util;

/**
 * @author User
 * 
 */
public class StringUtils {
	
	public static String join(String[] items, String splite){
		
		StringBuffer buf =  new StringBuffer();
		if(items!=null){
			for(int i=0;i<items.length;i++){
				String item = items[i];
				
				buf.append(item);
				if(i+1<items.length){
					buf.append(splite);
				}
			}
		}
		return buf.toString();
	}

	public static int getInteger(String str) {
		if (isNotEmpty(str)) {
			try {
				return Integer.parseInt(str);
			} catch (Exception e) {

			}
		}
		return 0;
	}

	public static boolean isNotEmpty(String str) {
		return str != null && !"".equals(str);
	}

	public static boolean isEmpty(String str) {
		return !isNotEmpty(str);
	}

	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}
	public static boolean hasBlank(String... strs) {
		return !isNotBlank(strs);
	}
	public static boolean isNotBlank(String... strs) {
		if(strs!=null){
			for(String str : strs){
				if(isBlank(str)){
					return false;
				}
			}
		}
		
		return true;
	}

	public static boolean isBlank(String str) {
		return isEmpty(str) || "".equals(str.trim());
	}

}
