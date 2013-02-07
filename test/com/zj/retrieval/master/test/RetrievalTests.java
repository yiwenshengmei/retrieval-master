package com.zj.retrieval.master.test;

import java.util.Arrays;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zj.retrieval.master.BizRetrieval;
import com.zj.retrieval.master.Matrix;
import com.zj.retrieval.master.Node;
import com.zj.retrieval.master.NodeFeature;
import com.zj.retrieval.master.RetrievalResult;

public class RetrievalTests {
	
	private final static Logger logger = LoggerFactory.getLogger(RetrievalTests.class);
	
	@Test
	public void testRetrieval() {
		Node parentNode = new Node("�����");
		
		parentNode.getChildNodes().addAll(Arrays.asList(
			new Node("������"), 
			new Node("���������")));
		
		parentNode.getRetrievalDataSource().getFeatures().addAll(Arrays.asList(
			new NodeFeature("������"), new NodeFeature("������"), new NodeFeature("�����ٶȽ���"), new NodeFeature("�����ٶȽϿ�")
		));
		
		Matrix matrix = new Matrix(Arrays.asList(
			Arrays.asList(NodeFeature.YES, NodeFeature.NO, NodeFeature.YES, NodeFeature.NO), // ������
			Arrays.asList(NodeFeature.NO, NodeFeature.YES, NodeFeature.NO, NodeFeature.YES)  // ���������
		));
		parentNode.getRetrievalDataSource().setMatrix(matrix);
		
		BizRetrieval bizRetrieval = new BizRetrieval(parentNode);
		RetrievalResult result = bizRetrieval.retrieval(Arrays.asList(8, 
			NodeFeature.UNKNOW, NodeFeature.UNKNOW, NodeFeature.UNKNOW, NodeFeature.UNKNOW));
		
		logger.debug("�Ƿ��н��: " + result.hasResult());
		if (result.hasResult()) {
			logger.debug("�����:");
			for (Node resultNode : result.getResult()) {
				logger.debug(resultNode.getName());
			}
		}
		else {
			logger.debug("��һ������������: " + result.getNextFeature().getName());
		}
		
	}
}
