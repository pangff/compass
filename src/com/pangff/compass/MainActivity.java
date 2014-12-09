package com.pangff.compass;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {

	CompassView compassView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		compassView = new CompassView(this, findViewById(R.id.rootView));

		// 21.422460825623126
		compassView.setPointer(39.79, 116.3964321279, 21.422460825623126,39.82620057548526 );

		// compassView.setPointer(114.9086400566,116.3964321279,18.56,72.49);
		Log.e("ddd", "getDensity=====getDensity:"+getDensity());
	}

	public  float getDensity() {
		return getResources().getDisplayMetrics().density;
	}

	@Override
	protected void onResume() {
		super.onResume();
		compassView.registerListener();
	}

	@Override
	protected void onPause() {
		super.onPause();
		compassView.unregisterListener();
	}
}
