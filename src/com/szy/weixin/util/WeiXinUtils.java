package com.szy.weixin.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.szy.weixin.domain.AccessToken;

public class WeiXinUtils {

	private static final String APPID = "wxf407840f6d83f810";
	private static final String APPSECRET = "ec73a93b97dc0eb81256bad87bcd5f4f";
	
	private static final String ACCESSTOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	private static final String UPLOAD_TEMP_URL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
	private static final String UPLOAD_PERM_NEWS_URL = "https://api.weixin.qq.com/cgi-bin/material/add_news?access_token=ACCESS_TOKEN";
	private static final String UPLOAD_PERM_OTHER_URL = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=ACCESS_TOKEN";
	
	/**
	 * get请求
	 * @param url:请求地址
	 * @return
	 */
	public static JSONObject dogetString(String url){
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		JSONObject jsonObject = null;
		try {
			HttpResponse response = httpClient.execute(httpGet);//执行get请求
			HttpEntity entity = response.getEntity();//获取请求结果,类型是HttpEntity
			if(entity != null){
				String result = EntityUtils.toString(entity,"UTF-8");//entity转为String
				jsonObject = JSONObject.fromObject(result);//String转为json格式
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			httpGet.releaseConnection();//释放连接
		}
			return jsonObject;
	}
	
	/**
	 * post请求
	 * @param url:请求地址
	 * @param outStr:请求参数
	 * @return
	 */
	public static JSONObject dopostString(String url, String outStr){
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		JSONObject jsonObject = null;
		try {
			httpPost.setEntity(new StringEntity(outStr,"UTF-8"));//设置请求参数,参数类型为HttpEntity
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if(entity != null){
				String result = EntityUtils.toString(entity);
				jsonObject = JSONObject.fromObject(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			httpPost.releaseConnection();//释放连接
		}
		return jsonObject;
	}
	
	
	//获取AccessToken
	public static AccessToken getAccessToken(){
		String url = ACCESSTOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
		JSONObject jsonObject = dogetString(url);
		AccessToken accessToken = new AccessToken();
		accessToken.setAccess_token(jsonObject.getString("access_token"));
		accessToken.setExpires_in(jsonObject.getInt("expires_in"));
		return accessToken;
	}
	
	//上传临时素材
	public static String upload(String filePath, String accessToken, String type) throws IOException{
		File file = new File(filePath);
		if(!file.isFile() || !file.exists()){
			throw new RuntimeException("文件不存在");
		}
		String url = UPLOAD_TEMP_URL.replace("ACCESS_TOKEN", accessToken).replace("TYPE", type);
		URL urlObj = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();//连接
		
		conn.setRequestMethod("POST");
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		
		conn.setRequestProperty("Connection", "Keep-Alive");//设置请求头信息
		conn.setRequestProperty("Charset", "utf-8");
		
		String BOUNDARY = "----------"+System.currentTimeMillis();//设置边界
		conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);
		
		StringBuilder sb = new StringBuilder();
		sb.append("--");
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition:form-data;name=\"file\";filename=\" "+file.getName()+" \"\r\n");
		sb.append("Content-Type:application/octet-stream\r\n\r\n");
		
		byte[] head = sb.toString().getBytes("utf-8");
		
		//获得输出流
		OutputStream out = new DataOutputStream(conn.getOutputStream());
		//输出表头
		out.write(head);
		
		//文件正文部分
		//把文件以流文件的方式,推入到url中
		DataInputStream dis = new DataInputStream(new FileInputStream(file));
		byte[] bufferOut = new byte[1024];
		int hasRead = 0;
		while((hasRead=dis.read(bufferOut)) != -1){
			out.write(bufferOut, 0, hasRead);
		}
		dis.close();
		
		//结尾部分
		byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("UTF-8");//定义最后数据分隔
		
		out.write(foot);
		out.flush();
		out.close();
		
		//获取微信服务器返回结果
		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		String result = null;
		try{
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = null;
			if((line=reader.readLine()) != null){
				buffer.append(line);         
			}  
			if(result == null){
				result = buffer.toString();
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if(reader != null){
				reader.close();
			}
			conn.disconnect();
		}         
System.out.println("reslut:"+result);	
		JSONObject jsonObject = JSONObject.fromObject(result);
		String typeName = "media_id";
		if(!"image".equals(type)){  
			typeName = type + "_media_id";
		}
		String mediaId = jsonObject.getString(typeName);
		return mediaId;
	}
	
	
	
}
