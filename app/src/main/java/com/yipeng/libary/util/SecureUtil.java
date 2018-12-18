package com.yipeng.libary.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;



/**
 * 加密处理器
 * 
 * @author FANGJINXIN
 * @jdk1.6+ 2014-3-4 下午5:49:04
 */
public class SecureUtil {
	/**
	 * md5加密
	 * 
	 * @param msg
	 *            :需要加密的消息
	 * @return 加密后的数据
	 */
	public static String encodeMD5(String msg) {
		return encode(msg, "MD5");
	}

	/**
	 * md5加密，并加入更新的种子
	 * 
	 * @param msg需要加密的消息
	 * @param updateSeed
	 *            :更新的种子
	 * @return 加密后的数据
	 */
//	public static String encodeMD5(String msg, String updateSeed) {
//		return encode(msg, updateSeed, "MD5");
//	}

	public static String encodeMD5To32bit(String msg) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(msg.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString().toUpperCase();// 32位的加密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * SHA-1加密
	 * 
	 * @param msg
	 *            :需要加密的消息
	 * @return 加密后的数据
	 */
	public static String encodeSHA1(String msg) {
		return encode(msg, "SHA-1");
	}

	/**
	 * SHA-256加密
	 * 
	 * @param msg
	 *            :需要加密的消息
	 * @return 加密后的数据
	 */
	public static String encodeSHA256(String msg) {
		return encode(msg, "SHA-256");
	}

	/**
	 * 对信息进行加密，加密算法如下： <li><tt>MD5</tt></li> <li><tt>SHA-1</tt></li> <li>
	 * <tt>SHA-256</tt></li>
	 * 
	 * @param msg
	 *            ：信息
	 * @param algorithm
	 *            ：加密算法呢
	 * @return 加密后的信息
	 */
	public static String encode(String msg, String algorithm) {
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			byte[] b = md.digest(msg.getBytes("UTF-8"));// 产生数据的指纹
			return toHexString(b);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return msg;
	}

	public static String toHexString(byte[] b) {
		// Base64编码
		StringBuilder sbDes = new StringBuilder();
		String tmp = null;
		for (int i = 0; i < b.length; i++) {
			tmp = (Integer.toHexString(b[i] & 0xFF));
			if (tmp.length() == 1) {
				sbDes.append("0");
			}
			sbDes.append(tmp);
		}
		return sbDes.toString();
	}

//	public static String encode(String msg, String updateSeed, String algorithm) {
//		try {
//			MessageDigest md = MessageDigest.getInstance(algorithm);
//			md.update(msg.getBytes());// 产生数据的指纹
//
//			if (StringUtils.isNotEmpty(updateSeed)) {
//				md.update(updateSeed.getBytes());
//			}
//			byte[] b = md.digest();
//
////			MessageDigest md = MessageDigest.getInstance("");
//			// Base64编码
//			String str2 = new String(Base64.getEncoder().encode(b));
//			return be.encode(b);// 制定一个编码
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		}
//		return msg;
//	}
	
	public static byte[] eccrypt(String info) throws NoSuchAlgorithmException{  
	      MessageDigest md5 = MessageDigest.getInstance("MD5");  
	      byte[] srcBytes = info.getBytes();  
	      //使用srcBytes更新摘要  
	      md5.update(srcBytes);  
	      //完成哈希计算，得到result  
	      byte[] resultBytes = md5.digest();  
	      return resultBytes;  
	}
	
	public static String encodeSecret(String msg){
		if(StringUtils.isEmpty(msg)){
			return null;
		}
		if(msg.length()>10){
			msg=msg.substring(0, 10);
		}
		
		msg=SecureUtil.encodeMD5(msg.toUpperCase());
		return msg;
	}

	
}