package com.xyt.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.xxcb.util.BaseAction;
import com.xyt.dao.PmessageDao;
import com.xyt.dao.UserAttantionDao;
import com.xyt.dao.XytScoreInfoDao;
import com.xyt.dao.XytUserInfoDao;
import com.xyt.po.UserAttantion;
import com.xyt.po.XytScoreInfo;
import com.xyt.po.XytUserInfo;
import com.xyt.util.GsonUtil;

@Scope("prototype")
@Component("XytUserInfoAction")
public class XytUserInfoAction extends BaseAction {

	
	private static final long serialVersionUID = 3499163941358654615L;
	
	private Integer userId;
	private Integer hisUserId;
	
	@Resource
	public XytUserInfoDao xytUserInfoDao;
	@Resource
	private UserAttantionDao userAttantionDao;
	@Resource
	private PmessageDao pmessageDao;
	@Resource
	private XytScoreInfoDao xytScoreInfoDao;
	
	
	public String getXytUserInfoByRid()
	{
		Integer userid = Integer.valueOf(this.getRequest().getParameter("userid"));
		List<XytUserInfo> listXytUserInfo = xytUserInfoDao.getXytUserInfoByRid(userid);
		this.showjsondata(GsonUtil.toJson(listXytUserInfo));
		return null;
	}
	
	public String getXytUserInfoByCollegeRid()
	{
		Integer xytCollegeRid = Integer.valueOf(this.getRequest().getParameter("xytCollegerRid"));
		List<XytUserInfo> listXytUserInfo = xytUserInfoDao.getXytUserInfoByCollegeRid(xytCollegeRid);
		this.showjsondata(GsonUtil.toJson(listXytUserInfo));
		return null;
	}
	
	
	/**
	 * 显示我的个人中心
	 * @return
	 */
	public String showMyHome() {
		
		if(userId == null) {
			getRequest().setAttribute("errorMsg", "用户不存在！");
			return "error";
		}
		
		XytUserInfo user = (XytUserInfo)xytUserInfoDao.findByID(XytUserInfo.class, userId);
		if(user == null) {
			getRequest().setAttribute("errorMsg", "用户不存在！");
			return "error";
		}
		
		//通过累加方式获取积分值
		Integer score = 0;
		List<XytScoreInfo> listXytScoreInfo = xytScoreInfoDao.getXytScoreInfoByUserId(userId);
		if(0 < listXytScoreInfo.size())
		{
			for(int index = 0 ; index < listXytScoreInfo.size(); index++)
			{
				XytScoreInfo xytScoreInfo = listXytScoreInfo.get(index);
				score = score + xytScoreInfo.getScoreOption();
			}
		}
		
		getRequest().setAttribute("user", user);
		getRequest().setAttribute("unReadCount", pmessageDao.findUnReadCount(userId));
		getRequest().setAttribute("friendsCount", userAttantionDao.findFriendsCount(user));
		getRequest().setAttribute("fansCount", userAttantionDao.findFansCount(user));
		getRequest().setAttribute("score", score);
		return "showMyHome";
	}
	
	
	/**
	 * 显示他的个人中心
	 * @return
	 */
	public String showHisHome() {
		
		if(userId == null) {
			getRequest().setAttribute("errorMsg", "用户不存在！");
			return "error";
		}
		
		XytUserInfo user = (XytUserInfo)xytUserInfoDao.findByID(XytUserInfo.class, userId);
		if(user == null) {
			getRequest().setAttribute("errorMsg", "用户不存在！");
			return "error";
		}
		
		if(hisUserId == null) {
			getRequest().setAttribute("errorMsg", "用户不存在！");
			return "error";
		}
		
		XytUserInfo ta = (XytUserInfo)xytUserInfoDao.findByID(XytUserInfo.class, hisUserId);
		if(ta == null) {
			getRequest().setAttribute("errorMsg", "用户不存在！");
			return "error";
		}
		
		boolean attantioned = false;
		UserAttantion userAttantion = userAttantionDao.findByUserAndAttantionUser(user, ta);
		if(userAttantion != null) {
			attantioned = true;
		}
		
		getRequest().setAttribute("user", user);
		getRequest().setAttribute("ta", ta);
		getRequest().setAttribute("attantioned", attantioned);
		getRequest().setAttribute("friendsCount", userAttantionDao.findFriendsCount(ta));
		getRequest().setAttribute("fansCount", userAttantionDao.findFansCount(ta));
		
		return "showHisHome";
	}
	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getHisUserId() {
		return hisUserId;
	}

	public void setHisUserId(Integer hisUserId) {
		this.hisUserId = hisUserId;
	}
	
	
}
