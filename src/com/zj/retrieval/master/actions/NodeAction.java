package com.zj.retrieval.master.actions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.json.JSONUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.zj.retrieval.master.BizNode;
import com.zj.retrieval.master.DALService;
import com.zj.retrieval.master.FeatureImage;
import com.zj.retrieval.master.IDALAction;
import com.zj.retrieval.master.MatrixItem;
import com.zj.retrieval.master.MatrixRow;
import com.zj.retrieval.master.Node;
import com.zj.retrieval.master.NodeAttribute;
import com.zj.retrieval.master.NodeFeature;
import com.zj.retrieval.master.NodeImage;
import com.zj.retrieval.master.RetrievalDataSource;

public class NodeAction implements ModelDriven<Node>, RequestAware, Preparable {
	
	private Node node;
	private String id = null;
	private Map<String, Object> requestMap;
	private Map<String, Object> dataMap = new HashMap<String, Object>();
	private final static Logger logger = LoggerFactory.getLogger(NodeAction.class);
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public final static String ACTION_RESULT_SHOW_NODE = "showNode";
	public final static String ACTION_RESULT_JSON      = "jsonRet";
	
	private List<String> saveImageFiles(File[] files, String[] fileNames, File folder, String msg) throws IOException {
		List<String> paths = new ArrayList<String>();
		if (files == null) return paths;
		for (int i = 0; i < files.length; i++) {
			File destFile = new File(folder, UUID.randomUUID().toString() + ".jpg");
			FileUtils.copyFile(files[i], destFile);
			logger.debug(String.format("%1$s, %2$s -> %3$s", msg, fileNames[i], destFile));
			paths.add(destFile.getPath());
		}
		return paths;
	}
	
	private void processImages() throws IOException {
		String realpath = ServletActionContext.getServletContext().getRealPath("/images");
		File folder = new File(realpath);
		if(!folder.exists()) {
			folder.mkdirs();
			logger.debug("用于保存图片的文件夹不存在，开始创建: " + folder.getPath());
		}
		
		List<String> paths = saveImageFiles(node.getImageFiles(), node.getImageFilesFileName(), folder, "Save a NodeImage");
		node.setImages(NodeImage.batchCreate(paths, node));
		
		if (node.getRetrievalDataSource() != null && node.getRetrievalDataSource().getFeatures() != null) {
			for (NodeFeature feature : node.getRetrievalDataSource().getFeatures()) {
				List<String> featureImagePaths = saveImageFiles(feature.getImageFiles(), 
						feature.getImageFilesFileName(), folder, "Save a NodeFeatureImage");
				feature.setImages(FeatureImage.batchCreate(featureImagePaths, feature));
			}
		}
	}
	
	private void deleteParentRelation(Node node) {
		node.getRetrievalDataSource().setNode(null);
		node.setParentNode(null);
		node.getRetrievalDataSource().getMatrix().setRetrievalDataSource(null);
		
		for (NodeAttribute attr : node.getAttributes())
			attr.setNode(null);
		for (Node child : node.getChildNodes()) 
			child.setParentNode(null);
		for (NodeImage image : node.getImages()) 
			image.setNode(null);
		for (NodeFeature feature : node.getRetrievalDataSource().getFeatures())
			feature.setRetrievalDataSource(null);
		for (MatrixRow row : node.getRetrievalDataSource().getMatrix().getRows()) {
			row.setMatrix(null);
			for (MatrixItem item : row.getItems())
				item.setRow(null);
		}
	}
	
	/**
	 * Action方法：添加非root节点
	 * @return
	 */
	public String addNode() {
		logger.debug("features数量: " + this.node.getRetrievalDataSource().getFeatures().size());
		this.dataMap.put("node", this.node);
		return ACTION_RESULT_JSON;
	}
	
	/**
	 * 
	 * @return 转向 view_node_detail.jsp
	 * @throws Exception
	 */
	public String getNode() throws Exception {
		Node nd = BizNode.getNode(node.getId());
		BizNode.changePath2Url(nd);
		this.requestMap.put("node_id", nd.getId());
		this.node = nd;
		return ACTION_RESULT_SHOW_NODE;
	}
	
	/**
	 * 
	 * @return 输出Node的JSON结果
	 * @throws Exception
	 */
	public String getNodeJSON() throws Exception {
		Node nd = BizNode.getNode(node.getId());
		BizNode.changePath2Url(nd);
		dataMap.put("node", nd);
		return ACTION_RESULT_JSON;
	}
	
	public String test() throws Exception {
		logger.debug("test被执行了");
		logger.debug("node.id=" + this.node.getId());
		return ACTION_RESULT_JSON;
	}
	
	/**	
	 * 添加根节点
	 * @return
	 * @throws Exception
	 */
	public String addRootNode() throws Exception {
		logger.debug("Param: " + JSONUtil.serialize(this.node));
		processImages();
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

	@Override
	public Node getModel() {
		logger.debug("getModel被执行了");
		logger.debug("node.id=" + this.node.getId());
		return this.node;
	}
	
	public Map<String, Object> getDataMap() {
		return dataMap;
	}

	@Override
	public void setRequest(Map<String, Object> requestMap) {
		this.requestMap = requestMap;
	}

	@Override
	public void prepare() throws Exception {
		logger.debug("prepare被执行了");
		logger.debug("id=" + this.id);
		if (id == null) {
			this.node = new Node();
		}
		else {
			this.node = new Node();
			this.node.setId(id);
		}
	}

}
