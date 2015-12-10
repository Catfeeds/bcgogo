package com.tonggou.andclient.network.parser;

import com.tonggou.andclient.jsonresponse.BaseResponse;

/**
 * ���ر��ػ�����첽���������������������
 * 
 * <p>��������ʱ������� {@link #onLoadCache(T)}, ���������������������ڷ���<b>�������</b>
 * @author lwz
 *
 * @param <T>
 */
public abstract class AsyncLoadCacheJsonBaseResponseParseHandler<T extends BaseResponse> 
								extends AsyncLoadCacheJsonResponseParseHandler<T> {

	@Override
	public IResponseParser<T> getJsonResponseParser() {
		return new BaseResponseParser<T>(getTypeClass());
	}
	
	public abstract Class<T> getTypeClass();
	
}
