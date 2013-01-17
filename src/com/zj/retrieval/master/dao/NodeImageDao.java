package com.zj.retrieval.master.dao;

import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.zj.retrieval.master.Node;
import com.zj.retrieval.master.NodeImage;
import com.zj.retrieval.master.dao.mapper.NodeImageRowMapper;

public class NodeImageDao {
	private SimpleJdbcTemplate template;
	private final Logger logger = LoggerFactory.getLogger(NodeImageDao.class);
	
	public void insert(NodeImage img) throws Exception {
		StringBuilder sql = new StringBuilder()
		.append("insert into `t_image`(`img_id`, `img_path`, `img_node_id`) values(:id, :path, :nodeId)");
		
		SqlParameterSource param = new BeanPropertySqlParameterSource(img);
		int result = template.update(sql.toString(), param);
		if (result != 1) throw new Exception("插入image时返回结果不为1");
	}
	
	public List<NodeImage> queryByNodeId(String nodeId) throws Exception {
		String sql = "select * from t_image img where img.node_id = ?";
		return template.query(sql, new NodeImageRowMapper(), new Object[] { nodeId });
	}
	
	public void setDataSource(DataSource dataSource) {
		this.template = new SimpleJdbcTemplate(dataSource);
	}
}
