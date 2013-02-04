package com.zj.retrieval.master;

import java.util.ArrayList;
import java.util.List;

public class MatrixRow {
	private List<MatrixItem> items;
	private Matrix matrix;
	private String id;
	
	public MatrixRow() { }
	
	public MatrixRow(List<Integer> values, Matrix matrix) {
		items = new ArrayList<MatrixItem>();
		for (Integer v : values) {
			items.add(new MatrixItem(v, this));
		}
		this.matrix = matrix;
	}
	
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
