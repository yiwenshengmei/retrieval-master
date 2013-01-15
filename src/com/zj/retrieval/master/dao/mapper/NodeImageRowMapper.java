package com.zj.retrieval.master.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import com.zj.retrieval.master.NodeImage;

public class NodeImageRowMapper implements ParameterizedRowMapper<NodeImage> {

	@Override
	public NodeImage mapRow(ResultSet rs, int arg1) throws SQLException {
		NodeImage image  = new NodeImage();
		image.setId(rs.getString("id"));
		image.setNodeId(rs.getString("nodeId"));
		image.setPath(rs.getString("path"));
		return image;
	}

}
