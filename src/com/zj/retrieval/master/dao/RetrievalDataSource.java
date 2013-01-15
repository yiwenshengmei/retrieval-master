package com.zj.retrieval.master.dao;

import java.util.ArrayList;
import java.util.List;

import com.zj.retrieval.master.NodeAttribute;
import com.zj.retrieval.master.Matrix;

public class RetrievalDataSource {
	// key代表特征举证中的列号，value代表该列号对应的特征对象
	private List<NodeAttribute> attributes;
	// key代表特征矩阵中的行号，value代表子结点在数据库中的id
	private List<String> childNodes;
	private Matrix matrix;
	
	public RetrievalDataSource() {
		attributes = new ArrayList<NodeAttribute>();
		childNodes = new ArrayList<String>();
		matrix = new Matrix();
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
}
