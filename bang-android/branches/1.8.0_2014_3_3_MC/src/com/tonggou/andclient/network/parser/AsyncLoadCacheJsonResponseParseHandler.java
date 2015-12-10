package com.tonggou.andclient.network.parser;

import com.tonggou.andclient.BaseActivity;
import com.tonggou.andclient.app.TongGouApplication;
import com.tonggou.andclient.db.LocalCacheDao;
import com.tonggou.andclient.jsonresponse.BaseResponse;
import com.tonggou.andclient.util.PreferenceUtil;

/**
 * ���ر��ػ�����첽�����������������
 * 
 * <p>��������ʱ������� {@link #onLoadCache(T)}, ���������������������ڷ���ֻ�����  {@link #onFinish()}
 * <p>NOTE: {@link #onLoadCache(T)}�������� {@link com.loopj.android.http.AsyncHttpResponseHandle #onStart()} ����֮ǰ������
 * @author lwz
 *
 * @param <T>
 */
public abstract class AsyncLoadCacheJsonResponseParseHandler<T extends BaseResponse> 
							extends AsyncJsonResponseParseHandler<T> implements ILoadCacheHandler<T> {

	private String cacheKey;
	
	@Override
	public void onParseSuccess(T result, String originResult) {
		LocalCacheDao.store(getUserNo(), cacheKey, originResult);
		super.onParseSuccess(result, originResult);
	}
	
	/**
	 * �� onLoadCache() ǰ��<b>�Զ�����<b>�÷�����������ȷ�Ļ���洢��ֵ
	 * @param cacheKey
	 */
	public void setCacheKey(String cacheKey) {
		this.cacheKey = cacheKey;
	}
	
	/**
	 * �����������û��޹�ʱ�븲д�÷��������� null ����
	 * @return
	 */
	public String getUserNo() {
		return PreferenceUtil.getString(TongGouApplication.getInstance(), BaseActivity.SETTING_INFOS, BaseActivity.NAME);
	}
	
	/**
	 * ������Ҫ�Զ��建��洢��ֵʱ�����಻��Ҫ��д�˷���
	 * @return
	 */
	public String getCacheKey() {
		return null;
	}
	
	/**
	 * �Ƿ񻺴�
	 * <p><b> �� ����ֵΪ false, ��ô����ص�  onLoadCache() ���� 
	 * @return true ���棬 false ������
	 */
	public boolean isCache() {
		return true;
	}
	
}
