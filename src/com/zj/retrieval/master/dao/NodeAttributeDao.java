package com.zj.retrieval.master.dao;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.zj.retrieval.master.Configuration;
import com.zj.retrieval.master.NodeAttribute;

public class NodeAttributeDao {
	private SimpleJdbcTemplate template;
	
	public void setDateSource(DataSource dataSource) {
		this.template = new SimpleJdbcTemplate(dataSource);
	}
	
	public void insert(NodeAttribute attr) throws Exception {
		StringBuilder sql = new StringBuilder()
		.append("INSERT INTO T_ATTRIBUTE(`ATTR_ID`, `ATTR_NAME`, `ATTR_NAME_EN`, `ATTR_HEADER_ID`, `ATTR_DESC`, `ATTR_INDEX`) ")
		.append("VALUES(:id, :name, :englishName, :headerId, :desc, :index)");
		
		SqlParameterSource param = new BeanPropertySqlParameterSource(attr);
		int result = template.update(sql.toString(), param);
		if (result != 1) throw new Exception("插入T_ATTRIBUTE时返回结果不为1");
	}
	
	public static NodeAttributeDao getInstance() {
		return (NodeAttributeDao) Configuration.getBean("nodeAttributeDao");
	}
}
