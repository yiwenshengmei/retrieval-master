package com.zj.retrieval.master;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

public abstract class AbstractAction {
	
	private final static Logger logger = LoggerFactory.getLogger(AbstractAction.class);
	protected String errorMessage;
	protected String message;
	
	public String execute() {
		try {
			doExecute();
			this.message = getSuccessfulMesssage();
			return getActionResult();
		}
		catch (Throwable ex) {
			logger.error("", ex);
			this.errorMessage = ex.getMessage();
			return ActionSupport.ERROR;
		}
	}
	
	protected abstract void doExecute() throws Exception;
	
	protected abstract String getSuccessfulMesssage();
	
	protected abstract String getActionResult();

	public String getErrorMessage() {
		return this.errorMessage;
	}
	
	public String getMessage() {
		return this.message;
	}
}
