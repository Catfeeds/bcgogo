package com.tonggou.andclient.vo;

/**
 * @author fbl
 *
 */
public class AppShopCommentDTO {
	
	private float commentScore;  //����
	private String commentContent;  // ��������
	private String commentTimeStr;  //����ʱ��
	private String commentTime;     //
	private String commentatorName;  //���۵��û���
	
	
	
	@Override
	public String toString() {
		return "AppShopCommentDTO [commentScore=" + commentScore
				+ ", commentContent=" + commentContent + ", commentTimeStr="
				+ commentTimeStr + ", commentTime=" + commentTime
				+ ", commentatorName=" + commentatorName + "]";
	}
	public float getCommentScore() {
		return commentScore;
	}
	public void setCommentScore(float commentScore) {
		this.commentScore = commentScore;
	}
	public String getCommentContent() {
		return commentContent;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
	public String getCommentTimeStr() {
		return commentTimeStr;
	}
	public void setCommentTimeStr(String commentTimeStr) {
		this.commentTimeStr = commentTimeStr;
	}
	public String getCommentTime() {
		return commentTime;
	}
	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
	}
	public String getCommentatorName() {
		return commentatorName;
	}
	public void setCommentatorName(String commentatorName) {
		this.commentatorName = commentatorName;
	}
	
	
	
	

}
