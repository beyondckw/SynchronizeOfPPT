package com.newppt.android.ui;

import java.util.List;
import java.util.ListIterator;

import com.newppt.android.data.AnimUtils2;
import com.newppt.android.logical.FileInfo;
import com.newppt.android.logical.JpgPathInfo;

import com.newppt1.android_v5.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

public class PPTView extends Activity implements OnGestureListener {

	// 触屏事件的一些辅助变量
	private int _count = 0;
	private long _firClick;
	private long _secClick;
	private boolean _scaleTip = true;
	private float _x;
	private float _y;

	// 控件
	ImageView _pptImageView = null;
	ImageView _noteImageView = null;
	ImageView _shadowImageView = null;
	GestureDetector _detector;

	final int FLIP_DISTANCE = 50;
	final private int FLIP_DISTANCE2 = 20;

	List<JpgPathInfo> _infos = null;

	ListIterator<JpgPathInfo> _iter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pptview);

		iniView();
	}

	private void iniView() {
		_detector = new GestureDetector(this, this);  //Creates a GestureDetector with the supplied listener.
														//You may only use this constructor from a Looper thread.

		_pptImageView = (ImageView) findViewById(R.id.image);
		_noteImageView = (ImageView) findViewById(R.id.image2);
		_shadowImageView = (ImageView) findViewById(R.id.image3);

		Intent intent = this.getIntent();

		String path1 = intent.getStringExtra("path");

		System.out.println("----1" + path1);

		_infos = FileInfo.getJpgPathInfos(path1);
		_iter = _infos.listIterator();

		JpgPathInfo info = _iter.next();
		Bitmap pptBitmap = BitmapFactory.decodeFile(info.getPptJpg());
		_pptImageView.setImageBitmap(pptBitmap);
		_shadowImageView.setImageBitmap(pptBitmap);

		if (info.getNoteJpg() != null) {
			Bitmap noteBitmap = BitmapFactory.decodeFile(info.getNoteJpg());
			_noteImageView.setImageBitmap(noteBitmap);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub

		if (MotionEvent.ACTION_DOWN == event.getAction()) {
			_count++;
			if (_count == 1) {
				_firClick = System.currentTimeMillis();
				_x = event.getX();
				_y = event.getY();
			} else if (_count == 2) {
				_secClick = System.currentTimeMillis();
				float mx = event.getX();
				float my = event.getY();
				if (_secClick - _firClick < 700
						&& Math.abs(mx - _x) < FLIP_DISTANCE2
						&& Math.abs(my - _y) < FLIP_DISTANCE2) {
					// 双锟斤拷锟铰硷拷
					if (_scaleTip) {
						_x = mx;
						_y = my;

						AnimUtils2 animUtils2 = new AnimUtils2();
						animUtils2.imageZoomOut(_pptImageView, 200, _x, _y);
						animUtils2.imageZoomOut(_noteImageView, 200, _x, _y);

						_scaleTip = false;

					}

					else {
						AnimUtils2 animUtils2 = new AnimUtils2();
						animUtils2.imageZoomIn(_pptImageView, 200, _x, _y);
						animUtils2.imageZoomIn(_noteImageView, 200, _x, _y);

						_scaleTip = true;
					}
				}

				_count = 0;
				_firClick = 0;
				_secClick = 0;

			}
			// return true;

		}
		return _detector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		// 如果第一个触点事件的X座标大于第二个触点事件的X座标超过FLIP_DISTANCE
		// 也就是手势从右向左滑。
		System.out.println("-----kkkk");
		AnimUtils2 animUtils2 = new AnimUtils2();
		if (e1.getX() - e2.getX() > FLIP_DISTANCE) {
			// 为flipper设置切换的的动画效果
			System.out.println("-----5yy");
			if (!_iter.hasNext()) {
				Toast.makeText(this, "最后一张", 2).show();
				return true;
			}

			JpgPathInfo info = _iter.next();
			Bitmap pptBitmap = BitmapFactory.decodeFile(info.getPptJpg());
			_pptImageView.setImageBitmap(pptBitmap);
			_shadowImageView.setImageBitmap(pptBitmap);
			
//			animUtils2.layoutLeftOut(_pptImageView, 200);						
//			animUtils2.drawViewIn(_shadowImageView, 400);
			
			animUtils2.left_Out(this, _pptImageView);
			

			if (info.getNoteJpg() != null) {
				Bitmap noteBitmap = BitmapFactory.decodeFile(info.getNoteJpg());
				_noteImageView.setImageBitmap(noteBitmap);
//				animUtils2.drawViewIn(_noteImageView, 400);
			} else {
				_noteImageView.setImageBitmap(null);
			}

			return true;
		}
		// 如果第二个触点事件的X座标大于第一个触点事件的X座标超过FLIP_DISTANCE
		// 也就是手势从右向左滑。
		else if (e2.getX() - e1.getX() > FLIP_DISTANCE) {
			// 为flipper设置切换的的动画效果
			if (!_iter.hasPrevious()) {
				Toast.makeText(this, "这是首页", 2).show();
				return true;
			}

			JpgPathInfo info = _iter.previous();
			Bitmap pptBitmap = BitmapFactory.decodeFile(info.getPptJpg());
			_pptImageView.setImageBitmap(pptBitmap);
			_shadowImageView.setImageBitmap(pptBitmap);
//			animUtils2.layoutLeftIn(_pptImageView, 400);
			
//			animUtils2.drawViewOut(_pptImageView, 200);						
//			animUtils2.layoutLeftIn(_shadowImageView, 400);			
			
			animUtils2.right_Out(this, _pptImageView);
//			animUtils2.left_in(this, _pptImageView);
			
			if (info.getNoteJpg() != null) {
				Bitmap noteBitmap = BitmapFactory.decodeFile(info.getNoteJpg());
				_noteImageView.setImageBitmap(noteBitmap);
//				animUtils2.layoutLeftIn(_noteImageView, 400);
//				animUtils2.left_in(this, _noteImageView);
			} else {
				_noteImageView.setImageBitmap(null);
			}

			return true;
		}
		return false;
	}

	private long exitTime = 0;

	// 退出提示
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次返回选择页面",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				this.finish();
				System.exit(0);
				
				
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
