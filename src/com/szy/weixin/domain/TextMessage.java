package com.szy.weixin.domain;

import com.szy.weixin.base.BaseMessage;

public class TextMessage extends BaseMessage{

	private String Content;
	
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	
	
}
