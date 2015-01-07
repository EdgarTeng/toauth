package com.tenchael.toauth.service;

public interface AuthService {

	String generateThirdPartLoginAddr(Integer uid);

	void storeBasicThirdPartInfo(String code, String openid, String openkey,
			Integer userId);

	String getUserInfo(Integer userId);

	String forwardMessage(Integer userId, String msg);

}
