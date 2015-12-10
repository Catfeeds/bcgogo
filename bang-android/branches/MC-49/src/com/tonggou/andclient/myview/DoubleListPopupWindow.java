package com.tonggou.andclient.myview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.tonggou.andclient.R;
import com.tonggou.andclient.app.TongGouApplication;
import com.tonggou.andclient.util.ScreenSizeUtil;

/**
 * �����б�ĵ�����
 * <p>ContentView ������ ListView ��PopupWindow
 * 
 * @author lwz
 *
 */
public class DoubleListPopupWindow implements OnItemClickListener {
	private Context mContext;
	private PopupWindow mPopupWindow;
	private ListView mLeftListView;
	private ListView mRightListView;
	private ListAdapter mLeftAdapter;
	private ListAdapter mRightAdapter;

	private ScreenSizeUtil screenSizeUtil;
	private OnItemClickListener mLeftItemClickListener;
	private OnItemClickListener mRightItemClickListener;
	
	private int mBackgroundRes = R.drawable.shopschooseback2;
	private int mItemWidth;
	private int mItemHeight;
	
	public DoubleListPopupWindow(Context context, ListAdapter leftAdapter, ListAdapter rightAdapter) {
		mContext = context;
		mLeftAdapter = leftAdapter;
		mRightAdapter = rightAdapter;
		screenSizeUtil = new ScreenSizeUtil(context);
		init();
	}
	
	private void init() {
		View contentView = View.inflate(mContext, R.layout.popup_double_list, null);
		mLeftListView = (ListView) contentView.findViewById(R.id.left_list);
		mRightListView = (ListView) contentView.findViewById(R.id.right_list);
		mLeftListView.setAdapter(mLeftAdapter);
		mRightListView.setAdapter(mRightAdapter);
		mLeftListView.setOnItemClickListener(this);
		mRightListView.setOnItemClickListener(this);
		
		mPopupWindow = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		setBackground(R.drawable.shopschooseback2);
		if( mBackgroundRes > 0 ) {
			mPopupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(mBackgroundRes));
		}
		mPopupWindow.setFocusable(true);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.update();
	}
	
	/**
	 * ���ñ���ͼƬ
	 * @param resId
	 */
	public void setBackground(int resId) {
		mBackgroundRes = resId;
	}
	
	/**
	 * ���� ListView item �Ŀ�ߣ�popupWindow ���Դ�Ϊ��׼��ʾ
	 * @param width
	 * @param height
	 */
	public void setListItemWidthAndHeight(int width, int height) {
		mItemWidth = width;
		mItemHeight = height;
	}
	
	/**
	 * ��� ListItem �Ŀ�ȣ���ֵ��Ĭ��ֵ
	 * @return
	 */
	public int getListItemWidth() {
		if( mItemWidth <= 0 ) {
			mItemWidth = mContext.getResources().getDimensionPixelSize(R.dimen.popup_list_width) * 2;
		} 
		return mItemWidth;
	}
	
	/**
	 * ��� ListItem �ĸ߶ȣ���ֵ��Ĭ��ֵ
	 * @return
	 */
	public int getListItemHeight() {
		if( mItemHeight <= 0 ) {
			mItemHeight = mContext.getResources().getDimensionPixelSize(R.dimen.popup_list_child_height);
		} 
		return mItemHeight;
	}
	
	/**
	 * ��ê����·���ʾ������
	 * @param anchor ê��
	 */
	public void showAsDropDown(View anchor) {
		if( mPopupWindow == null ) {
			init();
		}
		int[] location = new int[]{0, 0};
		anchor.getLocationInWindow(location);
		int listViewWidth = getListItemWidth();
		int listViewHeight = getMeasureHeight(mLeftListView, mRightListView);
		TongGouApplication.showLog(screenSizeUtil.getScreenHeight() - location[1] + "  " + listViewHeight);
		if( screenSizeUtil.getScreenHeight() - location[1] > listViewHeight ) {
			mPopupWindow.setHeight(listViewHeight);
		}
		if( screenSizeUtil.getScreenWidth() > listViewWidth ) {
			mPopupWindow.setWidth(listViewWidth);
		}
		mPopupWindow.showAsDropDown(anchor);
	}
	
	/**
	 * ���� �߶���ߵ� listView �ĸ߶���Ϊ�ܸ߶�
	 * @param leftList, rightList
	 * @return
	 */
	private int getMeasureHeight(ListView leftList, ListView rightList) {
        ListAdapter leftAdapter = leftList.getAdapter();
        ListAdapter rightAdapter = rightList.getAdapter();
        int childCount = Math.max(leftAdapter.getCount(), rightAdapter.getCount());
        int listDividerHeight = Math.max(leftList.getDividerHeight(), rightList.getDividerHeight());
        return (getListItemHeight() + listDividerHeight) * ( childCount + 1 );
	}
	
	/**
	 * ���� ��� listView �ĵ������
	 * @param l
	 */
	public void setOnLeftListItemClickListener(final OnItemClickListener l) {
		mLeftItemClickListener = l;
	}
	
	/**
	 * ���� �ұ� listView �ĵ������
	 * @param l
	 */
	public void setOnRightListItemClickListener(final OnItemClickListener l) {
		mRightItemClickListener = l;
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View arg1, int pos, long arg3) {
		if( adapterView.getId() == R.id.left_list ) {
			if( mLeftItemClickListener != null ) {
				mLeftItemClickListener.onItemClick(adapterView, arg1, pos, arg3);
			}
		} else {
			mPopupWindow.dismiss();
			mPopupWindow = null;
			if( mRightItemClickListener != null ) {
				mRightItemClickListener.onItemClick(adapterView, arg1, pos, arg3);
			}
		}
	}

}
