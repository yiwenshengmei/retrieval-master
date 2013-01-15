package com.zj.retrieval.master.dao;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.zj.retrieval.master.NodeAttribute;

public class NodeAttributeDao {
	private SimpleJdbcTemplate template;
	
	public void setDateSource(DataSource dataSource) {
		this.template = new SimpleJdbcTemplate(dataSource);
	}
	
	public void insert(NodeAttribute attr) throws Exception {
		StringBuilder sql = new StringBuilder()
		.append("insert into t_node_attribute(`attr_id`, `attr_name`, `attr_name_en`, `attr_node_id`, `attr_desc`) ")
		.append("values(:id, :name, :englishName, :nodeId, :desc)");
		
		SqlParameterSource param = new BeanPropertySqlParameterSource(attr);
		int result = template.update(sql.toString(), param);
		if (result != 1) throw new Exception("插入T_NODE_ATTRIBUTE时返回结果不为1");
	}
}
