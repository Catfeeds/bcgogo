package com.tonggou.andclient.jsonresponse;

import java.util.List;

import com.tonggou.andclient.vo.JuheTransgressArea;

/**
 * �ۺϳ����б���Ӧ����
 * @author lwz
 *
 */
public class JuheCityListResponse extends BaseResponse {

	private static final long serialVersionUID = 1737403159197750653L;

	private List<JuheTransgressArea> result;

	public List<JuheTransgressArea> getResult() {
		return result;
	}

	public void setResult(List<JuheTransgressArea> result) {
		this.result = result;
	}
	
}
