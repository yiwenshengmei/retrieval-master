package com.zj.retrieval.master.test;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Assert;
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
		node.setName("鸟");
		
		node.setImages(Arrays.asList(
			new NodeImage("E:\\1.jpg", node),
			new NodeImage("E:\\2.jpg", node)
		));
		
		node.setAttributes(Arrays.asList(
			new NodeAttribute("k1", "v1", node),	
			new NodeAttribute("k2", "v2", node)	
		));
		
		NodeFeature feature1 = new NodeFeature("可以飞", "Can Fly", StringUtils.EMPTY).withRetrievalDataSource(node.getRetrievalDataSource());
		NodeFeature feature2 = new NodeFeature("可以跳", "Can Jump", StringUtils.EMPTY).withRetrievalDataSource(node.getRetrievalDataSource());
		feature1.setImages(Arrays.asList(
				new FeatureImage("E:\\可以飞featureImage1.jpg", feature1), 
				new FeatureImage("C:\\可以飞featureImage2.jpg", feature1)));
		feature2.setImages(Arrays.asList(
				new FeatureImage("E:\\可以跳featureImage1.jpg", feature2), 
				new FeatureImage("C:\\可以跳featureImage2.jpg", feature2)));
		node.getRetrievalDataSource().setFeatures(Arrays.asList(feature1, feature2));
		
		sess.save(node);
	}
	
	@Test
	public void testAddRootNode() throws Exception {
		DALService.doAction(new IDALAction() {
			@Override
			public Object doAction(Session sess) throws Exception {
				addRootNode(sess);
				return null;
			}
		});
		
		DALService.doAction(new IDALAction() {
			
			@Override
			public Object doAction(Session sess) throws Exception {
				getNode(sess);
				return null;
			}
		});
	}
	
	private void getNode(Session sess) {
		List nodes = sess.createQuery("from Node nd where nd.name = '鸟'").list();
		Assert.assertEquals("查询数量不为1", 1, nodes.size());
		Node node = (Node) nodes.get(0);
		Assert.assertEquals("节点名称不正确", "鸟", node.getName());
		Assert.assertEquals("node.images的数量不对", 2, node.getImages().size());
		for (NodeImage img : node.getImages()) {
			logger.debug("图片路径: " + img.getPath());
			Assert.assertTrue("图片路径不正确", img.getPath().equals("E:\\1.jpg") || img.getPath().equals("E:\\2.jpg"));
			Assert.assertNotNull("nodeImage.node 为空！", img.getNode());
			Assert.assertTrue("nodeImage.node != node !", img.getNode() == node);
		}
		Assert.assertEquals("node.attributes的数量不对", 2, node.getAttributes().size());
		for (NodeAttribute attr : node.getAttributes()) {
			Assert.assertTrue("节点属性键不正确", attr.getKey().equals("k1") || attr.getKey().equals("k2"));
			Assert.assertTrue("节点属性值不正确", attr.getValue().equals("v1") || attr.getValue().equals("v2"));
			Assert.assertNotNull("nodeAttribute.node 为空!", attr.getNode());
			Assert.assertTrue("nodeAttribute.node != node !", attr.getNode() == node);
		}
		RetrievalDataSource rds = node.getRetrievalDataSource();
		Assert.assertTrue("rds.node != node !", rds.getNode() == node);
		Assert.assertEquals("rds.features的数量不对", 2, rds.getFeatures().size());
		for (NodeFeature ft : rds.getFeatures()) {
			logger.debug("节点特征名称: " + ft.getName());
			Assert.assertTrue("节点特征名称不正确", ft.getName().equals("可以飞") || ft.getName().equals("可以跳"));
			Assert.assertNotNull("feature.retrievalDataSource == null !", ft.getRetrievalDataSource());
			Assert.assertTrue("feature.retrievalDataSource != rds !", ft.getRetrievalDataSource() == rds);
			Assert.assertEquals("feature.images的数量不对", 2, ft.getImages().size());
			for (FeatureImage img : ft.getImages()) {
				Assert.assertTrue("特征图片路径不正确", img.getPath().equals("E:\\可以飞featureImage1.jpg")
						|| img.getPath().equals("C:\\可以飞featureImage2.jpg")
						|| img.getPath().equals("E:\\可以跳featureImage1.jpg")
						|| img.getPath().equals("C:\\可以跳featureImage2.jpg"));
				Assert.assertNotNull("featureImage.feature == null !", img.getFeature());
				Assert.assertTrue("featureImage.feature != feature !", img.getFeature() == ft);
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
