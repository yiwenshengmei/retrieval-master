package com.zj.retrieval.master.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import com.zj.retrieval.master.Attribute;
import com.zj.retrieval.master.AttributeSelector;
import com.zj.retrieval.master.DetailType;
import com.zj.retrieval.master.Matrix;
import com.zj.retrieval.master.Node;

public class NodeService {
	
	private SimpleJdbcTemplate sqlclient;
	private static Log log = LogFactory.getLog(NodeService.class);
	
	public List<Node> queryAllNodeAsBrief() {
		String sql = "select `id`, `name`, `parent_id` as parentId from `fish`, `detail_type` as detailType)";
		ParameterizedRowMapper<Node> rm = 
				ParameterizedBeanPropertyRowMapper.newInstance(Node.class);
		List<Node> queryResult = sqlclient.query(sql, rm);
		return queryResult;
	}
	
	public boolean deleteNode(Node nd) {
		try {
			// 原先的nd只有id信息，现在根据id从数据库中取出该node的完整信息
			nd = queryNodeById(nd.getId());
			// 找到它的父节点
			Node parent = queryNodeById(nd.getParentId());
			
			// 将nd在其父节点中的信息删除，即从父节点的子结点列表中删除nd
			RetrievalDataSource dataSource = parent.getRetrievalDataSource();
			int row = dataSource.getChildNodes().indexOf(nd.getId());
			dataSource.getChildNodes().remove(new Integer(nd.getId()));
			// 删除父节点矩阵中的相关信息
			dataSource.getMatrix().removeRow(row);
			// 更新父节点的owl
			parent.setOwl(Node.getOwlFromNode(parent, sqlclient));
			// 将父节点写回数据库
			String sqlUpdateParentNode = "UPDATE `fish` SET `owl`=:owl WHERE `id`=:id";
			SqlParameterSource paramUpdateParentNode = new BeanPropertySqlParameterSource(parent);
			if (sqlclient.update(sqlUpdateParentNode, paramUpdateParentNode) != 1) {
				throw new Exception("更新父类时失败@NodeService.delNode()"); // Rollback
			}
			
			// 开始从数据库中删除nd			
			String sql = "DELETE FROM `fish` where id=?";
			if (sqlclient.update(sql, nd.getId()) != 1)
				throw new Exception(String.format("删除节点[id=%1$s]时失败", nd.getId()));
			else
				return true;
		} catch (Exception ex) {
			log.error("delNode()发生错误", ex);
			return false;
		}
	}

	public boolean updateNode(Node nd) {
		// TODO Auto-generated method stub
		return false;
	}
		
	public Node queryNodeById(String id) {
		try {
			String sql = "select `id`, `uri_name` as uriName, `name`, " +
					"`name_en` as englishName, `parent_id` as parentId, " +
					"`owl`, `uri` from `fish` where `id`=?";
			ParameterizedRowMapper<Node> rowMapper = 
				ParameterizedBeanPropertyRowMapper.newInstance(Node.class);
			Node result = sqlclient.queryForObject(sql, rowMapper, id);
			
			Node.parseNodeFromOWL(result);
			return result;
		} catch (Exception e) {
			log.error(String.format("查询节点时出错[id=%1$s]", id), e);
			throw new RuntimeException("查询节点时出错@NodeServiceImpl.findNodeById()", e);
		}
	}

	public Node findNodeByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	public AttributeSelector getAttributeSelector(Node nd) {
		List<Integer> resultData = new ArrayList<Integer>();
		List<Attribute> attrs = nd.getRetrievalDataSource().getAttributes();
		for (int i = 0; i < attrs.size(); i++) {
			resultData.add(i);
		}
		return new AttributeSelector(resultData);
	}

