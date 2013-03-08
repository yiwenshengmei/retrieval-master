package com.zj.retrieval.master.test;

import java.util.Arrays;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zj.retrieval.master.MatrixItem;
import com.zj.retrieval.master.MatrixRow;
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
}
