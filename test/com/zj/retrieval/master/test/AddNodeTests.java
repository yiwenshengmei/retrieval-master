package com.zj.retrieval.master.test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zj.retrieval.master.DALService;
import com.zj.retrieval.master.FeatureImage;
import com.zj.retrieval.master.IDALAction;
import com.zj.retrieval.master.Matrix;
import com.zj.retrieval.master.Node;
import com.zj.retrieval.master.NodeAttribute;
import com.zj.retrieval.master.NodeFeature;
import com.zj.retrieval.master.NodeImage;
import com.zj.retrieval.master.RetrievalDataSource;

public class AddNodeTests {
	
	private final static Logger logger = LoggerFactory.getLogger(AddNodeTests.class);
	
	public static String addNode() throws Exception {
		String id = (String) DALService.doAction(new IDALAction() {
			
			@Override
			public Object doAction(Session sess, Transaction tx) throws Exception {
				return addNodeWithParent(sess, null).getId();
			}
		});
		
		return id;
	}
	
	public static String addNodeWithParentId(Session sess, final String parentId) throws Exception {
		Node parentNode = (Node) sess.get(Node.class, parentId);
		return addNodeWithParent(sess, parentNode).getId();
	}
	
	/**
	 * 注意，parent必须是已经托管的对象
	 * @param sess
	 * @param parent
	 * @return
	 */
	public static Node addNodeWithParent(Session sess, Node parent) {
		Node node = new Node();
		node.setParentNode(parent);
		node.setName("鸟");
		
		node.setImages(Arrays.asList(
			new NodeImage("E:\\1.jpg", node),
			new NodeImage("E:\\2.jpg", node)
		));
		
		node.setAttributes(Arrays.asList(
			new NodeAttribute("k1", "v1", node),	
			new NodeAttribute("k2", "v2", node)	
		));
		
		NodeFeature feature1 = new NodeFeature("可以飞", "Can Fly", StringUtils.EMPTY)
			.withRetrievalDataSource(node.getRetrievalDataSource());
		NodeFeature feature2 = new NodeFeature("可以跳", "Can Jump", StringUtils.EMPTY)
			.withRetrievalDataSource(node.getRetrievalDataSource());
		feature1.setImages(Arrays.asList(
				new FeatureImage("E:\\可以飞featureImage1.jpg", feature1), 
				new FeatureImage("C:\\可以飞featureImage2.jpg", feature1)));
		feature2.setImages(Arrays.asList(
				new FeatureImage("E:\\可以跳featureImage1.jpg", feature2), 
				new FeatureImage("C:\\可以跳featureImage2.jpg", feature2)));
		node.getRetrievalDataSource().setFeatures(Arrays.asList(feature1, feature2));
		
		Matrix mtx = new Matrix(Arrays.asList(
			Arrays.asList(1, 2, 0),
			Arrays.asList(3, 3, 2)
		));
		mtx.setRetrievalDataSource(node.getRetrievalDataSource());
		node.getRetrievalDataSource().setMatrix(mtx);
		
		if (parent != null) {
			parent.getChildNodes().add(node);
			node.setParentNode(parent);
		}
		sess.save(node);
		
		return node;
	}
	
	public void checkAddRootNode() throws Exception {
		DALService.doAction(new IDALAction() {
			@Override
			public Object doAction(Session sess, Transaction tx) throws Exception {
				checkNodeBase(sess);
				return null;
			}
		});
	}
	
	private void checkAddChildNode(final String childId) throws Exception {
		DALService.doAction(new IDALAction() {
			
			@Override
			public Object doAction(Session sess, Transaction tx) throws Exception {
				Node child = (Node) sess.get(Node.class, childId);
				Node parent = child.getParentNode();
				Assert.assertNotNull("父节点为null！", parent);
				Assert.assertEquals("父节点的child不为1", 1, parent.getChildNodes().size());
				return null;
			}
		});
	}
	
	private void checkNodeBase(Session sess) {
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
				Assert.assertTrue("特征图片路径不正确", 
						   img.getPath().equals("E:\\可以飞featureImage1.jpg")
						|| img.getPath().equals("C:\\可以飞featureImage2.jpg")
						|| img.getPath().equals("E:\\可以跳featureImage1.jpg")
						|| img.getPath().equals("C:\\可以跳featureImage2.jpg"));
				Assert.assertNotNull("featureImage.feature == null !", img.getFeature());
				Assert.assertTrue("featureImage.feature != feature !", img.getFeature() == ft);
			}
		}
		
		Matrix mtx = rds.getMatrix();
		Assert.assertNotNull("Matrix为空！", mtx);
		logger.debug(mtx.toString());
		Assert.assertEquals("Matrix的值不正确", "\n[1, 2, 0]\n[3, 3, 2]", mtx.toString());
	}
	
	@Test
	public void testAddRootNode() throws Exception {
		addNode();
		checkAddRootNode();
	}
	
	@Test
	public void testAddChildNode() throws Exception {
		String childId = (String) DALService.doAction(new IDALAction() {
			
			@Override
			public Object doAction(Session sess, Transaction tx) throws Exception {
				Node parent = addNodeWithParent(sess, null);
				return addNodeWithParent(sess, parent).getId();
			}
		});
		
		checkAddChildNode(childId);
	}

	@Test
	public void testGetNode() throws Exception {
		DALService.doAction(new IDALAction() {
			
			@Override
			public Object doAction(Session sess, Transaction tx) throws Exception {
				checkNodeBase(sess);
				return null;
			}
		});
	}
	
	@Test
	public void testFilePath() {
		File file = new File("E:\\Projects\\java_retrieval\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\retrieval-master\\images\\b6b09e58-74bd-44bc-8d26-4f63736a9cb9.jpg");
		logger.debug("name: " + file.getName());
		String name = FilenameUtils.getName("E:\\Projects\\java_retrieval\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\retrieval-master\\images\\b6b09e58-74bd-44bc-8d26-4f63736a9cb9.jpg");
		logger.debug(name);
	}
}
