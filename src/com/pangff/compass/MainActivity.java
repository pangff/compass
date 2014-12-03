package com.pangff.compass;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity  {
	
	CompassView compassView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		compassView = new CompassView(this, findViewById(R.id.rootView));
		
		
		compassView.setPointer(39.9086400566,116.3964321279,-26.9086400566,-116.3964321279);
		
		//compassView.setPointer(114.9086400566,116.3964321279,18.56,72.49);
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

