package com.tenchael.toauth.service.impl;

import static com.tenchael.toauth.commons.Settings.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	public String getLoginUrl(Integer uid) {
		String url = AUTHORIZE_URI + "?client_id=" + CLIENT_ID
				+ "&response_type=code&redirect_uri=" + REDIRECT_URI + "/"
				+ uid;
		return url;
	}

	@Override
	public void obtainBasicAccountInfo(String code, String openid, Integer uid) {
		UserDetails userDetails = userDetailsService.getByUserId(uid);
		userDetails.setCode(code);
		userDetails.setOpenid(openid);
		String url = ACCESS_TOKEN_URI;
		String parameters = "client_id=" + CLIENT_ID + "&client_secret="
				+ CLIENT_SECRET + "&redirect_uri=" + REDIRECT_URI + "/" + uid
				+ "&grant_type=authorization_code&code="
				+ userDetails.getCode();
		String respText = postUrl(url, parameters);
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
		String ip = getPubIP();
		String url = OBTAIN_USER_INFO_URL;
		String parameters = "format=json&oauth_consumer_key=" + CLIENT_ID
				+ "&access_token=" + userDetails.getAccessToken()
				+ "&redirect_uri=" + "&openid=" + userDetails.getOpenid()
				+ "&clientip=" + ip + "&oauth_version=2.a&scope=all";
		String respText = postUrl(url, parameters);
		logger.info("userInfo: " + respText);
		return respText;
	}

	@Override
	public String forwardMsg(Integer uid, String msg) {
		UserDetails userDetails = userDetailsService.getByUserId(uid);
		String ip = getPubIP();
		String url = FORWARD_MSG_URL;
		String parameters = "format=xml&content="
				+ msg
				+ "&longitude=113.421234&latitude=22.354231&syncflag=0&oauth_consumer_key="
				+ CLIENT_ID + "&access_token=" + userDetails.getAccessToken()
				+ "&openid=" + userDetails.getOpenid() + "&clientip=" + ip
				+ "&oauth_version=2.a&scope=all";
		String respText = postUrl(url, parameters);
		logger.info("forward message and response text: " + respText);
		return respText;
	}

	private static String getPubIP() {
		return "202.197.9.8";
	}

	private static String postUrl(String url, String parameters) {
		StringBuffer respInfo = new StringBuffer();
		try {
			trustAllHttpsCertificates();// 设置信任所有的http证书
			URLConnection conn = new URL(url).openConnection();
			conn.setDoOutput(true);// 这里是关键，表示我们要向链接里注入的参数
			OutputStreamWriter out = new OutputStreamWriter(
					conn.getOutputStream());// 获得连接输出流
			out.write(parameters);
			out.flush();
			out.close();
			// 到这里已经完成了，开始打印返回的HTML代码
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				respInfo.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return respInfo.toString();
	}

	private static void trustAllHttpsCertificates() {
		javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
		trustAllCerts[0] = new X509TrustManager() {
			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkServerTrusted(X509Certificate[] arg0, String arg1)
					throws CertificateException {
			}

			@Override
			public void checkClientTrusted(X509Certificate[] arg0, String arg1)
					throws CertificateException {
			}
		};
		javax.net.ssl.SSLContext sc;
		try {
			sc = javax.net.ssl.SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, null);
			javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc
					.getSocketFactory());
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

	}

}
