package com.zj.retrieval.master.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zj.retrieval.master.BizNode;
import com.zj.retrieval.master.Matrix;
import com.zj.retrieval.master.Node;
import com.zj.retrieval.master.NodeFeature;

public class MatrixTests {
	
	private final static Logger logger = LoggerFactory.getLogger(MatrixTests.class);
	
	@Test
	public void testAddNodeToParent() {
		// ���ڵ�
		Node parent = new Node("���ڵ�����");
		// ���ڵ�����ֵ
		List<List<Integer>> mtxValue = new ArrayList<List<Integer>>(Arrays.asList(
			new ArrayList<Integer>(Arrays.asList(2, 2, 2)),
			new ArrayList<Integer>(Arrays.asList(2, 2, 1)),
			new ArrayList<Integer>(Arrays.asList(2, 1, 1))
		));
		Matrix mtx = new Matrix(mtxValue);
		parent.getRetrievalDataSource().setMatrix(mtx);
		
		// ���ڵ������
		List<NodeFeature> features = new ArrayList<NodeFeature>(Arrays.asList(
			new NodeFeature("feature1", "1"),
			new NodeFeature("featrue2", "2"),
			new NodeFeature("feature3", "3")
		));
		parent.getRetrievalDataSource().setFeatures(features);
		
		// ���ڵ���ӽڵ��б�
		parent.getChildNodes().addAll(new ArrayList<Node>(Arrays.asList(
			new Node("n1"),
			new Node("n2"),
			new Node("n3")
		)));
		
		// �ӽڵ�
		Node child = new Node("newNode");
		// �ӽڵ�ӵ�еĸ��ڵ�����
		child.setFeaturesOfParent(new ArrayList<NodeFeature>(Arrays.asList(
			features.get(0),
			features.get(1))
		));
		// �ӽڵ������������
		List<NodeFeature> newFeatures = new ArrayList<NodeFeature>(Arrays.asList(
			new NodeFeature("feature4", "4"),
			new NodeFeature("feature5", "5")
		));
		
		logger.debug("����ǰ��" + parent.toString());
		// ���Կ�ʼ
		BizNode.addChildToParent(child, parent, newFeatures);
		logger.debug("���º�" + parent.toString());
	}
}
