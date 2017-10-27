package com.gt.cscity.planning.utils;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author frankiewei.
 * Json封装的工具类.
 */
public class MyJSONUtil {
	/**
	 * 获取json内容
	 * @param  url
	 * @return JSONArray
	 * @throws JSONException
	 * @throws ConnectionException
	 */
	public static String getJSON(String url,String parameter) throws JSONException, Exception {
		return getRequest(url,parameter);
	}

	/**
	 * 向api发送get请求，返回从后台取得的信息。
	 *
	 * @param url
	 * @param client
	 * @return String
	 */
	protected static String getRequest(String url,String parameter) throws Exception {

		String result = null;
		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestMethod("POST");
		conn.setUseCaches(false);
		conn.setInstanceFollowRedirects(true);
		conn.setConnectTimeout(30 * 1000);
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		conn.setRequestProperty("Connection", "Keep-Alive");
		conn.setRequestProperty("Charset", "UTF-8");
		try {
			//getMethod.setHeader("User-Agent", USER_AGENT);
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			dos.write(parameter.getBytes("UTF-8"));
			dos.flush();
			dos.close();

			// 获得服务器端输出流
			if (conn.getResponseCode() == 200) {
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						conn.getInputStream(), "utf-8"));
				StringBuffer stringBuffer = new StringBuffer(); 
				/*InputStreamReader rd = new InputStreamReader(
						conn.getInputStream(), "utf-8");
				StringBuffer stringBuffer = new StringBuffer(); 
				char buffer[] = new char[10000];  
	            int count;  
	            while ((count = rd.read(buffer, 0, 10000 - 1)) > 0) {  
	                stringBuffer.append(buffer, 0, count);  
	            }*/
				String lines;

				while( (lines = rd.readLine())!=null ){
					stringBuffer.append( lines );
				}
				rd.close();
				result = stringBuffer.toString();
			} else {

			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
		}
		return result;
	}


	public static int getNetWork(String url){

		HttpURLConnection conn = null;
		int responseCode = -1;
		try {
			conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setUseCaches(false);
			conn.setInstanceFollowRedirects(true);
			conn.setConnectTimeout(30 * 1000);
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "UTF-8");
			if(conn!=null)
				responseCode = conn.getResponseCode();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return responseCode;
	}
}
