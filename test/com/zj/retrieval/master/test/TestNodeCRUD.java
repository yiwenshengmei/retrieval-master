package com.zj.retrieval.master.test;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;
import org.junit.Test;

import com.zj.retrieval.master.CustomerField;
import com.zj.retrieval.master.Node;
import com.zj.retrieval.master.NodeAttribute;
import com.zj.retrieval.master.NodeImage;

public class TestNodeCRUD {
	
	@Test
	public void testAddRootNode() throws Exception {
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		Session sess = sessionFactory.openSession();
		sess.beginTransaction();
		
		Node node = new Node();
		node.setName("��");
		node.setEnglishName("Bird");
		node.setDesc("һ����");
		node.setContact("yiwenshengmei@163.com");
		node.setParentId(Node.VIRTUAL_NODE_ID);
		node.setUri("http://www.bird.com");
		node.setUriName("birduriname");
		node.setLabel("b");
		
		node.addImage(new NodeImage("E:\\1.jpg"), sess)
		    .addImage(new NodeImage("E:\\2.jpg"), sess);
		
		node.addCustomerField(new CustomerField("k1", "v1"), sess)
			.addCustomerField(new CustomerField("k2", "v2"), sess);
		
		node.addNodeAttribute(
				new NodeAttribute("���Է�", "Can Fly", "ʹ�ó�����").withImage(new NodeImage("E:\\fly.jpg"), sess), sess);
		node.addNodeAttribute(
				new NodeAttribute("������", "Can Jump", "�ý���").withImage(new NodeImage("E:\\jump.jpg"), sess), sess);
		
		sess.save(node);
		
		sess.getTransaction().commit();
		
	}
}
