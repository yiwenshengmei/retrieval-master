package com.zj.retrieval.master.test;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zj.retrieval.master.DALService;
import com.zj.retrieval.master.FeatureImage;
import com.zj.retrieval.master.IDALAction;
import com.zj.retrieval.master.Node;
import com.zj.retrieval.master.NodeAttribute;
import com.zj.retrieval.master.NodeFeature;
import com.zj.retrieval.master.NodeImage;
import com.zj.retrieval.master.RetrievalDataSource;

public class NodeCRUDTests {
	
	private final static Logger logger = LoggerFactory.getLogger(NodeCRUDTests.class);
	
	public void addRootNode(Session sess) {
		Node node = new Node();
		node.setName("��");
		
		node.setImages(Arrays.asList(
			new NodeImage("E:\\1.jpg"),
			new NodeImage("E:\\2.jpg")
		));
		
		node.setAttributes(Arrays.asList(
			new NodeAttribute("k1", "v1"),	
			new NodeAttribute("k2", "v2")	
		));
		
		node.getRetrievalDataSource().setFeatures(Arrays.asList(new NodeFeature[] {
			new NodeFeature("���Է�", "Can Fly", StringUtils.EMPTY).withImages(Arrays.asList(
				new FeatureImage("E:\\���Է�featureImage1.jpg"),
				new FeatureImage("C:\\���Է�featureImage2.jpg")
			)),
			new NodeFeature("������", "Can Jump", StringUtils.EMPTY).withImages(Arrays.asList(
				new FeatureImage("E:\\������featureImage1.jpg"),
				new FeatureImage("C:\\������featureImage2.jpg")	
			))
		}));
		
		sess.save(node);
	}
	
	@Test
	public void testAddRootNode() throws Exception {
		DALService.doAction(new IDALAction() {
			@Override
			public Object doAction(Session sess) throws Exception {
				addRootNode(sess);
				getNode(sess);
				return null;
			}
		});
		testGetNode();
	}
	
	@Test
	public void testHibernateIninialize() throws Exception {
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
	}
	
	
	private void getNode(Session sess) {
		List nodes = sess.createQuery("from Node nd where nd.name = '��'").list();
		logger.debug("��ѯ���Ľڵ�����: " + nodes.size());
		if (nodes.size() > 0) {
			Node node = (Node) nodes.get(0);
			logger.debug("�ڵ�����: " + node.getName());
			for (NodeImage img : node.getImages()) {
				logger.debug("ͼƬ·��: " + img.getPath());
				logger.debug("img.node == node ? " + (img.getNode() == node));
			}
			for (NodeAttribute attr : node.getAttributes()) {
				logger.debug("�ڵ����Լ�: " + attr.getKey());
				logger.debug("�ڵ�����ֵ: " + attr.getValue());
			}
			RetrievalDataSource rds = node.getRetrievalDataSource();
			for (NodeFeature ft : rds.getFeatures()) {
				logger.debug("�ڵ���������: " + ft.getName());
				for (FeatureImage img : ft.getImages()) {
					logger.debug("����ͼƬ·��: " + img.getPath());
				}
			}
		}
	}

	@Test
	public void testGetNode() throws Exception {
		DALService.doAction(new IDALAction() {
			
			@Override
			public Object doAction(Session sess) throws Exception {
				getNode(sess);
				return null;
			}
		});
	}
}
