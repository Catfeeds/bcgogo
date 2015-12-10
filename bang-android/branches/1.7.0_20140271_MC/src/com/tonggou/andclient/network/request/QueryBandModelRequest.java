package com.tonggou.andclient.network.request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.text.TextUtils;

import com.tonggou.andclient.network.API;

public class QueryBandModelRequest extends AbsTonggouHttpGetRequest {

	@Override
	public String getOriginApi() {
		return API.QUERY_VEHICLE_BRAND_MODEL;
	}
	
	/**
	 * 
	 * @param keywords			can be null
	 * @param isQueryBrand		�Ƿ��ѯƷ�ƣ� true ��ѯƷ�ƣ� false ��ѯ����
	 * @param brandId			�� isQueryBrand = false, brandId ����Ϊ null, ����Ϊ null
	 */
	public void setApiParams(String keywords, boolean isQueryBrand, String brandId) {
		APIQueryParam params = new APIQueryParam();
		params.put("keywords", getEncodeStr(keywords));
		params.put("type", isQueryBrand ? "brand" : "model");
		params.put("brandId", 
				( TextUtils.isEmpty(brandId) || isQueryBrand ) ? "NULL" : brandId);
		super.setApiParams(params);
	}
	
	private String getEncodeStr(String str) {
		if( !TextUtils.isEmpty(str) ) {
			try {
				return URLEncoder.encode(str, "UTF-8");
			} catch (UnsupportedEncodingException e) {
			}
		}
		return "NULL";
	}

}
