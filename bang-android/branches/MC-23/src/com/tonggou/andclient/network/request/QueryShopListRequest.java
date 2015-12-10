package com.tonggou.andclient.network.request;

import com.tonggou.andclient.network.API;
import com.tonggou.andclient.vo.type.CoordinateType;
import com.tonggou.andclient.vo.type.ShopType;
import com.tonggou.andclient.vo.type.SortType;

/**
 * �����б��ѯ
 * @author lwz
 *
 */
public class QueryShopListRequest extends AbsTonggouHttpGetRequest {

	private final String KEY_COORDINATE_TYPE = "coordinateType";
	private final String KEY_COORDINATE = "coordinate";
	private final String KEY_SERVICE_SCOPE_IDS = "serviceScopeIds";
	private final String KEY_SORT_TYPE = "sortType";
	private final String KEY_AREA_ID = "areaId";
	private final String KEY_CITY_CODE = "cityCode";
	private final String KEY_SHOP_TYPE = "shopType";
	private final String KEY_KEYWORDS = "keywords";
	private final String KEY_IS_MORE = "isMore";
	private final String KEY_PAGE_NO = "pageNo";
	private final String KEY_PAGE_SIZE = "pageSize";
	
	/**
	 * ���� API ����
	 * @param coordinateType	{@link CoordinateType}
	 * @param coordinate		�������꣨��γ�ȣ���ʽ��lon,lat
	 * @param serviceScopeIds	���ŷָ�
	 * @param sortType			������� {@link SortType}
	 * @param areaId			����Id Ĭ��areaId ���������ʹ��cityCode
	 * @param cityCode			��ͼ�����еĳ��б��
	 * @param shopType			�̵����� {@link ShopType}
	 * @param keywords			�ؼ���
	 * @param isMore			��ʾ����
	 * @param pageNo			��ǰ��ҳ
	 * @param pageSize			��ҳ��С
	 */
	public void setApiParams(CoordinateType coordinateType, String coordinate, 
			String serviceScopeIds, SortType sortType, String areaId, String cityCode, 
			ShopType shopType, String keywords, boolean isMore, int pageNo, int pageSize) {
		
		APIQueryParam params = new APIQueryParam(false);
		params.put(KEY_COORDINATE_TYPE, coordinateType.getValue());
		params.put(KEY_COORDINATE, coordinate);
		params.put(KEY_SERVICE_SCOPE_IDS, serviceScopeIds);
		params.put(KEY_SORT_TYPE, sortType.getValue());
		params.put(KEY_AREA_ID, areaId);
		params.put(KEY_CITY_CODE, cityCode);
		params.put(KEY_SHOP_TYPE, shopType.getValue());
		params.put(KEY_KEYWORDS, keywords);
		params.put(KEY_IS_MORE, isMore);
		params.put(KEY_PAGE_NO, pageNo);
		params.put(KEY_PAGE_SIZE, pageSize);
		super.setApiParams(params);
	}
	
	/**
	 * ���� API ����
	 * @param coordinateType	{@link CoordinateType}
	 * @param coordinate		�������꣨��γ�ȣ���ʽ��lon,lat
	 * @param areaId			����Id Ĭ��areaId ���������ʹ��cityCode
	 * @param serviceScopeIds	���ŷָ�
	 * @param sortType			������� {@link SortType}
	 * @param areaId			����Id Ĭ��areaId ���������ʹ��cityCode
	 * @param cityCode			��ͼ�����еĳ��б��
	 * @param shopType			�̵����� {@link ShopType}
	 * @param keywords			�ؼ���
	 * @param isMore			��ʾ����
	 * @param pageNo			��ǰ��ҳ
	 * @param pageSize			��ҳ��С
	 */
	public void setGuestApiParams(CoordinateType coordinateType, String coordinate, 
			String areaId, String serviceScopeIds, SortType sortType, ShopType shopType, 
			String keywords, int pageNo, int pageSize) {
		
		APIQueryParam params = new APIQueryParam(false);
		params.put(KEY_COORDINATE_TYPE, coordinateType.getValue());
		params.put(KEY_COORDINATE, coordinate);
		params.put(KEY_AREA_ID, areaId);
		params.put(KEY_SERVICE_SCOPE_IDS, serviceScopeIds);
		params.put(KEY_SORT_TYPE, sortType.getValue());
		params.put(KEY_SHOP_TYPE, shopType.getValue());
		params.put(KEY_KEYWORDS, keywords);
		params.put(KEY_PAGE_NO, pageNo);
		params.put(KEY_PAGE_SIZE, pageSize);
		super.setGuestApiParams(params);
	}

	@Override
	public String getOriginApi() {
		return isGuestMode() ? API.GUEST.QUERY_SHOP_LIST : API.QUERY_SHOP_LIST;
	}
}
