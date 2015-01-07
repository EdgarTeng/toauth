package com.tenchael.toauth.service.impl;

import static com.tenchael.toauth.commons.Settings.ACCESS_TOKEN_URI;
import static com.tenchael.toauth.commons.Settings.AUTHORIZE_URI;
import static com.tenchael.toauth.commons.Settings.CLIENT_ID;
import static com.tenchael.toauth.commons.Settings.CLIENT_SECRET;
import static com.tenchael.toauth.commons.Settings.FORWARD_MSG_URL;
import static com.tenchael.toauth.commons.Settings.OBTAIN_USER_INFO_URL;
import static com.tenchael.toauth.commons.Settings.PUBLIC_IP_ADDRESS;
import static com.tenchael.toauth.commons.Settings.REDIRECT_URI;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tenchael.toauth.commons.HttpUtils;
import com.tenchael.toauth.domain.UserDetails;
import com.tenchael.toauth.service.AuthService;
import com.tenchael.toauth.service.UserDetailsService;

@Service
public class AuthServiceImpl implements AuthService {

	private static final Logger logger = Logger
			.getLogger(AuthServiceImpl.class);

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	public String generateThirdPartLoginAddr(Integer uid) {
		String url = AUTHORIZE_URI;

		Map<String, String> params = new HashMap<String, String>();
		params.put("client_id", CLIENT_ID);
		params.put("response_type", "code");
		params.put("redirect_uri", REDIRECT_URI + "/" + uid);

		String loginUrl = HttpUtils.jointParams(url, params);
		logger.info("loginUrl=" + loginUrl);
		return loginUrl;
	}

	@Override
	public void storeBasicThirdPartInfo(String code, String openid,
			String openkey, Integer uid) {
		UserDetails userDetails = userDetailsService.getByUserId(uid);
		userDetails.setCode(code);
		userDetails.setOpenid(openid);
		userDetails.setOpenkey(openkey);
		String url = ACCESS_TOKEN_URI;

		Map<String, String> params = new HashMap<String, String>();
		params.put("client_id", CLIENT_ID);
		params.put("client_secret", CLIENT_SECRET);
		params.put("redirect_uri", REDIRECT_URI + "/" + uid);
		params.put("grant_type", "authorization_code");
		params.put("code", userDetails.getCode());

		String respText = null;
		try {
			respText = HttpUtils.postRequest(url, params);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		logger.info(respText);
		String[] tmp = respText.toString().split("&");
		String[] token = tmp[0].split("=");

		userDetails.setAccessToken(token[1]);
		token = tmp[2].split("=");

		userDetails.setRefreshToken(token[1]);
		userDetailsService.update(userDetails);
	}

	@Override
	public String getUserInfo(Integer uid) {
		UserDetails userDetails = userDetailsService.getByUserId(uid);
		String url = OBTAIN_USER_INFO_URL;

		Map<String, String> params = new HashMap<String, String>();
		params.put("format", "json");
		params.put("oauth_consumer_key", CLIENT_ID);
		params.put("access_token", userDetails.getAccessToken());
		params.put("openid", userDetails.getOpenid());
		params.put("clientip", PUBLIC_IP_ADDRESS);
		params.put("redirect_uri", "");
		params.put("oauth_version", "2.a");
		params.put("scope", "all");

		String respText = null;
		try {
			respText = HttpUtils.postRequest(url, params);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		logger.info("userInfo: " + respText);
		return respText;
	}

	@Override
	public String forwardMessage(Integer uid, String msg) {
		UserDetails userDetails = userDetailsService.getByUserId(uid);
		String url = FORWARD_MSG_URL;

		Map<String, String> params = new HashMap<String, String>();
		params.put("content", msg);
		params.put("access_token", userDetails.getAccessToken());
		params.put("openid", userDetails.getOpenid());
		params.put("clientip", PUBLIC_IP_ADDRESS);
		params.put("format", "json");
		params.put("longitude", "113.421234");
		params.put("latitude", "22.354231");
		params.put("syncflag", "0");
		params.put("oauth_consumer_key", CLIENT_ID);
		params.put("oauth_version", "2.a");
		params.put("scope", "all");

		String respText = null;
		try {
			respText = HttpUtils.postRequest(url, params);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		logger.info("forward message and response text: " + respText);
		return respText;
	}

}
