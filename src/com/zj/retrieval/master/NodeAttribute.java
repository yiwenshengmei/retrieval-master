package com.zj.retrieval.master;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

public class NodeAttribute {

	public static final int YES    = 2;
	public static final int NO     = 1;
	public static final int UNKNOW = 3;
	
	private String desc = StringUtils.EMPTY;
	private String name = StringUtils.EMPTY;
	private String enName = StringUtils.EMPTY;
	private List<NodeImage> images;
	private String headerId = StringUtils.EMPTY;
	private String id;
	private int index = -1;
	private Map<String, String> customerFields = new HashMap<String, String>();
	
	public NodeAttribute(String name, String enName, String desc) {
		this.id = UUID.randomUUID().toString();
		this.desc = desc;
		this.name = name;
		this.enName = enName;
	}
	
	public NodeAttribute addImage(NodeImage img) {
		if (images == null) 
			images = new ArrayList<NodeImage>();
		img.setHeaderId(this.id);
		images.add(img);
		return this;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEnglishName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	public String getHeaderId() {
		return headerId;
	}

	public void setHeaderId(String headerId) {
		this.headerId = headerId;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public List<NodeImage> getImages() {
		return images;
	}

	public void setImages(List<NodeImage> images) {
		this.images = images;
	}

	public Map<String, String> getCustomerFields() {
		return customerFields;
	}

	public void setCustomerFields(Map<String, String> customerFields) {
		this.customerFields = customerFields;
	}

	public String getEnName() {
		return enName;
	}
}
