package com.adhub.services;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.os.PowerManager;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.adhub.listeners.NotificationListener;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

public class SearchBeaconService {

	private static SearchBeaconService instance;
	private Context context;

	private static final String ESTIMOTE_BEACON_PROXIMITY_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
	private static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region("rid", ESTIMOTE_BEACON_PROXIMITY_UUID, null, null);

	private BeaconManager beaconManager;

	private NotificationListener notificationListener;

	private boolean serviceStarted;

	private SearchBeaconService() {
	}

	private SearchBeaconService(Context context) {
		this.context = context;
	}

	public static SearchBeaconService getInstance(Context context) {
		if (instance == null) {
			instance = new SearchBeaconService(context);
		}

		return instance;
	}

	public void startService() {
		if (serviceStarted) {
			return;
		}

		// Configure BeaconManager.

		beaconManager = new BeaconManager(context);
		beaconManager.setRangingListener(new BeaconManager.RangingListener() {
			@Override
			public void onBeaconsDiscovered(Region region, final List<Beacon> beacons) {
				// Note that beacons reported here are already sorted by
				// estimated
				// distance between device and beacon.

				if (beacons.size() == 0) {
					return;
				}

				// buscar propaganda no servidor
				// verificar se já está no cache
				// se não estiver, cachear e notificar

				// *******************************************//

				if (notificationListener != null) {
					notificationListener.updateNotificationCounter();
				}

				ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

				List<RunningTaskInfo> services = activityManager.getRunningTasks(Integer.MAX_VALUE);

				String activity = services.get(0).topActivity.toString();

				PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

				if (activity.contains("ComponentInfo{com.adhub") == false || !powerManager.isScreenOn()) {
					// criar notificação informando que tem uma nova propaganda
				}
			}
		});

		connectToService();
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

	public NotificationListener getNotificationListener() {
		return notificationListener;
	}

	public void setNotificationListener(NotificationListener notificationListener) {
		this.notificationListener = notificationListener;
	}

}
