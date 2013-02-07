package com.zj.retrieval.master;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Node {
	private static Logger logger = LoggerFactory.getLogger(Node.class);

	public final static String VIRTUAL_NODE_ID = "VIRTUAL_NODE";

	private String uri;
	private String id;
	private String name;
	private String uriName;
	private String englishName;
	private String desc;
	private RetrievalDataSource retrievalDataSource;
	private String label;
	private String authorContact;
	private List<NodeImage> images;
	private List<NodeAttribute> attributes;
	private List<Node> childNodes;
	private Node parentNode;

	public Node() {
		retrievalDataSource = new RetrievalDataSource(this);
		images = new ArrayList<NodeImage>();
		childNodes = new ArrayList<Node>();
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
}
