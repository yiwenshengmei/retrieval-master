package com.zj.retrieval.master.test;

import java.util.List;


import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Assert;
import org.junit.Test;

import com.zj.retrieval.master.DALService;
import com.zj.retrieval.master.FeatureImage;
import com.zj.retrieval.master.IDALAction;
import com.zj.retrieval.master.Node;
import com.zj.retrieval.master.NodeFeature;

public class UpdateNodeTests {
	@Test
	public void testUpdateNode() throws Exception {
		AddNodeTests.addRootNode();
		updateFeatureImagePath();
		checkUpdate();
	}
	
	public void checkUpdate() throws Exception {
		DALService.doAction(new IDALAction() {
			
			@Override
			public Object doAction(Session sess, Transaction tx) throws Exception {
				List<Node> nodes = sess.createQuery("from Node nd where nd.name = '鸟'").list();
				for (Node nd : nodes) {
					for (NodeFeature ft : nd.getRetrievalDataSource().getFeatures()) {
						for (FeatureImage img : ft.getImages()) {
							Assert.assertEquals("修改FeatureImage的path没有成功！", "X", img.getPath());
						}
					}
				}
				return null;
			}
		});
	}
	
	public void updateFeatureImagePath() throws Exception {
		DALService.doAction(new IDALAction() {
			@Override
			public Object doAction(Session sess, Transaction tx) throws Exception {
				List<Node> nodes = sess.createQuery("from Node nd where nd.name = '鸟'").list();
				for (Node nd : nodes) {
					for (NodeFeature ft : nd.getRetrievalDataSource().getFeatures()) {
						for (FeatureImage img : ft.getImages()) {
							img.setPath("X");
						}
					}
				}
				return null;
			}
		});
	}
}
