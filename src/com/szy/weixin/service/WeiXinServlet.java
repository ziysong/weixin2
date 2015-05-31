package com.szy.weixin.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.szy.weixin.domain.TextMessage;
import com.szy.weixin.util.CheckUtils;
import com.szy.weixin.util.MessageUtils;

@WebServlet(urlPatterns={"/weixinServlet"})
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
	}

	
	/**普通消息的接收通过post请求*/
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		//获得消息
		Map<String,String> map = MessageUtils.xmlToMessage(request);
		String toUserName = map.get("ToUserName"); //接收方name
		String fromUserName = map.get("FromUserName"); //发送方name 
		String msgType = map.get("MsgType"); //消息类型
		String content = map.get("Content"); //消息内容
		String createTime = map.get("CreateTime");

		//回复消息
		String message = null;
		TextMessage textMessage = new TextMessage();
		textMessage.setToUserName(fromUserName);//设置接收方的name,即原来的FromUserName
		textMessage.setFromUserName(toUserName);
		textMessage.setCreateTime(createTime); //必须要,否则不会把消息推送给用户
//		textMessage.setMsgId("123"); //不是必须的
		if(MessageUtils.MESSAGE_TEXT.equals(msgType)){
			textMessage.setMsgType("text");
			switch(content){  
			case("1"):
				textMessage.setContent("宋子扬,性别:男,爱好:女");
				break;
			case("2"):
				textMessage.setContent("宋子扬,有着无比魅力的雄性声音,如果你跟他交流,你会很快的被他深深的迷倒。");
				break;
			case("？"):
				textMessage.setContent(MessageUtils.getHelpMenu());
				break;
			case("?"):
				textMessage.setContent(MessageUtils.getHelpMenu());
				break;
			default:
				textMessage.setContent("您发送的内容是:"+content);
				break;
			}
			
		}else if(MessageUtils.MESSAGE_EVENT.equals(msgType)){
			textMessage.setMsgType("text");
			String eventType = map.get("Event"); //获取事件类型
			if(MessageUtils.MESSAGE_SUBSCRIBE.equals(eventType)){
				textMessage.setContent(MessageUtils.getFirstMenu());
System.out.println("用户"+fromUserName+"关注了你哦！");				
			}else if(MessageUtils.MESSAGE_UNSUBSCRIBE.equals(eventType)){
				textMessage.setContent("用户"+fromUserName+"取消关注了你！");
System.out.println("用户"+fromUserName+"取消关注了你！");
			}else{
				textMessage.setContent("其他事件！");
			}
		}
		
		message = MessageUtils.textMessageToXml(textMessage);//将消息对象转为xml数据
System.out.println(message);
		out.print(message);
		return;
	}
	
	
	
	
}
