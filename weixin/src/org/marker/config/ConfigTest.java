package org.marker.config;


/**
 * 测试环境配置文件
 * @author marker
 * @date 2014年8月30日
 * @version 1.0
 * @author 李杜
 */
public interface ConfigTest {

	// 赋权类型 
	String grant_type = "client_credential";
	
	// 修改为开发者申请的appid
	String APPID      = "wxe5976fada5d432e3";
	
	String appName = "gh_2f4cc9c27365";
	
	// 修改为开发者申请的secret密钥
	String SECRET     = "5eeb956fb265c79005a362d57e73de91";
	
	//安全密钥
	String key = "05347148538346029poiuytrewqLKJHG";
	
	//商户号
	String mch_id = "1365949902";
	
	//日志目录
	String logPath = "C:/Program Files/Apache Software Foundation/Tomcat 6.0/webapps/weixin/WEB-INF/log4j.properties";
	
	//商品描述
	String body = "xyt-course";
	
	//支付回调通知地址
	String personal_notify_url = "http://222.240.171.100/weixin/xyt/XytNotifyFromWxAction!PersonalOrderNotifyFromWx.action";
	
	//团队订单支付回调通知地址
	String group_notify_url = "http://222.240.171.100/weixin/xyt/XytNotifyFromWxAction!GroupOrderNotifyFromWx.action";
	
}
