package com.zj.retrieval.master;

import java.util.UUID;

public class NodeImage {
	private String id;
	private String path;
	private String headerId;
	
	public NodeImage(String path, String headerId) {
		this.id = UUID.randomUUID().toString();;
		this.path = path;
		this.headerId = headerId;
	}
	
	public NodeImage(String path) {
		this.id = UUID.randomUUID().toString();;
		this.path = path;
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}

	public String getHeaderId() {
		return headerId;
	}

	public void setHeaderId(String headerId) {
		this.headerId = headerId;
	}
}
