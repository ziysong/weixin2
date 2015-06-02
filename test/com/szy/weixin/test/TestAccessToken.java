package com.szy.weixin.test;

import org.junit.Test;

import com.szy.weixin.domain.AccessToken;
import com.szy.weixin.util.WeiXinUtils;

//≤‚ ‘ªÒ»°AccessToken
public class TestAccessToken {

	public static void main(String[] args){
		AccessToken accessToken = WeiXinUtils.getAccessToken();
		System.out.println(accessToken.getAccess_token());
		System.out.println(accessToken.getExpires_in());
	}
	
}