	public void addNode(Node newNode, Node parentNode, AttributeSelector as) {
		try {
			// 更新parent的matrix属性
			log.info("更新父节点的特征矩阵");
			Matrix matrix = parentNode.getRetrievalDataSource().getMatrix();
			//   先修改行：将newNode和parentNode中已有特性的进行匹配
			//   如果matrix为空[列数或行数等于零]，从特征矩阵的语义上来说无论添加多少行
			//   都等于没有添加，因为没有已知的特性与其匹配
			int[] newRow = new int[matrix.getColSize()];
			for(int i = 0; i < newRow.length; i++)
				newRow[i] = as.getAttributeMapping().get(i) ? Attribute.YES : Attribute.NO;
			matrix.addRow(newRow, 0, newRow.length);

			//   再修改列：向parentNode添加创建newNode时一起添加的新特性
			//   在添加新特性的同时将新特性加入parentNode的attribute列表
			List<Attribute> parentAttributes = parentNode.getRetrievalDataSource().getAttributes();
			for(Attribute attr : as.getNewAttributeMapping().keySet()) {
				// matrix可能为空，向空矩阵添加1列需要的长度始终是1
				int[] newCol = matrix.getRowSize() == 0 ? new int[1] : new int[matrix.getRowSize()];
				for(int j = 0; j < newCol.length; j++) {
					newCol[j] = (j != newCol.length - 1 ? 0 : 
						(as.getNewAttributeMapping().get(attr) ? Attribute.YES : Attribute.NO));
				}
				matrix.addCol(newCol, 0, newCol.length);
				// 同时更新parentNode的attribte列表
				parentAttributes.add(attr);
			}
			
			// 创建并设置newNode的owl字符串
			log.info("创建并设置新节点的owl字符串");
			newNode.setOwl(Node.getOwlFromNode(newNode, sqlclient));
			
			// 向数据库中插入newNode并且获得自动生成的id值
			// 将id值设置到newNode中
			log.info("将新节点写入数据库");
			newNode.setId(UUID.randomUUID().toString());
			String sqlInsertNewNode = "insert into fish(`id`, `uriName`, `name`, " +
					"`enName`, `parentId`, `owl`, `uri`) values(:id, :uriName, :name, :enName, :parentId, :owl, :uri)";
			SqlParameterSource paramInsertNewNode = new BeanPropertySqlParameterSource(newNode) ;
			if (sqlclient.update(sqlInsertNewNode, paramInsertNewNode) != 1) {
				throw new Exception("插入节点时失败@NodeService.addNode()"); // Rollback
			}
	
			// 将newNode的id值加入parentNode的childNodes列表中
			log.info("更新父节点的子结点列表");
			parentNode.getRetrievalDataSource().getChildNodes().add(newNode.getId());
			// 更新parentNode的owl字符串
			log.info("更新父节点的owl字符串");
			parentNode.setOwl(Node.getOwlFromNode(parentNode, sqlclient));
					
			// 将parentNode重新写回数据库
			// 由于只修改了parentNode的owl信息，所以这里只更新owl字段
			log.info("将父节点写回数据库");
			String sqlUpdateParentNode = "update `fish` set `owl`=:owl where id=:id";
			SqlParameterSource paramUpdateParentNode = new BeanPropertySqlParameterSource(parentNode);
			if (sqlclient.update(sqlUpdateParentNode, paramUpdateParentNode) != 1) {
				throw new Exception("更新父类时失败@NodeService.addNode()"); // Rollback
			}
			
		} catch (Exception ex) {
			log.error("NodeService.addNode()方法发生错误", ex);
			throw new RuntimeException("NodeService.addNode()方法发生错误", ex);
		}
	}

	public void setDataSource(DataSource dataSource) {
		sqlclient = new SimpleJdbcTemplate(dataSource);
	}

	public void addRootNode(Node rootNode) {
		try {

			// 1.添加rootNode
			log.info("开始插入rootNode...");
			rootNode.setParentId(""); // 父节点保持patentId为空字符串
			String new_node_id = UUID.randomUUID().toString();
			rootNode.setId(new_node_id);
			rootNode.setOwl(Node.getOwlFromNode(rootNode, sqlclient));
			String sql = "insert into `fish`(`id`, `uri_name`, `name`, `name_en`, " +
					"`parent_id`, `owl`, `uri`) values(:id, :uriName, :name, :englishName, :parentId, :owl, :uri)";
			SqlParameterSource param = new BeanPropertySqlParameterSource(rootNode);
			int result = sqlclient.update(sql, param);
			
			if (result != 1) throw new Exception("插入rootNode时返回结果不等于1.");
			
			// 2.更新abstractRootNode[id="virtual_node"]节点的childNodes属性
			log.info("开始更新abstractRootNode的子节点列表...");
			String virtual_node_query_sql = "select `owl` from `fish` where `id`=?";
			List<Node> vrtl_node_query_result = sqlclient.query(virtual_node_query_sql, new ParameterizedRowMapper<Node>() {

				public Node mapRow(ResultSet rs, int rowNum) throws SQLException {
					Node vrtl_node = null;
					try {
						String owl = rs.getString("owl");
						if(owl == null || owl.equals("")) {
							log.error("VirtualNode存在，但是owl信息为空，这怎么可能？");
							return null;
						}
						vrtl_node = Node.parseVirtualNodeFromOWL(owl);
					} catch (Exception e) {
						log.error("解析虚拟节点中的owl时出错。");
						return null;
					}
					return vrtl_node;
				}
			}, Node.VIRTUAL_NODE_NAME);
			
			Node virtual_node = null;
			if (vrtl_node_query_result == null) {
				log.error("查询虚拟节点时出错。");
				throw new Exception("查询虚拟节点时出错。");
			}
			if (vrtl_node_query_result.size() == 0) {
				// 这说明id="VirtualNode"的记录不存在
				// 这种情况通常只发生在程序第一次运行时
				// 创建一个新的虚根节点，设置id="VirtualNode"，并写入数据库
				log.info("因为abstractRootNode记录不存在，将新建一个abstractRootNode");
				virtual_node = new Node();
				int insert_vrtl_node_result = sqlclient.getJdbcOperations().update(
						"INSERT INTO `fish`(" +
						"`id`, `uri_name`, `name`, `name_en`, `parent_id`, `owl`, `uri`) " +
						"VALUES('virtual_node','','','',-1,'','');"
				);
				if (insert_vrtl_node_result != 1) {
					log.error("创建VirtualNode失败。");
					throw new Exception("创建VirtualNode失败。");
				}
				log.info("新建的VirtualNode插入数据库成功");
			} else {
				virtual_node = vrtl_node_query_result.get(0);
			}
			
			virtual_node.getRetrievalDataSource().getChildNodes().add(new_node_id);
	
			// 重建VirtualNode的owl字段
			virtual_node.setOwl(Node.getOwlFromNode(virtual_node, sqlclient));
			log.info("生成新的owl字符串：" + virtual_node.getOwl());
			
			// 将VirtualNode写回数据库
			log.info("开始将VirtualNode写回数据...");
			String update_vrtl_node_sql = "update `fish` set `owl`=:owl where `id`='virtual_node'";
			SqlParameterSource vrtl_node_param = new BeanPropertySqlParameterSource(virtual_node);
			sqlclient.update(update_vrtl_node_sql, vrtl_node_param);
			log.info("VirtualNode写回数据库成功");
			
		} catch (Exception ex) {
			log.error("添加rootNode时发生错误，事务将回滚", ex);
			throw new RuntimeException("添加rootNode时发生错误", ex); // 回滚
		}
	}

