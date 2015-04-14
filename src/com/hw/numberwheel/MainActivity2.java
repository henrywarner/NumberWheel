package com.hw.numberwheel;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity2 extends Activity {

	NumberWheel2 nw;
	int i =1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);
		nw = (NumberWheel2) findViewById(R.id.b);

		nw.setNumber(126548);
		
		nw.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(i % 2 == 0){
					nw.stopAnimation();
				}else{
					nw.startAnimation();
				}
				i++;
			}
		});
	}
}
