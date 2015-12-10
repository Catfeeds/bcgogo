package com.tonggou.andclient;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviPara;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
//import com.tonggou.andclient.StoreMapActivity.MyOverlay;
import com.tonggou.andclient.app.TongGouApplication;

public class StoreDetilMapActivity extends BaseActivity{// ��λ���

	private RouteOverlay routeOverlay = null; 
	//��λͼ��
	private ItemizedOverlay mOverlay = null;
	private LocationData locData = null;
	private MyLocationOverlay myLocationOverlay = null;
	//��������ͼ��
	private GeoPoint  pt1,pt2;
	private MKSearch mSearch = null;	// ����ģ�飬Ҳ��ȥ����ͼģ�����ʹ��
	private ProgressBar topProgress;
	private MapView mMapView = null;
	private View back,list;
	private Double lat,lot,lat0,lot0;
	private MapController mMapController = null;
	boolean first=true;
	private String latStr ;  //γ�� 
	private String lonStr ;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		TongGouApplication app = (TongGouApplication)this.getApplication();
		
		setContentView(R.layout.storedetilmap);
		topProgress=(ProgressBar) findViewById(R.id.shopdetilmappro);  
		lat=getIntent().getExtras().getDouble("lat");
		lot=getIntent().getExtras().getDouble("lot");
		
		latStr = sharedPreferences.getString(BaseActivity.LOCATION_LAST_POSITION_LAT, "");  //γ�� 
		lonStr = sharedPreferences.getString(BaseActivity.LOCATION_LAST_POSITION_LON, "");  //���� 
		if(latStr!=null&&!"".equals(latStr)&&lonStr!=null&&!"".equals(lonStr)){
			try{
				lat0=Double.parseDouble(latStr);
				lot0=Double.parseDouble(lonStr);
			}catch(NumberFormatException ex){}
		}
		int latFrom = (int) (lat0*1E6);
		int lonFrom = (int) (lot0 *1E6);   	
		pt1 = new GeoPoint(latFrom, lonFrom);                               //�ҵ�λ�� 

		int latTo = (int) (lat *1E6);
		int lonTo = (int) (lot *1E6);
		pt2 = new GeoPoint(latTo, lonTo);                                   //Ŀ��λ��
		
		
		back=findViewById(R.id.left_button);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				StoreDetilMapActivity.this.finish();
			}
		});

		list=findViewById(R.id.right_button);
		list.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				StoreDetilMapActivity.this.finish();
				/*Intent intent=new Intent(StoreDetilMapActivity.this,StoreDetilActivity.class);
				startActivity(intent);
				StoreDetilMapActivity.this.finish();*/
			}
		});
		//��ͼ��ʼ��
		mMapView = (MapView)findViewById(R.id.map_View);
		mMapController = mMapView.getController();
		mMapView.getController().setZoom(14);
		mMapView.getController().enableClick(true);
		mMapView.setBuiltInZoomControls(true);
		//���� ��������ͼ��
		//createPaopao();





		mSearch = new MKSearch();
		mSearch.init(app.mBMapManager, new MKSearchListener(){

			public void onGetDrivingRouteResult(MKDrivingRouteResult res,
					int error) {
				//�����յ������壬��Ҫѡ�����ĳ����б���ַ�б�
				if (error == MKEvent.ERROR_ROUTE_ADDR){
					return;
				}
				// ����ſɲο�MKEvent�еĶ���
				if (error != 0 || res == null) {
					Toast.makeText(StoreDetilMapActivity.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_SHORT).show();
					topProgress.setVisibility(View.INVISIBLE);
					return;
				}
				routeOverlay = new RouteOverlay(StoreDetilMapActivity.this, mMapView);

				routeOverlay.setData(res.getPlan(0).getRoute(0));
				//�������ͼ��
				mMapView.getOverlays().clear();
				//���·��ͼ��
				mMapView.getOverlays().add(routeOverlay);
				//ִ��ˢ��ʹ��Ч
				mMapView.refresh();
				// ʹ��zoomToSpan()���ŵ�ͼ��ʹ·������ȫ��ʾ�ڵ�ͼ��
				mMapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(), routeOverlay.getLonSpanE6());
				//�ƶ���ͼ�����
				mMapView.getController().animateTo(res.getStart().pt);
				topProgress.setVisibility(View.INVISIBLE);
			}

			public void onGetTransitRouteResult(MKTransitRouteResult res,
					int error) {}	
			public void onGetWalkingRouteResult(MKWalkingRouteResult res,
					int error) {}	
			public void onGetAddrResult(MKAddrInfo res, int error) {}
			public void onGetPoiResult(MKPoiResult res, int arg1, int arg2) {}
			public void onGetBusDetailResult(MKBusLineResult result, int iError) {}
			@Override
			public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {}
			@Override
			public void onGetPoiDetailSearchResult(int type, int iError) {}
			@Override
			public void onGetShareUrlResult(MKShareUrlResult result, int type,
					int error) {}
		});
		//��λͼ���ʼ��
		myLocationOverlay = new MyLocationOverlay(mMapView);
		locData = new LocationData();
		if(latStr!=null&&!"".equals(latStr)&&lonStr!=null&&!"".equals(lonStr)){
			try{
				locData.latitude = Double.parseDouble(latStr);
				locData.longitude =Double.parseDouble(lonStr);
			}catch(NumberFormatException ex){}
		}
		
//		locData.latitude = TongGouApplication.getInstance().bdlocation.getLatitude();
//		locData.longitude =TongGouApplication.getInstance().bdlocation.getLongitude();
		myLocationOverlay.setData(locData);
		mOverlay = new ItemizedOverlay(getResources().getDrawable(R.drawable.position),mMapView);	
		OverlayItem item = new OverlayItem(pt2,"","");
		mOverlay.addItem(item);
		
		//��Ӷ�λͼ��
		mMapView.getOverlays().add(myLocationOverlay);
		mMapView.getOverlays().add(mOverlay);
		myLocationOverlay.enableCompass();
		mMapController.setCenter(pt2);
		
		//�޸Ķ�λ���ݺ�ˢ��ͼ����Ч
		mMapView.refresh();
		//SearchButtonProcess();
		startNavi();
	}
	public void startNavi(){	
		// ���� ��������
		NaviPara para = new NaviPara();
		para.startPoint = pt1;
		para.startName= "�����￪ʼ";
		para.endPoint  = pt2;
		para.endName   = "���������";
	try {

			BaiduMapNavigation.openBaiduMapNavi(para, this);
			topProgress.setVisibility(View.INVISIBLE);
			StoreDetilMapActivity.this.finish();
		} catch (BaiduMapAppNotSupportNaviException e) {
			e.printStackTrace();
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("����δ��װ�ٶȵ�ͼapp��app�汾���ͣ����ȷ�ϰ�װ��");
			builder.setTitle("��ʾ");
			builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					topProgress.setVisibility(View.INVISIBLE);
					BaiduMapNavigation.GetLatestBaiduMapApp(StoreDetilMapActivity.this);
				}
			}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					//��������
					SearchButtonProcess();
				}
			});

			builder.create().show();
		}
	}




	 @Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mMapView.onRestoreInstanceState(savedInstanceState);
	}
	void SearchButtonProcess() {

		// ������յ��name���и�ֵ��Ҳ����ֱ�Ӷ����긳ֵ����ֵ�����򽫸��������������
		MKPlanNode stNode = new MKPlanNode();
		stNode.pt = pt1;
		MKPlanNode enNode = new MKPlanNode();
		enNode.pt = pt2;
	
		mSearch.drivingSearch(null, stNode,null, enNode);
	}
}
