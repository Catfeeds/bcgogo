package com.tonggou.andclient.network;

import android.content.Context;

@Deprecated
public abstract class UpdateCheck {
	protected Context context;
	
	public UpdateCheck(Context context) {
		this.context = context;
	}
	/**
     * @return false ��ʾ��鵽ǿ�Ƹ���,true���������ߵ�½���̣��汾������.
     */
	abstract public boolean checkUpgradeAction();
}
