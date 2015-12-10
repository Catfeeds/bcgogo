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
 * ���б�ĵ�����
 * <p>ContentView �ǵ��� ListView ��PopupWindow
 * @author lwz
 *
 */
public class SingleListPopupWindow implements OnItemClickListener {
	private Context mContext;
	private PopupWindow mPopupWindow;
	private ListView mListView;
	private ListAdapter mAdapter;
	
	private ScreenSizeUtil screenSizeUtil;
	private OnItemClickListener mOnItemClickListener;
	private int mBackgroundRes = R.drawable.shopschooseback2;
	private int mItemWidth;
	private int mItemHeight;
	
	public SingleListPopupWindow(Context context, ListAdapter adapter) {
		mContext = context;
		mAdapter = adapter;
		screenSizeUtil = new ScreenSizeUtil(context);
		init();
	}
	
	private void init() {
		View contentView = View.inflate(mContext, R.layout.popup_single_list, null);
		mListView = (ListView) contentView.findViewById(R.id.single_list);
		mListView.setEmptyView( contentView.findViewById(R.id.loading_indicator) );
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		
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
			mItemWidth = mContext.getResources().getDimensionPixelSize(R.dimen.popup_list_width);
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
		int listViewHeight = getMeasureHeight(mListView);
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
	 * ���� listView �ĸ߶�
	 * @param list
	 * @return
	 */
	private int getMeasureHeight(ListView list) {
        ListAdapter adapter = list.getAdapter();
        int childCount = adapter.getCount();
        int listDividerHeight = list.getDividerHeight();
        return (getListItemHeight() + listDividerHeight) * ( childCount + 1 );
	}
	
	/**
	 * ���� listView �ĵ������
	 * @param l
	 */
	public void setOnListItemClickListener(final OnItemClickListener l) {
		mOnItemClickListener = l;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
		mPopupWindow.dismiss();
		mPopupWindow = null;
		if( mOnItemClickListener != null ) {
			mOnItemClickListener.onItemClick(arg0, arg1, pos, arg3);
		}
	}

	public void dismiss() {
		if( mPopupWindow != null && mPopupWindow.isShowing() ) {
			mPopupWindow.dismiss();
		}
	}
}
