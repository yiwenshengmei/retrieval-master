package com.zj.retrieval.master;

import javax.sql.DataSource;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public abstract class AbstractCRUDHandler {
	protected SimpleJdbcTemplate template;
	
	public void setDataSource(DataSource dataSource) {
		this.template = new SimpleJdbcTemplate(dataSource);
	}
}
