package com.tonggou.andclient.vo;

public class Pager {

	private int currentPage ;        //��ǰ��ҳλ��  int
	private int pageSize;      //��ҳ��С    int
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
