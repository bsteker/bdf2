package com.bstek.bdf2.wizard.utils;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtils {
	private static final Logger log = LoggerFactory.getLogger(HttpClientUtils.class);

	public static HttpClient getHttpClient() {
		return new DefaultHttpClient();
	}

	public static String getHttpRequestResult(HttpClient httpClient, String url) {
		log.debug("requestUrl:" + url);
		HttpGet httpGet = new HttpGet(url);
		httpGet.addHeader("Content-Type", "text/html;charset=UTF-8");
		HttpEntity entity = null;
		try {
			HttpResponse response = httpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				entity = response.getEntity();
				if (entity != null) {
					String result = new String(EntityUtils.toString(entity).getBytes("ISO-8859-1"), "UTF-8");
					log.debug("requestResult:" + result);
					return result;
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			EntityUtils.consumeQuietly(entity);
			httpGet.abort();
		}
		return "";
	}

	public static String getHttpRequestResult(String url) {
		return getHttpRequestResult(getHttpClient(), url);
	}

	public static void testWithProxy(HttpClient httpClient) {
		HttpHost proxy = new HttpHost("172.16.80.8", 8080);
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		UsernamePasswordCredentials creds = new UsernamePasswordCredentials("yaoman", "sinochem1");
		credsProvider.setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), creds);
		httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		((DefaultHttpClient) httpClient).setCredentialsProvider(credsProvider);
	}

}
