package com.zj.retrieval.master.actions;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ModelDriven;
import com.zj.retrieval.master.Node;
import com.zj.retrieval.master.NodeFeature;

public class NodeAction implements ModelDriven<Node> {
	
	private final static Logger logger = LoggerFactory.getLogger(NodeAction.class);
	private Map<String, Object> dataMap = new HashMap<String, Object>();
	private Node node;
	
	private void saveImageFiles(File[] files, String[] fileNames, File folder, String msg) throws IOException {
		if (files == null) return;
		for (int i = 0; i < files.length; i++) {
			File destFile = new File(folder, UUID.randomUUID().toString() + ".jpg");
			FileUtils.copyFile(files[i], destFile);
			logger.debug(String.format("%1$s, %2$s -> %3$s", msg, fileNames[i], destFile));
		}
	}
	
	private void saveImages() throws IOException {
		String realpath = ServletActionContext.getServletContext().getRealPath("/images");
		File folder = new File(realpath);
		if(!folder.exists()) {
			folder.mkdirs();
			logger.debug("用于保存图片的文件夹不存在，开始创建: " + folder.getPath());
		}
		
		saveImageFiles(node.getImageFiles(), node.getImageFilesFileName(), folder, "Save a NodeImage");
		
		if (node.getRetrievalDataSource() != null && node.getRetrievalDataSource().getFeatures() != null) {
			for (NodeFeature feature : node.getRetrievalDataSource().getFeatures()) {
				saveImageFiles(feature.getImageFiles(), feature.getImageFilesFileName(), folder, "Save a NodeFeatureImage");
			}
		}
	}
	
	public String add() throws IOException {
		saveImages();
		dataMap.put("node", this.node);
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
