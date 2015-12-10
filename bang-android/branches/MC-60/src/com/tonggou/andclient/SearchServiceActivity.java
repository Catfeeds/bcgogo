package com.tonggou.andclient;


import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tonggou.andclient.app.TongGouApplication;
import com.tonggou.andclient.myview.SimpleTitleBar;
import com.tonggou.andclient.network.Network;
import com.tonggou.andclient.network.NetworkState;
import com.tonggou.andclient.parse.SearchServiceParser;
import com.tonggou.andclient.util.INFO;
import com.tonggou.andclient.util.SomeUtil;
import com.tonggou.andclient.vo.Pager;
import com.tonggou.andclient.vo.TGService;
/**
 * �����ѯҳ��
 * @author think
 *
 */
public class SearchServiceActivity extends AbsBackableActivity {

	private static final int  NETWORK_FAILD=-1;
	private static final int  ACTION_SUCCEED=0x001;
	private static final int  ACTION_FAILD=0x002;	
	private static final int  NETWORK_NODATA=0x003;	
	private static final int  FINISH_NETWORK_FAILD=4;
	private static final int NEXTPAGE_NETWORK_SUCCEED =0x007;    //��һҳ�ɹ�
	private static final int NEXTPAGE_FINISHED_NETWORK_NODATA =0x008;     //��һҳû������
	private static final int CLOSE_PRO_UNFINISHED=0x0011;
	private static final int CLOSE_PRO_FINISHED=0x0012;
	private static final int IMAGE_FLUSH_UNFINISHED=0x0013;
	private static final int IMAGE_FLUSH_FINISHED=0x0014;

	private Handler handler;
	private ListView  listView;
	private ShopsListAdapter shopsListAdapter;
	private ArrayList<TGService> allServersNo = new ArrayList<TGService>();
	private ArrayList<TGService> allServersNoNext = new ArrayList<TGService>();
	private ArrayList<TGService> allServersReady = new ArrayList<TGService>();
	private Pager mPager;
	
//	private int pageNumNo=1;
	private int finishPageNum=1;
	private String unfinshedNum="";

	int visbleCountUnfinished=0;
	private int firstListItemUnfinished = 0;                      //���ĸ�λ�ÿ�ʼȡͼƬ
	private boolean nextPageUnfinishedLock = false,hasToLastfinished=false;   
	int visbleCountFinished=0;
	private int firstListItemFinished = 0;                      //���ĸ�λ�ÿ�ʼȡͼƬ
	
//	private int nowSelect = 0;      
	private boolean mIsNetworking = false;
//	private boolean reNetworking = false;
	
	@Override
	protected int getContentLayout() {
		return R.layout.search_service;
	}
	
