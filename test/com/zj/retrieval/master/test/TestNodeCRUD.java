package com.zj.retrieval.master.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;

import com.zj.retrieval.master.CustomerField;
import com.zj.retrieval.master.Node;
import com.zj.retrieval.master.NodeAttribute;
import com.zj.retrieval.master.NodeImage;
import com.zj.retrieval.master.dao.NodeDao;

public class TestNodeCRUD {
	
	@Test
	public void testAddRootNode() throws Exception {
		Node node = new Node();
		node.setName("��");
		node.setEnglishName("Bird");
		node.setDesc("һ����");
		node.setContact("yiwenshengmei@163.com");
		node.setParentId(Node.VIRTUAL_NODE_ID);
		node.setUri("http://www.bird.com");
		node.setUriName("birduriname");
		node.setLabel("b");
		
		node.addImage(new NodeImage("E:\\1.jpg")).addImage(new NodeImage("E:\\2.jpg"));
		
		node.addCustomerField(new CustomerField("k1", "v1")).addCustomerField(new CustomerField("k2", "v2"));
		
		node.addNodeAttribute(new NodeAttribute("���Է�", "Can Fly", "ʹ�ó�����").addImage(new NodeImage("E:\\fly.jpg")));
		node.addNodeAttribute(new NodeAttribute("������", "Can Jump", "�ý���").addImage(new NodeImage("E:\\jump.jpg")));
		
		NodeDao.getInstance().insert(node);
	}
}
