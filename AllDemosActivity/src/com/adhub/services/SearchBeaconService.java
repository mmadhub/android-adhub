package com.adhub.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.PowerManager;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;
import ch.boye.httpclientandroidlib.client.ClientProtocolException;

import com.adhub.R;
import com.adhub.activities.PropagandaActivity;
import com.adhub.dao.ClienteDAO;
import com.adhub.dao.ProdutoDAO;
import com.adhub.listeners.NotificationListener;
import com.adhub.objects.Cliente;
import com.adhub.objects.Produto;
import com.adhub.objects.Propaganda;
import com.adhub.utils.Cache;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;

public class SearchBeaconService {

	private static SearchBeaconService instance;
	private Context context;

	private static final String ESTIMOTE_BEACON_PROXIMITY_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
	private static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region("rid", ESTIMOTE_BEACON_PROXIMITY_UUID, null, null);
	private static final int TIME_TO_UPDATE = 5;
	private static final int TIME_TO_WAIT = 25000;
	private static final String PREFS_NAME = "NOTIFICATION_COUNTER";
	private static final int NOTIFICATION_ID = 123;

	private BeaconManager beaconManager;

	private NotificationListener notificationListener;

	private boolean serviceStarted;

	private NotificationManager notificationManager;

	private SearchBeaconService() {
	}

	private SearchBeaconService(Context context) {
		this.context = context;
		notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
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
		beaconManager.setForegroundScanPeriod(5000, TIME_TO_WAIT);
		beaconManager.setRangingListener(new BeaconManager.RangingListener() {
			@Override
			public void onBeaconsDiscovered(Region region, final List<Beacon> beacons) {
				// Note that beacons reported here are already sorted by
				// estimated
				// distance between device and beacon.

				if (beacons.size() == 0) {
					return;
				}
				new Thread(new Runnable() {

					@Override
					public void run() {
						Date d = new Date();
						long now = d.getTime();

						long time = TIME_TO_UPDATE * 60 * 1000;

						ArrayList<HashMap<String, Integer>> newAds = new ArrayList<HashMap<String, Integer>>();

						for (Beacon beacon : beacons) {

							String key = Cache.makeKey(beacon.getMajor(), beacon.getMinor());
							Produto produto = (Produto) Cache.readObject(context, key);

							if (produto == null || now > produto.getLastUpdate() + time) {

								try {
									produto = ProdutoDAO.getProduto(beacon.getMajor(), beacon.getMinor(), context);
								} catch (ClientProtocolException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								}

							}

							int proximity = 0;
							switch (Utils.computeProximity(beacon)) {
							case IMMEDIATE:
								proximity = 0;
								break;
							case NEAR:
								proximity = 1;
								break;
							case FAR:
								proximity = 2;
								break;
							default:
								proximity = 4;
								break;
							}
							Propaganda propaganda = produto.getPropaganda(proximity);
							if (propaganda != null && !propaganda.isVisualized()) {
								HashMap<String, Integer> newAd = new HashMap<String, Integer>();
								newAd.put("major", produto.getMajorID());
								newAd.put("minor", produto.getMinorID());
								newAd.put("propaganda", propaganda.getPropagandaID());

								newAds.add(newAd);

								propaganda.setVisualized(true);

								try {
									Cache.writeObject(context, key, produto);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}

						}

						if (newAds.size() > 0) {

							SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
							int notificationCounter = settings.getInt(PREFS_NAME, 0);

							notificationCounter += newAds.size();

							SharedPreferences.Editor editor = settings.edit();
							editor.putInt(PREFS_NAME, notificationCounter);

							editor.commit();

							if (notificationListener != null) {
								notificationListener.updateNotificationCounter(notificationCounter);
							}

							ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

							List<RunningTaskInfo> services = activityManager.getRunningTasks(Integer.MAX_VALUE);

							String activity = services.get(0).topActivity.toString();

							PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

							if (activity.contains("ComponentInfo{com.adhub") == false || !powerManager.isScreenOn()) {
								Intent notifyIntent = null;

								String msg = "";
								String title = "";
								Bitmap icon = null;
								String ticker = "";
								if (newAds.size() == 1) {
									HashMap<String, Integer> ad = newAds.get(0);
									int major = ad.get("major");
									int minor = ad.get("minor");
									int propagandaID = ad.get("propaganda");

									String key = major + "_" + minor;

									Produto produto = (Produto) Cache.readObject(context, key);

									Propaganda prop = null;

									for (Propaganda p : produto.getPropagandas()) {
										if (p.getPropagandaID() == propagandaID) {
											prop = p;
											break;
										}
									}

									Cliente cliente = ClienteDAO.getCliente(major);

									title = cliente.getNome();
									msg = prop.getMensagemNotificacao();
									icon = getBitmapFromURL(cliente.getUrlLogo());
									ticker = "Você possui um anúncio não visualizado";

									notifyIntent = new Intent(context, PropagandaActivity.class);
									notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
									notifyIntent.putExtra("url", prop.getUrl());
									notifyIntent.putExtra("color", prop.getNavbarColor());
									notifyIntent.putExtra("empresa", cliente.getNome());
									notifyIntent.putExtra("icon", icon);
								} else {
									ticker = "Você possui " + newAds.size() + " anúncios não visualizados";
								}

								PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
								Notification notification = new Notification.Builder(context).setSmallIcon(R.drawable.notify).setLargeIcon(icon).setTicker(ticker).setContentTitle(title).setContentText(msg).setAutoCancel(true).setContentIntent(pendingIntent).build();
								notification.defaults |= Notification.DEFAULT_ALL;
								notificationManager.notify(NOTIFICATION_ID, notification);

							}
						}

					}
				}).start();
			}

		});
		connectToService();
	}

	public Bitmap getBitmapFromURL(String strURL) {
		try {
			URL url = new URL(strURL);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
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

	public NotificationManager getNotificationManager() {
		return notificationManager;
	}

	public void setContext(Context context) {
		this.context = context;
	}

}
