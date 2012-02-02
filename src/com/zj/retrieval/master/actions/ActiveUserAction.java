package com.zj.retrieval.master.actions;

import com.opensymphony.xwork2.ActionSupport;
import com.zj.retrieval.master.Util;
import com.zj.retrieval.master.dao.UserDao;

public class ActiveUserAction {
	private String id;
	private boolean isError;
	private String message;
	
	public String execute() {
		try {
			UserDao userDao = Util.getUserDao();
			userDao.activeUser(id);
			
			this.isError = false;
			this.message = "¼¤»î³É¹¦o(¡É_¡É)o...";
			return ActionSupport.SUCCESS;
		} catch (Exception ex) {
			this.isError = true;
			this.message = ex.getMessage();
			return ActionSupport.ERROR;
		}
	}

	public boolean getIsError() {
		return isError;
	}

	public String getMessage() {
		return message;
	}

	public void setId(String id) {
		this.id = id;
	}
}
