package com.zj.retrieval.master;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

public class Matrix {
	private String id;
	private List<MatrixRow> rows;
	private RetrievalDataSource retrievalDataSource;

	public Matrix() { }
	
	public void addRow(MatrixRow row, int index) {
		int newRowColSize = row.getItems().size();
		int oldRowColSize = getColSize();
		
		if (newRowColSize == 0) 
			return;
		if (newRowColSize < oldRowColSize) 
			throw new IllegalArgumentException("新MatrixRow的列数不能小于Matrix的列数");
		
		rows.add(row);
		
		int diff = newRowColSize - oldRowColSize;
		// 如果新增的行的列数比现有的列数多，则需要把现有的列数扩充（填充0）
		if (diff > 0) {
			List<MatrixItem> newCols = new ArrayList<MatrixItem>();
			for (int i = 0; i < diff; i++)
				newCols.add(new MatrixItem(0));
			for (MatrixRow oldRow : rows) 
				oldRow.getItems().addAll(newCols);
		}
	}
	
	public void setItem(int row, int col, MatrixItem item) {
		this.rows.get(row).getItems().set(col, item);
	}
	
	public List<Integer> getCol(int col) {
		List<Integer> ret = new ArrayList<Integer>();
		for (MatrixRow row : rows) 
			ret.add(row.getItems().get(col).getValue());
		return ret;
	}
	
	public MatrixItem getItem(int row, int col) {
		return this.rows.get(row).getItems().get(col);
	}

	public int getRowSize() {
		return this.rows.size();
	}

	public int getColSize() {
		return rows.size() == 0 ? 0 : rows.get(0).getItems().size();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Matrix: \n");
		for (MatrixRow row : rows) {
			sb.append(ArrayUtils.toString(row.getItems()));
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public void removeRow(int row) {
		this.rows.remove(row);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<MatrixRow> getRows() {
		return rows;
	}

	public void setRows(List<MatrixRow> rows) {
		this.rows = rows;
	}

	public RetrievalDataSource getRetrievalDataSource() {
		return retrievalDataSource;
	}

	public void setRetrievalDataSource(RetrievalDataSource retrievalDataSource) {
		this.retrievalDataSource = retrievalDataSource;
	}
}
