package com.adhub.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.estimote.sdk.BeaconManager;

public class BootCompletedReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		BeaconManager beaconManager = new BeaconManager(context);
		if (beaconManager.hasBluetooth() && beaconManager.isBluetoothEnabled()) {
			Toast.makeText(context, "Bluetooth enabled", Toast.LENGTH_LONG).show();
			SearchBeaconService searchBeaconService = SearchBeaconService.getInstance(context);
		} else {
			Toast.makeText(context, "Bluetooth disabled", Toast.LENGTH_LONG).show();
		}
	}

}