	@Override
	protected void afterTitleBarCreated(SimpleTitleBar titleBar,
			Bundle savedInstanceState) {
		super.afterTitleBarCreated(titleBar, savedInstanceState);
		titleBar.setRightImageButton(R.drawable.searching_obd_refresh, android.R.color.transparent);
		titleBar.setTitle(R.string.service_title);
		titleBar.setOnRightButtonClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onRefresh();
			}
		});
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		listView = (ListView) findViewById(R.id.list_no);
		shopsListAdapter = new ShopsListAdapter(SearchServiceActivity.this); 
		listView.setDividerHeight(0);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> eachAdaper, View arg1, int position, long arg3) {
				Intent intent=new Intent(SearchServiceActivity.this,OrderDetilActivity.class);
				intent.putExtra("tonggou.server.orderid", allServersNo.get(position).getOrderId());
				intent.putExtra("tonggou.server.from", "SearchServiceActivity");
				startActivityForResult(intent, 6060);
			}
		});
		listView.setOnScrollListener(new OnScrollListener(){
			int firstItem = 0;
			int lastItem =0;
			public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
				firstItem = firstVisibleItem;
				visbleCountUnfinished = visibleItemCount;
				lastItem = firstVisibleItem + visibleItemCount ;
				firstListItemUnfinished= firstVisibleItem ;
			}
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if(scrollState == OnScrollListener.SCROLL_STATE_IDLE||scrollState == 2){	
					TongGouApplication.showLog( " pager info -- current no" + mPager.getCurrentPage() + "  size" + mPager.getPageSize() + "  " + mPager.isHasNextPage());
					if( mIsNetworking ) {
						// ˵���Ѿ��ڼ�����
						return;
					}
					if(allServersNo!=null && allServersNo.size()>0){
						new Thread(){
							public void run(){
								addPhoto(allServersNo,firstItem,visbleCountUnfinished,"no");									
							}
						}.start();
						new Thread(){
							public void run(){
								removePhoto(allServersNo,firstItem,visbleCountUnfinished);
							}
						}.start();
					}
					/*if(firstItem==0){
						if(!canNext){
							return;
						}
					}*/
					if(!hasToLastfinished&&allServersNo.size()>=INFO.ITEMS_PER_PAGE){	
						if(lastItem==allServersNo.size()){			
							if (!nextPageUnfinishedLock) {
								nextPageUnfinishedLock = true;
								getNextPage();
							}
						}
					}
				}
			}        	
		});	
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg){		
				getSimpleTitle().hideLoadingIndicator();
				switch(msg.what){
				case NETWORK_FAILD: 
					Toast.makeText(SearchServiceActivity.this,(String)msg.obj,Toast.LENGTH_SHORT).show();
					break;
				case FINISH_NETWORK_FAILD: 
					Toast.makeText(SearchServiceActivity.this,(String)msg.obj,Toast.LENGTH_SHORT).show();
					break;
				case ACTION_SUCCEED: 
					shopsListAdapter.notifyDataSetChanged();
					shopsListAdapter.setDate(allServersNo);
					listView.setAdapter(shopsListAdapter);
					shopsListAdapter.notifyDataSetChanged();
					break;
				case NETWORK_NODATA: 
					Toast.makeText(SearchServiceActivity.this,"û������", Toast.LENGTH_SHORT).show();
					break;
				case ACTION_FAILD: 							
					Toast.makeText(SearchServiceActivity.this,(String)msg.obj,Toast.LENGTH_SHORT).show();
					break;
				case NEXTPAGE_NETWORK_SUCCEED:
					mergeDataUnfinished();
					setImagesUnfinished(); //ȡ��Ƭ
					shopsListAdapter.notifyDataSetChanged();
					break;
				case NEXTPAGE_FINISHED_NETWORK_NODATA:
//					//û��������ʾ	
//					hasToLastfinished=true;						
//					Toast.makeText(SearchServiceActivity.this,getString(R.string.nextpage_nodata),Toast.LENGTH_LONG).show();
					break;
				case CLOSE_PRO_UNFINISHED:	
					break;
				case IMAGE_FLUSH_UNFINISHED:	
					shopsListAdapter.notifyDataSetChanged();					
					break;
				}
			}
		};

		doGetServices();
	}
	
	private void doGetServices() {
		if( mPager == null ) {
			mPager = new Pager();
			mPager.setCurrentPage(0);
		}
		doGetServicesByPage();
	}
	
	private void doGetServicesByPage() {
		getSimpleTitle().showLoadingIndicator();
		new Thread(){
			public void run(){
				getServices("NULL","unfinished",currentUserId,mPager.getCurrentPage() + 1,INFO.ITEMS_PER_PAGE+"");
			}
		}.start();
	}
	
	private void onRefresh() {
//		if(nowSelect==0){
//			if(unNetworking){
//				return;
//			}
////			pageNumNo = 1;
//			
//		
//		}else{
//			if(reNetworking){
//				return;
//			}
//			finishPageNum = 1;
////			hastoLastFinished=false;
//			if(allServersReady!=null){
//				allServersReady.clear();
//			}
//			doGetServicesByPage();
//			
//		}
		
		hasToLastfinished=false;
		mPager = null;
		if(allServersNo!=null){
			allServersNo.clear();
			shopsListAdapter.notifyDataSetChanged();
		}
		doGetServices();
	}


	private class ShopsListAdapter extends BaseAdapter{	
		private LayoutInflater layoutInflater;
		private List<TGService>  shops;
		SearchServiceActivity context;
		public ShopsListAdapter(SearchServiceActivity searchServiceActivity ){
			layoutInflater = LayoutInflater.from(searchServiceActivity);	
			this.context=searchServiceActivity;
		}
		public void setDate(List<TGService>   shops) {	
			this.shops =  shops;
		}
		public int getCount() {			
			return shops.size();
		}
		public TGService getItem(int position) {
			return shops.get(position);
		}
		public long getItemId(int position) {		
			return position;
		}
		public View getView(int position, View convertView, ViewGroup parent) {	
			if(convertView == null){
				convertView = layoutInflater.inflate(R.layout.search_service_item, null);
			}				
			TextView name = ((TextView)convertView.findViewById(R.id.shoplistname));
			TextView num = ((TextView)convertView.findViewById(R.id.shoplistnum_tv));
			TextView state = ((TextView)convertView.findViewById(R.id.shopliststate_tv));
			TextView service = ((TextView)convertView.findViewById(R.id.shoplistservice_tv));
			TextView time = ((TextView)convertView.findViewById(R.id.shoplisttime));
			ImageView shoplistpicView=(ImageView) convertView.findViewById(R.id.shoplistpicView);
			shoplistpicView.setImageBitmap(getItem(position).getBtm());
		
			name.setText(shops.get(position).getShopName());
			num.setText(shops.get(position).getShopId());
			state.setText(shops.get(position).getStatus());
			service.setText(shops.get(position).getOrderType());
			time.setText(SomeUtil.longToStringDate(shops.get(position).getOrderTime()));
			return convertView;	


		}		
	}


	/**
	 * ��ȡ��ҷ���
	 * @param type
	 * @param status
	 * @param userNo
	 * @param pageNo
	 * @param pageSize
	 */
	private void getServices(String type,String status,String userNo,int pageNo,String pageSize) {
		mIsNetworking = true;
		String url = INFO.HTTP_HEAD+INFO.HOST_IP+"/service/AllHistoryList/pageNo/" + pageNo + "/pageSize/" + pageSize;

		SearchServiceParser searchServiceParser = new SearchServiceParser();	
		NetworkState ns = Network.getNetwork(SearchServiceActivity.this).httpGetUpdateString(url,searchServiceParser);	
		mIsNetworking = false;
		if(ns.isNetworkSuccess()){
			if(searchServiceParser.isSuccessfull()){
				mPager = searchServiceParser.getStoreQueryResponse().getPager();
				allServersNo = (ArrayList<TGService>) searchServiceParser.getStoreQueryResponse().getResults();
				if(allServersNo!=null){
					/*if(!(allServersNo.size()+"").equals(unfinshedNum)){
						unfinshedNum=allServersNo.size()+"";
					}*/
					if(allServersNo.size()>0){
						
						sendMessage(ACTION_SUCCEED, searchServiceParser.getStoreQueryResponse().getMessage());
						setImagesUnfinished();
					}else{
						sendMessage(NETWORK_NODATA,searchServiceParser.getStoreQueryResponse().getMessage());
					}
				}else{
					sendMessage(NETWORK_NODATA,searchServiceParser.getStoreQueryResponse().getMessage());
				}

			}else{
				sendMessage(NETWORK_NODATA, searchServiceParser.getErrorMessage());
			}
		}else{
			//�������
			sendMessage(NETWORK_FAILD, ns.getErrorMessage());
		}
	}
	
	private  boolean nextPageNetworking(String type,String status,String userNo,int pageNo,String pageSize) {
		mIsNetworking = true;
		String url = INFO.HTTP_HEAD+INFO.HOST_IP+"/service/AllHistoryList/pageNo/" + pageNo + "/pageSize/" + pageSize;

		SearchServiceParser searchServiceParser = new SearchServiceParser();		
		NetworkState ns = Network.getNetwork(SearchServiceActivity.this).httpGetUpdateString(url,searchServiceParser);	
		mIsNetworking = false;
		if(ns.isNetworkSuccess()){
			if(searchServiceParser.isSuccessfull()){
				mPager = searchServiceParser.getStoreQueryResponse().getPager();
				allServersNoNext = (ArrayList<TGService>) searchServiceParser.getStoreQueryResponse().getResults();
				if(allServersNoNext==null||allServersNoNext.size()<=0){
					sendMessage(NEXTPAGE_FINISHED_NETWORK_NODATA, searchServiceParser.getErrorMessage());
					return false;
				}
				return true;

			}else{
				sendMessage(NEXTPAGE_FINISHED_NETWORK_NODATA, searchServiceParser.getErrorMessage());
				return false;
			}
		}else{
			//�������
			sendMessage(NETWORK_FAILD, ns.getErrorMessage());
			return false;
		}
	}
	
	private void getNextPage() {
		getSimpleTitle().showLoadingIndicator();
		if( mPager == null || !mPager.isHasNextPage() ) {
			hasToLastfinished = true;
			TongGouApplication.showToast(getString(R.string.nextpage_nodata));
			return;
		}
		new Thread() {
			@Override
			public void run() {
//				pageNumNo++;
				if (!nextPageNetworking("NULL","unfinished",currentUserId,mPager.getCurrentPage() + 1,INFO.ITEMS_PER_PAGE+"")) {
//					pageNumNo--;
					nextPageUnfinishedLock = false;
				} else {
					sendMessage(NEXTPAGE_NETWORK_SUCCEED, "");
					nextPageUnfinishedLock = false;
				}
				super.run();
			}
		}.start();
		
	}
	/**
	 * �ϲ���һҳ���ݵ����ڵ������� ����Ҫʱ����ȥ�ظ���
	 */
	private void mergeDataUnfinished() {
		int begin = 0;
		for (int j = begin; j < allServersNoNext.size(); j++) {
			if(allServersNo!=null){
				allServersNo.add(allServersNoNext.get(j));
			}
		}
	}
	public void setImagesUnfinished(){
		new Thread(){
			public void run(){
				addPhoto(allServersNo,firstListItemUnfinished,12,"no");	
				sendMessage(CLOSE_PRO_UNFINISHED, null);
			}
		}.start();
	}
	public void setImagesFinished(){
		new Thread(){
			public void run(){
				addPhoto(allServersReady,firstListItemFinished,12,"already");	
				sendMessage(CLOSE_PRO_FINISHED, null);
			}
		}.start();
	}
	private void sendMessage(int what, String content) {
		if (what < 0) {
			what = BaseActivity.SEND_MESSAGE;
		}
		Message msg = Message.obtain(handler, what, content);
		if(msg!=null){
			msg.sendToTarget();
		}
	}public void addPhoto(ArrayList<TGService> scanBooks, int first, int count,String from) {
		int begin = first /*- 3*/;
		if (begin < 0) {
			begin = 0;
		}
		int all = scanBooks.size();
		int end = first + count /*+ 12*/;

		if (end > all) {
			end = all;
		}
		TGService concise = null;
		String imageUrl= null;
		for (int i = begin; i < end; i++) {
			if(i<scanBooks.size()){
				concise = scanBooks.get(i);
						
				imageUrl = concise.getShopImageUrl();
				
				if ( concise.getBtm() == null &&imageUrl!= null) {
					concise.setBtm(getPicture(imageUrl));
				}
				if("no".equals(from)){
					sendMessage(IMAGE_FLUSH_UNFINISHED,null);
				}if("already".equals(from)){
					sendMessage(IMAGE_FLUSH_FINISHED,null);
				}
			}
		}
	}
	
	public void removePhoto(ArrayList<TGService> scanBooks, int first, int count) {
		/*
		 * ���ں�������ÿպ���յ�ԭ�򣬿����������Σ�
		 *
		 * ��һ�ι���ֹͣ��T1��ʼ����6-10��T2��ʼ����0-5��11-15
		 *
		 * �ڶ��ι���ֹͣ��T3��ʼ����11-15������ʱ����T2���������ȣ�T3����ǰ������ˣ�
		 * T3��ʼ���أ�Ȼ��T2��ʼ���ա���T2�պû���ͼƬ��û�ÿ�ǰ������Ȩ���л���T3����ʱ
		 * T3�жϵ�ͼƬ�����ǿյģ������ListView��ʾ����ʱ�ͻ������쳣������ʾ�ѻ��յ�
		 * ͼƬ���ؼ����Ǳ�֤���պ��ÿ���ԭ�ӵ�
		 */
		int all = scanBooks.size();
		int removeup = first - 3;
		int end = first + count + 3;
		TGService concise = null;
		if (removeup > 0) {// �������
			for (int i = 0; i < removeup; i++) {
				if(i<scanBooks.size()){					
					concise = scanBooks.get(i);
					
					if ((concise.getBtm()) != null) {
						/** Ҫ���ÿգ��ڻ��� */
						concise.setBtm(null);
					}
				}
			}
		}
		if (all > end) {// �������
			for (int j = end; j < all; j++) {
				if(j<scanBooks.size()){					
					
					concise = scanBooks.get(j);
					if ((concise.getBtm()) != null) {
						/** Ҫ���ÿգ��ڻ��� */
						concise.setBtm(null);
					}
				}
			}
		}
	}
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==6060){		
			//��������
			if("yes".equals(data.getStringExtra("tonggou.isOk"))){		
				if(allServersNo!=null){
					allServersNo.clear();
					shopsListAdapter.notifyDataSetChanged();
				}
				
				doGetServices();
			}
		}
	}
}
