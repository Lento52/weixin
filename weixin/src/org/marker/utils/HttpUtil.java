package org.marker.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.HttpClient;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jfree.util.Log;
import org.marker.config.Config;
import org.marker.config.ConfigTest;
import org.marker.config.DxjConfig;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xxcb.po.AccessToken;
import com.xxcb.util.DateUtil;
import com.xxcb.weixin.SignUtil;
import com.xyt.po.XytAccessToken;

import net.sf.json.JSONObject;
 


/**
 * 这个Https协议工具类，采用HttpsURLConnection实现。
 * 提供get和post两种请求静态方法
 * 
 * @author marker
 * @date 2014年8月30日
 * @version 1.0
 */
public class HttpUtil {
	
	
	private static TrustManager myX509TrustManager = new X509TrustManager() {

		public void checkClientTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException { 

		}

		public void checkServerTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException { 

		}

		public X509Certificate[] getAcceptedIssuers() { 
			return null;
		}

	};


	public static String sendHttpsPOST(String url, String data) {
		String result = null;

		try {
			// 设置SSLContext
			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(null, new TrustManager[] { myX509TrustManager },
					null);

			// 打开连接
			// 要发送的POST请求url?Key=Value&amp;Key2=Value2&amp;Key3=Value3的形式
			URL requestUrl = new URL(url);
			HttpsURLConnection httpsConn = (HttpsURLConnection) requestUrl
					.openConnection();

			// 设置套接工厂
			httpsConn.setSSLSocketFactory(sslcontext.getSocketFactory());

			// 加入数据
			httpsConn.setRequestMethod("POST");
			httpsConn.setDoOutput(true);
			OutputStream out = httpsConn.getOutputStream() ;
			 
			if (data != null)
				out.write(data.getBytes("UTF-8")); 
			out.flush();
			out.close();
			
			// 获取输入流
			BufferedReader in = new BufferedReader(new InputStreamReader(
					httpsConn.getInputStream(),"utf-8"));
			int code = httpsConn.getResponseCode();
			if (HttpsURLConnection.HTTP_OK == code) {
				String temp = in.readLine();
				/* 连接成一个字符串 */
				while (temp != null) {
					if (result != null)
						result += temp;
					else
						result = temp;
					temp = in.readLine();
				}
			}
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}
	
	
	
	public static String sendHttpsGET(String url) {
		String result = null;

		try {
			// 设置SSLContext
			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(null, new TrustManager[] { myX509TrustManager },
					null);

			// 打开连接
			// 要发送的POST请求url?Key=Value&amp;Key2=Value2&amp;Key3=Value3的形式
			URL requestUrl = new URL(url);
			HttpsURLConnection httpsConn = (HttpsURLConnection) requestUrl
					.openConnection();

			// 设置套接工厂
			httpsConn.setSSLSocketFactory(sslcontext.getSocketFactory());

			// 加入数据
			httpsConn.setRequestMethod("GET");
//			httpsConn.setDoOutput(true);
			  
			// 获取输入流
			BufferedReader in = new BufferedReader(new InputStreamReader(
					httpsConn.getInputStream()));
			int code = httpsConn.getResponseCode();
			if (HttpsURLConnection.HTTP_OK == code) {
				String temp = in.readLine();
				/* 连接成一个字符串 */
				while (temp != null) {
					if (result != null)
						result += temp;
					else
						result = temp;
					temp = in.readLine();
				}
			}
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}
	
	
	/**
	 * 发送https请求
	 * @param requestUrl  请求地址
	 * @param requestMethod	请求方式（GET,POST）
	 * @param outputStr	提交的数据
	 * @return	JSONObject
	 * @author  lidu
	 * @date 2015.8.3
	 */
	public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr)
	{
		JSONObject jsonObject = null;
		try{
			//使用指定的信任管理器初始化
			TrustManager [] tm = {new MyX509TrustManager()};
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			
			URL url = new URL(requestUrl);
			HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
			conn.setSSLSocketFactory(ssf);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod(requestMethod);
			
			if(null != outputStr)
			{
				OutputStream outputStream = conn.getOutputStream();
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}
			
			//从输入流读取返回结果
			InputStream inputStream = conn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			StringBuffer buffer = new StringBuffer();
			while((str = bufferedReader.readLine()) != null )
			{
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			conn.disconnect();
			jsonObject = JSONObject.fromObject(buffer.toString());
			
			
		}catch(ConnectException ce)
		{
			Log.error("连接超时：{}",ce);
		}catch(Exception e)
		{
			Log.error("https请求异常：{}",e);
		}
		return jsonObject;
	}
	
	/**
	 * 获取AccessToken
	 * @return	AccessToken
	 * @author  lidu
	 * @date 2015.8.3
	 */
	
	public static AccessToken getAccessToken()
	{
		AccessToken accessToken = null;
		String requestUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
		requestUrl = requestUrl.replace("APPID", Config.APPID).replace("APPSECRET", Config.SECRET);
		JSONObject jsonObject = HttpUtil.httpsRequest(requestUrl, "GET", null);
		if(null != jsonObject)
		{
			try
			{
				accessToken = new AccessToken();
				accessToken.setAccessToken(jsonObject.getString("access_token"));
				accessToken.setTime(new Date());
				accessToken.setName("hnzhenhaowan");
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return accessToken;
	}
	
	/**
	 * 获取XxtAccessToken
	 * @return	AccessToken
	 * @author  lidu
	 * @date 2015.8.3
	 */
	public static XytAccessToken getXytAccessToken()
	{
		XytAccessToken accessToken = null;
		String requestUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
		requestUrl = requestUrl.replace("APPID", ConfigTest.APPID).replace("APPSECRET", ConfigTest.SECRET);
		JSONObject jsonObject = HttpUtil.httpsRequest(requestUrl, "GET", null);
		if(null != jsonObject)
		{
			try
			{
				accessToken = new XytAccessToken();
				accessToken.setAccessToken(jsonObject.getString("access_token"));
				accessToken.setTime(new Date());
				accessToken.setName("hnjihaowan");
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return accessToken;
	}
	
	
	/**
	 * 获取大学伽的XxtAccessToken
	 * @return	AccessToken
	 * @author  lidu
	 * @date 2015.8.3
	 */
	public static XytAccessToken getDxjAccessToken()
	{
		XytAccessToken accessToken = null;
		String requestUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
		requestUrl = requestUrl.replace("APPID", DxjConfig.APPID).replace("APPSECRET", DxjConfig.SECRET);
		JSONObject jsonObject = HttpUtil.httpsRequest(requestUrl, "GET", null);
		if(null != jsonObject)
		{
			try
			{
				accessToken = new XytAccessToken();
				accessToken.setAccessToken(jsonObject.getString("access_token"));
				accessToken.setTime(new Date());
				accessToken.setName("dxj");
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return accessToken;
	}
	
	/**
	 * 获取prepay_id
	 * @return	AccessToken
	 * @author  lidu
	 * @date 2015.10.12
	 */
	@SuppressWarnings("null")
	public static String getPrepayId(String userid, String addrip)
	{
		//打印日志
		PropertyConfigurator.configure( "C:/Program Files/Apache Software Foundation/Tomcat 6.0/webapps/weixin/WEB-INF/log4j.properties" );
		final Logger logger  =  Logger.getLogger(HttpUtil.class );
				
		//公众账号ID
		String appid = Config.APPID;
		
		//商户号
		String mch_id = "1244003502";
		
		//设备号
		String device_info = "WEB";
		
		//随机字符串
		String nonce_str = String.valueOf(Math.random());
		
		//商品描述(待完善)
		String body = "1yuanmiaosha";
		
		//商品详情(待完善)
		String detail = "miaosha";
		
		//附加数据(待完善)
		String attach = "detail";
		
		//商户订单号
		String out_trade_no = HttpUtil.GenerateTradeNo();
		
		//货币类型  
		String fee_type = "CNY";
		
		//总金额(待完善)
		int total_fee = 1;
		
		//终端IP
		String spbill_create_ip = addrip;
		
		//交易起始时间(待完善)
		String time_start = DateUtil.getNowTime().replace("-", "").replace(" ", "").replace(":", "");
		
		//交易结束时间(待完善)
		String time_expire = String.valueOf(Long.valueOf(time_start)+(long)320);
		
		//商品标记(待完善)
		String goods_tag = "XXCB";
		
		//通知地址(待完善)
		String notify_url ="http://www.baidu.com";
		
		//交易类型
		String trade_type ="JSAPI";
		
		//商品ID(待完善)
		String product_id = "12235413214070356458058";
		
		//指定支付方式
		String limit_pay = "no_credit";
		
		//用户标识
		String openid = userid;
		
		//签名(最后获取)
		String []array = {"appid=".concat(appid), "mch_id=".concat(mch_id), "device_info=".concat(device_info),
				"nonce_str=".concat(nonce_str), "body=".concat(body), "detail=".concat(detail),  
				"attach=".concat(attach), "out_trade_no=".concat(out_trade_no), "fee_type=".concat(fee_type), 
				"total_fee=".concat(String.valueOf(total_fee)), 
				"spbill_create_ip=".concat(spbill_create_ip), "time_start=".concat(time_start), "time_expire=".concat(time_expire), 
				"goods_tag=".concat(goods_tag), "notify_url=".concat(notify_url), "trade_type=".concat(trade_type),
				"product_id=".concat(product_id), "limit_pay=".concat(limit_pay), "openid=".concat(openid)};
		
		//对数组进行字典排序
		SignUtil.sort(array);
		
		//组合成stringA
		String stringA = new String() ;
		
		String stringSignTemp = new String();
		
		String sign = new String();
		
		String xmlStr = new String();
		
		String prepay_id = new String();
		
		String key = "05347148538346029poiuytrewqLKJHG";
		
		for(int i = 0 ; i < array.length ; i++)
		{
			stringA = stringA.concat(array[i].concat("&"));
		}
		
		stringSignTemp = stringA.concat("key=").concat(key);
		
		//对stringSignTemp进行MD5加密
		MySecurity mySecurity = new MySecurity();
		
		sign = mySecurity.encode(stringSignTemp, "MD5");
		
		//将sign所有的字母换成大写  得到sign
		sign = sign.toUpperCase();
		logger.error("sign ="+sign);
		Map<String, String> paraMap = new HashMap<String, String>();
		
		paraMap.put("appid", appid);
		paraMap.put("attach", attach);
		paraMap.put("body", body);
		paraMap.put("detail", detail);
		paraMap.put("device_info", device_info);
		paraMap.put("fee_type", fee_type);
		paraMap.put("goods_tag", goods_tag);
		paraMap.put("limit_pay", limit_pay);
		paraMap.put("mch_id", mch_id);
		paraMap.put("nonce_str", nonce_str);
		paraMap.put("notify_url", notify_url);
		paraMap.put("openid", openid);
		paraMap.put("out_trade_no", out_trade_no);
		paraMap.put("product_id", product_id);
		paraMap.put("spbill_create_ip", spbill_create_ip);
		paraMap.put("time_expire", time_expire);
		paraMap.put("time_start", time_start);
		paraMap.put("total_fee", String.valueOf(total_fee));
		paraMap.put("trade_type", trade_type);
		paraMap.put("sign", sign);
		
		String requestUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		String xml = ArrayToXml(paraMap);
		//System.out.println(" xml 为"+ xml);
		xmlStr = HttpUtil.sendHttpsPOST(requestUrl, xml);
		logger.error("xmlStr ="+xmlStr);
		//转换编码格式
		String start = "<prepay_id><![CDATA[";
		String end = "]]></prepay_id>";
		prepay_id = xmlStr.substring(xmlStr.indexOf(start)+start.length(), xmlStr.indexOf(end));
		logger.error("prepay_id ="+prepay_id);
		return prepay_id;
	}
	
	/**
	 * 根据当前系统时间加随机序列来生成订单号
	 * 
	 */
	public static String GenerateTradeNo()
	{
		String nonce_str = String.valueOf(Math.random());
		String timeStamp = String.valueOf(System.currentTimeMillis()/1000);
		String tradeNo = "xxcb".concat(timeStamp).concat(nonce_str.substring(2));
		return tradeNo;
	}
	
	
	
	/**
	 * map转成xml
	 * 
	 * @param arr
	 * @return
	 */
	public static String ArrayToXml(Map<String, String> arr) {
		String xml = "<xml>";

		Iterator<Entry<String, String>> iter = arr.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, String> entry = iter.next();
			String key = entry.getKey();
			String val = entry.getValue();
			xml += "<" + key + ">" + val + "</" + key + ">";
		}

		xml += "</xml>";
		return xml;
	}
	
	private static Map<String, String> doXMLParse(String xml)
			throws  IOException {

		InputStream inputStream = new ByteArrayInputStream(xml.getBytes());

		Map<String, String> map = null;

		XmlPullParser pullParser = null;
		try {
			pullParser = XmlPullParserFactory.newInstance()
					.newPullParser();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			pullParser.setInput(inputStream, "UTF-8");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 为xml设置要解析的xml数据

		int eventType = 0;
		try {
			eventType = pullParser.getEventType();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				map = new HashMap<String, String>();
				break;

			case XmlPullParser.START_TAG:
				String key = pullParser.getName();
				if (key.equals("xml"))
					break;

				String value = null;
				try {
					value = pullParser.nextText();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				map.put(key, value);

				break;

			case XmlPullParser.END_TAG:
				break;

			}

			try {
				eventType = pullParser.next();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return map;
	}
	
	
	public static Map<String, String> transStringToMap(String mapString){  
		  Map<String, String> map = new HashMap();  
		  java.util.StringTokenizer items;  
		  for(StringTokenizer entrys = new StringTokenizer(mapString, "^");entrys.hasMoreTokens();   
		    map.put(items.nextToken(), (String) (items.hasMoreTokens() ? ((Object) (items.nextToken())) : null)))  
		      items = new StringTokenizer(entrys.nextToken(), "'");  
		  return map;  
	}  
	
	public static String uploadImage(String url, String access_token,String type, File file)
	{
		org.apache.commons.httpclient.HttpClient client = new org.apache.commons.httpclient.HttpClient();
		String uploadurl = String.format("%s?access_token=%s&type=%s", url,access_token, type);
		PostMethod post = new PostMethod(uploadurl);
		post.setRequestHeader("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:30.0) Gecko/20100101 Firefox/30.0");
		post.setRequestHeader("Host", "file.api.weixin.qq.com");
		post.setRequestHeader("Cache-Control", "no-cache");
		String result = null;
		try{
			if (file != null && file.exists())
			{
				FilePart filepart = new FilePart("media", file, "image/jpeg","UTF-8");
				Part[] parts = new Part[] { filepart };
				MultipartRequestEntity entity = new MultipartRequestEntity(parts,post.getParams());
				post.setRequestEntity(entity);
				int status = client.executeMethod(post);
				if (status == HttpStatus.SC_OK)
				{
					String responseContent = post.getResponseBodyAsString();
					JsonParser jsonparer = new JsonParser();// 初始化解析json格式的对象
					JsonObject json = jsonparer.parse(responseContent).getAsJsonObject();
					if (json.get("errcode") == null)
					{
						result = json.get("media_id").getAsString();
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
}
