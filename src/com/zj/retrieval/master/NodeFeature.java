package com.zj.retrieval.master;

import java.util.List;
import org.apache.commons.lang.StringUtils;

public class NodeFeature {

	public static final int YES    = 2;
	public static final int NO     = 1;
	public static final int UNKNOW = 3;
	
	private String desc = StringUtils.EMPTY;
	private String name = StringUtils.EMPTY;
	private String englishName = StringUtils.EMPTY;
	private List<FeatureImage> images;
	private List<NodeAttribute> attributes;
	private RetrievalDataSource retrievalDataSource;
	private String id;
	private int index = -1;
	
	public NodeFeature() { }
	
	public NodeFeature(String name, String enName, String desc) {
		this.desc = desc;
		this.name = name;
		this.englishName = enName;
	}
	
	public NodeFeature withImages(List<FeatureImage> images) {
		this.images = images;
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

	public List<FeatureImage> getImages() {
		return images;
	}

	public void setImages(List<FeatureImage> images) {
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

	public List<NodeAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<NodeAttribute> attributes) {
		this.attributes = attributes;
	}

	public RetrievalDataSource getRetrievalDataSource() {
		return retrievalDataSource;
	}

	public void setRetrievalDataSource(RetrievalDataSource retrievalDataSource) {
		this.retrievalDataSource = retrievalDataSource;
	}
}
