package com.newppt.android.ui;

import com.newppt.android.data.AnimUtils2;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

public class ScaleImage extends ImageView {

	final private int FLIP_DISTANCE = 30;

	public ScaleImage(Context context) {
		super(context);
		// TODO Auto-generated constructor stub

	}

	public ScaleImage(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ScaleImage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	private int count = 0;
	private long firClick;
	private long secClick;
	private boolean scaleTip = true;
	private float x;
	private float y;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (MotionEvent.ACTION_DOWN == event.getAction()) {
			count++;
			if (count == 1) {
				firClick = System.currentTimeMillis();
				x = event.getX();
				y = event.getY();
			} else if (count == 2) {
				secClick = System.currentTimeMillis();
				float mx = event.getX();
				float my = event.getY();
				if (secClick - firClick < 700
						&& Math.abs(mx - x) < FLIP_DISTANCE
						&& Math.abs(my - y) < FLIP_DISTANCE) {
					// ˫���¼�
					if (scaleTip) {
						x = event.getX();
						y = event.getY();

						AnimUtils2 animUtils2 = new AnimUtils2();
						animUtils2.imageZoomOut(this, 200, x, y);

						scaleTip = false;

					}

					else {
						AnimUtils2 animUtils2 = new AnimUtils2();
						animUtils2.imageZoomIn(this, 200, x, y);

						scaleTip = true;
					}
				}

				count = 0;
				firClick = 0;
				secClick = 0;

			}

		}
		return true;
		// return super.onTouchEvent(event);

	}
}
