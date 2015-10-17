package com.newppt.android.ui;

import android.app.Activity;
import android.os.Bundle;

import com.newppt1.android_v5.R;

public class LoadingActivity extends Activity{
	
	public static LoadingActivity load = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.loading);	
		load = this;
   }
}