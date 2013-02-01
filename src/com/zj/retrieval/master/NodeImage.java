package com.zj.retrieval.master;


public class NodeImage {
	private String id;
	private String path;
	private Node node;
	
	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public NodeImage(String path) {
		this(path, null);
	}
	
	public NodeImage(String path, Node node) {
		this.path = path;
		this.node = node;
	}
	
	public NodeImage() {}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
}
