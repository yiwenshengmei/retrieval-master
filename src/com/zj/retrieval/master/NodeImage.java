package com.zj.retrieval.master;


public class NodeImage {
	private String id;
	private String path;
	private String nodeId;
	private Node node;
	
	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public NodeImage(String path) {
		this.path = path;
	}
	
	public NodeImage() {}

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

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nodeId == null) ? 0 : nodeId.hashCode());
		result = prime * result + ((path == null) ? 0 : path.hashCode());
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
		NodeImage other = (NodeImage) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		if (nodeId == null) {
			if (other.nodeId != null)
				return false;
		}
		else if (!nodeId.equals(other.nodeId))
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		}
		else if (!path.equals(other.path))
			return false;
		return true;
	}
}
