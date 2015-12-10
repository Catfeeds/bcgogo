package com.tonggou.andclient.myview;

import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * ���������� ������
 * 
 * @author lwz
 *
 * @param <T>
 */
public abstract class AbsViewHolderAdapter<T> extends BaseAdapter {
	
	private Context mContext;
	private List<T> mData;
	private int mLayoutRes;
	
	public AbsViewHolderAdapter(Context context, List<T> data, int layoutRes) {
		mContext = context;
		mData = data;
		mLayoutRes = layoutRes;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public T getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public List<T> getData() {
		return mData;
	}
	
	/**
	 * ���������滻���еľ�����
	 * @param newData
	 */
	public synchronized void update(Collection<? extends T> newData) {
		mData.clear();
		if( newData != null ) {
			mData.addAll(newData);
		}
		notifyDataSetChanged();
	}
	
	/**
	 * ֱ�ӽ�mDataָ��newData����
	 * @param newData
	 */
	public void quickUpdate (Collection<? extends T> newData) {
		mData=(List<T>) newData;
		notifyDataSetChanged();
	}
	
	
	/**
	 * ��ԭ�����ݵĻ��������������
	 * @param appendData
	 */
	public synchronized void append(Collection<? extends T> appendData) {
		if( appendData == null || appendData.isEmpty() ) {
			return;
		}
		mData.addAll(appendData);
		notifyDataSetChanged();
	}
	
	/**
	 * ���һ������
	 * @param item
	 */
	public synchronized void add(T item) {
		mData.add(item);
		notifyDataSetChanged();
	}
	
	/**
	 * �����������
	 */
	public void clear() {
		mData.clear();
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if( convertView == null ) {
			convertView = View.inflate(mContext, mLayoutRes, null);
		}
		
		setData(position, convertView, getItem(position));
		
		return convertView;
	}
	
	abstract protected void setData(int pos, View convertView, T itemData);

	public <K extends View> K getViewFromHolder( View convertView, int id ) {
		return ViewHolder.getView(convertView, id);
	}
}
