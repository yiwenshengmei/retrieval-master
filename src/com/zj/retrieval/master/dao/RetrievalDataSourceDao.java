package com.zj.retrieval.master.dao;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.zj.retrieval.master.Configuration;
import com.zj.retrieval.master.NodeAttribute;

public class RetrievalDataSourceDao {
	private SimpleJdbcTemplate template;
	
	public void setDataSource(DataSource dataSource) {
		this.template = new SimpleJdbcTemplate(dataSource);
	}
	
	public void insert(RetrievalDataSource rds) throws Exception {
		
		String sql = "INSERT INTO T_RETRIEVAL_DATA_SOURCE(`RDS_ID`, `RDS_HEADER_ID`) VALUES(:id, :headerId)";
		SqlParameterSource param = new MapSqlParameterSource()
			.addValue("id", rds.getId())
			.addValue("headerId", rds.getHeaderId());
		template.update(sql, param);
		
		NodeAttributeDao attrdao = NodeAttributeDao.getInstance();
		for (NodeAttribute attr : rds.getAttributes()) {
			attrdao.insert(attr);
		}
		
		RetrievalDataSourceChildDao.getInstance().insert(rds.getChildNodes(), rds.getId());
		
		MatrixDao.getInstance().insert(rds.getMatrix());
	}
	
	public static RetrievalDataSourceDao getInstance() {
		return (RetrievalDataSourceDao) Configuration.getBean("retrievalDataSourceDao");
	}
}