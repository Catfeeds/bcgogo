package com.tonggou.andclient.myview;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tonggou.andclient.R;

/**
 * ��Ϣȷ�� �Ի���
 * 
 * <p>Ĭ��Ϊ ����Ի��򣬿���ͨ�� ���� {@link DialogType} ������Ϊ��ȷ����Ϣ�Ի���</p>
 * 
 * @author lwz
 *
 */
public class MessageDialog extends AbsCustomAlertDialog implements View.OnClickListener {
	
	/**
	 * �Ի�������
	 * 	<ul>
	 * 		<li>NEGATIVE ������Ϣ�Ի���
	 * 		<li>POSITIVE ��ȷ��Ϣ�Ի���
	 * </ul>
	 * @author lwz
	 *
	 */
	public static enum DialogType {
		/** ������Ϣ�Ի��� */
		NEGATIVE,
		/** ��ȷ��Ϣ�Ի��� */
		POSITIVE,
	}
	
	private DialogType type = DialogType.NEGATIVE;
	
	public MessageDialog(Context context) {
		super(context);
	}
	
	/**
	 * 
	 * @param context
	 * @param type	{@link DialogType}
	 */
	public MessageDialog(Context context, DialogType type) {
		this(context);
		this.type = type;
	}

	@Override
	protected View getCustomContentView(CharSequence msg) {
		View view = View.inflate(getContext(), R.layout.widget_dialog_error, null);
		ImageView icon = (ImageView) view.findViewById(R.id.dialog_icon);
		icon.setImageResource( 
				type == DialogType.NEGATIVE ? R.drawable.ic_negative : R.drawable.ic_positive  );
		TextView msgText =(TextView) view.findViewById(R.id.message);
		msgText.setText(msg);
		view.findViewById(R.id.btn_ok).setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		dismissDialog(this);
	}
	
	
}
