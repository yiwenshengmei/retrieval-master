package com.zj.retrieval.master.test;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zj.retrieval.master.DALService;
import com.zj.retrieval.master.FeatureImage;
import com.zj.retrieval.master.IDALAction;
import com.zj.retrieval.master.Node;
import com.zj.retrieval.master.NodeFeature;
import com.zj.retrieval.master.RetrievalDataSource;

public class DeleteNodeFeaturesTests {
	
	private final static Logger logger = LoggerFactory.getLogger(DeleteNodeFeaturesTests.class);
	
	public void deleteNodeFeature() throws Exception {
		DALService.doAction(new IDALAction() {
			@Override
			public Object doAction(Session sess, Transaction tx) throws Exception {
				Node node = (Node) sess.createQuery("from Node nd where nd.name = '��'").uniqueResult();
				RetrievalDataSource rds = node.getRetrievalDataSource();
				List<NodeFeature> features = rds.getFeatures();
				Assert.assertEquals("ɾ��ǰû�в����㹻��2��NodeFeature", 2, features.size());
				logger.debug("��ʼɾ��");
				for (NodeFeature feature : features) {
					for (FeatureImage img : feature.getImages()) {
						sess.delete(img); // ���û�������룬�򲻻����delete���
					}
					feature.getImages().clear(); // ������ʡ��
					sess.delete(feature);
				}
				features.clear(); // ��䲻��ʡ��
				return null;
			}
		});
	}
	
	public void checkDeleteNodeFeature() throws Exception {
		DALService.doAction(new IDALAction() {
			@Override
			public Object doAction(Session sess, Transaction tx) throws Exception {
				Node node = (Node) sess.createQuery("from Node nd where nd.name = '��'").uniqueResult();
				Assert.assertEquals("NodeFeatureû��ɾ���ɹ�", 0, node.getRetrievalDataSource().getFeatures().size());
				return null;
			}
		});
	}
	
	@Test
	public void testDeleteNodeFeature() throws Exception {
		AddNodeTests.addNode();
		deleteNodeFeature();
		checkDeleteNodeFeature();
	}
}
