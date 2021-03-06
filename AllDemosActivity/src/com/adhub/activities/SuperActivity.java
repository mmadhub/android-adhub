package com.adhub.activities;

import com.adhub.R;
import com.adhub.listeners.NotificationListener;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SuperActivity extends Activity implements NotificationListener {

	private static final String PREFS_NAME = "NOTIFICATION_COUNTER";
	private TextView txtNotification;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);

		RelativeLayout badgeLayout = (RelativeLayout) menu.findItem(R.id.badge).getActionView();
		txtNotification = (TextView) badgeLayout.findViewById(R.id.actionbar_notifcation_textview);

		int notificationCounter = getNotificationCounter();

		updateNotificationCounter(notificationCounter);
		return super.onCreateOptionsMenu(menu);
	}

	public void toNotifications(View v) {
		Intent intent = new Intent(this, NotificationsActivity.class);
		startActivity(intent);
	}

	public int getNotificationCounter() {
		SharedPreferences settings = this.getSharedPreferences(PREFS_NAME, 0);
		return settings.getInt(PREFS_NAME, 0);
	}

	public void setNotificationCounter(int notificationCounter) {
		SharedPreferences settings = this.getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(PREFS_NAME, notificationCounter);

		editor.commit();
	}

	@Override
	public void updateNotificationCounter(final int count) {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (count > 0) {
					txtNotification.setText(String.valueOf(count));
					txtNotification.setVisibility(View.VISIBLE);
				} else {
					txtNotification.setVisibility(View.GONE);
				}

			}
		});

	}

	@Override
	public void updateNotificationCounter() {
		int count = getNotificationCounter();

		updateNotificationCounter(count);

	}

}
