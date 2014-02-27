package com.adhub.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.adhub.R;
import com.adhub.services.SearchBeaconService;
import com.estimote.sdk.BeaconManager;

public class MainActivity extends SuperActivity {

	private static final int REQUEST_ENABLE_BT = 1234;

	private BeaconManager beaconManager;
	private SearchBeaconService searchBeaconService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		// Configure BeaconManager.
		beaconManager = new BeaconManager(this);
		searchBeaconService = SearchBeaconService.getInstance(this);
		searchBeaconService.setNotificationListener(this);
	}

	@Override
	protected void onStart() {
		super.onStart();

		// Check if device supports Bluetooth Low Energy.
		if (!beaconManager.hasBluetooth()) {
			Toast.makeText(this, "Device does not have Bluetooth Low Energy", Toast.LENGTH_LONG).show();
			return;
		}

		// If Bluetooth is not enabled, let user enable it.
		if (!beaconManager.isBluetoothEnabled()) {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		} else {
			searchBeaconService.startService();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		try {
			searchBeaconService.setNotificationListener(this);
			updateNotificationCounter();
		} catch (Exception e) {

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_ENABLE_BT) {
			if (resultCode == Activity.RESULT_OK) {
				searchBeaconService.startService();
			} else {
				Toast.makeText(this, "Bluetooth not enabled", Toast.LENGTH_LONG).show();
				getActionBar().setSubtitle("Bluetooth not enabled");
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
