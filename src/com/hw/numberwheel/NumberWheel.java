package com.hw.numberwheel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.ViewGroup;

/**
 */
public class NumberWheel extends SurfaceView implements Callback,Runnable {
	/**����λ��,��С6λ*/
	public int length = 6;
	public int textGap = 10;
	/** ÿ��λ�õ�x,y���� */
	public int[][] locations = null;
	public int endShowNum = 0;// ������ʾ��ֵ
	public String startShowNumString = "";// ��ʼ�����ַ���
	public String endShowNumString = "";// ���������ַ���
	/** �������е������¼,0��ǰ����,1,2,3�Ƿ�ֹͣ�򼴽�ֹͣ,4 */
	public int[][] runningNum;
	public Paint paint;
	private Context context;
	/** һ�����ֿ�� */
	private int textWidth;
	private int textHeight;

	private int viewWidth;
	private int totalTextWidth;
	
	
	
	
	private int oldms = 500;// ������UIʱ�߳�����ʱ��
	private int sleepms = 20;// ����UIʱ�߳�����ʱ��(֡)
	private int mvingDistance = 6;// ����UIʱ����ÿ�ι�������
	private long millisecond = 500;// �մ���ʱ�ӳ����к�����

	private boolean isRunnable = false;
	private String down = null;
	private String up = null;
	private SurfaceHolder holder = null;

	// �ص�����
	private CallBackListener cbl;

	public NumberWheel(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public NumberWheel(Context context, AttributeSet attr) {
		super(context, attr);
		this.context = context;
		init();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);

		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		int width;
		int height;
		if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.EXACTLY) {
			width = widthSize;
		} else { // (mode == MeasureSpec.UNSPECIFIED) ������view
			width = viewWidth;
		}

