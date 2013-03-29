package com.zj.retrieval.master.test;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.hibernate.Session;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Transaction;
import com.zj.retrieval.master.DALService;
import com.zj.retrieval.master.IDALAction;
import com.zj.retrieval.master.Node;
import com.zj.retrieval.master.NodeAttribute;

public class CodeTest {
	private final static Logger logger = LoggerFactory.getLogger(CodeTest.class);
	
	@Test
	public void testMapNull() {
		// HashMap是可以添加以null为key的值
		Map<String, String> m1 = new HashMap<String, String>();
		m1.put(null, "ssss");
		logger.debug(m1.get(null));
		
		// HashTable是不可以添加以null为key的值的
		Map<String, String> m2 = new Hashtable<String, String>();
		m2.put(null, "ssss");
		logger.debug(m2.get(null));
	}
	
	@Test
	public void testHibernateList() throws Exception {
		DALService.doAction(new IDALAction() {
			@Override
			public Object doAction(Session sess, Transaction tx) throws Exception {
				Node nd = new Node("zhaojie");
				NodeAttribute attr = new NodeAttribute("k1", "v1");
				nd.getAttributes().add(attr);
				sess.save(nd);
				return null;
			}
		});
	}
}
