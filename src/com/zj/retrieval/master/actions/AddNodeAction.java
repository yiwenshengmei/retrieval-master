package com.zj.retrieval.master.actions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.zj.retrieval.master.Attribute;
import com.zj.retrieval.master.AttributeSelectedWrongException;
import com.zj.retrieval.master.AttributeSelector;
import com.zj.retrieval.master.Node;
import com.zj.retrieval.master.UserField;
import com.zj.retrieval.master.Util;
import com.zj.retrieval.master.dao.NodeDao;
import com.zj.retrieval.master.dao.UserDao;

public class AddNodeAction {
	private static Log log = LogFactory.getLog(AddNodeAction.class);
	private String desc;
	private String name_en;
	private String name;
	private int node_type;
	private String parent_id;
	private String uri;
	private String images;
	private String user_field;
	private String parent_attr;
	private String new_attr;
	
	private String message;
	private boolean isError;
	
	public boolean getIsError() {
		return this.isError;
	}
	
	private String post_user_name;
	private String post_user_password;
	
	public String execute() {
		try {
			UserDao userDao = Util.getUserDao();
			if (!userDao.verifyUser(post_user_name, post_user_password)) {
				this.isError = true;
				this.message = "用户名或密码错误.";
				return ActionSupport.ERROR;
			}
			
			Node new_node = new Node();
			new_node.setDesc(desc);
			new_node.setEnglishName(name_en);
			new_node.setName(name);
			new_node.setNodeType(node_type);
			new_node.setParentId(parent_id);
			new_node.setUri(uri);
			new_node.setUriName(new_node.getUri() + "#" + new_node.getEnglishName());
			
			// 解析images
			List<String> images_path = new ArrayList<String>();
			for (String image_id : images.split(";")) {
				images_path.add("images/" + image_id);
			}
			new_node.setImages(images_path);
			
			// 解析自定义字段
			if (user_field != null && !user_field.isEmpty()) {
				JSONArray user_field_jsonarray = new JSONArray(user_field);
				new_node.setUserfields(UserField.parse(user_field_jsonarray));
			}
			
			NodeDao ndService =  Util.getNodeDao();;
			
			Node parent_node = ndService.getNodeById(new_node.getParentId());
			log.info("找到父节点：" + parent_node);
			AttributeSelector attrSelector = ndService.getAttributeSelector(parent_node);
			String[] selectedAttributes = parent_attr.equals("") ?
				new String[0] : parent_attr.split(" ");
			for (int i = 0; i < selectedAttributes.length; i++) {
				int selectedAttribute = Integer.valueOf(selectedAttributes[i]);
				attrSelector.select(selectedAttribute, true);
				log.info(String.format("选择父节点属性[id=%1$s, name=%2$s]", selectedAttribute, 
						parent_node.getRetrievalDataSource().getAttributes().get(selectedAttribute).getName()));
			}
			JSONArray jNewAttributes = new JSONArray(new_attr);
			for (int i = 0; i < jNewAttributes.length(); i++) {
				JSONObject jAttr = jNewAttributes.getJSONObject(i);
				Attribute newAttr = new Attribute(jAttr.getString("new_attr_name"),
						                          jAttr.getString("new_attr_name_en"),
						                          jAttr.getString("new_attr_desc"),
						                          jAttr.getString("new_attr_image"));
				JSONArray jAttrUserfields = jAttr.getJSONArray("user_fields");
				newAttr.setUserFields(UserField.parse(jAttrUserfields));
				log.info("新添加的属性：" + newAttr);
				attrSelector.addNewAttribute(newAttr, true);
			}
			ndService.addNode(new_node, parent_node, attrSelector);
			
			this.message = "Success, o(∩_∩)o...";
			return ActionSupport.SUCCESS;
			
		} catch (JSONException e) {
			log.error("数据格式错误", e);
			this.message = "客户端程序发送数据格式错误！请使用最新的客户端程序。";
			return ActionSupport.ERROR;
		} catch (AttributeSelectedWrongException e) {
			log.info("不存在这样的父节点");
			this.message = "不存在这样的父节点属性！";
			return ActionSupport.ERROR;
		} catch (NumberFormatException ex) {
			log.info("客户端输入了错误的父节点属性");
			this.message = "父节点属性格式错误！";
			return ActionSupport.ERROR;
		} catch (Exception ex) {
			log.error("在添加子结点时发生未知错误", ex);
			this.message = "在添加子结点时发生未知错误";
			return ActionSupport.ERROR;
		}
	
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setName_en(String name_en) {
		this.name_en = name_en;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNode_type(int node_type) {
		this.node_type = node_type;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public void setUser_field(String user_field) {
		this.user_field = user_field;
	}

	public void setParent_attr(String parent_attr) {
		this.parent_attr = parent_attr;
	}

	public void setNew_attr(String new_attr) {
		this.new_attr = new_attr;
	}

	public String getMessage() {
		return message;
	}
}
