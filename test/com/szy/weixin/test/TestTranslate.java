package com.szy.weixin.test;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import com.szy.weixin.util.WeiXinUtils;

public class TestTranslate {

	@Test
	public void test1() throws UnsupportedEncodingException{
		WeiXinUtils.translate("×ãÇò");
	}
	
	@Test
	public void test2() throws UnsupportedEncodingException{
		WeiXinUtils.translate("hello");
	}
	
	@Test
	public void test3() throws UnsupportedEncodingException{
		WeiXinUtils.translateFull("ÖÐ¹ú×ãÇò");
	}
	
}
