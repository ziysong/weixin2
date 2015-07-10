package com.szy.weixin.test;

import java.io.IOException;

import com.szy.weixin.util.WeiXinUtils;

public class TestDetectFace {

	public static void main(String[] args) throws IOException{
		WeiXinUtils.detectFace3("http://ziysong.tunnel.mobi/weixin2/image/soccer.jpg",null);
	}
	
}
