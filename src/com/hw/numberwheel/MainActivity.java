package com.hw.numberwheel;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity {

	NumberWheel nw;
	NumberWheel nw2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		nw = (NumberWheel) findViewById(R.id.b);
		nw2 = (NumberWheel) findViewById(R.id.c);

//		 NumberBean nBean=new NumberBean(this);
//		 RelativeLayout.LayoutParams rl=new
//		 RelativeLayout.LayoutParams(400,100);
//		 final NumberWheel nw=new NumberWheel(this,nBean,rl,500);
//		 ((FrameLayout)rootView).addView(nw);
		nw.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				nw.setNumber(6123);
			}
		});
		nw2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				nw2.setNumber(6123);
			}
		});
	}
}
