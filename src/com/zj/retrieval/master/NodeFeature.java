package com.zj.retrieval.master;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.hibernate.classic.Session;

public class NodeFeature {

	public static final int YES    = 2;
	public static final int NO     = 1;
	public static final int UNKNOW = 3;
	
	private String desc = StringUtils.EMPTY;
	private String name = StringUtils.EMPTY;
	private String englishName = StringUtils.EMPTY;
	private Set<NodeImage> images;
	private String npde = StringUtils.EMPTY;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((englishName == null) ? 0 : englishName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + index;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NodeFeature other = (NodeFeature) obj;
		if (englishName == null) {
			if (other.englishName != null)
				return false;
		}
		else if (!englishName.equals(other.englishName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		if (index != other.index)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		return true;
	}

	private String id;
	private int index = -1;
	private Map<String, String> customerFields = new HashMap<String, String>();
	
	public NodeFeature(String name, String enName, String desc) {
		this.id = UUID.randomUUID().toString();
		this.desc = desc;
		this.name = name;
		this.englishName = enName;
	}
	
	public NodeFeature addImage(NodeImage img) {
		if (images == null) 
			images = new HashSet<NodeImage>();
		images.add(img);
		return this;
	}
	
	public NodeFeature withImage(NodeImage img) {
		return addImage(img);
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
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
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

	public Set<NodeImage> getImages() {
		return images;
	}

	public void setImages(Set<NodeImage> images) {
		this.images = images;
	}

	public Map<String, String> getCustomerFields() {
		return customerFields;
	}

	public void setCustomerFields(Map<String, String> customerFields) {
		this.customerFields = customerFields;
	}

	public String getEnName() {
		return englishName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
