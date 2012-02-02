package com.zj.retrieval.master;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.zj.retrieval.master.dao.NodeDao;
import com.zj.retrieval.master.dao.UserDao;

public class Util {
	public static ApplicationContext applicationContext = null;
	
	public static ApplicationContext getApplicationContext() {
		if (applicationContext == null) {
			ServletContext servletCtx = ServletActionContext.getServletContext();
			WebApplicationContext springCtx = WebApplicationContextUtils.getWebApplicationContext(servletCtx);
			return springCtx;
		} else {
			return applicationContext;
		}
	}
	
	public static UserDao getUserDao() {
		ApplicationContext ctx = getApplicationContext();
		return (UserDao) ctx.getBean("userService");
	}
	
	public static NodeDao getNodeDao() {
		ApplicationContext ctx = getApplicationContext();
		return (NodeDao) ctx.getBean("nodeService");
	}
	
	public static NodeRetrieval getNodeRetrieval() {
		ApplicationContext ctx = getApplicationContext();
		return (NodeRetrieval) ctx.getBean("nodeRetrieval");
	}
}
