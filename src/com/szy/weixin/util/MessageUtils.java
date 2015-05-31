package com.szy.weixin.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.szy.weixin.domain.TextMessage;
import com.thoughtworks.xstream.XStream;


public class MessageUtils {

	//MsgType:��Ϣ����
	public final static String MESSAGE_TEXT = "text";
	public final static String MESSAGE_IMAGE = "image";
	public final static String MESSAGE_VOICE = "voice";
	public final static String MESSAGE_LOCATION = "location";
	public final static String MESSAGE_LINK = "link";
	public final static String MESSAGE_EVENT = "event";
	
	//�¼�����:Event.������Ϣ������event
	public final static String MESSAGE_SUBSCRIBE = "subscribe"; //��ע
	public final static String MESSAGE_UNSUBSCRIBE = "unsubscribe";//ȡ����ע
	public final static String MESSAGE_CLICK = "click";//�����Ӳ˵�
	public final static String MESSAGE_LOCATION_EVENT = "location";//����λ���¼�
	
	
	/**��xml����תΪ�ı���Ϣ����,��map����*/
	public static Map<String,String> xmlToMessage(HttpServletRequest request){
		Map<String,String> map = new HashMap<String,String>();
		SAXReader reader = new SAXReader();
		InputStream is = null;
		Document doc = null;
		try {
			is = request.getInputStream();//��ȡ΢��post������������
			doc = reader.read(is);
			Element root = doc.getRootElement();
			List<Element> elements = root.elements();
			for(Element element : elements){
				map.put(element.getName(), element.getText());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}finally{
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return map;
	}
	
	/**���ı���Ϣ����תΪxml����*/
	public static String textMessageToXml(TextMessage textMessage){
		XStream xstream = new XStream();
		xstream.alias("xml", textMessage.getClass());//���ø�Ԫ��Ϊxml.��ȻĬ����TextMessage��ȫ����
		return xstream.toXML(textMessage);
	}

	//��עʱ����
	public static String getFirstMenu() {
		StringBuffer sb = new StringBuffer();
		sb.append("��л����ע�������΢�Ź����˺�/:,@-D/:,@-D\n");
		sb.append("�ظ�'1'�ɻ�ȡ������Ļ�����Ϣ\n");
		sb.append("�ظ�'2'�ɻ�ȡ������ĸ������\n");
		sb.append("�ظ�'?'�ɻ�ȡ����");
		return sb.toString();
	}
	
	//����ʱ����
	public static String getHelpMenu() {
		StringBuffer sb = new StringBuffer();
		sb.append("�ظ�'1'�ɻ�ȡ������Ļ�����Ϣ\n");
		sb.append("�ظ�'2'�ɻ�ȡ������ĸ������\n");
		sb.append("�ظ�'?'�ɻ�ȡ����");
		return sb.toString();
	}

	
}
