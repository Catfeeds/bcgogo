package com.tonggou.andclient.vo;

public class Pager {

	private int currentPage ;        	//��ǰ��ҳλ��  int
	private int pageSize;      			//��ҳ��С    int
	private boolean hasNextPage; 		// �Ƿ�����һҳ
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
	public boolean isHasNextPage() {
		return hasNextPage;
	}
	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}
}
