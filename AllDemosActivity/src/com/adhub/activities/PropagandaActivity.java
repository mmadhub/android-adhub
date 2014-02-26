package com.adhub.activities;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

import com.adhub.R;
import com.adhub.services.SearchBeaconService;

public class PropagandaActivity extends SuperActivity {

	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.propaganda);

		webView = (WebView) findViewById(R.id.webView);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String url = extras.getString("url");

			webView.loadUrl(url);

			String color = extras.getString("color");

			getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));

			String empresa = extras.getString("empresa");
			Bitmap icon = (Bitmap) extras.get("icon");

			getActionBar().setTitle(empresa);

			Drawable drawable = new BitmapDrawable(getResources(), icon);
			getActionBar().setIcon(drawable);

		}

		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	protected void onStart() {
		super.onStart();

		SearchBeaconService searchBeaconService = SearchBeaconService.getInstance(this);
		searchBeaconService.setNotificationListener(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
