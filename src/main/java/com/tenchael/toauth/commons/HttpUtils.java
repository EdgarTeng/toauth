package com.tenchael.toauth.commons;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpUtils {

	public static String jointParams(String url, Map<String, String> params) {
		StringBuffer buf = new StringBuffer(url);
		// 拼接参数
		Set<String> keys = params.keySet();
		if (keys != null && !keys.isEmpty()) {
			buf.append("?");
			for (String key : keys) {
				String value = params.get(key);
				buf.append(key + "=" + value + "&");
			}
			buf.deleteCharAt(buf.lastIndexOf("&"));
		}
		return buf.toString();
	}

	public static String getReqeuest(String url, Map<String, String> params)
			throws ClientProtocolException, IOException {
		StringBuffer buf = new StringBuffer(url);
		// 拼接参数
		Set<String> keys = params.keySet();
		if (keys != null && !keys.isEmpty()) {
			buf.append("?");
			for (String key : keys) {
				String value = params.get(key);
				buf.append(key + "=" + value + "&");
			}
			buf.deleteCharAt(buf.lastIndexOf("&"));
		}

		// 发送请求
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(buf.toString());
		// Create a custom response handler
		ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
			public String handleResponse(final HttpResponse response)
					throws ClientProtocolException, IOException {
				int status = response.getStatusLine().getStatusCode();
				if (status >= 200 && status < 300) {
					HttpEntity entity = response.getEntity();
					return entity != null ? EntityUtils.toString(entity) : null;
				} else {
					throw new ClientProtocolException(
							"Unexpected response status: " + status);
				}
			}

		};
		return httpclient.execute(httpget, responseHandler);
	}

	public static String postRequest(String url, Map<String, String> params)
			throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();

		Set<String> keys = params.keySet();
		if (keys != null && !keys.isEmpty()) {
			for (String name : keys) {
				String value = params.get(name);
				nvps.add(new BasicNameValuePair(name, value));
			}
		}

		httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
		ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
			@Override
			public String handleResponse(HttpResponse response)
					throws ClientProtocolException, IOException {
				int status = response.getStatusLine().getStatusCode();
				if (status >= 200 && status < 300) {
					HttpEntity entity = response.getEntity();
					return entity != null ? EntityUtils.toString(entity) : null;
				} else {
					throw new ClientProtocolException(
							"Unexpected response status: " + status);
				}
			}
		};
		return httpclient.execute(httpPost, responseHandler);
	}

}