		if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		} else { // (mode == MeasureSpec.UNSPECIFIED) ������view
			height = textHeight;
		}
		setMeasuredDimension(viewWidth, textHeight);
	}
	
	//�ؼ���ʼ��
	public void init(){
		
		initValue();
		this.millisecond=500;
		
		ViewGroup.LayoutParams lp=getLayoutParams();
		
		doLocations(getContext(), totalTextWidth, textHeight);
		millisecond+=System.currentTimeMillis();
		
		holder = getHolder();  
		holder.addCallback(this); //����Surface�������ڻص�  
	}
	
	
	
	public CallBackListener getCallBackListener() {
		return cbl;
	}
	public void setCallBackListener(CallBackListener cbl) {
		this.cbl = cbl;
	}
	
	public void setNumber(int endShowNum) {
		sleepms = 17;
		down = null;
		up = null;
		this.endShowNum = endShowNum;
		
		init();
		
		isRunnable = true;
		if (cbl != null) {
			cbl.start();
		}
		
		new Thread(this).start();
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		doDraw();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}
	
	//�߳�����
	@Override
	public void run() {
		while(isRunnable){
			try {
				if (millisecond < System.currentTimeMillis() && sleepms != oldms) {
					if (endByOrder(-1)) {// �Ƿ�������ָ���
						sleepms = oldms;
						if (null != cbl)
							cbl.end();
					} else
						doDraw();
				}
				Thread.sleep(sleepms);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void doDraw(){
		Canvas canvas = holder.lockCanvas(null);
		if(canvas != null){
//			Paint paint2 = new Paint();
//			paint2.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
//			canvas.drawPaint(paint2);
			canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
		}
		for (int i = length - 1; i >= 0; i--) {
			int[] temp = runningNum[i];
			String d = temp[0] + "";
			if (temp[3] == 0) {
				if(canvas != null){
					canvas.drawText(temp[0] + "", locations[i][0], locations[i][1] + textHeight, paint);
				}
				continue;
			}
			if (temp[2] < -textHeight) {// ��������
				temp[2] = temp[2] + textHeight * 2;
				temp[0] = temp[0] + 2 > 9 ? temp[0] + 2 - 10 : temp[0] + 2;
				d = temp[0] + "";
			}
			int location = 0;// ����λ��0��1��
			if (temp[2] >= 0) {
				location = 1;
			} else {
				location = 0;
			}
			int x = endShowNumString.charAt(i) - 48;
			boolean isContinue=false;
			int z = 0;
			switch (location) {
			case 0://shang
				z = temp[0] + 1 > 9 ? temp[0] + 1 - 10 : temp[0] + 1;
				if (x == z && (temp[2] + textHeight) >= 0 && endByOrder(i)) {
					isContinue = true;
					d = z + "";
				}
				break;
			case 1://xia
				z = temp[0];
				if (x == z && temp[2] >= 0 && endByOrder(i)) {
					isContinue = true;
					d = z + "";
				}
				break;
			}
			
			if(isContinue){
				temp[0] = z;
				temp[3] = 0;
				if(canvas != null){
					canvas.drawText(d, locations[i][0], locations[i][1] + textHeight, paint);
				}
				continue;
			}
			Rect r=new Rect(temp[1], temp[2], textWidth+temp[1], textHeight+temp[2]);
			switch (location){
			case 0://shang
				up = d;
				if(canvas != null){
					canvas.drawText(up, r.left, r.bottom, paint);
				}

				r.top += textHeight;
				r.bottom += textHeight;
				down = (temp[0] + 1 > 9 ? temp[0] + 1 - 10 : temp[0] + 1) + "";
				if(canvas != null){
					canvas.drawText(down, r.left, r.bottom, paint);
				}
				
				break;
			case 1://xia
				down = d;
				if(canvas != null){
					canvas.drawText(down, r.left, r.bottom, paint);
				}

				r.top -= textHeight;
				r.bottom -= textHeight;
				up = (temp[0] - 1 < 0 ? temp[0] - 1 + 10 : temp[0] - 1) + "";
				if(canvas != null){
					canvas.drawText(up, r.left, r.bottom, paint);
				}
				break;
			}
			
			temp[2] -= mvingDistance;
		}
		if(canvas != null){
			holder.unlockCanvasAndPost(canvas);
		}
	}

	//�ص�
	public interface CallBackListener{
		public void start();

		public void end();
	}	
	
		
	//��ʹ��������Ϣ
	public void initValue(){
		paint = new Paint();
		paint.setTextSize(getSpPixel(context, 20));
		paint.setColor(Color.parseColor("#1692dc"));
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		textWidth = (int) paint.measureText("0");
		FontMetrics fm = paint.getFontMetrics();
		textHeight = (int) Math.ceil(fm.descent - fm.ascent);

		startShowNumString = "";
		endShowNumString = endShowNum + "";

		if (endShowNumString.length() > length) {
			length = endShowNumString.length();
		}
		
		int endLength = endShowNumString.length();
		for (int i = 0; i < length - endLength; i++) {
			endShowNumString = "0" + endShowNumString;
		}
		for (int i = 0; i < length; i++) {
			startShowNumString += "0";
		}
		
		//�ؼ����
		int sLength = (int) paint.measureText(startShowNumString);
		int eLength = (int) paint.measureText(endShowNumString);
		totalTextWidth = sLength > eLength ? sLength : eLength;
		viewWidth = (length - 1) * textGap + totalTextWidth;
		
		runningNum = new int[length][];
		char[] ssns = startShowNumString.toCharArray();
		char[] esns = endShowNumString.toCharArray();
		for (int i = length - 1; i >= 0; i--) {
			char c = ssns[i];
			if (c > 47 && c < 58) {// 0-9
				runningNum[i] = new int[4];
				runningNum[i][0] = c - 48;
				int ec = esns[i] - 48;
				if (runningNum[i][0] == ec && endByOrder(i)) {
					runningNum[i][3] = 0;
				} else {
					runningNum[i][3] = 1;
				}
			}
		}
	}
	
	//��˳���������
	private boolean endByOrder(int j) {
		for (int i = length - 1; i > j; i--) {
			int[] temp = runningNum[i];
			if (temp[3] != 0)
				return false;
		}
		return true;
	}
	
	//��ֵλ������
	public void doLocations(Context context, int width, int height) {
		locations = new int[length][];
		for (int i = 0; i < length; i++) {
			locations[i] = new int[2];
			locations[i][0] = width / length * i + i * textGap;
			runningNum[i][1] = locations[i][0];
			runningNum[i][2] = locations[i][1];
			locations[i][1] = (height - textHeight) / 2;
		}
	}
	
	public static int getSpPixel(Context context, float sp) {
		if (context == null) {
			return 0;
		}
		return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
				sp, context.getResources().getDisplayMetrics()));
	}
}
