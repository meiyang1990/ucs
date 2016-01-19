/**
 * 
 */
package com.netfinworks.basis.inf.ucs.mqlistener.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * MNS发送工具
 * 
 * @author bigknife
 * 
 */
public class MNSUtil {
	private String mnsMsgNotifyUrl = "http://10.65.178.20:8080/services/rest/msgNotify";
	private Logger logger = LoggerFactory.getLogger(getClass());
	private String mnsAppId;
	private HttpClient httpClient;
	private String emailTitle = "微汇验证码邮件";

	private int timeoutMs = 1000;// socket timeout ，默认1分钟

	public void init() {
		PoolingClientConnectionManager manager = new PoolingClientConnectionManager();
		httpClient = new DefaultHttpClient(manager);
		
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				timeoutMs);
		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, timeoutMs);

	}

	/**
	 * @param emailTitle
	 *            the emailTitle to set
	 */
	public void setEmailTitle(String emailTitle) {
		this.emailTitle = emailTitle;
	}

	/**
	 * @param mnsAppId
	 *            the mnsAppId to set
	 */
	public void setMnsAppId(String mnsAppId) {
		this.mnsAppId = mnsAppId;
	}

	/**
	 * @param timeoutMs
	 *            the timeoutMs to set
	 */
	public void setTimeoutMs(int timeoutMs) {
		this.timeoutMs = timeoutMs;
	}

	/**
	 * @param mnsMsgNotifyUrl
	 *            the mnsMsgNotifyUrl to set
	 */
	public void setMnsMsgNotifyUrl(String mnsMsgNotifyUrl) {
		this.mnsMsgNotifyUrl = mnsMsgNotifyUrl;
	}

	/**
	 * 发送短信
	 * 
	 * @param msg
	 */
	public void sendSms(String phoneNos, String msg) {
		HttpPost post = newHttpPost();

		// 拼装参数
		List<NameValuePair> parameter = buildSmsParameters(phoneNos, msg);

		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameter,
					"UTF-8");
			post.setEntity(entity);
			String response = httpClient.execute(post,
					new BasicResponseHandler());
			logger.info(
					"向 {} 发送短信：{}, 接口返回 {}",
					new Object[] {
							phoneNos,
							msg,
							new String(response.getBytes("iso8859_1"),
									"UTF-8") });
		} catch (Exception e) {
			logger.error("发送短信失败", e);
			throw new RuntimeException(e);
		}

	}

	/**
	 * 发邮件
	 * 
	 * @param emails
	 * @param msg
	 */
	public void sendEmail(String emails, String msg) {
		HttpPost post = newHttpPost();
		// 拼装参数
		List<NameValuePair> parameter = buildEmailParameters(emails, msg);

		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameter,
					"UTF-8");
			post.setEntity(entity);

			String response = httpClient.execute(post,
					new BasicResponseHandler());
			logger.info(
					"向 {} 发送邮件：{}, 接口返回 {}",
					new Object[] {
							emails,
							msg,
							new String(response.getBytes("iso8859_1"),
									"UTF-8") });
		} catch (Exception e) {
			logger.error("发送邮件失败", e);
			throw new RuntimeException(e);
		}
	}

	private HttpPost newHttpPost() {
		HttpPost post = new HttpPost(mnsMsgNotifyUrl);

		post.setHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=utf-8");
		post.setHeader("Accept", "application/json");
		post.setHeader("Accept-Encoding", "UTF-8");

		return post;
	}

	private List<NameValuePair> buildSmsParameters(String phoneNos, String msg) {
		List<NameValuePair> nvp = buildParameters(phoneNos, msg);
		nvp.add(new BasicNameValuePair("protocol", "S"));
		nvp.add(new BasicNameValuePair("content", msg));
		return nvp;
	}

	private List<NameValuePair> buildEmailParameters(String emails, String msg) {
		List<NameValuePair> nvp = buildParameters(emails, msg);
		nvp.add(new BasicNameValuePair("protocol", "M"));

		nvp.add(new BasicNameValuePair("content", msg));
		nvp.add(new BasicNameValuePair("subject", emailTitle));

		return nvp;
	}

	private List<NameValuePair> buildParameters(String targets, String msg) {
		List<NameValuePair> parameter = new ArrayList<NameValuePair>();
		parameter.add(new BasicNameValuePair("appId", mnsAppId));
		parameter.add(new BasicNameValuePair("orderNo", UUID.randomUUID()
				.toString().replace("-", "")));

		parameter.add(new BasicNameValuePair("targetIdenty", targets));
		parameter.add(new BasicNameValuePair("targetCount", String
				.valueOf(targets.split(",").length)));
		parameter.add(new BasicNameValuePair("isRealTime", "true"));

		return parameter;
	}
}
