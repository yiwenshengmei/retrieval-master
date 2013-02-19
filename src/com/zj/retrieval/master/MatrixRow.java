package com.zj.retrieval.master;

import java.util.ArrayList;
import java.util.List;

public class MatrixRow {
	private List<MatrixItem> items;
	private Matrix matrix;
	private String id;
	
	public MatrixRow() { 
		items = new ArrayList<MatrixItem>();
	}
	
	public MatrixRow(List<Integer> values, Matrix matrix) {
		this();
		for (Integer v : values) {
			items.add(new MatrixItem(v, this));
		}
		this.matrix = matrix;
	}
	
	public List<Integer> getValueList() {
		List<Integer> ret = new ArrayList<Integer>();
		for (MatrixItem item : items) {
			ret.add(item.getValue());
		}
		return ret;
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
