package com.adhub.utils;

import java.io.IOException;

import android.util.Log;
import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.HttpVersion;
import ch.boye.httpclientandroidlib.client.ClientProtocolException;
import ch.boye.httpclientandroidlib.client.HttpClient;
import ch.boye.httpclientandroidlib.client.methods.HttpGet;
import ch.boye.httpclientandroidlib.client.methods.HttpPost;
import ch.boye.httpclientandroidlib.client.params.HttpClientParams;
import ch.boye.httpclientandroidlib.conn.params.ConnManagerParams;
import ch.boye.httpclientandroidlib.conn.params.ConnPerRouteBean;
import ch.boye.httpclientandroidlib.conn.scheme.PlainSocketFactory;
import ch.boye.httpclientandroidlib.conn.scheme.Scheme;
import ch.boye.httpclientandroidlib.conn.scheme.SchemeRegistry;
import ch.boye.httpclientandroidlib.entity.StringEntity;
import ch.boye.httpclientandroidlib.impl.client.DefaultHttpClient;
import ch.boye.httpclientandroidlib.impl.conn.tsccm.ThreadSafeClientConnManager;
import ch.boye.httpclientandroidlib.params.BasicHttpParams;
import ch.boye.httpclientandroidlib.params.HttpConnectionParams;
import ch.boye.httpclientandroidlib.params.HttpParams;
import ch.boye.httpclientandroidlib.params.HttpProtocolParams;
import ch.boye.httpclientandroidlib.protocol.HTTP;

import com.google.gson.Gson;

public class HttpAction {

	private static DefaultHttpClient httpGetClient;

	public static HttpEntity GET(String url) throws ClientProtocolException, IOException {
		
		Log.e("url", "xxx " + url);
		
		HttpResponse httpResponse;
		HttpGet httpGet;
		HttpEntity entity;

		if (httpGetClient == null)
			httpGetClient = createHttpClient();

		httpGet = new HttpGet(url);

		// httpResponse = httpGetClient.execute(httpGet);
		httpResponse = httpGetClient.execute(httpGet);

		return httpResponse.getEntity();

	}

	public static HttpEntity POST(String url, Object objeto) throws ClientProtocolException, IOException {

		Gson gsonPost;
		HttpPost postRequest;
		StringEntity inputPost;
		HttpResponse response;
		HttpEntity entity;
		HttpClient httpClient = createHttpClient();

		postRequest = new HttpPost(url);
		gsonPost = new Gson();

		inputPost = new StringEntity(gsonPost.toJson(objeto), "UTF-8");
		inputPost.setContentType("application/json");
		postRequest.setEntity(inputPost);

		response = httpClient.execute(postRequest);
		/*
		 * try{ entity = response.getEntity(); } finally {
		 * postRequest.releaseConnection(); }
		 */

		return response.getEntity();

	}

	private static DefaultHttpClient createHttpClient() {
		HttpParams params = new BasicHttpParams();
		// params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
		// HttpVersion.HTTP_1_1);

		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

		HttpConnectionParams.setConnectionTimeout(params, 10000);
		HttpConnectionParams.setSoTimeout(params, 10000);

		ConnManagerParams.setMaxTotalConnections(params, 6);
		ConnManagerParams.setMaxConnectionsPerRoute(params, new ConnPerRouteBean(6));

		HttpConnectionParams.setTcpNoDelay(params, true);
		HttpConnectionParams.setStaleCheckingEnabled(params, false);
		HttpProtocolParams.setUseExpectContinue(params, false);
		HttpClientParams.setRedirecting(params, false);
		// HttpClientParams.setCookiePolicy(params, "IGNORE_COOKIES");
		// HttpProtocolParams.setUserAgent(params, "Android client");

		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		registry.register(new Scheme("https", PlainSocketFactory.getSocketFactory(), 80));
		ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(params, registry);

		return new DefaultHttpClient(manager, params);
	}
}
