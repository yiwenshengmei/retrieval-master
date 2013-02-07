package com.zj.retrieval.master.test;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zj.retrieval.master.BizNode;
import com.zj.retrieval.master.DALService;
import com.zj.retrieval.master.IDALAction;
import com.zj.retrieval.master.Node;
import com.zj.retrieval.master.actions.XMLUtils;

public class OWLTests {
	
	private final static Logger logger = LoggerFactory.getLogger(OWLTests.class);
	
	@Test
	public void testCreateClassTypeOWL() throws Exception {
		final String childId = (String) DALService.doAction(new IDALAction() {
			
			@Override
			public Object doAction(Session sess, Transaction tx) throws Exception {
				String parentId = AddNodeTests.addNode();
				return AddNodeTests.addNodeWithParentId(sess, parentId);
			}
		});
		
		DALService.doAction(new IDALAction() {
			
			@Override
			public Object doAction(Session sess, Transaction tx) throws Exception {
				Node child = (Node) sess.get(Node.class, childId);
				logger.debug("\n" + XMLUtils.format(BizNode.createOwl(child)));
				return null;
			}
		});
	}
	
	@Test
	public void testCreateIndividualTypeOWL() {
		
	}
}
