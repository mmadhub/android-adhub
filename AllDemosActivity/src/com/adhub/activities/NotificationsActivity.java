package com.adhub.activities;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.adhub.R;
import com.adhub.adapters.NotificationsAdapter;
import com.adhub.objects.Cliente;
import com.adhub.objects.Produto;
import com.adhub.objects.Propaganda;
import com.adhub.utils.Cache;

public class NotificationsActivity extends SuperActivity {

	private ListView listView;
	private TextView emptyText;
	private ArrayList<HashMap<String, Integer>> listCached;
	private NotificationsAdapter adapter;
	private ArrayList<HashMap<String, Object>> adsList = new ArrayList<HashMap<String, Object>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.notifications);

		listView = (ListView) findViewById(R.id.listView);
		emptyText = (TextView) findViewById(android.R.id.empty);
		listView.setEmptyView(emptyText);

		listCached = (ArrayList<HashMap<String, Integer>>) Cache.readObject(this, "ads");

		prepareList();

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				HashMap<String, Object> propHash = adsList.get(position);
				Propaganda propaganda = (Propaganda) propHash.get("propaganda");
				Bitmap logo = (Bitmap) propHash.get("logo");
				String empresa = (String) propHash.get("company");

				Intent intent = new Intent(NotificationsActivity.this, PropagandaActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.putExtra("propaganda", propaganda);
				intent.putExtra("empresa", empresa);
				intent.putExtra("icon", logo);

				startActivity(intent);

				finish();
			}
		});
	}

	private void prepareList() {
		for (HashMap<String, Integer> ad : listCached) {
			int major = ad.get("major");
			int minor = ad.get("minor");
			int propagandaId = ad.get("propagandaId");

			String productKey = major + "_" + minor;

			Produto produto = (Produto) Cache.readObject(this, productKey);

			Propaganda propaganda = produto.getPropagandaById(propagandaId);

			HashMap<String, Object> propHash = new HashMap<String, Object>();

			propHash.put("propaganda", propaganda);

			String clientKey = "Client_" + major;

			Cliente cliente = (Cliente) Cache.readObject(this, clientKey);

			propHash.put("logo", cliente.getLogo());
			propHash.put("company", cliente.getNome());

			adsList.add(propHash);
		}

		adapter = new NotificationsAdapter(this, R.layout.notifications, R.layout.notification_item, adsList);
		listView.setAdapter(adapter);
	}
}
