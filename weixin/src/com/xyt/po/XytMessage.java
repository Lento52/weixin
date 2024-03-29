package com.xyt.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.dxj.po.DxjSuperStar;
import com.dxj.po.DxjTravelRoute;

/**
 * 校园通留言信息
 */
@Entity
@Table(name = "XytMessage")
public class XytMessage {
	private Integer rid; //id
	
	//创建时间
	private Date createDate;
	
	//留言内容
	private String content;
	
	//学生信息
	private XytUserInfo xytUserInfo;
	
	//是否显示
	private boolean isShow;
	
	//课程
	private XytCourse xytCourse;
	
	//教师
	private XytTercherInfo xytTercherInfo;
	
	//是否匿名留言
	private boolean isAnonymity;
	
	//学校留言
	private XytCollege xytCollege;
	
	//网红留言
	private DxjSuperStar star;
	
	//旅游线路留言
	private DxjTravelRoute route;

	@Id
	@GeneratedValue(generator = "XytMessage_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "XytMessage_seq", sequenceName = "s_xytMessage")
	public Integer getRid() {
		return rid;
	}

	public void setRid(Integer rid) {
		this.rid = rid;
	}

	@Column(nullable = true)
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(nullable = true)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true)
	public XytUserInfo getXytUserInfo() {
		return xytUserInfo;
	}

	public void setXytUserInfo(XytUserInfo xytUserInfo) {
		this.xytUserInfo = xytUserInfo;
	}

	@Column(nullable = true)
	public boolean isShow() {
		return isShow;
	}

	public void setShow(boolean isShow) {
		this.isShow = isShow;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true)
	public XytCourse getXytCourse() {
		return xytCourse;
	}

	public void setXytCourse(XytCourse xytCourse) {
		this.xytCourse = xytCourse;
	}

	@JoinColumn(nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	public XytTercherInfo getXytTercherInfo() {
		return xytTercherInfo;
	}

	public void setXytTercherInfo(XytTercherInfo xytTercherInfo) {
		this.xytTercherInfo = xytTercherInfo;
	}
	
	@Column(nullable = false)
	public boolean isAnonymity() {
		return isAnonymity;
	}

	public void setAnonymity(boolean isAnonymity) {
		this.isAnonymity = isAnonymity;
	}

	@JoinColumn(nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	public XytCollege getXytCollege() {
		return xytCollege;
	}

	public void setXytCollege(XytCollege xytCollege) {
		this.xytCollege = xytCollege;
	}

	@JoinColumn(nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	public DxjSuperStar getStar() {
		return star;
	}

	public void setStar(DxjSuperStar star) {
		this.star = star;
	}

	@JoinColumn(nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	public DxjTravelRoute getRoute() {
		return route;
	}

	public void setRoute(DxjTravelRoute route) {
		this.route = route;
	}
	
	
	
}
