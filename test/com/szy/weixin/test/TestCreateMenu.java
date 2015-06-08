package com.szy.weixin.test;

import java.util.Date;

import org.junit.Test;

import com.szy.weixin.domain.AccessToken;
import com.szy.weixin.util.WeiXinUtils;

public class TestCreateMenu {

	public static void main(String[] args){
		WeiXinUtils.createMenu();
	}
	   
	@Test
	public void test1(){
		long date1 = new Date().getTime();
		long date2 = new Date(System.currentTimeMillis()+7200000).getTime();
		System.out.println(date1);
		System.out.println(date2);
		System.out.println(date2-date1);
	}
	
	@Test
	public void test2(){
		AccessToken accessToken = WeiXinUtils.getAccessToken();
		System.out.println(accessToken.getAccess_token());
		System.out.println(accessToken.getCreateTime());
	}
	       
}   
