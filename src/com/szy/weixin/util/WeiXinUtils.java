package com.szy.weixin.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.szy.weixin.domain.AccessToken;
import com.szy.weixin.menu.Button;
import com.szy.weixin.menu.ClickButton;
import com.szy.weixin.menu.Menu;
import com.szy.weixin.menu.ViewButton;

public class WeiXinUtils {

	private static final String APPID = "wxfc4f6c743be6597e";
	private static final String APPSECRET = "2617642f5759ed72b41ea1c89c5ff206";
	
	private static final String ACCESSTOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	private static final String UPLOAD_TEMP_URL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
	private static final String UPLOAD_PERM_NEWS_URL = "https://api.weixin.qq.com/cgi-bin/material/add_news?access_token=ACCESS_TOKEN";
	private static final String UPLOAD_PERM_OTHER_URL = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=ACCESS_TOKEN";
	private static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	
	/**
	 * get����
	 * @param url:�����ַ
	 * @return
	 */
	public static JSONObject dogetString(String url){
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		JSONObject jsonObject = null;
		try {
			HttpResponse response = httpClient.execute(httpGet);//ִ��get����
			HttpEntity entity = response.getEntity();//��ȡ������,������HttpEntity
			if(entity != null){
				String result = EntityUtils.toString(entity,"UTF-8");//entityתΪString
				jsonObject = JSONObject.fromObject(result);//StringתΪjson��ʽ
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			httpGet.releaseConnection();//�ͷ�����
		}
			return jsonObject;
	}
	
	/**
	 * post����
	 * @param url:�����ַ
	 * @param outStr:�������
	 * @return
	 */
	public static JSONObject dopostString(String url, String outStr){
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		JSONObject jsonObject = null;
		try {
			httpPost.setEntity(new StringEntity(outStr,"UTF-8"));//�����������,��������ΪHttpEntity
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if(entity != null){
				String result = EntityUtils.toString(entity);
				jsonObject = JSONObject.fromObject(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			httpPost.releaseConnection();//�ͷ�����
		}
		return jsonObject;
	}
	
	
	//��ȡAccessToken
	public static AccessToken getAccessToken(){
		File file = new File("Y:\\weixin\\AccessToken.txt");;
		ObjectInputStream ois = null;
		ObjectOutputStream oos = null;
		try {
			//���û�й����򷵻ظ�accessToken����
//			ois = new ObjectInputStream(new FileInputStream(file));//ִ�д˷���ǰ�����Ƚ�����д���ļ��У����򱨴�
//			if(ois != null){
//				AccessToken accessTokenOld = (AccessToken) ois.readObject();
//				long nowTime = new Date().getTime();
//				long createTime= accessTokenOld.getCreateTime();
//				if(nowTime-createTime < 7200000){ 
//					return accessTokenOld;
//				}
//			}
			//��ȡ�µ�AccessToken
			String url = ACCESSTOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
			JSONObject jsonObject = dogetString(url);
			AccessToken accessTokenNew = new AccessToken();
			accessTokenNew.setAccess_token(jsonObject.getString("access_token"));
			accessTokenNew.setExpires_in(jsonObject.getInt("expires_in"));
			accessTokenNew.setCreateTime(new Date().getTime());
			
			//���µ�AccessToken�������л����ļ���
			oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(accessTokenNew);
			return accessTokenNew;
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				if(ois != null){
					ois.close();
				}
				if(oos != null){
					oos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	//�ϴ���ʱ�ز�
	public static String upload(String filePath, String accessToken, String type) throws IOException{
		File file = new File(filePath);
		if(!file.isFile() || !file.exists()){
			throw new RuntimeException("�ļ�������");
		}
		String url = UPLOAD_TEMP_URL.replace("ACCESS_TOKEN", accessToken).replace("TYPE", type);
		URL urlObj = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();//����
		
		conn.setRequestMethod("POST");
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		
		conn.setRequestProperty("Connection", "Keep-Alive");//��������ͷ��Ϣ
		conn.setRequestProperty("Charset", "utf-8");
		
		String BOUNDARY = "----------"+System.currentTimeMillis();//���ñ߽�
		conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);
		
		StringBuilder sb = new StringBuilder();
		sb.append("--");
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition:form-data;name=\"file\";filename=\" "+file.getName()+" \"\r\n");
		sb.append("Content-Type:application/octet-stream\r\n\r\n");
		
		byte[] head = sb.toString().getBytes("utf-8");
		
		//��������
		OutputStream out = new DataOutputStream(conn.getOutputStream());
		//�����ͷ
		out.write(head);
		
		//�ļ����Ĳ���
		//���ļ������ļ��ķ�ʽ,���뵽url��
		DataInputStream dis = new DataInputStream(new FileInputStream(file));
		byte[] bufferOut = new byte[1024];
		int hasRead = 0;
		while((hasRead=dis.read(bufferOut)) != -1){
			out.write(bufferOut, 0, hasRead);
		}
		dis.close();
		
		//��β����
		byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("UTF-8");//����������ݷָ�
		
		out.write(foot);
		out.flush();
		out.close();
		
		//��ȡ΢�ŷ��������ؽ��
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
		String mediaId = null;
		try{
			JSONObject jsonObject = JSONObject.fromObject(result);
			String typeName = "media_id";
			if(!"image".equals(type)){  
				typeName = type + "_media_id";
			}
			mediaId = jsonObject.getString(typeName);
		}catch(Exception e){
			System.out.println("api����δ��Ȩ����ȷ�Ϲ��ں��ѻ�øýӿ�!");
		}
		return mediaId;
	}
	
	//�����˵���
	public static void createMenu(){
		//һ���˵�1
		ViewButton button1  = new ViewButton();
		button1.setName("֪��");
		button1.setType("view");
		button1.setUrl("http://www.zhihu.com/");
		
		//һ���˵�2
		ViewButton button2  = new ViewButton();
		button2.setName("΢��");
		button2.setType("view");
		button2.setUrl("http://www.weibo.com/");
		
		//�Ӳ˵�1
		ClickButton button31 = new ClickButton();
		button31.setName("ɨһɨ");
		button31.setKey("31");
		button31.setType("scancode_push");
		//�Ӳ˵�2
		ClickButton button32 = new ClickButton();
		button32.setName("��һ��");
		button32.setKey("32");
		button32.setType("pic_sysphoto");
		//�Ӳ˵�3
		ClickButton button33 = new ClickButton();
		button33.setName("������");
		button33.setKey("33");
		button33.setType("location_select");
		//�Ӳ˵�4
		ViewButton button34 = new ViewButton();
		button34.setName("�ҵĲ���");
		button34.setUrl("http://blog.csdn.net/u012599724/");
		button34.setType("view");
		//�Ӳ˵�5
		ViewButton button35 = new ViewButton();
		button35.setName("java����");
		button35.setUrl("http://www.imooc.com/");
		button35.setType("view");
		//һ���˵�3
		Button button3 = new Button();  
		button3.setName("ʲô��");
		button3.setSub_button(new Button[]{button31,button32,button33,button34,button35});
		
		//��װһ���˵�
		Menu menu = new Menu();
		menu.setButton(new Button[]{button1,button2,button3});
		
		AccessToken accessToken = WeiXinUtils.getAccessToken();
		String access_token = accessToken.getAccess_token();
		String url = CREATE_MENU_URL.replace("ACCESS_TOKEN", access_token);
		String outStr = JSONObject.fromObject(menu).toString();
		JSONObject jsonResult = dopostString(url,outStr);
System.out.println(jsonResult);		
	}
		
}
