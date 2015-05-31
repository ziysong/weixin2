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
	}

	
	/**��ͨ��Ϣ�Ľ���ͨ��post����*/
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		//�����Ϣ
		Map<String,String> map = MessageUtils.xmlToMessage(request);
		String toUserName = map.get("ToUserName"); //���շ�name
		String fromUserName = map.get("FromUserName"); //���ͷ�name 
		String msgType = map.get("MsgType"); //��Ϣ����
		String content = map.get("Content"); //��Ϣ����
		String createTime = map.get("CreateTime");

		//�ظ���Ϣ
		String message = null;
		TextMessage textMessage = new TextMessage();
		textMessage.setToUserName(fromUserName);//���ý��շ���name,��ԭ����FromUserName
		textMessage.setFromUserName(toUserName);
		textMessage.setCreateTime(createTime); //����Ҫ,���򲻻����Ϣ���͸��û�
//		textMessage.setMsgId("123"); //���Ǳ����
		if(MessageUtils.MESSAGE_TEXT.equals(msgType)){
			textMessage.setMsgType("text");
			switch(content){  
			case("1"):
				textMessage.setContent("������,�Ա�:��,����:Ů");
				break;
			case("2"):
				textMessage.setContent("������,�����ޱ���������������,������������,���ܿ�ı���������Ե���");
				break;
			case("��"):
				textMessage.setContent(MessageUtils.getHelpMenu());
				break;
			case("?"):
				textMessage.setContent(MessageUtils.getHelpMenu());
				break;
			default:
				textMessage.setContent("�����͵�������:"+content);
				break;
			}
			
		}else if(MessageUtils.MESSAGE_EVENT.equals(msgType)){
			textMessage.setMsgType("text");
			String eventType = map.get("Event"); //��ȡ�¼�����
			if(MessageUtils.MESSAGE_SUBSCRIBE.equals(eventType)){
				textMessage.setContent(MessageUtils.getFirstMenu());
System.out.println("�û�"+fromUserName+"��ע����Ŷ��");				
			}else if(MessageUtils.MESSAGE_UNSUBSCRIBE.equals(eventType)){
				textMessage.setContent("�û�"+fromUserName+"ȡ����ע���㣡");
System.out.println("�û�"+fromUserName+"ȡ����ע���㣡");
			}else{
				textMessage.setContent("�����¼���");
			}
		}
		
		message = MessageUtils.textMessageToXml(textMessage);//����Ϣ����תΪxml����
System.out.println(message);
		out.print(message);
		return;
	}
	
	
	
	
}
