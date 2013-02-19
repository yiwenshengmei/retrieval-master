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
import org.apache.struts2.json.JSONUtil;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ModelDriven;
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

public class NodeAction implements ModelDriven<Node> {
	
	private final static Logger logger = LoggerFactory.getLogger(NodeAction.class);
	private Map<String, Object> dataMap = new HashMap<String, Object>();
	private Node node;
	
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
	
	private void initialize(Node node) {
		Hibernate.initialize(node.getChildNodes());
		Hibernate.initialize(node.getImages());
		Hibernate.initialize(node.getParentNode());
		Hibernate.initialize(node.getAttributes());
		RetrievalDataSource rds = node.getRetrievalDataSource();
		Hibernate.initialize(rds.getMatrix());
		for (NodeFeature feature : rds.getFeatures()) {
			Hibernate.initialize(feature.getImages());
		}
		for (MatrixRow row : rds.getMatrix().getRows()) {
			Hibernate.initialize(row.getItems());
		}
	}
	
	private Node doGetNode() throws Exception {
		Node nd = (Node) DALService.doAction(new IDALAction() {
			@Override
			public Object doAction(Session sess, Transaction tx) throws Exception {
				Node nd = (Node) sess.get(Node.class, node.getId());
				initialize(nd);
				return nd;
			}
		});
		return nd;
	}
	
	/**
	 * 
	 * @return 转向 show_node.jsp
	 * @throws Exception
	 */
	public String getNode() throws Exception {
		Node nd = doGetNode();
		this.node = nd;
		return ACTION_RESULT_SHOW_NODE;
	}
	
	/**
	 * 
	 * @return 输出Node的JSON结果
	 * @throws Exception
	 */
	public String getNodeJSON() throws Exception {
		Node nd = doGetNode();
//		deleteParentRelation(nd);
		dataMap.put("node", nd);
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
		return "jsonRet";
	}

	@Override
	public Node getModel() {
		this.node = new Node();
		return this.node;
	}

	public Map<String, Object> getDataMap() {
		return dataMap;
	}
}
