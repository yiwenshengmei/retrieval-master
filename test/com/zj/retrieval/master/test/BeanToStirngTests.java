package com.zj.retrieval.master.test;

import java.util.Arrays;
import java.util.Calendar;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zj.retrieval.master.BizNode;
import com.zj.retrieval.master.Matrix;
import com.zj.retrieval.master.MatrixItem;
import com.zj.retrieval.master.MatrixRow;
import com.zj.retrieval.master.Node;
import com.zj.retrieval.master.NodeFeature;

public class BeanToStirngTests {
	
	private final static Logger logger = LoggerFactory.getLogger(BeanToStirngTests.class);
	
	@Test
	public void testMatrixItemToString() {
		logger.debug(MatrixItem.Yes(null).toString());
		logger.debug(MatrixItem.No(null).toString());
		logger.debug(MatrixItem.Unknow(null).toString());
	}
	
	@Test
	public void testMatrixRowToString() {
		MatrixRow row = new MatrixRow(Arrays.asList(
			NodeFeature.YES, NodeFeature.NO, NodeFeature.UNKNOW
		), null);
		logger.debug(row.toString());
	}
	
	@Test
	public void testMatrixToString() {
		Matrix mtx = new Matrix();
		mtx.addRow(new MatrixRow(Arrays.asList(
			NodeFeature.YES, NodeFeature.NO, NodeFeature.UNKNOW
		), null));
		mtx.addRow(new MatrixRow(Arrays.asList(
			NodeFeature.NO, NodeFeature.NO, NodeFeature.NO
		), null));
		logger.debug(mtx.toString());
	}
	
	@Test
	public void testNodeToString() {
		Node parent = new Node("Parent" + Calendar.getInstance().getTime());
		
		NodeFeature nf1 = new NodeFeature("nf1");
		nf1.setId("19989");
		NodeFeature nf2 = new NodeFeature("nf2");
		nf2.setId("24545");
		
		BizNode.addNewFeaturesToNode(parent, Arrays.asList(nf1, nf2));
		
		Node child = new Node("child1");
		child.setFeaturesOfParent(Arrays.asList(nf1));
		NodeFeature newFeature = new NodeFeature("newFeature1");
		
		BizNode.addChildToParent(child, parent, Arrays.asList(newFeature));
		
		BizNode.buildRelation(parent);
		logger.debug(parent.toString());
		logger.debug(child.toString());
	}
}
