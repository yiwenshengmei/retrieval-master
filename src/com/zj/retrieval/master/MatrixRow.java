package com.zj.retrieval.master;

import java.util.List;

public class MatrixRow {
	private List<MatrixItem> items;
	private Matrix matrix;
	private String id;
	
	private MatrixRow() { }

	public List<MatrixItem> getItems() {
		return items;
	}

	public void setItems(List<MatrixItem> items) {
		this.items = items;
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
}
