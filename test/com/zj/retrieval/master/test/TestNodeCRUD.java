package com.zj.retrieval.master.test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.Session;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zj.retrieval.master.FeatureImage;
import com.zj.retrieval.master.NodeAttribute;
import com.zj.retrieval.master.Node;
import com.zj.retrieval.master.NodeFeature;
import com.zj.retrieval.master.NodeImage;
import com.zj.retrieval.master.RetrievalDataSource;

public class TestNodeCRUD {
	
	private final static Logger logger = LoggerFactory.getLogger(TestNodeCRUD.class);
	
	@Test
	public void testAddRootNode() throws Exception {
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		Session sess = null;
		Transaction tx = null;
		try {
		sess = sessionFactory.openSession();
		tx = sess.beginTransaction();
		
		Node node = new Node();
		node.setName("鸟");
		
		node.setImages(new HashSet<NodeImage>(Arrays.asList(new NodeImage[] {
			new NodeImage("E:\\1.jpg"),
			new NodeImage("E:\\2.jpg")
		})));
		
		node.setAttributes(new HashSet<NodeAttribute>(Arrays.asList(new NodeAttribute[] {
			new NodeAttribute("k1", "v1"),	
			new NodeAttribute("k2", "v2")	
		})));
		
		if (node.getRetrievalDataSource() == null) 
			node.setRetrievalDataSource(new RetrievalDataSource());
		node.getRetrievalDataSource().setFeatures(new HashSet<NodeFeature>(Arrays.asList(new NodeFeature[] {
			new NodeFeature("可以飞", "Can Fly", StringUtils.EMPTY),
			new NodeFeature("可以跳", "Can Jump", StringUtils.EMPTY)
		})));
		
//		node.addCustomerField(new CustomerField("k1", "v1")).addCustomerField(new CustomerField("k2", "v2"));
//		
//		node.addNodeAttribute(
//				new NodeAttribute("可以飞", "Can Fly", "使用翅膀飞行").withImage(new NodeImage("E:\\fly.jpg")));
//		node.addNodeAttribute(
//				new NodeAttribute("可以跳", "Can Jump", "用脚跳").withImage(new NodeImage("E:\\jump.jpg")));
		
//		RetrievalDataSource rds = node.getRetrievalDataSource();
//		for (NodeAttribute attr : rds.getAttributes()) {
//			for (NodeImage img : attr.getImages()) {
//				sess.save(img);
//			}
//			sess.save(attr);
//		}
//		sess.save(rds);
//		for (NodeImage img : node.getImages()) {
//			sess.save(img);
//		}
//		for (CustomerField cf : node.getCustomerFields()) {
//			sess.save(cf);
//		}
			
		sess.save(node);
		for (NodeImage img : node.getImages()) {
			sess.save(img);
		}
		for (NodeAttribute attr : node.getAttributes()) {
			sess.save(attr);
		}
		sess.save(node.getRetrievalDataSource());
		for (NodeFeature feature : node.getRetrievalDataSource().getFeatures()) {
			sess.save(feature);
			for (FeatureImage img : feature.getImages()) {
				sess.save(img);
			}
		}
		
		tx.commit();
		
		}
		catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			throw ex;
		}
		finally {
			if (sess != null)
				sess.close();
		}
		
	}

	@Test
	public void testGetNode() throws Exception {
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		Session sess = sessionFactory.getCurrentSession();
		List nodes = sess.createQuery("from Node nd where nd.name = '鸟'").list();
//		Node node = (Node) sess.get(Node.class, "818183973c6fa68d013c6fa68f940001");
		logger.debug("nodes.size = " + nodes.size());
		if (nodes.size() > 0) {
			Node node = (Node) nodes.get(0);
			logger.debug("Node.name = " + node.getName());
			for (NodeImage img : node.getImages()) {
				logger.debug("NodeImg.path = " + img.getPath());
				logger.debug("img.node == node: " + (img.getNode() == node));
			}
		}
		sess.close();
	}
}
