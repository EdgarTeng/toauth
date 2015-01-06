package com.tenchael.toauth.commons;

import java.util.Properties;

public class Settings {

	public static String AUTHORIZE_URI;
	public static String ACCESS_TOKEN_URI;
	public static String OBTAIN_USER_INFO_URL;
	public static String FORWARD_MSG_URL;
	public static String CLIENT_ID;
	public static String CLIENT_SECRET;
	public static String REDIRECT_URI;

	public final static String LOGINED_USER = "loginedUser";

	private Properties configProperties;

	public String getProperty(final String property) throws NotFoundException {
		String retVal = configProperties.getProperty(property);
		if (retVal == null) {
			throw new NotFoundException("Property not found: " + property);
		}
		return retVal;
	}

	public Properties getConfigProperties() {
		return configProperties;
	}

	public void setConfigProperties(final Properties configProperties) {
		this.configProperties = configProperties;

		try {
			Settings.AUTHORIZE_URI = this
					.getProperty("com.tencent.weibo.authorize_uri");
			Settings.ACCESS_TOKEN_URI = this
					.getProperty("com.tencent.weibo.access_token_uri");
			Settings.OBTAIN_USER_INFO_URL = this
					.getProperty("com.tencent.weibo.obtain_user_info_url");
			Settings.FORWARD_MSG_URL = this
					.getProperty("com.tencent.weibo.forward_msg_url");
			Settings.CLIENT_ID = this
					.getProperty("com.tencent.weibo.client_id");
			Settings.CLIENT_SECRET = this
					.getProperty("com.tencent.weibo.client_secret");
			Settings.REDIRECT_URI = this
					.getProperty("com.tencent.weibo.redirect_uri");
		} catch (NotFoundException e) {
			e.printStackTrace();
		}

	}
}
