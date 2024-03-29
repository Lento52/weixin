package com.xyt.dao;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.xxcb.util.GenericHibernateDao;
import com.xyt.po.XytMessage;

@Transactional
@Component("XytMessageDao")
public class XytMessageDao extends GenericHibernateDao{
	/**
	 * 根据旅游线路查找对应的留言
	 */
	@SuppressWarnings("unchecked")
	public List<XytMessage> getXytMessageByrouteId(Integer routeId)
	{
		return this.findBySql(" from XytMessage where route_id = "+routeId+" order by rid desc", false);
	}
	
	/**
	 * 根据网红信息查找对应的留言
	 */
	@SuppressWarnings("unchecked")
	public List<XytMessage> getXytMessageByStar(Integer starId)
	{
		return this.findBySql(" from XytMessage where star = "+starId+" order by rid desc", false);
	}
	
	
	/**
	 * 根据课程查找对应的留言
	 */
	@SuppressWarnings("unchecked")
	public List<XytMessage> getXytMessageByCourse(Integer courseId)
	{
		return this.findBySql(" from XytMessage where xytCourse = "+courseId+" order by rid desc", false);
	}
	
	/**
	 * 根据学校查找对应的留言
	 */
	@SuppressWarnings("unchecked")
	public List<XytMessage> getXytMessageByCollege(Integer collegeId)
	{
		return this.findBySql(" from XytMessage where xytCollege = "+collegeId+" order by rid desc", false);
	}
	
	
	/**
	 * 根据教师查找对应的留言
	 */
	@SuppressWarnings("unchecked")
	public List<XytMessage> getXytMessageByTeacher(Integer teacherId)
	{
		return this.findBySql(" from XytMessage where xytTercherInfo = "+teacherId+" order by rid desc", false);
	}
	
	
}
