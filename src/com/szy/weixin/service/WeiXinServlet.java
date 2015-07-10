package com.szy.weixin.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;

import com.szy.weixin.domain.Student;
import com.szy.weixin.mark.MarkQueryUtils;
import com.szy.weixin.util.CheckUtils;
import com.szy.weixin.util.DaoUtils;
import com.szy.weixin.util.MessageUtils;

@WebServlet(urlPatterns={"/weiXinServlet"})
public class WeiXinServlet extends HttpServlet{

	/**�ӿڵ���֤ͨ��get����*/
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		boolean result = CheckUtils.checkSignature(signature,timestamp,nonce);
		PrintWriter out = response.getWriter();
		if(result){
			out.print(echostr);  //У��ɹ���echostrԭ������
		}
		return;
	}

	
	/**��ͨ��Ϣ�Ľ���ͨ��post����*/
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		//�����Ϣ
		Map<String, String> map = null;
		try {
			map = MessageUtils.xmlToMessage(request);
			request.getServletContext().setAttribute("userMessage", map);
		} catch (DocumentException e) {
			out.print("");
			e.printStackTrace();
		} 
		
		String content = null;
		String toUserName = null;
		String fromUserName = null;
		String msgType = null;
		String createTime = null;
		toUserName = map.get("ToUserName"); //���շ�name
		fromUserName = map.get("FromUserName"); //���ͷ�name 
		msgType = map.get("MsgType"); //��Ϣ����
		createTime = map.get("CreateTime");
		if(map.containsKey("Content")){
			content = map.get("Content").trim(); //��Ϣ����
		}
		
		//�ظ���Ϣ
		String message = null;
		
		//������Ϣ
		if(MessageUtils.MESSAGE_VOICE.equals(msgType)){
			content = map.get("Recognition").trim();
			if(content.startsWith("����")){//��������
				message = MessageUtils.getTransMsg(content,fromUserName,toUserName);
			}else if(content.endsWith("��") || content.endsWith("��")){//������ѯ
				message = MessageUtils.getWeatherMsg(content,fromUserName,toUserName);
			}else{
				message = MessageUtils.getTransErrorMsg(fromUserName, toUserName);
			}
			
		}
		//�ı���Ϣ
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
			case("����"):
				message = MessageUtils.getWeatherHelp(fromUserName, toUserName);
				break;
			case("��"):
				message = MessageUtils.getHelpMenu(fromUserName,toUserName);
				break;  
			case("?"):
				message = MessageUtils.getHelpMenu(fromUserName,toUserName);
				break;  
			default:     
				message = MessageUtils.getTextMsg2(fromUserName,toUserName,content);
				break;
			}
			
			//����
			if(content.startsWith("����")){
				message = MessageUtils.getTransMsg(content,fromUserName,toUserName);
			}else if(content.endsWith("��") || content.endsWith("��")){
				message = MessageUtils.getWeatherMsg(content,fromUserName,toUserName);
			}
			
		}else if(MessageUtils.MESSAGE_EVENT.equals(msgType)){//��Ϣ����Ϊevent
			String eventType = map.get("Event"); //��ȡ�¼�����
			//��ע�¼�
			if(MessageUtils.MESSAGE_SUBSCRIBE.equals(eventType)){
				message = MessageUtils.getFirstMenu(fromUserName,toUserName);
			}
			//����¼�
			if(MessageUtils.MESSAGE_CLICK.equals(eventType)){
				String eventKey = map.get("EventKey");
				if("34".equals(eventKey)){
					message = MessageUtils.getImageMsg1(fromUserName, toUserName);
				}
			}
			//view�¼�
			if(MessageUtils.MESSAGE_VIEW.equals(eventType)){
				String eventKey = map.get("EventKey");
				if("http://ziysong.tunnel.mobi/weixin2/markQueryServlet".equals(eventKey)){
					MarkQueryServlet.setFromUserName(fromUserName);
				}
			}  
		//��Ϣ����Ϊlocation:����λ��ѡ��
		}else if(MessageUtils.MESSAGE_LOCATION.equals(msgType)){
			message = MessageUtils.getTextMsg2(fromUserName,toUserName,map.get("Label"));
		}
		
		//������Ϣ
		if(message != null){
System.out.println("�ظ�����Ϣ��"+message);
			out.print(message);
		}else{
			out.print("");
		}
		return;
	}
	
	
	
	
}
