package com.adhub.activities;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

import com.adhub.R;
import com.adhub.objects.Produto;
import com.adhub.objects.Propaganda;
import com.adhub.services.SearchBeaconService;
import com.adhub.utils.Cache;

public class PropagandaActivity extends SuperActivity {

	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.propaganda);

		webView = (WebView) findViewById(R.id.webView);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			Propaganda propaganda = (Propaganda) extras.get("propaganda");

			String url = propaganda.getUrl();
			webView.loadUrl(url);

			String empresa = extras.getString("empresa");
			Bitmap icon = (Bitmap) extras.get("icon");

			getActionBar().setTitle(empresa);

			Drawable drawable = new BitmapDrawable(getResources(), icon);
			getActionBar().setIcon(drawable);

			String color = propaganda.getNavbarColor();

			getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));

			String key = propaganda.getMajorID() + "_" + propaganda.getMinorID();

			Produto produto = (Produto) Cache.readObject(this, key);

			if (!propaganda.isVisualized()) {

				int notificationCounter = getNotificationCounter();

				notificationCounter--;

				if (notificationCounter >= 0) {
					setNotificationCounter(notificationCounter);
				}
				
				propaganda.setVisualized(true);
				
				produto.setPropaganda(propaganda);

				try {
					Cache.writeObject(this, key, produto);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

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
