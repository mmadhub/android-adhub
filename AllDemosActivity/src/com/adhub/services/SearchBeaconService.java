package com.adhub.services;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.os.PowerManager;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

public class SearchBeaconService {

	private static SearchBeaconService instance;
	private static Context context;

	private static final String ESTIMOTE_BEACON_PROXIMITY_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
	private static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region("rid", ESTIMOTE_BEACON_PROXIMITY_UUID, null, null);

	private BeaconManager beaconManager;

	private SearchBeaconService() {
		// Configure BeaconManager.

		beaconManager = new BeaconManager(context);
		beaconManager.setRangingListener(new BeaconManager.RangingListener() {
			@Override
			public void onBeaconsDiscovered(Region region, final List<Beacon> beacons) {
				// Note that beacons reported here are already sorted by
				// estimated
				// distance between device and beacon.
				try {
					Thread.sleep(5000);

					ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

					List<RunningTaskInfo> services = activityManager.getRunningTasks(Integer.MAX_VALUE);

					String activity = services.get(0).topActivity.toString();

					PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

					if (activity.contains("ComponentInfo{com.adhub") == true && powerManager.isScreenOn()) {
						Toast.makeText(context, "Activity is in foreground, active", Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(context, "Activity is in background, active", Toast.LENGTH_LONG).show();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		connectToService();
	}

	public static SearchBeaconService getInstance(Context context) {
		if (instance == null) {
			SearchBeaconService.context = context;
			instance = new SearchBeaconService();
		}

		return instance;
	}

	private void connectToService() {
		beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
			@Override
			public void onServiceReady() {
				try {
					beaconManager.startRanging(ALL_ESTIMOTE_BEACONS_REGION);
				} catch (RemoteException e) {
					Toast.makeText(context, "Cannot start ranging, something terrible happened", Toast.LENGTH_LONG).show();
					Log.e("Error", "Cannot start ranging", e);
				}
			}
		});
	}

}
