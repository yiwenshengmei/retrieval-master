package com.zj.retrieval.master.actions;

public class FileUploadConfig {
	private String thumbfileFolderName = "thumbImages";
	private String originalfileFolderName = "originalImages";
	private int thumbfileHeight = 320;
	private int thumbfileWidth = 240;
	private String httpFileUploadField = "file_upload";
	private String thumbfileTag = "_thumb";
	
	public FileUploadConfig() {}
	
	public String getThumbfileFolderName() {
		return thumbfileFolderName;
	}
	public void setThumbfileFolderName(String thumbfileFolderName) {
		this.thumbfileFolderName = thumbfileFolderName;
	}
	public String getOriginalfileFolderName() {
		return originalfileFolderName;
	}
	public void setOriginalfileFolderName(String originalfileFolderName) {
		this.originalfileFolderName = originalfileFolderName;
	}
	public int getThumbfileHeight() {
		return thumbfileHeight;
	}
	public void setThumbfileHeight(int thumbfileHeight) {
		this.thumbfileHeight = thumbfileHeight;
	}
	public String getHttpFileUploadField() {
		return httpFileUploadField;
	}
	public void setHttpFileUploadField(String httpFileUploadField) {
		this.httpFileUploadField = httpFileUploadField;
	}
	public String getThumbfileTag() {
		return thumbfileTag;
	}
	public void setThumbfileTag(String thumbfileTag) {
		this.thumbfileTag = thumbfileTag;
	}

	public int getThumbfileWidth() {
		return thumbfileWidth;
	}

	public void setThumbfileWidth(int thumbfileWidth) {
		this.thumbfileWidth = thumbfileWidth;
	}
	
}
