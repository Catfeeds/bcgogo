package com.tonggou.andclient.vo;

import java.util.Map;

public class ShopScore {
	float   totalScore;//�ܷ�    float
	Map<String,Float> scoreItems;//������ϸ, mapkeyö���ɿ���ʱ����ʵ��ҵ��ȷ��
	public float getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(float totalScore) {
		this.totalScore = totalScore;
	}
	public Map<String, Float> getScoreItems() {
		return scoreItems;
	}
	public void setScoreItems(Map<String, Float> scoreItems) {
		this.scoreItems = scoreItems;
	}

}
