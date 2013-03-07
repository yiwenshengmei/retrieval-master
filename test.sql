-- source E:/Projects/workspace_retrieval/retrieval-master/test.sql
SELECT 
	nd.id as "node_id",
	fi.path
FROM t_node nd
LEFT JOIN t_node_attribute attr on attr.node_id = nd.id
left join t_retrieval_data_source rds on rds.node_id = nd.id
left join t_node_feature f on f.rds_id = rds.id
left join t_feature_image fi on fi.feature_id = f.id