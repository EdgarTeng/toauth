package com.tenchael.toauth.service;

public interface AuthService {

	String getLoginUrl(Integer uid);

	void obtainBasicAccountInfo(String code, String openid,Integer userId);

	String getUserInfo(Integer userId);

	String forwardMsg(Integer userId, String msg);

}
