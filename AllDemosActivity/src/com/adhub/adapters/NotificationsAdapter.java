package com.adhub.adapters;

import java.util.HashMap;
import java.util.List;

import com.adhub.R;
import com.adhub.objects.Propaganda;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NotificationsAdapter extends ArrayAdapter<HashMap<String, Object>> {

	private List<HashMap<String, Object>> ads;
	private Context context;
	private int layoutResourceId;

	public NotificationsAdapter(Context context, int resource, int layoutResourceId, List<HashMap<String, Object>> ads) {
		super(context, resource, layoutResourceId, ads);

		this.context = context;
		this.ads = ads;
		this.layoutResourceId = layoutResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		AdHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new AdHolder();
			holder.imgLogo = (ImageView) row.findViewById(R.id.imgLogo);
			holder.company = (TextView) row.findViewById(R.id.company);
			holder.adDescription = (TextView) row.findViewById(R.id.adDescription);

			row.setTag(holder);
		} else {
			holder = (AdHolder) row.getTag();
		}

		HashMap<String, Object> advertise = ads.get(position);

		Propaganda propaganda = (Propaganda) advertise.get("propaganda");

		holder.company.setText((String) advertise.get("company"));
		holder.adDescription.setText(propaganda.getMensagemNotificacao());
		holder.imgLogo.setImageBitmap((Bitmap) advertise.get("logo"));

		row.setActivated(propaganda.isVisualized());

		return row;
	}

	private static class AdHolder {
		ImageView imgLogo;
		TextView company;
		TextView adDescription;
	}

}
