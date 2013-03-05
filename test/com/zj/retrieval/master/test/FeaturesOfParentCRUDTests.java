package com.zj.retrieval.master.test;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zj.retrieval.master.DALService;
import com.zj.retrieval.master.IDALAction;
import com.zj.retrieval.master.Node;
import com.zj.retrieval.master.NodeFeature;

public class FeaturesOfParentCRUDTests {
	
	private final static Logger logger = LoggerFactory.getLogger(FeaturesOfParentCRUDTests.class);
	
	@Test
	public void testAdd() throws Exception {
		DALService.doAction(new IDALAction() {
			
			@Override
			public Object doAction(Session sess, Transaction tx) throws Exception {
				Node parent = new Node("ParentNode");
				parent.getRetrievalDataSource().getFeatures().add(new NodeFeature("feature1"));
				parent.getRetrievalDataSource().getFeatures().add(new NodeFeature("feature2"));
				
				sess.save(parent);
				logger.debug("parent node id: " + parent.getId());
				
				Node child = new Node("ChildNode");
				child.getFeaturesOfParent().add(parent.getRetrievalDataSource().getFeatures().get(0));
				
				sess.save(child);
				return null;
			}
		});
	}
	
	@Test
	public void testQuery() throws Exception {
		DALService.doAction(new IDALAction() {
			
			@Override
			public Object doAction(Session sess, Transaction tx) throws Exception {
				Node node = (Node) sess.createQuery("from Node child where child.name = :name")
					.setString("name", "ChildNode")
					.uniqueResult();
				
				List<NodeFeature> featuresOfParent = node.getFeaturesOfParent();
				logger.debug("featuresOfParent.size: " + featuresOfParent.size());
				if (featuresOfParent.size() > 0)
					logger.debug("featuresOfParent[0].name: " + featuresOfParent.get(0).getName());
				return null;
			}
		});
	}
}
