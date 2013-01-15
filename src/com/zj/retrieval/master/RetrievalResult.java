package com.zj.retrieval.master;

import java.util.ArrayList;
import java.util.List;

public class RetrievalResult {
	private boolean hasResult;
	private NodeAttribute next;
	private List<String> result;
	private String lastState;
	
	public RetrievalResult() {
		hasResult = false;
		next = null;
		result = new ArrayList<String>();
		lastState = "";
	}
	
	public boolean hasResult() {
		return hasResult;
	}
	public void hasResult(boolean hasResult) {
		this.hasResult = hasResult;
	}
	public NodeAttribute getNext() {
		return next;
	}
	public void setNext(NodeAttribute next) {
		this.next = next;
		hasResult = false;
	}
	public List<String> getResult() {
		return result;
	}
	public void setResult(List<String> result) {
		this.result = result;
		hasResult = true;
	}
	public String getLastState() {
		return lastState;
	}
	public void setLastState(String lastState) {
		this.lastState = lastState;
	}
	
	
}
