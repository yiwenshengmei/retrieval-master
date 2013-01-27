package com.zj.retrieval.master;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class RetrievalDataSource {
	// key����������֤�е��кţ�value������кŶ�Ӧ����������
	private Set<NodeFeature> attributes;
	// key�������������е��кţ�value�����ӽ�������ݿ��е�id
	private List<String> childNodes;
	private Matrix matrix;
	private String id;
	private String headerId;
	private Node node;
	private Set<NodeFeature> features;
	
	public RetrievalDataSource() {
		attributes = new HashSet<NodeFeature>();
		childNodes = new ArrayList<String>();
		matrix = new Matrix();
		matrix.setHeaderId(this.id);
	}
	
	public Set<NodeFeature> getAttributes() {
		return attributes;
	}
	public void setAttributes(Set<NodeFeature> attributes) {
		this.attributes = attributes;
	}
	public List<String> getChildNodes() {
		return childNodes;
	}
	public void setChildNodes(List<String> childNodes) {
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

	public String getHeaderId() {
		return headerId;
	}

	public void setHeaderId(String headerId) {
		this.headerId = headerId;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public Set<NodeFeature> getFeatures() {
		return features;
	}

	public void setFeatures(Set<NodeFeature> features) {
		this.features = features;
	}
}
