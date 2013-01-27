package com.zj.retrieval.master;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

public class NodeFeature {

	public static final int YES    = 2;
	public static final int NO     = 1;
	public static final int UNKNOW = 3;
	
	private String desc = StringUtils.EMPTY;
	private String name = StringUtils.EMPTY;
	private String englishName = StringUtils.EMPTY;
	private Set<FeatureImage> images;
	private Set<NodeAttribute> attributes;
	private RetrievalDataSource retrievalDataSource;
	private String id;
	private int index = -1;
	
	public NodeFeature() { }
	
	public NodeFeature(String name, String enName, String desc) {
		this.id = UUID.randomUUID().toString();
		this.desc = desc;
		this.name = name;
		this.englishName = enName;
	}
	
	public NodeFeature addImage(FeatureImage img) {
		if (images == null) 
			images = new HashSet<FeatureImage>();
		images.add(img);
		return this;
	}
	
	public NodeFeature withImage(FeatureImage img) {
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

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Set<FeatureImage> getImages() {
		return images;
	}

	public void setImages(Set<FeatureImage> images) {
		this.images = images;
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

	public Set<NodeAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(Set<NodeAttribute> attributes) {
		this.attributes = attributes;
	}

	public RetrievalDataSource getRetrievalDataSource() {
		return retrievalDataSource;
	}

	public void setRetrievalDataSource(RetrievalDataSource retrievalDataSource) {
		this.retrievalDataSource = retrievalDataSource;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((desc == null) ? 0 : desc.hashCode());
		result = prime * result + ((englishName == null) ? 0 : englishName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((images == null) ? 0 : images.hashCode());
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
		if (desc == null) {
			if (other.desc != null)
				return false;
		}
		else if (!desc.equals(other.desc))
			return false;
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
		if (images == null) {
			if (other.images != null)
				return false;
		}
		else if (!images.equals(other.images))
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
}
