package com.zj.retrieval.master;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractNodeCRUDAction extends AbstractAction {
	private String node_name, node_name_en, uri, uri_name, desc, json_node_attribute;
	private File[] images;
	private Logger logger = LoggerFactory.getLogger(AbstractNodeCRUDAction.class);
	
	@Override
	public void doExecute() throws Exception {
		DALService.doAction(new IDALAction() {
			
			@Override
			public Object doAction(Session sess, Transaction tx) throws Exception {
				Node node = new Node();
				node.setDesc(desc);
				node.setEnglishName(node_name_en);
				node.setName(node_name);
//				root.setNodeType(NodeType.NODETYPE_CLASS);
				node.setUri(uri);
				node.setUriName(node.getUri() + "#" + uri_name);
				
				// 解析images
				List<NodeImage> nodeImages = new ArrayList<NodeImage>();
				String realpath = ServletActionContext.getServletContext().getRealPath("/images");
				File folder = new File(realpath);
				if(!folder.exists()) {
					folder.mkdirs();
					logger.debug("用于保存图片的文件夹不存在，开始创建。" + folder.getPath());
				}
				if (null != images) {
					for (File srcfile : images) {
						String filename = UUID.randomUUID().toString() + ".jpg";
						File destfile = new File(folder, filename);
						FileUtils.copyFile(srcfile, destfile);
						nodeImages.add(new NodeImage("images/" + filename, node));
					}
				}
				node.setImages(nodeImages);
				
				// 解析自定义字段
				if (StringUtils.isNotBlank(json_node_attribute)) {
					JSONArray jNodeAttrs = new JSONArray(json_node_attribute);
					for (int i = 0; i < jNodeAttrs.length(); i++) {
						JSONObject jNodeAttr = jNodeAttrs.getJSONObject(i);
						
						node.getAttributes().add(new NodeAttribute(
								jNodeAttr.getString("key"), jNodeAttr.getString("value"), node));
					}
				}
				
				beforeSave(node, sess);
				sess.save(node);
				afterSave(node, sess);
				
				return null;
			}
		});
	}
	
	protected void beforeSave(Node node, Session sess) {
		
	}
	
	protected void afterSave(Node node, Session sess) {
		
	}
	
	public void setNode_name(String node_name) {
		this.node_name = node_name;
	}

	public void setNode_name_en(String node_name_en) {
		this.node_name_en = node_name_en;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public void setUri_name(String uri_name) {
		this.uri_name = uri_name;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setJson_node_attribute(String json_node_attribute) {
		this.json_node_attribute = json_node_attribute;
	}

	public void setImages(File[] images) {
		this.images = images;
	}
}
