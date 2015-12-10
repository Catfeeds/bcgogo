package com.tonggou.andclient;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.tonggou.andclient.app.TongGouApplication;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public abstract class BDMapBaseActivity extends BaseActivity {
	protected static final String TAG = "BDMapBaseActivity";
	protected Resources mResources;
	protected Toast mToast;
	protected BMapManager mBMapManager;
	protected MapController mMapController;

	public static final float ZOOM_LEVEL = 14;
	public static final int SCAN_SPAN = 5000;
	public static final String BD09LL = "bd09ll";
	public static final int SUCCESS_FROM_GPS = 61;
	public static final int SUCCESS_FROM_NETWORK = 161;

	protected void showToast(String text) {
		if (mToast == null) {
			mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(text);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}
		mToast.show();
	}

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		mResources = getResources();
	}

	protected void initBMap(MKGeneralListener listener) {
		mBMapManager = new BMapManager(getApplicationContext());
		mBMapManager.init(TongGouApplication.strKey, listener);
	}

	protected void initMapController(MapView mapView) {
		mMapController = mapView.getController();
		mMapController.enableClick(true);
		mMapController.setZoom(ZOOM_LEVEL);
		mapView.setBuiltInZoomControls(true);
		mapView.showScaleControl(true);
	}

	protected LocationData getLocData(BDLocation location) {
		LocationData locData = new LocationData();
		locData.latitude = location.getLatitude();
		locData.longitude = location.getLongitude();
		locData.accuracy = location.getRadius();
		locData.direction = location.getDerect();

		return locData;
	}

	protected MKGeneralListener mMKGeneralListener = new MKGeneralListener() {
		@Override
		public void onGetNetworkState(int iError) {
			// һЩ����״̬�Ĵ�����ص�����
			if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
				Log.d(TAG, "�������Ӵ���!");
				// showToast("���������������");
			}
		}

		@Override
		public void onGetPermissionState(int iError) {
			// ��Ȩ�����ʱ����õĻص�����
			if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
				Log.d(TAG, "API KEY����!");
			}
		}
	};

	@Override
	protected void onStart() {
		super.onStart();
		if (mBMapManager != null) {
			mBMapManager.start();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mBMapManager != null) {
			mBMapManager.stop();
		}
	}

	@Override
	protected void onDestroy() {
		// if (mBMapManager != null) {
		// mBMapManager.destroy();
		// mBMapManager = null;
		// }
		super.onDestroy();
	}
}
