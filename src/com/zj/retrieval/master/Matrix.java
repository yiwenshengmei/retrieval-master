package com.zj.retrieval.master;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

public class Matrix {
	private String id;
	private List<MatrixRow> rows;
	private RetrievalDataSource retrievalDataSource;

	public Matrix() {
		rows = new ArrayList<MatrixRow>();
	}
	
	public Matrix(List<List<Integer>> values) {
		rows = new ArrayList<MatrixRow>();
		for (List<Integer> row : values) {
			rows.add(new MatrixRow(row, this));
		}
	}
	
	public MatrixRow getRow(int rowIndex) {
		return rows.get(rowIndex);
	}
	
	public void addCol(List<MatrixItem> items) {
		if (items.size() != getRowSize())
			throw new IllegalArgumentException(String.format("新列的行数不正确，应该为%1$s，传入的是%2$s", getRowSize(), items.size()));
		
		int rowSize = getRowSize();
		for (int i = 0; i < rowSize; i++) {
			items.get(i).setRow(getRow(i));
			getRow(i).addItem(items.get(i));
		}
	}
	
	public void addRow(MatrixRow row) {
		int newRowColSize = row.getItems().size();
		int oldRowColSize = getColSize();
		List<MatrixRow> oldRows = new ArrayList<MatrixRow>();
		oldRows.addAll(rows);
		
		if (newRowColSize == 0) 
			return;
		if (newRowColSize < oldRowColSize) 
			throw new IllegalArgumentException("新MatrixRow的列数不能小于Matrix的列数");
		
		rows.add(row);
		
		int diff = newRowColSize - oldRowColSize;
		// 如果新增的行的列数比现有的列数多，则需要把现有的列数扩充（填充Unknow）
		extendColumns(diff, NodeFeature.UNKNOW, oldRows);
	}
	
	private void extendColumns(int size, int value, List<MatrixRow> rows) {
		if (size > 0) {
			for (MatrixRow row : rows) {
				// 注意：每行都要单独new一组列
				List<MatrixItem> extendedCols = new ArrayList<MatrixItem>();
				for (int i = 0; i < size; i++)
					extendedCols.add(MatrixItem.Unknow(row));
				row.getItems().addAll(extendedCols);
			}
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
		List<String> rowStrs = new ArrayList<String>();
		for (MatrixRow row : rows) {
			rowStrs.add(ArrayUtils.toString(row.getItems()));
		}
		return "\n" + StringUtils.join(rowStrs, "\n");
	}
	
	public String toTextString() {
		List<String> rowStrs = new ArrayList<String>();
		List<String> items = new ArrayList<String>();
		for (MatrixRow row : rows) {
			for (MatrixItem item : row.getItems()) {
				items.add(item.getTextValue());
			}
			rowStrs.add("[" + StringUtils.join(items, ", ") + "]");
		}
		return "\n" + StringUtils.join(rowStrs, "\n");
	}
	
	public String toShortTextString() {
		List<String> rowStrs = new ArrayList<String>();
		List<String> items = new ArrayList<String>();
		for (MatrixRow row : rows) {
			items.clear();
			for (MatrixItem item : row.getItems()) {
				items.add(item.getShortTextValue());
			}
			rowStrs.add("[" + StringUtils.join(items, "  ") + "]");
		}
		return "\n" + StringUtils.join(rowStrs, "\n");
	}
	
	public MatrixRow removeRow(int row) {
		return this.rows.remove(row);
	}
	
	public List<MatrixItem> removeCol(int col) {
		List<MatrixItem> items = new ArrayList<MatrixItem>();
		if (col > getColSize() || col < 0)
			throw new IllegalArgumentException("要删除的列不在矩阵的列范围内");
		for (MatrixRow row : rows) {
			items.add(row.getItems().remove(col));
		}
		return items;
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
