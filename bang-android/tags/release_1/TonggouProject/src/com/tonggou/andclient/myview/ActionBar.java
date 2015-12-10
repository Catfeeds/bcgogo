package com.tonggou.andclient.myview;

import java.io.BufferedInputStream;

import com.tonggou.andclient.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

/***
 * �Զ���ؼ�
 * 
 * 
 *         ��������Ҫ˵��һ�� �����ڴ���RectF���ε�ʱ��
 * 
 *         ������ԭ��������"���ؼ������Ͻ�".
 * 
 */
public class ActionBar extends LinearLayout implements OnClickListener {

	private ImageView tv1;
	private ImageView tv2;
	private ImageView tv3;
	//private ImageView tv4;
	private Paint paint;// ����
	private RectF curRectF;// draw��ǰbar
	private RectF tarRectF;// draw�����bar

	private final int space_x = 0;// �൱��pading.
	private final int space_y = 0;// �൱��pading
	private final double step = 32;// �ٶ�step.

	private Action action;// ����

	public interface Action {
		void action();
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public ActionBar(Context context) {
		super(context);
	}

	/***
	 * ���췽��
	 * 
	 * @param context
	 * @param attrs
	 */
	public ActionBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		setWillNotDraw(false);
		LayoutInflater.from(context).inflate(R.layout.home_tab, this, true);
		paint = new Paint();
		paint.setAntiAlias(true);
		tv1 = (ImageView) findViewById(R.id.tv1);
		tv2 = (ImageView) findViewById(R.id.tv2);
		tv3 = (ImageView) findViewById(R.id.tv3);
		//tv4 = (ImageView) findViewById(R.id.tv4);
		tv1.setOnClickListener(this);
		tv2.setOnClickListener(this);
		tv3.setOnClickListener(this);
		//tv4.setOnClickListener(this);
		curRectF = null;
		tarRectF = null;
		
		
	}

	/***
	 * invalidate()���������������ִ��onDraw()����������ǰ���ǣ��Լ���invalidate()����ִ�н����ڽ���ִ��.
	 */

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawColor(Color.WHITE);
		paint.setColor(Color.RED);
		// �����ǰcurRectF=null��Ҳ���ǵ�һ�η��ʣ���Ĭ��Ϊdraw��һ��bar
		if (curRectF == null)
			curRectF = new RectF(tv1.getLeft() + space_x, tv1.getTop()
					+ space_y, tv1.getRight() - space_x, tv1.getBottom()
					- space_y);

		// ��һ�η�λtarRectF=null��Ĭ��Ϊdraw
		if (tarRectF == null)
			tarRectF = new RectF(tv1.getLeft() + space_x, tv1.getTop()
					+ space_y, tv1.getRight() - space_x, tv1.getBottom()
					- space_y);
		/***
		 * ���ã�����������Χ���������Ϊ����λ�ã��������İ׵Ļ�������԰����ע�����������֪��why��.��
		 */
		if (Math.abs(curRectF.left - tarRectF.left) < step) {
			curRectF.left = tarRectF.left;
			curRectF.right = tarRectF.right;
		}

		/***
		 * ˵��Ŀ���ڵ�ǰ�����,��Ҫ�����ƶ���ÿ�ξ����ƶ�step�������invalidate����,���½����ƶ�...��
		 */
		if (curRectF.left > tarRectF.left) {
			curRectF.left -= step;
			curRectF.right -= step;
			invalidate();// ����ˢ�£��Ӷ�ʵ�ֻ���Ч����ÿ��step32.
		}
		/***
		 * ˵��Ŀ���ڵ�ǰ���Ҳ�,��Ҫ�����ƶ���ÿ�ξ����ƶ�step�������invalidate����,���½����ƶ�...��
		 */
		else if (curRectF.left < tarRectF.left) {
			curRectF.left += step;
			curRectF.right += step;
			invalidate();
		}
		// canvas.drawRect(curRectF, paint);
		// ���������Σ����ȣ�����
		canvas.drawRoundRect(curRectF, 5, 5, paint);
		
	}

	/****
	 * ����Ҫ��¼Ŀ����ε�����
	 */
	@Override
	public void onClick(View v) {
		tarRectF.left = v.getLeft() + space_x;
		tarRectF.right = v.getRight() - space_x;
		invalidate();// ˢ��

		System.out.println("tarRectF.top=" + tarRectF.top + ",v.getTop()="
				+ v.getTop() + ", v.getBottom()" + v.getBottom());
	}

}
