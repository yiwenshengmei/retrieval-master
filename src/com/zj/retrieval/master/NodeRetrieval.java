package com.zj.retrieval.master;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NodeRetrieval {
	private Node retrievalNode;
	private List<Integer> mappingRows;
	private boolean hasInited = false;
	
	public NodeRetrieval(Node retrievalNode) {
		this.retrievalNode = retrievalNode;
		this.mappingRows = new ArrayList<Integer>();
		initMappingRows();
	}
	
	private void initMappingRows() {
		// ��ʼ����ʱ????matchRows�����е�ֵ����matrix�����е��к�
		if (hasInited)
			return;
		if (mappingRows == null)
			mappingRows = new ArrayList<Integer>();
		// ��ΪNodeRetrieval�ǵ�����????ÿ�ζ�Ҫ�����״??
		mappingRows.clear();
		for (int i = 0; i < retrievalNode.getRetrievalDataSource().getChildNodes().size(); i++) {
			mappingRows.add(i);
		}
		hasInited = true;
	}
	
	public NodeRetrieval() {}
	
	public RetrievalResult retrieval(String selectState) {
		// selectState�ĵ�????����retrievalNode��id�����±�1????����answer
		for (int i = 1; i < selectState.length(); i++) {
			int perAnswer = Integer.valueOf(selectState.substring(i, i + 1));
			// ����ش�unknown���ֿձ��Σ�����ܽ�����????�𰸳��ֶ��
			if (perAnswer == Attribute.UNKNOW)
				continue;
			Matrix matrix = retrievalNode.getRetrievalDataSource().getMatrix();
			Iterator<Integer> iter = mappingRows.iterator();
			// i��Ӧ��selectState�жԵ�i�������Ļش�ͬʱ��Ӧ��matrix�е�i-1??
			int[] currComparedAttrCol = matrix.getCol(i - 1);
			while (iter.hasNext()) {
				if (currComparedAttrCol[iter.next()] == perAnswer)
					continue;
				else
					iter.remove();
			}
		}
		// ����????�жϸ÷���ʲ??
		RetrievalResult result = new RetrievalResult();
		if ((selectState.length() - 1) >= retrievalNode.getRetrievalDataSource().getAttributes().size()
				|| mappingRows.size() <= 1) {
			// 1.????��Ȼ�������������������������ѯ�ʹ�
			// 2.���ܻ�û��ѯ�ʹ�ȫ���������������Ѿ�ȷ��û��ƥ����ӽ��??
			// 3.ƥ����ӽ���б���ֻʣ��????Ԫ��??
			result.hasResult(true);
			result.setResult(getMappedNode());
		} else {
			// ????��û����ɣ�������һ????ѯ�ʵ���??
			result.hasResult(false);
			int nextAttributeId = selectState.length() - 1;
			result.setNext(retrievalNode.getRetrievalDataSource().getAttributes().get(nextAttributeId));
		}
		result.setLastState(selectState);
		return result;
	}
	
	public void setRetrievalNode(Node retrievalNode) {
		this.retrievalNode = retrievalNode;
		initMappingRows();
	}

	private List<String> getMappedNode() {
		List<String> mappedNodes = new ArrayList<String>();
		for (int mappedRow : mappingRows) {
			String mappedNodeId = retrievalNode.getRetrievalDataSource().getChildNodes().get(mappedRow);
			mappedNodes.add(mappedNodeId);
		}
		return mappedNodes;
	}
}