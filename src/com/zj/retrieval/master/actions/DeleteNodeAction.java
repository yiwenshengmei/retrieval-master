package com.zj.retrieval.master.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.opensymphony.xwork2.ActionSupport;
import com.zj.retrieval.master.Node;
import com.zj.retrieval.master.Util;
import com.zj.retrieval.master.dao.NodeService;

public class DeleteNodeAction {

	private static Log log = LogFactory.getLog(AddNodeAction.class);
	private String node_id;
	private String message;
	private boolean isError;

	public String execute() {
		try {
			Node nd = new Node();
			nd.setId(node_id);
			log.info("要删除的节点id为：" + nd.getId());
			
			NodeService ndService =  Util.getNodeService();
			
			if (ndService.deleteNode(nd)) {
				this.isError = false;
				this.message = "Success, o(∩_∩)o...";
				return ActionSupport.SUCCESS;
			} else {
				this.isError = true;
				this.message = "Fail.";
				return ActionSupport.ERROR;
			}
			
		} catch (Exception ex) {
			log.error("在删除结点时发生未知错误", ex);
			this.isError = true;
			this.message = "Fail.";
			return ActionSupport.ERROR;
		}
	}
	public String getMessage() {
		return message;
	}
	public void setNode_id(String node_id) {
		this.node_id = node_id;
	}
	public boolean getIsError() {
		return this.isError;
	}

}
