package com.tonggou.andclient;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.tonggou.andclient.myview.AbsViewHolderAdapter;
import com.tonggou.andclient.myview.DoubleListPopupWindow;
import com.tonggou.andclient.myview.SimpleTitleBar;
import com.tonggou.andclient.myview.SingleListPopupWindow;

/**
 * Υ�²�ѯ
 * @author lwz
 *
 */
public class TransgressQueryActivity extends AbsBackableActivity implements View.OnClickListener {
	
	private TextView mVehicleSelectText;	// ����ѡ���
	private TextView mCitySelectText;		// ����ѡ���
	private Button mQueryButton;			// ��ѯ��ť
	private ViewGroup mTotalDeductScoreContainer;	// �ۻ��۷ָ�����
	private TextView mTotalDeductScoreText;			// �ۻ��۷�
	private TextView mTotalForfeitText;				// �ۻ�����
	private ListView mQueryResultList;				// ��ѯ����б�
	private SingleListPopupWindow mVehicleSelectPopupWindow;		// ����ѡ�񵯳���
	private DoubleListPopupWindow mCitySelectPopupWindow;			// ����ѡ�񵯳���

	@Override
	protected int getContentLayout() {
		return R.layout.activity_transgress_query;
	}
	
	@Override
	protected void afterTitleBarCreated(SimpleTitleBar titleBar) {
		super.afterTitleBarCreated(titleBar);
		titleBar.setTitle(R.string.transgress_query_title);
		titleBar.setRightButtonText(R.string.set_bindcar_title);
		titleBar.setOnRightButtonClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				turnToBindCarsActivity();
			}
		});
	}
	
	/**
	 * ��ת�������������
	 */
	private void turnToBindCarsActivity() {
		Intent intent = new Intent(this, BindCarsActivity.class);
		startActivity(intent);
	}

	@Override
	protected void findViews() {
		super.findViews();
		
		mVehicleSelectText = (TextView) findViewById(R.id.select_vehicle_text);
		mCitySelectText = (TextView) findViewById(R.id.select_city_text);
		mQueryButton = (Button) findViewById(R.id.query_btn);
		mTotalDeductScoreContainer = (ViewGroup) findViewById(R.id.total_deduct_container);
		mTotalDeductScoreText = (TextView) findViewById(R.id.total_deduct_score);
		mTotalForfeitText = (TextView) findViewById(R.id.total_forfeit);
		mQueryResultList = (ListView) findViewById(R.id.query_result_list);
		afterViews();
	}

	private void afterViews() {
		setListener();
	}

	private void setListener() {
		mVehicleSelectText.setOnClickListener(this);
		mCitySelectText.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.select_vehicle_text: onSelectVehicle(); break;
			case R.id.select_city_text: onSelectCity(); break;
		}
	}

	/**
	 * ѡ����û��󶨵�����
	 */
	private void onSelectVehicle() {
		List<String> data = new ArrayList<String>();
		for( int i=0; i<9; i++ ) {
			data.add("��E1234" + i);
		}
		VehicleAadpter adapter = new VehicleAadpter(this, data, R.layout.popview_item);
		mVehicleSelectPopupWindow = new SingleListPopupWindow(this, adapter);
		mVehicleSelectPopupWindow.showAsDropDown(mVehicleSelectText);
	}
	
	/**
	 * ѡ���ѯΥ�µĳ���
	 */
	private void onSelectCity() {
		List<String> provinceData = new ArrayList<String>();
		List<String> cityData = new ArrayList<String>();
		
		for( int i=0; i<4; i++ ) {
			if( i<5 ) { 
				provinceData.add("����" + i);
			}
			
			cityData.add("����" + i);
		}
		
		VehicleAadpter provinceAdapter = new VehicleAadpter(this, provinceData, R.layout.popview_item);
		VehicleAadpter cityAdapter = new VehicleAadpter(this, cityData, R.layout.popview_item);
		mCitySelectPopupWindow = new DoubleListPopupWindow(this, provinceAdapter, cityAdapter);
		mCitySelectPopupWindow.showAsDropDown(mCitySelectText);
	}
	
	/**
	 * ��ʾ�ۼƿ۷ּ�����Ĳ���
	 * @param deductScore �۷���
	 * @param forfeit ������
	 */
	private void showTotalDeductContainer(String deductScore, String forfeit) {
		mTotalDeductScoreContainer.setVisibility(View.VISIBLE);
		mTotalDeductScoreText.setText(deductScore);
		mTotalForfeitText.setText(forfeit);
	}
	
	/**
	 * �����ۼƿ۷ּ�����Ĳ���
	 */
	private void hideTotalDeductContainer() {
		mTotalDeductScoreContainer.setVisibility(View.GONE);
	}

	class VehicleAadpter extends AbsViewHolderAdapter<String> {

		public VehicleAadpter(Context context, List<String> data, int layoutRes) {
			super(context, data, layoutRes);
		}

		@Override
		protected void setData(int pos, View convertView, String itemData) {
			TextView name = getViewFromHolder(convertView, R.id.popview_name);
			name.setText(itemData);
		}
		
	}
	
}
