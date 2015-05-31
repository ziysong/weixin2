package com.szy.weixin.util;

import java.security.MessageDigest;
import java.util.Arrays;

public class CheckUtils {

	private static final String  token = "ziysong";//token即在配置项中填写的Token
	
	public static boolean checkSignature(String signature, String timestamp, String nonce){
		String[] str  = new String[]{token,timestamp,nonce};
		//排序
		Arrays.sort(str);
		//拼接为一个字符串
		StringBuffer content = new StringBuffer();
		for(int i=0; i<str.length; i++){
			content.append(str[i]);
		}
		//sha1加密
		String sha1Content = getSha1(content.toString());
		//加密后的密文和微信服务器发来的signature比较
		return signature.equals(sha1Content);
	}
	
	/**sha1加密算法*/
	public static String getSha1(String str){
		
		if(str==null || str.length()==0){
			return null;
		}
		char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9',
							'a','b','c','d','e','f'}; 
		try{
			MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
			mdTemp.update(str.getBytes("UTF-8"));
			
			byte[] md =  mdTemp.digest();
			int j = md.length;
			char buf[] = new char[j*2];
			int k = 0;
			for(int i=0; i<j; i++){
				byte byte0 = md[i];
				buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
				buf[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(buf);
		}catch(Exception e){
			return null;
		}
	}
	
	
	
}
