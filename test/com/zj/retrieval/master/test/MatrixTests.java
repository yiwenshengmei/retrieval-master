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
		// 父节点
		Node parent = new Node("父节点名称");
		// 父节点矩阵的值
		List<List<Integer>> mtxValue = new ArrayList<List<Integer>>(Arrays.asList(
			new ArrayList<Integer>(Arrays.asList(2, 2, 2)),
			new ArrayList<Integer>(Arrays.asList(2, 2, 1)),
			new ArrayList<Integer>(Arrays.asList(2, 1, 1))
		));
		Matrix mtx = new Matrix(mtxValue);
		parent.getRetrievalDataSource().setMatrix(mtx);
		
		// 父节点的特征
		List<NodeFeature> features = new ArrayList<NodeFeature>(Arrays.asList(
			new NodeFeature("feature1", "1"),
			new NodeFeature("featrue2", "2"),
			new NodeFeature("feature3", "3")
		));
		parent.getRetrievalDataSource().setFeatures(features);
		
		// 父节点的子节点列表
		parent.getChildNodes().addAll(new ArrayList<Node>(Arrays.asList(
			new Node("n1"),
			new Node("n2"),
			new Node("n3")
		)));
		
		// 子节点
		Node child = new Node("newNode");
		// 子节点拥有的父节点属性
		child.setFeaturesOfParent(new ArrayList<NodeFeature>(Arrays.asList(
			features.get(0),
			features.get(1))
		));
		// 子节点带来的新特征
		List<NodeFeature> newFeatures = new ArrayList<NodeFeature>(Arrays.asList(
			new NodeFeature("feature4", "4"),
			new NodeFeature("feature5", "5")
		));
		
		logger.debug("更新前：" + parent.toString());
		// 测试开始
		BizNode.addChildToParent(child, parent, newFeatures);
		logger.debug("更新后：" + parent.toString());
	}
}
