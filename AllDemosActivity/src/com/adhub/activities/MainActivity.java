package com.adhub.activities;

import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.adhub.R;
import com.adhub.services.BootCompletedReceiver;
import com.adhub.services.SearchBeaconService;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

public class MainActivity extends Activity {

	private static final String ESTIMOTE_BEACON_PROXIMITY_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
	private static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region("rid", ESTIMOTE_BEACON_PROXIMITY_UUID, null, null);
	private static final int REQUEST_ENABLE_BT = 1234;

	private BeaconManager beaconManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Configure BeaconManager.
		beaconManager = new BeaconManager(this);
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
			SearchBeaconService searchBeaconService = SearchBeaconService.getInstance(this);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_ENABLE_BT) {
			if (resultCode == Activity.RESULT_OK) {
				SearchBeaconService searchBeaconService = SearchBeaconService.getInstance(this);
			} else {
				Toast.makeText(this, "Bluetooth not enabled", Toast.LENGTH_LONG).show();
				getActionBar().setSubtitle("Bluetooth not enabled");
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
