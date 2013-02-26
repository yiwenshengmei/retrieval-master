package com.zj.retrieval.master.actions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.RequestAware;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.zj.retrieval.master.BizNode;
import com.zj.retrieval.master.DALService;
import com.zj.retrieval.master.IDALAction;
import com.zj.retrieval.master.Node;
import com.zj.retrieval.master.NodeAttribute;
import com.zj.retrieval.master.NodeFeature;
import com.zj.retrieval.master.RetrievalDataSource;
import com.zj.retrieval.master.Utils;

public class NodeAction implements ModelDriven<Node>, RequestAware, Preparable {
	
	private Node node;
	private String id = null;
	private Map<String, Object> requestMap;
	private Map<String, Object> dataMap = new HashMap<String, Object>();
	private List<NodeFeature> newFeatures;
	public final static String ACTION_RESULT_SHOW_NODE = "showNode";
	public final static String ACTION_RESULT_JSON      = "jsonResult";
	public final static String ACTION_RESULT_ADD_SUCCESS = "addSuccess";
	private final static Logger logger = LoggerFactory.getLogger(NodeAction.class);
	
	/**************
	 * Action ���� *
	 **************/
	
	public String addRootNode() throws Exception {
		BizNode.saveAndPrepareImages(node, ServletActionContext.getServletContext().getRealPath("/images"));
		RetrievalDataSource rds = node.getRetrievalDataSource();
		rds.setNode(node);
		for (NodeAttribute attr : node.getAttributes()) {
			attr.setNode(node);
		}
		rds.getMatrix().setRetrievalDataSource(rds);
		
		DALService.doAction(new IDALAction() {
			@Override
			public Object doAction(Session sess, Transaction tx) throws Exception {
				Node virtual = (Node) sess.get(Node.class, node.VIRTUAL_NODE_ID);
				node.setParentNode(virtual);
				sess.save(node);
				return null;
			}
		});
		
//		dataMap.put("node", this.node);
		return ACTION_RESULT_JSON;
	}
	
	public String getNodesAjax() throws Exception {
		List<Node> nodes = (List<Node>) DALService.doAction(new IDALAction() {
			@Override
			public Object doAction(Session sess, Transaction tx) throws Exception {
				return sess.createQuery(
					"select nd.name from Node nd where nd.id <> '" + Node.VIRTUAL_NODE_ID + "'").list();
			}
		});
		
		dataMap.put("nodes", nodes);
		return ACTION_RESULT_JSON;
	}
	
	public String addNode() throws Exception {
		// ������ʱĿ¼�е�ͼƬ��ָ���ļ��У�ΪͼƬͳһ����������Ӧ��ͼƬ�ֶ�
		BizNode.saveAndPrepareImages(node, ServletActionContext.getServletContext().getRealPath("/images"));
		// �ڶ��һ�Ķ����һ�˽�������
		BizNode.rebuildRelation(node);
		
		RetrievalDataSource rds = node.getRetrievalDataSource();
		// �����ЩList��Ϊnull��Ԫ��
		Utils.cleanList(node.getAttributes(), rds.getFeatures(), node.getImages(), newFeatures);
		for (NodeFeature feature : rds.getFeatures()) 
			Utils.cleanList(feature.getImages());
		
		DALService.doAction(new IDALAction() {
			@Override
			public Object doAction(Session sess, Transaction tx) throws Exception {
				// ��ȡ���ڵ�
				Node parent = (Node) sess.get(Node.class, node.getParentNode().getId());
				node.setParentNode(parent);
				
				// ���¸��ڵ�
				BizNode.addChildToParent(node, parent, newFeatures);
				
				sess.save(node);
				sess.update(parent);
				return null;
			}
		});
		
		return ACTION_RESULT_ADD_SUCCESS;
	}
	
	public String getNode() throws Exception {
		Node nd = BizNode.getNode(node.getId());
		BizNode.changePath2Url(nd);
		this.requestMap.put("node_id", nd.getId());
		this.node = nd;
		return ACTION_RESULT_SHOW_NODE;
	}
	
	public String getNodeJSON() throws Exception {
		Node nd = BizNode.getNode(node.getId());
		BizNode.changePath2Url(nd);
		dataMap.put("node", nd);
		return ACTION_RESULT_JSON;
	}
	
	public String test() throws Exception {
		logger.debug("test��ִ����");
		logger.debug("node.id=" + this.node.getId());
		return ACTION_RESULT_JSON;
	}
	
	/*********************************************************/
	
	@Override
	public void prepare() throws Exception {
		if (id == null) {
			this.node = new Node();
		}
		else {
			// �����ݿ���ȡ��Node
			this.node = new Node();
			this.node.setId(id);
		}
	}

	@Override
	public Node getModel() {
		logger.debug("getModel��ִ����");
		logger.debug("node.id=" + this.node.getId());
		return this.node;
	}

	@Override
	public void setRequest(Map<String, Object> requestMap) {
		this.requestMap = requestMap;
	}
	
	public Map<String, Object> getDataMap() {
		return dataMap;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setNewFeatures(List<NodeFeature> newFeatures) {
		this.newFeatures = newFeatures;
	}
}
