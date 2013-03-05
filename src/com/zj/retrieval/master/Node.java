package com.zj.retrieval.master;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Node {
	private static Logger logger = LoggerFactory.getLogger(Node.class);

	public final static String VIRTUAL_NODE_ID = "VIRTUAL_NODE";

	private String id;
	private String uri;
	private String owl;
	private String name;
	private String desc;
	private String label;
	private String uriName;
	private String englishName;
	private String authorContact;
	private Node parentNode;
	private RetrievalDataSource retrievalDataSource;
	private List<Node> childNodes;
	private List<NodeImage> images;
	private List<NodeAttribute> attributes;
	private List<NodeFeature> featuresOfParent;
	private String[] imageFilesFileName;
	private String[] imageFilesContentType;
	private File[] imageFiles;

	public Node() {
		retrievalDataSource = new RetrievalDataSource(this);
		images = new ArrayList<NodeImage>();
		childNodes = new ArrayList<Node>();
		featuresOfParent = new ArrayList<NodeFeature>();
		attributes = new ArrayList<NodeAttribute>();
	}
	
	public Node(String name, String desc, String id) {
		this();
		this.name = name;
		this.desc = desc;
		this.id = id;
	}
	
	public Node(String name) {
		this();
		this.name = name;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUriName() {
		return uriName;
	}

	public void setUriName(String uriName) {
		this.uriName = uriName;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String enName) {
		this.englishName = enName;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public RetrievalDataSource getRetrievalDataSource() {
		return retrievalDataSource;
	}

	public void setRetrievalDataSource(RetrievalDataSource retrievalDataSource) {
		this.retrievalDataSource = retrievalDataSource;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<NodeImage> getImages() {
		return images;
	}

	public void setImages(List<NodeImage> image) {
		this.images = image;
	}

	public List<NodeAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<NodeAttribute> attributes) {
		this.attributes = attributes;
	}

	public List<Node> getChildNodes() {
		return childNodes;
	}

	public void setChildNodes(List<Node> childNodes) {
		this.childNodes = childNodes;
	}

	public Node getParentNode() {
		return parentNode;
	}

	public void setParentNode(Node parentNode) {
		this.parentNode = parentNode;
	}

	public String getAuthorContact() {
		return authorContact;
	}

	public void setAuthorContact(String authorContact) {
		this.authorContact = authorContact;
	}

	public File[] getImageFiles() {
		return imageFiles;
	}

	public void setImageFiles(File[] imageFiles) {
		this.imageFiles = imageFiles;
	}

	public String[] getImageFilesFileName() {
		return imageFilesFileName;
	}

	public void setImageFilesFileName(String[] imageFilesFileName) {
		this.imageFilesFileName = imageFilesFileName;
	}

	public String[] getImageFilesContentType() {
		return imageFilesContentType;
	}

	public void setImageFilesContentType(String[] imageFilesContentType) {
		this.imageFilesContentType = imageFilesContentType;
	}

	public String getOwl() {
		return owl;
	}

	public void setOwl(String owl) {
		this.owl = owl;
	}
	
	public List<NodeFeature> getFeaturesOfParent() {
		return featuresOfParent;
	}

	public void setFeaturesOfParent(List<NodeFeature> featuresOfParent) {
		this.featuresOfParent = featuresOfParent;
	}

	@Override
	public String toString() {
		Matrix mtx = getRetrievalDataSource().getMatrix();
		List<MatrixRow> rows = mtx.getRows();
		List<Node> childs = getChildNodes();
		List<NodeFeature> features = getRetrievalDataSource().getFeatures();
		if (rows.size() != childs.size())
			throw new IllegalArgumentException("矩阵行数和子节点数目不相符，不能调用Node.toString方法！");
		if (features.size() != mtx.getColSize())
			throw new IllegalArgumentException("矩阵列数和特征数目不相符，不能调用Node.toString方法！");
		
		StringBuilder str = new StringBuilder();
		str.append("\n\nName: ").append(getName());
		// 输出Features
		List<String> featureNames = new ArrayList<String>();
		for (NodeFeature feature : features) {
			featureNames.add(feature.getName());
		}
		str.append("\nFeatures: [").append(StringUtils.join(featureNames, ", ")).append("]");
		// 输出子节点
		List<String> childNames = new ArrayList<String>();
		for (Node child : getChildNodes()) {
			childNames.add(child.getName());
		}
		str.append("\nChilds: [").append(StringUtils.join(childNames, ", ")).append("]");
		// 输出矩阵
		str.append("\nMatrix: ").append(mtx.toShortTextString());
		str.append("\n");
		return str.toString();
	}
	
}
