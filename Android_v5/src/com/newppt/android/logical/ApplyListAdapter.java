package com.newppt.android.logical;

import java.util.List;

import com.newppt.android.ui.TongbuTagFragment;
import com.newppt1.android_v5.R;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ApplyListAdapter extends BaseAdapter {

	private LayoutInflater _inflater;
	// private String[] _pptNames;
	private List<String> _pptNames;
	private List<String> _lastRenewTimes;

	// private String[] _lastRenewTimes;

	private class MyViewHolder {
		ImageView logoImage;
		TextView showpptName;
		TextView showLastRenewTime;
	}

	public ApplyListAdapter(LayoutInflater inflater, List<String> pptNames,
			List<String> lastRenewTimes) {
		this._inflater = inflater;
		this._pptNames = pptNames;
		this._lastRenewTimes = lastRenewTimes;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return _pptNames.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return _pptNames.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MyViewHolder holder;
		// TODO Auto-generated method stub
//		LayoutInflater inflater = (LayoutInflater) _inflater
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = _inflater.inflate(R.layout.pptlist_ada, parent, false);
			holder = new MyViewHolder();
			holder.logoImage = (ImageView) convertView
					.findViewById(R.id.pptlogo);
			holder.showpptName = (TextView) convertView
					.findViewById(R.id.pptfilename);// PPT名称
			holder.showLastRenewTime = (TextView) convertView
					.findViewById(R.id.time);// 修改时间
			holder.showpptName.setText(_pptNames.get(position));
			holder.showLastRenewTime.setText("修改时间: "
					+ _lastRenewTimes.get(position));

			// convertView.setOnClickListener(new MyApplyListener(position));
			convertView.setTag(holder);
		} else {
			
			holder = (MyViewHolder) convertView.getTag();			
			holder.showpptName.setText(_pptNames.get(position));
			holder.showLastRenewTime.setText("修改时间: "
					+ _lastRenewTimes.get(position));
		}

		return convertView;
	}
}
