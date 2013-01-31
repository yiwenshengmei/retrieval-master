package com.zj.retrieval.master;

import java.util.ArrayList;
import java.util.List;


public class RetrievalDataSource {
	private List<NodeFeature> attributes;
	private List<Node> childNodes;
	private Matrix matrix;
	private String id;
	private Node node;
	private List<NodeFeature> features;
	
	public RetrievalDataSource() { 
		attributes = new ArrayList<NodeFeature>();
		childNodes = new ArrayList<Node>();
		features = new ArrayList<NodeFeature>();
	}
	
	public RetrievalDataSource(Node node) {
		this();
		this.node = node;
	}
	
	public List<NodeFeature> getAttributes() {
		return attributes;
	}
	
	public void setAttributes(List<NodeFeature> attributes) {
		this.attributes = attributes;
	}
	
	public List<Node> getChildNodes() {
		return childNodes;
	}
	
	public void setChildNodes(List<Node> childNodes) {
		this.childNodes = childNodes;
	}
	
	public Matrix getMatrix() {
		return matrix;
	}
	public void setMatrix(Matrix matrix) {
		this.matrix = matrix;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public List<NodeFeature> getFeatures() {
		return features;
	}

	public void setFeatures(List<NodeFeature> features) {
		this.features = features;
	}
}
