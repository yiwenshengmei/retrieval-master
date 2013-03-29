package com.zj.retrieval.master.test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zj.retrieval.master.BizNode;
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
	
	@Test
	public void t1() {
		List<Integer> array = new ArrayList<Integer>();
		array.add(2);
		array.add(null);
		array.add(2);
		Iterator<Integer> iter = array.iterator();
		while (iter.hasNext()) {
			logger.debug("next");
			if (iter.next() == null)
				iter.remove();
		}
		logger.debug("size: " + array.size());
	}
	
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
	
	@Test
	public void testAddToParent() throws Exception {
		final String parentName = "Parent" + Calendar.getInstance().getTime();
		final String childName = "Child" + Calendar.getInstance().getTime();
		
		DALService.doAction(new IDALAction() {
			@Override
			public Object doAction(Session sess, Transaction tx) throws Exception {
				Node parent = new Node(parentName);
				BizNode.addNewFeaturesToNode(parent, Arrays.asList(new NodeFeature("nf1")));
				
				BizNode.buildRelation(parent);
				sess.save(parent);
				
				logger.debug(parent.toString());
				return null;
			}
		});
		
		DALService.doAction(new IDALAction() {
			
			@Override
			public Object doAction(Session sess, Transaction tx) throws Exception {
				Node child = new Node(childName);
				Node parent = (Node) sess.createQuery("from Node nd where nd.name = :ndName")
					.setString("ndName", parentName)
					.uniqueResult();
				if (parent == null) throw new IllegalArgumentException("没有找到名称为Parent的节点");
				child.setFeaturesOfParent(Arrays.asList(parent.getFeatures().get(0)));
				List<NodeFeature> newFeatures = Arrays.asList(new NodeFeature("nf2"));
				BizNode.addChildToParent(child, parent, newFeatures);
				BizNode.buildRelation(child, parent);
				sess.save(child);
				sess.update(parent);
				
				logger.debug("Parent: \n" + parent.toString());
				logger.debug("Child: \n" + child.toString());
				return null;
			}
		});
		
		DALService.doAction(new IDALAction() {
			
			@Override
			public Object doAction(Session sess, Transaction tx) throws Exception {
				Node parent = (Node) sess.createQuery("from Node nd where nd.name = :name")
					.setString("name", parentName)
					.uniqueResult();
				logger.debug(parent.toString());
				return null;
			}
		});
	}
	
	@Test
	public void testDeleteFeature() throws Exception {
		final String parentName = "Parent " + Calendar.getInstance().getTime().toString();
		final String childName = "Child " + Calendar.getInstance().getTime().toString();
		
		DALService.doAction(new IDALAction() {
			@Override
			public Object doAction(Session sess, Transaction tx) throws Exception {
				Node parent = saveAndGetTestNode(parentName, sess);
				
				List<NodeFeature> features = new ArrayList<NodeFeature>();
				features.add(new NodeFeature("ndf1"));
				features.add(new NodeFeature("ndf2"));
				
				parent.addFeatures(features);
				
				Node child = saveAndGetTestNode(childName, sess);
				child.addFeaturesOfParent(Arrays.asList(parent.getFeatures().get(0)));
				
				parent.addChilds(Arrays.asList(child), null);
				BizNode.buildRelation(parent, child);
				sess.update(parent);
				sess.update(child);
				return null;
			}
		});
		
		DALService.doAction(new IDALAction() {
			@Override
			public Object doAction(Session sess, Transaction tx) throws Exception {
				Node parent = BizNode.getNodeByName(parentName, sess);
				logger.debug("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ 输出准备好的数据");
				logger.debug(parent.toString());
				BizNode.deleteFeatureFromNode(parent, Arrays.asList(parent.getFeatures().get(0)));
				sess.update(parent);
				return null;
			}
		});
		
		DALService.doAction(new IDALAction() {
			@Override
			public Object doAction(Session sess, Transaction tx) throws Exception {
				Node parent = BizNode.getNodeByName(parentName, sess);
				logger.debug("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ 输出删除特征后的数据");
				logger.debug(parent.toString());
				return null;
			}
		});
	}
	
	@Test
	public void testDeleteAttribute() throws Exception {
		final String parentName = "Parent " + Calendar.getInstance().getTime().toString();
		
		DALService.doAction(new IDALAction() {
			@Override
			public Object doAction(Session sess, Transaction tx) throws Exception {
				Node node = new Node(parentName);
				return null;
			}
		});
	}
	
	private Node saveAndGetTestNode(final String nodeName, Session sess) throws Exception {
		Node node = new Node(nodeName);
		BizNode.buildRelation(node);
		sess.save(node);
		return node;
	}
	
	@Test
	public void testNode() throws Exception {
		final String nodeName = "ParentTue Mar 26 10:10:47 CST 2013";
		DALService.doAction(new IDALAction() {
			
			@Override
			public Object doAction(Session sess, Transaction tx) throws Exception {
				Node node = (Node) sess.createQuery("from Node nd where nd.name = :name")
					.setString("name", nodeName)
					.uniqueResult();
				logger.debug(node.toString());
				return null;
			}
		});
	}
}
