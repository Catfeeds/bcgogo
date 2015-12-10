package com.tonggou.andclient;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.tonggou.andclient.app.TongGouApplication;
import com.tonggou.andclient.myview.ScrollLayout;
import com.tonggou.andclient.network.Network;
import com.tonggou.andclient.network.NetworkState;
import com.tonggou.andclient.parse.CommonParser;
import com.tonggou.andclient.parse.ServerDetailParser;
import com.tonggou.andclient.util.INFO;
import com.tonggou.andclient.util.ListViewRun;
import com.tonggou.andclient.util.SomeUtil;
import com.tonggou.andclient.vo.OrderItem;
import com.tonggou.andclient.vo.ServerDetail;
/**
 * ��������ҳ��
 * 
 * @author think
 *
 */
public class OrderDetilActivity extends BaseActivity{
	private static final int  NETWORK_FAILD=-1;
	private static final int  ACTION_SUCCEED=0x001;
	private static final int  ACTION_FAILD=0x002;
	private static final int  CANCEL_SUCCEED=0x003;

	private ProgressBar networkingPB;
	private Handler handler;
	private String orderID,from;
	private TextView ordername,action,orderstate;
	private ServerDetail sDetail;
	private AlertDialog cancelingAlert;
	private LinearLayout orderItems,orderItems1;
	private ScrollView scrollView;
	private ListViewRun tableListView,tableListView1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orderdetil);

		orderID = getIntent().getStringExtra("tonggou.server.orderid");
		from = getIntent().getStringExtra("tonggou.server.from");
		scrollView = (ScrollView)findViewById(R.id.my_scrollview);

		View back=findViewById(R.id.left_button);
		back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				OrderDetilActivity.this.finish();
			}
		});
		ordername=(TextView)findViewById(R.id.ordername);

		orderstate=((TextView)findViewById(R.id.order_state_tv));
		action = (TextView)findViewById(R.id.order_state_iv);                  //��Ӧ����

		orderItems = (LinearLayout)findViewById(R.id.shopkeep);
		orderItems1 = (LinearLayout)findViewById(R.id.shopkeep1);
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg){	
				networkingPB.setVisibility(View.GONE);
				switch(msg.what){
				case ACTION_SUCCEED: 
					//Toast.makeText(OrderDetilActivity.this,(String)msg.obj,Toast.LENGTH_SHORT).show();
					((TextView)findViewById(R.id.ordernum_tx)).setText(sDetail.getReceiptNo());           //���ݺ�   
					((TextView)findViewById(R.id.orderstate_tx)).setText(sDetail.getServiceType());           //��������
					((TextView)findViewById(R.id.ordertime_tx)).setText(SomeUtil.longToStringDate(""+sDetail.getOrderTime()));           //ʱ��
					ordername.setText(sDetail.getShopName());           //��������

					//������̽������
					/*ordername.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							if(sDetail.getShopId()!=null&&!"".equals(sDetail.getShopId())){
								Intent intent=new Intent(OrderDetilActivity.this,StoreDetilActivity.class);
								intent.putExtra("tonggou.shopId",sDetail.getShopId()+"");
								intent.putExtra("tonggou.shopname",sDetail.getShopName());
								intent.putExtra("tonggou.shopmeter","");
								startActivity(intent);
							}
						}
					});*/

					orderstate.setText(sDetail.getStatus());           //״̬
					if("�ѽ���".equals(sDetail.getStatus())){

						action.setText("����");  						
						if(sDetail.getComment()==null||sDetail.getComment().getCommentContent()==null){
							action.setVisibility(View.VISIBLE);
						}else{
							action.setVisibility(View.INVISIBLE);
						}
						action.setOnClickListener(new OnClickListener() {
							public void onClick(View v) {
								Intent intent=new Intent(OrderDetilActivity.this,SetScroeActivity.class);
								intent.putExtra("tonggou.server.orderid",sDetail.getOrderId());
								intent.putExtra("tonggou.server.from", "OrderDetilActivity");
								startActivityForResult(intent, 7070);
							}
						});
						findViewById(R.id.order_comment).setVisibility(View.VISIBLE);
						findViewById(R.id.shoplike).setVisibility(View.VISIBLE);
						findViewById(R.id.accounts_part).setVisibility(View.VISIBLE);

						findViewById(R.id.order_beizhu).setVisibility(View.VISIBLE);
						findViewById(R.id.order_line).setVisibility(View.VISIBLE);
						findViewById(R.id.appinfo).setVisibility(View.GONE);
						ImageView like1=(ImageView)findViewById(R.id.shoplistlike1);
						ImageView like2=(ImageView)findViewById(R.id.shoplistlike2);
						ImageView like3=(ImageView)findViewById(R.id.shoplistlike3);
						ImageView like4=(ImageView)findViewById(R.id.shoplistlike4);
						ImageView like5=(ImageView)findViewById(R.id.shoplistlike5);
						if(sDetail.getComment()!=null){
							((TextView)findViewById(R.id.shoplistlikesorce)).setText(sDetail.getComment().getCommentScore()+"��");
							OrderDetilActivity.this.setLikes(sDetail.getComment().getCommentScore()+"", like1, like2, like3, like4, like5);
							if(sDetail.getComment().getCommentContent()!=null){
								((TextView)findViewById(R.id.order_beizhu)).setText(sDetail.getComment().getCommentContent());           //��ע
							}	
						}         //��ע
					}else{
						if("��ȷ��".equals(sDetail.getStatus())||"�ѽ���".equals(sDetail.getStatus())){
							action.setText("ȡ��");  
							action.setVisibility(View.VISIBLE);
							action.setOnClickListener(new OnClickListener() {
								public void onClick(View v) {
									cancelAlert();
								}
							});
						}else {	
							action.setVisibility(View.INVISIBLE); 


						}
						findViewById(R.id.order_beizhu).setVisibility(View.GONE);
						findViewById(R.id.shoplike).setVisibility(View.GONE);
						findViewById(R.id.accounts_part).setVisibility(View.GONE);
						findViewById(R.id.order_comment).setVisibility(View.GONE);
						findViewById(R.id.order_line).setVisibility(View.GONE);
						findViewById(R.id.appinfo).setVisibility(View.VISIBLE);
					}
					((TextView)findViewById(R.id.orderman_tx)).setText(sDetail.getCustomerName());           //�ͻ���
					((TextView)findViewById(R.id.ordercarman_tx)).setText(sDetail.getVehicleNo());           //���ƺ�
					if(sDetail.getVehicleContact()!=null){
						((TextView)findViewById(R.id.appman_tx)).setText(sDetail.getVehicleContact());           //�ͻ���
					}
					if(sDetail.getVehicleMobile()!=null){
						((TextView)findViewById(R.id.appcall_tx)).setText(sDetail.getVehicleMobile());           //�ͻ���
					}
					((TextView)findViewById(R.id.apptime_tx)).setText(SomeUtil.longToStringDate(""+sDetail.getOrderTime()));           //�ͻ���
					if(sDetail.getRemark()!=null){
						((TextView)findViewById(R.id.appother_tx)).setText(sDetail.getRemark());           //�ͻ���
					}
					if(sDetail.getVehicleBrandModelStr()!=null){
						((TextView)findViewById(R.id.ordercarstate_tx)).setText(sDetail.getVehicleBrandModelStr());           //�ͻ���
					}
					//�������б�
					//					LinearLayout orderItem = (LinearLayout)findViewById(R.id.shopkeep);
					//	
					//					View tempIte = getLayoutInflater().inflate(R.layout.orderdetil_item, null);
					//					orderItem.addView(tempIte,new LayoutParams(LayoutParams.FILL_PARENT ,LayoutParams.WRAP_CONTENT ) );
					//					View tempIte1 = getLayoutInflater().inflate(R.layout.orderdetil_item, null);
					//					orderItem.addView(tempIte1,new LayoutParams(LayoutParams.FILL_PARENT ,LayoutParams.WRAP_CONTENT ) );



					if(sDetail.getOrderItems()!=null && sDetail.getOrderItems().size()>0){
						
						List<OrderItem> list = new ArrayList<OrderItem>();
						List<OrderItem> list1 = new ArrayList<OrderItem>();
						for(int i=0;i<sDetail.getOrderItems().size();i++){
							OrderItem ot = sDetail.getOrderItems().get(i);
							TongGouApplication.showLog("type"+ot.getType());

							if(ot.getType() != null && "��Ʒ".equals(ot.getType())){
								list1.add(new OrderItem(ot.getContent(),ot.getType(),ot.getAmount()));
								orderItems1.setVisibility(View.VISIBLE);
							}
							if(ot.getType() != null && "��������".equals(ot.getType())){
								list.add(new OrderItem(ot.getContent(),ot.getType(),ot.getAmount()));
								orderItems.setVisibility(View.VISIBLE);
							}
						}
						 //��������
						//���ñ�����ı�����ɫ
						ViewGroup tableTitle = (ViewGroup) findViewById(R.id.table_title);
						tableTitle.setBackgroundColor(getResources().getColor(R.color.item_background));
                        
						tableListView = (ListViewRun)findViewById(R.id.list1); 
						//tableListView = (ListViewRun) findViewById(R.id.list1);
						TableAdapter adapter = new TableAdapter(OrderDetilActivity.this, list);
						tableListView.setAdapter(adapter);

						tableListView.setParentScrollView(scrollView);
						//�̶��߶ȣ���Ȼû�й���Ч��
						tableListView.setMaxHeight(800);
						//						��Ʒ
						ViewGroup tableTitle1 = (ViewGroup) findViewById(R.id.table_title1);
						tableTitle1.setBackgroundColor(getResources().getColor(R.color.item_background));
						tableListView1 = (ListViewRun) findViewById(R.id.list2);
						TableAdapter adapter2 = new TableAdapter(OrderDetilActivity.this, list1);
						tableListView1.setAdapter(adapter2);
						tableListView1.setParentScrollView(scrollView);
						tableListView1.setMaxHeight(800);

					}else{
						//findViewById(R.id.accounts_part).setVisibility(View.VISIBLE);
					}

					//�۸񲿷�
					if(sDetail.getSettleAccounts()==null){
						findViewById(R.id.accounts_part).setVisibility(View.GONE);
					}else{
						findViewById(R.id.accounts_part).setVisibility(View.VISIBLE);
						TextView total = (TextView)findViewById(R.id.totalnum_tx);
						total.setText(sDetail.getSettleAccounts().getTotalAmount());
						TextView settled = (TextView)findViewById(R.id.settlednum_tx);
						settled.setText(sDetail.getSettleAccounts().getSettledAmount());
						TextView discount = (TextView)findViewById(R.id.discountnum_tx);
						discount.setText(sDetail.getSettleAccounts().getDiscount());
						TextView debt = (TextView)findViewById(R.id.debtnum_tx);
						debt.setText(sDetail.getSettleAccounts().getDebt());
					}

					//networkingPB.setVisibility(View.GONE);
					break;
				case NETWORK_FAILD: 
					if(cancelingAlert!=null){					
						cancelingAlert.cancel();
						cancelingAlert.dismiss();
					}
					Toast.makeText(OrderDetilActivity.this,(String)msg.obj,Toast.LENGTH_LONG).show();
					break;
				case CANCEL_SUCCEED: 
					if(cancelingAlert!=null){					
						cancelingAlert.cancel();
						cancelingAlert.dismiss();
					}
					Toast.makeText(OrderDetilActivity.this,(String)msg.obj,Toast.LENGTH_LONG).show();
					if("SearchServiceActivity".equals(from)){
						Intent dataIntent = new Intent();
						dataIntent.putExtra("tonggou.isOk","yes");
						setResult(6060, dataIntent);
						OrderDetilActivity.this.finish();	
					}else{
						orderstate.setText("��ȡ��");
						action.setVisibility(View.INVISIBLE); 
					}
					break;
				case ACTION_FAILD: 	
					if(cancelingAlert!=null){					
						cancelingAlert.cancel();
						cancelingAlert.dismiss();
					}
					Toast.makeText(OrderDetilActivity.this,(String)msg.obj,Toast.LENGTH_LONG).show();

					break;
				}
			}
		};
		networkingPB = (ProgressBar) findViewById(R.id.orderdetail_pb);
		new Thread(){
			public void run(){
				getServerDetail("NULL",orderID);
			}
		}.start();
	}

	private void getServerDetail(String serviceScope,String orderId) {
		String url = INFO.HTTP_HEAD+INFO.HOST_IP+"/service/historyDetail/orderId/"+orderId+"/serviceScope/"+serviceScope;

		ServerDetailParser serverDetailParser = new ServerDetailParser();		
		NetworkState ns = Network.getNetwork(OrderDetilActivity.this).httpGetUpdateString(url,serverDetailParser);	

		if(ns.isNetworkSuccess()){
			if(serverDetailParser.isSuccessfull()){
				sDetail = serverDetailParser.getServerDetailReponse().getServerDetail();
				if(sDetail!=null){					
					sendMessage(ACTION_SUCCEED,serverDetailParser.getServerDetailReponse().getMessage());				
				}else{
					sendMessage(ACTION_FAILD,serverDetailParser.getServerDetailReponse().getMessage());
				}

			}else{
				sendMessage(ACTION_FAILD, serverDetailParser.getErrorMessage());
			}
		}else{
			//�������
			sendMessage(ACTION_FAILD, ns.getErrorMessage());
		}
	}

	private void sendMessage(int what, String content) {
		if (what < 0) {
			what = BaseActivity.SEND_MESSAGE;
		}
		Message msg = Message.obtain(handler, what, content);
		if(msg!=null){
			msg.sendToTarget();
		}
	}


	private void cancelAlert() {
		new AlertDialog.Builder(this) 		
		.setTitle(getString(R.string.exit_title)) 
		.setMessage("��ȷ��ȡ��������") 
		.setPositiveButton(getString(R.string.exit_submit), new DialogInterface.OnClickListener() { 
			public void onClick(DialogInterface dialog, int whichButton) {
				cancelingAlert= new AlertDialog.Builder(OrderDetilActivity.this).create();
				cancelingAlert.show();			
				Window window = cancelingAlert.getWindow();
				window.setContentView(R.layout.widget_dialog_loading);
				TextView waiting_message =(TextView) window.findViewById(R.id.message);
				waiting_message.setText(R.string.register_waiting);
				new Thread(){
					public void run(){
						cancelServerNetwork(orderID);
					}
				}.start();
			} 
		}).setNeutralButton(getString(R.string.exit_cancel), new DialogInterface.OnClickListener(){ 
			public void onClick(DialogInterface dialog, int whichButton){ 
			} 
		}).show();
	}


	private void cancelServerNetwork(String orderid) {
		String url = INFO.HTTP_HEAD+INFO.HOST_IP+"/service/singleService/orderId/"+orderid+"/userNo/"+sharedPreferences.getString(BaseActivity.NAME, null);

		CommonParser commonParser = new CommonParser();		
		NetworkState ns = Network.getNetwork(OrderDetilActivity.this).httpDeleteUpdateString(url,commonParser);	

		if(ns.isNetworkSuccess()){

			if(commonParser.isSuccessfull()){					
				sendMessage(CANCEL_SUCCEED,commonParser.getCommonResponse().getMessage());				
			}else{
				sendMessage(ACTION_FAILD, commonParser.getErrorMessage());
			}
		}else{
			//�������
			sendMessage(ACTION_FAILD, ns.getErrorMessage());
		}
	}
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==7070){		
			//��������
			if("yes".equals(data.getStringExtra("tonggou.isOk"))){		
				networkingPB.setVisibility(View.VISIBLE);
				new Thread(){
					public void run(){
						getServerDetail("NULL",orderID);
					}
				}.start();
			}
		}
	}


	private class TableAdapter extends BaseAdapter {
		private List<OrderItem> list;
		private LayoutInflater inflater;

		public TableAdapter(Context context, List<OrderItem> list){
			this.list = list;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//Person person = (Person) this.getItem(position);
			OrderItem orderItem = (OrderItem) this.getItem(position);
			ViewHolder viewHolder;

			if(convertView == null){
				viewHolder = new ViewHolder();
				convertView = inflater.inflate(R.layout.orderdetil_listitem, null);
				viewHolder.service = (TextView) convertView.findViewById(R.id.service);
				viewHolder.service_money = (TextView) convertView.findViewById(R.id.service_money);
				convertView.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder) convertView.getTag();
			}

			viewHolder.service.setText(orderItem.getContent());
			viewHolder.service_money.setText(orderItem.getAmount()+"Ԫ");

			return convertView;
		}

		class ViewHolder{
			public TextView service;
			public TextView service_money;
		}

	}


}
