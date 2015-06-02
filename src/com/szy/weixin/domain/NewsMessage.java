package com.szy.weixin.domain;

import java.util.ArrayList;
import java.util.List;

import com.szy.weixin.base.BaseMessage;

public class NewsMessage extends BaseMessage{
	
	private int ArticleCount;
	private List<News> Articles = new ArrayList<News>();//不要用set,因为set添加的消息显示顺序不确定
	
	public int getArticleCount() {
		return ArticleCount;
	}
	public void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
	}
	public List<News> getArticles() {
		return Articles;
	}
	public void setArticles(List<News> articles) {
		Articles = articles;
	}
	

}
