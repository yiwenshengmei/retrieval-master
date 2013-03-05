package com.zj.retrieval.master.test;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CodeTest {
	private final static Logger logger = LoggerFactory.getLogger(CodeTest.class);
	
	@Test
	public void testMapNull() {
		// HashMap�ǿ��������nullΪkey��ֵ
		Map<String, String> m1 = new HashMap<String, String>();
		m1.put(null, "ssss");
		logger.debug(m1.get(null));
		
		// HashTable�ǲ����������nullΪkey��ֵ��
		Map<String, String> m2 = new Hashtable<String, String>();
		m2.put(null, "ssss");
		logger.debug(m2.get(null));
	}
}
