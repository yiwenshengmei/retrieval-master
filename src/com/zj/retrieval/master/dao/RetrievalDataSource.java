package com.zj.retrieval.master.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.zj.retrieval.master.NodeAttribute;
import com.zj.retrieval.master.Matrix;

public class RetrievalDataSource {
	// key����������֤�е��кţ�value������кŶ�Ӧ����������
	private List<NodeAttribute> attributes;
	// key�������������е��кţ�value�����ӽ�������ݿ��е�id
	private List<String> childNodes;
	private Matrix matrix;
	private String id;
	private String headerId;
	
	public RetrievalDataSource() {
		this.id = UUID.randomUUID().toString();
		attributes = new ArrayList<NodeAttribute>();
		childNodes = new ArrayList<String>();
		matrix = new Matrix();
		matrix.setHeaderId(this.id);
	}
	
	public List<NodeAttribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<NodeAttribute> attributes) {
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
}
