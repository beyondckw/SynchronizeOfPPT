package com.newppt.android.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

import com.newppt1.android_v5.R;

public class PLFragmentActivity extends FragmentActivity {
	
	private ImageView _tongBu;
	private ImageView _xiaZai;
	private List<Fragment> fragmentList = new ArrayList<Fragment>();
	private FragmentPagerAdapter _adapter;
	private ViewPager _viewpager;
	private int _currentPageIndex;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);    //设置Activity没有标题
		setContentView(R.layout.pptlist);
		
		inniView();
			
	}

	private void inniView() {
		_tongBu = (ImageView) findViewById(R.id.tongbuppt);
		_xiaZai = (ImageView) findViewById(R.id.xiazaippt);
		_viewpager = (ViewPager) findViewById(R.id.id_viewpager);
		
		TongbuTagFragment tongbuFragment = new TongbuTagFragment();
		XiazaiTagFragment xiazaiFragment = new XiazaiTagFragment();

		fragmentList.add(tongbuFragment);
		fragmentList.add(xiazaiFragment);
		
		_adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return fragmentList.size();
			}
			
			@Override
			public Fragment getItem(int arg0) {
				// TODO Auto-generated method stub
				return fragmentList.get(arg0);
			}
		};
		
		_viewpager.setAdapter(_adapter);
		
		
		optionEvent();
	}

	private void optionEvent() {
		
		_viewpager.setOnPageChangeListener(new OnPageChangeListener() {			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				
				switch (arg0) {
				case 0:
					_tongBu.setImageResource(R.drawable.tongbu_1);
					_xiaZai.setImageResource(R.drawable.xiazai_2);
					
					break;

				case 1:
					_tongBu.setImageResource(R.drawable.tongbu_2);
					_xiaZai.setImageResource(R.drawable.xiazai_1);
					break;
				}
				_currentPageIndex = arg0;
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		_tongBu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(_currentPageIndex == 0) {
					return;
				}
				
				_currentPageIndex = 0;
				_viewpager.setCurrentItem(_currentPageIndex, true);
				
				_tongBu.setImageResource(R.drawable.tongbu_1);
				_xiaZai.setImageResource(R.drawable.xiazai_2);
			}
		});
		
		_xiaZai.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(_currentPageIndex == 1) {
					return;
				}
				
				_currentPageIndex = 1;
				_viewpager.setCurrentItem(_currentPageIndex, true);
				
				_tongBu.setImageResource(R.drawable.tongbu_2);
				_xiaZai.setImageResource(R.drawable.xiazai_1);
			}
		});
	}
	
}
