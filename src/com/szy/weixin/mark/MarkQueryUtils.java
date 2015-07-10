package com.szy.weixin.mark;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class MarkQueryUtils {
	
	private final static int TIME_OUT = 20*1000;  
	
	/**用于获取iPlanetDirectoryPro和JSESSIONID的方法*/
	public static Map<String,String> query(String username, String password){
		Map<String,String> map = new HashMap<>();
		try {
			//模拟登陆首页获取返回的iPlanetDirectoryPro(在Set-Cookie响应头里)
			String loginUrl = "http://portal.chd.edu.cn/userPasswordValidate.portal"; 
			Response response1 = null;
			String iPlanetDirectoryPro = null;
			response1 = Jsoup.connect(loginUrl)
							.data("Login.Token1", username, "Login.Token2", password)
						    .method(Method.POST)
						    .timeout(TIME_OUT)
						    .execute();
			iPlanetDirectoryPro = response1.cookie("iPlanetDirectoryPro");
			map.put("iPlanetDirectoryPro", iPlanetDirectoryPro);
			
			//请求个人信息页面时获取返回的JSESSIONID,需要iPlanetDirectoryPro
			String infoUrl = "http://bksjw.chd.edu.cn/idslogin.jsp";
			Response response2 = null;
			String JSESSIONID = null; 
			response2 = Jsoup.connect(infoUrl)
							 .cookie("iPlanetDirectoryPro", iPlanetDirectoryPro)
							 .method(Method.GET)
							 .timeout(TIME_OUT)
							 .execute();
			JSESSIONID = response2.cookie("JSESSIONID");
			map.put("JSESSIONID", JSESSIONID);
		} catch (IOException e) {
			map.put("error1", "网络异常~");
			return map;
		} catch(IllegalArgumentException ex) {
			map.put("error2", "学号或密码输错啦~");
			return map;
		}
		return map;
	}
	
	/**查询成绩：需要iPlanetDirectoryPro和JSESSIONID*/
	public static Map<String, String> markQuery(String username, String password){
		Map<String, String> map = query(username,password);
		Map<String,String> mapResult = new HashMap<>();
		if(map.containsKey("error1")){
			mapResult.put("error", "网络异常~");
			return mapResult;
		}
		if(map.containsKey("error2")){
			mapResult.put("error", "学号或密码输错啦~");
			return mapResult;
		}
		
		
		String iPlanetDirectoryPro = map.get("iPlanetDirectoryPro");
		String JSESSIONID = map.get("JSESSIONID");
		String gradeUrl = "http://bksjw.chd.edu.cn/bxqcjcxAction.do";
		Document gradeDoc = null;
		try{
			gradeDoc = Jsoup.connect(gradeUrl)
							.header("Accept","application/x-ms-application, image/jpeg, application/xaml+xml, image/gif, image/pjpeg, application/x-ms-xbap, application/x-shockwave-flash, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*")
					        .header("Accept-Encoding", "gzip,deflate")
					        .header("Accept-Language", "zh-CN")
					        .header("DNT", "1")
					        .header("Referer","http://bksjw.chd.edu.cn/menu/s_menu.jsp")
					        .header("Connection", "keep-alive")
					        .header("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; InfoPath.2; .NET4.0C; .NET4.0E; MALC)")
							.cookie("iPlanetDirectoryPro", iPlanetDirectoryPro)
							.cookie("JSESSIONID", JSESSIONID)
							.timeout(TIME_OUT)
							.get();
			
			StringBuffer sb = new StringBuffer();
			Elements elements = gradeDoc.select("tr.odd");
			int courseNum = elements.size();
			for(int i=0; i<courseNum; i++){
//				Elements elements2 = elements.get(i).select("td[align=center]");
				sb.append(elements.get(i).text()+";");
			}
			mapResult.put("courseRes", sb.toString());
			mapResult.put("courseNum", courseNum+"");
		}catch(IOException e){
			mapResult.put("error", "网络异常~~");
			return mapResult;
		}
		return mapResult;
	}
	
	
	//查询课表：
	
	//修改密码：
	
	//教学评估：
	
}
