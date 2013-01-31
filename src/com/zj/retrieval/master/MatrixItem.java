package com.zj.retrieval.master;

public class MatrixItem {
	private Integer value;
	private MatrixRow row;
	private String id;
	
	public MatrixItem() { }
	
	public MatrixItem(Integer item) {
		this();
		this.value = item;
	}

	public MatrixRow getRow() {
		return row;
	}

	public void setRow(MatrixRow row) {
		this.row = row;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
