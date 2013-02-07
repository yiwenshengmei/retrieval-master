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
		Node parentNode = new Node("计算机");
		
		parentNode.getChildNodes().addAll(Arrays.asList(
			new Node("上网本"), 
			new Node("超级计算机")));
		
		parentNode.getRetrievalDataSource().getFeatures().addAll(Arrays.asList(
			new NodeFeature("重量轻"), new NodeFeature("重量重"), new NodeFeature("运算速度较慢"), new NodeFeature("运算速度较快")
		));
		
		Matrix matrix = new Matrix(Arrays.asList(
			Arrays.asList(NodeFeature.YES, NodeFeature.NO, NodeFeature.YES, NodeFeature.NO), // 上网本
			Arrays.asList(NodeFeature.NO, NodeFeature.YES, NodeFeature.NO, NodeFeature.YES)  // 超级计算机
		));
		parentNode.getRetrievalDataSource().setMatrix(matrix);
		
		BizRetrieval bizRetrieval = new BizRetrieval(parentNode);
		RetrievalResult result = bizRetrieval.retrieval(Arrays.asList(8, 
			NodeFeature.UNKNOW, NodeFeature.UNKNOW, NodeFeature.UNKNOW, NodeFeature.UNKNOW));
		
		logger.debug("是否有结果: " + result.hasResult());
		if (result.hasResult()) {
			logger.debug("结果是:");
			for (Node resultNode : result.getResult()) {
				logger.debug(resultNode.getName());
			}
		}
		else {
			logger.debug("下一个特征问题是: " + result.getNextFeature().getName());
		}
		
	}
}
