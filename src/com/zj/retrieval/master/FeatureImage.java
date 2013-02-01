package com.zj.retrieval.master;


public class FeatureImage {
	private String id;
	private String path;
	private String nodeId;
	private NodeFeature feature;
	
	public FeatureImage(String path, NodeFeature feature) {
		this.path = path;
		this.feature = feature;
	}
	
	public FeatureImage() {}

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

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public NodeFeature getFeature() {
		return feature;
	}

	public void setFeature(NodeFeature feature) {
		this.feature = feature;
	}
}
