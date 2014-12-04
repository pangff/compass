package com.pangff.compass;

import android.app.Activity;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class CompassView implements SensorEventListener {
	// 定义显示指南针图片的组件
	private View compassPanel;
	private ImageView compassPointer;
	// 记录指南针图片转过的角度
	// 定义真机的Sensor管理器
	private SensorManager mSensorManager;
	private Activity activity;
	private Sensor gsensor;
	private Sensor msensor;
	private float[] mGravity = new float[3];
	private float[] mGeomagnetic = new float[3];
	private float azimuth = 0f;
	private float currectAzimuth = 0;

	public CompassView(Activity activity, View rootView) {
		this.activity = activity;
		compassPanel = rootView.findViewById(R.id.compass);
		compassPointer = (ImageView) rootView.findViewById(R.id.compassPointer);
		// 获取真机的传感器管理服务
		mSensorManager = (SensorManager) activity
				.getSystemService(activity.SENSOR_SERVICE);
		gsensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		msensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
	}

	public void registerListener() {
		mSensorManager.registerListener(this, gsensor,
				SensorManager.SENSOR_DELAY_GAME);
		mSensorManager.registerListener(this, msensor,
				SensorManager.SENSOR_DELAY_GAME);
	}

	public void unregisterListener() {
		mSensorManager.unregisterListener(this);
	}

	private void adjustArrow() {
		if (compassPanel == null) {
			return;
		}
		Animation an = new RotateAnimation(-currectAzimuth, -azimuth,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);

		an.setDuration(500);
		an.setRepeatCount(0);
		an.setFillAfter(true);

		compassPanel.startAnimation(an);

		// Log.e("-azimuth+dxDegree","-azimuth+dxDegree======"+(-azimuth+dxDegree));
		RotateAnimation rb = new RotateAnimation(
				(float) (-currectAzimuth - dxDegree),
				(float) (-azimuth - dxDegree), Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		// 设置动画的持续时间
		rb.setDuration(500);
		// 设置动画结束后的保留状态
		rb.setFillAfter(true);
		compassPointer.startAnimation(rb);
		currectAzimuth = azimuth;
	}
//
//	private double getDeclination(Location location) {
//		GeomagneticField geoField = new GeomagneticField(
//				(float) location.getLatitude(),
//				(float) location.getLongitude(),
//				(float) location.getAltitude(), System.currentTimeMillis());
//		return geoField.getDeclination();
//	}

	/**
	 * 获取方位
	 * 
	 * @return
	 */
	public String getBeering() {
		if (dxDegree > 0 && dxDegree < 90) {
			return "NE" + dxDegree;
		} else if (dxDegree == 90) {
			return "E" + dxDegree;
		} else if (dxDegree > 90 && dxDegree < 180) {
			return "SE" + dxDegree;
		} else if (dxDegree == 180) {
			return "S" + dxDegree;
		} else if (dxDegree == 0) {
			return "N" + dxDegree;
		} else if (dxDegree < 0 && dxDegree > -90) {
			return "NW" + dxDegree * -1;
		} else if (dxDegree == -90) {
			return "W" + dxDegree * -1;
		} else if (dxDegree < -90 && dxDegree > -180) {
			return "SW" + dxDegree * -1;
		} else if (dxDegree == -180) {
			return "S" + dxDegree * -1;
		} else {
			return "";
		}
	}

	double dxDegree = 0;

	/**
	 * @param lat_a纬度a
	 * @param lng_a
	 * @param lat_b纬度b
	 * @param lng_b
	 */
	public void setPointer(double lat_a, double lng_a, double lat_b,
			double lng_b) {
		Location aLocation = new Location("");// provider name is unecessary
		aLocation.setLatitude(lat_a);// your coords of course
		aLocation.setLongitude(lng_a);

		Location bLocation = new Location("");// provider name is unecessary
		bLocation.setLatitude(lat_b);// your coords of course
		bLocation.setLongitude(lng_b);
		dxDegree = aLocation.bearingTo(bLocation)+180;
	}


	@Override
	public void onSensorChanged(SensorEvent event) {
		final float alpha = 0.97f;

		synchronized (this) {
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

				mGravity[0] = alpha * mGravity[0] + (1 - alpha)
						* event.values[0];
				mGravity[1] = alpha * mGravity[1] + (1 - alpha)
						* event.values[1];
				mGravity[2] = alpha * mGravity[2] + (1 - alpha)
						* event.values[2];
			}

			if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
				mGeomagnetic[0] = alpha * mGeomagnetic[0] + (1 - alpha)
						* event.values[0];
				mGeomagnetic[1] = alpha * mGeomagnetic[1] + (1 - alpha)
						* event.values[1];
				mGeomagnetic[2] = alpha * mGeomagnetic[2] + (1 - alpha)
						* event.values[2];
			}

			float R[] = new float[9];
			float I[] = new float[9];
			boolean success = SensorManager.getRotationMatrix(R, I, mGravity,
					mGeomagnetic);
			if (success) {
				float orientation[] = new float[3];
				SensorManager.getOrientation(R, orientation);
				azimuth = (float) Math.toDegrees(orientation[0]); // orientation
				azimuth = (azimuth + 360) % 360;
				adjustArrow();
			}
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

}