	public void addNodeBrief(Node newNode, Node parentNode,
			AttributeSelector as) {
		try {
			// 更新parent的matrix属性
			log.info("更新父节点的特征矩阵");
			Matrix matrix = parentNode.getRetrievalDataSource().getMatrix();
			//   先修改行：将newNode和parentNode中已有特性的进行匹配
			//   如果matrix为空[列数或行数等于零]，从特征矩阵的语义上来说无论添加多少行
			//   都等于没有添加，因为没有已知的特性与其匹配
			int[] newRow = new int[matrix.getColSize()];
			for(int i = 0; i < newRow.length; i++)
				newRow[i] = as.getAttributeMapping().get(i) ? Attribute.YES : Attribute.NO;
			matrix.addRow(newRow, 0, newRow.length);

			//   再修改列：向parentNode添加创建newNode时一起添加的新特性
			//   在添加新特性的同时将新特性加入parentNode的attribute列表
			List<Attribute> parentAttributes = parentNode.getRetrievalDataSource().getAttributes();
			for(Attribute attr : as.getNewAttributeMapping().keySet()) {
				// matrix可能为空，向空矩阵添加1列需要的长度始终是1
				int[] newCol = matrix.getRowSize() == 0 ? new int[1] : new int[matrix.getRowSize()];
				for(int j = 0; j < newCol.length; j++) {
					newCol[j] = (j != newCol.length - 1 ? 0 : 
						(as.getNewAttributeMapping().get(attr) ? Attribute.YES : Attribute.NO));
				}
				matrix.addCol(newCol, 0, newCol.length);
				// 同时更新parentNode的attribte列表
				parentAttributes.add(attr);
			}
			
			// 创建并设置newNode的owl字符串
//			log.info("创建并设置新节点的owl字符串");
//			newNode.setOwl(Node.getOwlFromNode(newNode, sqlclient));
			
			log.info("将新节点写入数据库");
			// 注意，brief节点的ID值是由客户端提供的！！！
			// 为新节点detailType设置值: brief
			newNode.setDetailType(DetailType.BRIEF);
			String sqlInsertNewNode = "insert into fish(`id`, `name`, `detail_type`" +
					"`enName`, `parentId`, `contact`) values(:id, :name, :enName, :parentId, :detailType, :contact)";
			SqlParameterSource paramInsertNewNode = new BeanPropertySqlParameterSource(newNode) ;
			if (sqlclient.update(sqlInsertNewNode, paramInsertNewNode) != 1) {
				throw new Exception("插入节点时失败@NodeService.addNode()"); // Rollback
			}
	
			// 将newNode的id值加入parentNode的childNodes列表中
			log.info("更新父节点的子结点列表");
			parentNode.getRetrievalDataSource().getChildNodes().add(newNode.getId());
			// 更新parentNode的owl字符串
			log.info("更新父节点的owl字符串");
			parentNode.setOwl(Node.getOwlFromNode(parentNode, sqlclient));
					
			// 将parentNode重新写回数据库
			// 由于只修改了parentNode的owl信息，所以这里只更新owl字段
			log.info("将父节点写回数据库");
			String sqlUpdateParentNode = "update `fish` set `owl`=:owl where id=:id";
			SqlParameterSource paramUpdateParentNode = new BeanPropertySqlParameterSource(parentNode);
			if (sqlclient.update(sqlUpdateParentNode, paramUpdateParentNode) != 1) {
				throw new Exception("更新父类时失败@NodeService.addNode()"); // Rollback
			}
			
		} catch (Exception ex) {
			log.error("NodeService.addNode()方法发生错误", ex);
			throw new RuntimeException("NodeService.addNode()方法发生错误", ex);
		}
	}
}
