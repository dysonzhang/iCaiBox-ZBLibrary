package com.icaihe.model;

import java.io.Serializable;
import java.util.List;

public class NoticeList implements Serializable {

	private static final long serialVersionUID = -6677501901294583592L;
	
	private int pageNo;
	private int pageSize;
	private int totalRecord;
	private int totalPage;
	private List<Notice> results;

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public List<Notice> getResults() {
		return results;
	}

	public void setResults(List<Notice> results) {
		this.results = results;
	}

}
