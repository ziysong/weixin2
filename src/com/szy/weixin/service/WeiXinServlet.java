package com.szy.weixin.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;

import com.szy.weixin.domain.NewsMessage;
import com.szy.weixin.domain.TextMessage;
import com.szy.weixin.util.CheckUtils;
import com.szy.weixin.util.MessageUtils;

@WebServlet(urlPatterns={"/weiXinServlet"})
public class WeiXinServlet extends HttpServlet{

	/**接口的验证通过get请求*/
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		boolean result = CheckUtils.checkSignature(signature,timestamp,nonce);
		PrintWriter out = response.getWriter();
		if(result){
			out.print(echostr);  //校验成功则将echostr原样返回
		}
		return;
	}

	
	/**普通消息的接收通过post请求*/
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		//获得消息
		Map<String, String> map = null;
		try {
			map = MessageUtils.xmlToMessage(request);
		} catch (DocumentException e) {
			out.print("");
			e.printStackTrace();
		}
		String toUserName = map.get("ToUserName"); //接收方name
		String fromUserName = map.get("FromUserName"); //发送方name 
		String msgType = map.get("MsgType"); //消息类型
		String content = map.get("Content"); //消息内容
		String createTime = map.get("CreateTime");

		//回复消息
		String message = null;
		if(MessageUtils.MESSAGE_TEXT.equals(msgType)){
			switch(content){  
			case("1"):
				message = MessageUtils.getTextMsg1(fromUserName,toUserName);
				break;
			case("2"):
				message = MessageUtils.getNewsMsg1(fromUserName,toUserName);
				break;
			case("3"):
				message = MessageUtils.getNewsMsg2(fromUserName,toUserName);
				break;
			case("4"):
				message = MessageUtils.getImageMsg1(fromUserName, toUserName);
				break;
			case("5"):
				message = MessageUtils.getMusicMsg1(fromUserName, toUserName);
				break;
			case("？"):
				message = MessageUtils.getHelpMenu(fromUserName,toUserName);
				break;  
			case("?"):
				message = MessageUtils.getHelpMenu(fromUserName,toUserName);
				break;  
			default:     
				message = MessageUtils.getTextMsg2(fromUserName,toUserName,content);
				break;
			}
			
		}else if(MessageUtils.MESSAGE_EVENT.equals(msgType)){
			String eventType = map.get("Event"); //获取事件类型
			if(MessageUtils.MESSAGE_SUBSCRIBE.equals(eventType)){
				message = MessageUtils.getFirstMenu(fromUserName,toUserName);
			}else if(MessageUtils.MESSAGE_UNSUBSCRIBE.equals(eventType)){
				System.out.println("用户"+fromUserName+"取消关注了你！");
			}else{
				out.print("");
			}
		}
		
		//发送消息
		if(message != null){
System.out.println(message);
			out.print(message);
		}else{
			out.print("");
		}
		return;
	}
	
	
	
	
}
