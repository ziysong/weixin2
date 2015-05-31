package com.szy.weixin.util;

import java.security.MessageDigest;
import java.util.Arrays;

public class CheckUtils {

	private static final String  token = "ziysong";//token��������������д��Token
	
	public static boolean checkSignature(String signature, String timestamp, String nonce){
		String[] str  = new String[]{token,timestamp,nonce};
		//����
		Arrays.sort(str);
		//ƴ��Ϊһ���ַ���
		StringBuffer content = new StringBuffer();
		for(int i=0; i<str.length; i++){
			content.append(str[i]);
		}
		//sha1����
		String sha1Content = getSha1(content.toString());
		//���ܺ�����ĺ�΢�ŷ�����������signature�Ƚ�
		return signature.equals(sha1Content);
	}
	
	/**sha1�����㷨*/
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
