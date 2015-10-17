package com.newppt.android.ui;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/***
 * 透明画布类
 * @author Administrator
 *
 */
public class NewImage extends ImageView {

	float _mX;
	float _mY;
	private Path _path;
	public Paint _paint = null;
	final int VIEW_WIDTH = 320;
	final int VIEW_HEIGHT = 480;
	public Bitmap _bitmapDraw = null;
	public Canvas _canvas = null;
	private int _penSize = 6;
	public List<DrawPath> _paths = null;
	private List<DrawPath> _redoPaths = null;
	private DrawPath _dPath = null;

	public NewImage(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		innitCanvas();
		innitPaint();

		_paths = new ArrayList<DrawPath>();
		_redoPaths = new ArrayList<DrawPath>();

	}

	public NewImage(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		innitCanvas();
		innitPaint();

		_paths = new ArrayList<DrawPath>();
		_redoPaths = new ArrayList<DrawPath>();

	}

	public NewImage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	private void innitCanvas() {
		_bitmapDraw = Bitmap.createBitmap(VIEW_WIDTH, VIEW_HEIGHT,
				Config.ARGB_8888);
		_bitmapDraw.eraseColor(Color.TRANSPARENT);
		_canvas = new Canvas();
		_canvas.setBitmap(_bitmapDraw);
	}

	private void innitPaint() {
		_path = new Path();
		_paint = new Paint(Paint.DITHER_FLAG);
		_paint.setColor(Color.RED);

		_paint.setStyle(Paint.Style.STROKE);     /* 设置paint的 style 为STROKE：空心 */

		_paint.setStrokeJoin(Paint.Join.ROUND);    //设置结合处的样子，Miter:结合处为锐角， Round:结合处为圆弧：BEVEL：结合处为直线。
		_paint.setStrokeCap(Paint.Cap.ROUND);
		_paint.setAntiAlias(true);	 /* 去锯齿 */ 
		_paint.setDither(true);           //防抖动
		_paint.setStrokeWidth(_penSize);
	}

	public void setPaint() {
		_paint = new Paint(Paint.DITHER_FLAG);
		_paint.setColor(Color.RED);

		_paint.setStyle(Paint.Style.STROKE);

		_paint.setStrokeJoin(Paint.Join.ROUND);
		_paint.setStrokeCap(Paint.Cap.ROUND);
		_paint.setAntiAlias(true);
		_paint.setDither(true);
		_paint.setStrokeWidth(_penSize);
	}

	public void clear() {
		_paths.clear();
		_redoPaths.clear();

		invalidate();			//更新view
	}

	public void initEraserPaint() {
		_paint.setStrokeWidth(30);
		_paint.setColor(Color.WHITE);   //setXfermode 设置两张图片相交时的模式 
								//我们知道 在正常的情况下，在已有的图像上绘图将会在其上面添加一层新的形状。
								//如果新的Paint是完全不透明的，那么它将完全遮挡住下面的Paint； 
								//而setXfermode就可以来解决这个问题 
		_paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
	}

	public void undo() {
		if (!_paths.isEmpty() && _paths.size() > 0) {
			int location = _paths.size() - 1;
			_redoPaths.add(_paths.get(location));
			_paths.remove(location);
			// innitCanvas();
			Iterator<DrawPath> iter = _paths.iterator();
			while (iter.hasNext()) {
				DrawPath dp = iter.next();
				// dp.paint.setColor(dp.color);
				// dp.paint.setMaskFilter(dp.mf);
				// dp.paint.setStrokeWidth(dp.strokeWidth);
				_canvas.drawPath(dp.path, dp.paint);
			}
			invalidate();
		}
	}

	public void redo() {
		if (!_redoPaths.isEmpty() && _redoPaths.size() > 0) {
			// innitCanvas();
			if (!_paths.isEmpty() && _paths.size() > 0) {
				Iterator<DrawPath> iter = _paths.iterator();
				while (iter.hasNext()) {
					DrawPath dp = iter.next();
					_canvas.drawPath(dp.path, dp.paint);
				}
			}
			int pre = _redoPaths.size() - 1;
			DrawPath dp = _redoPaths.get(pre);
			dp.paint.setColor(dp.color);
			dp.paint.setMaskFilter(dp.mf);
			dp.paint.setStrokeWidth(dp.strokeWidth);
			_canvas.drawPath(dp.path, dp.paint);
			_paths.add(dp);
			_redoPaths.remove(pre);
			invalidate();
		}
	}

	public void clearMemery(Bitmap bitmap) {
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}
	}

	public byte[] Bitmap2Bytes(Bitmap bitmap) {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);   //图片压缩
		return outStream.toByteArray();
	}

	public void save(byte[] buffer) {

		
	}

	public Bitmap getBmp() {
		Bitmap mCombinedBmp = Bitmap.createBitmap(_bitmapDraw.getWidth(),
				_bitmapDraw.getHeight(), Bitmap.Config.ARGB_8888);
		mCombinedBmp.eraseColor(Color.TRANSPARENT);
		Canvas canvas = new Canvas(mCombinedBmp);
		canvas.drawColor(Color.WHITE);
		canvas.drawBitmap(_bitmapDraw, 0, 0, null);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		return mCombinedBmp;
	}

	public void touchStart(float x, float y) {
		_path.moveTo(x, y);
		_mX = x;
		_mY = y;
		System.out.println("----------x=" + x + "  " + "y=" + y);
	}

	public void touchMove(float x, float y) {
		float dx = Math.abs(x - _mX);
		float dy = Math.abs(y - _mY);
		if (dx != 0 && dy != 0) {
			_path.quadTo(_mX, _mY, (x + _mX) / 2, (y + _mY) / 2);//quadTo这个方法同我们平时在photoshop中所使用到的“笔迹”功能一样。
																//说白了，就是将一条线段变成一个曲线。而这条曲线顾名思义是一条抛物线而已
		}
		_mX = x;
		_mY = y;
	}

	public void touchUP() {
		_path.lineTo(_mX, _mY);
		_canvas.drawPath(_path, _paint);
		_paths.add(_dPath);
		_path = null;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {

		case MotionEvent.ACTION_DOWN:
			_dPath = new DrawPath();
			_path = new Path();
			_dPath.paint = _paint;
			_dPath.path = _path;
			_dPath.color = _paint.getColor();
			_dPath.mf = _paint.getMaskFilter();
			_dPath.strokeWidth = _paint.getStrokeWidth();
			touchStart(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			touchMove(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			touchUP();
			invalidate();
			System.out.println("----->dafaf");
			break;
		}
		return true;

	}

	@Override
	public void onDraw(Canvas canvas) {
		Paint bmpPaint = new Paint();
		canvas.drawBitmap(_bitmapDraw, 0, 0, bmpPaint); 
		if (_path != null) {
			canvas.drawPath(_path, _paint);

		}
	}

	public class DrawPath {
		private Path path;
		private Paint paint;
		private int color; 
		private MaskFilter mf; /*MaskFilter类可以为Paint分配边缘效果。
								对MaskFilter的扩展可以对一个Paint边缘的alpha通道应用转换。Android包含了下面几种MaskFilter：
								BlurMaskFilter   指定了一个模糊的样式和半径来处理Paint的边缘。
								EmbossMaskFilter  指定了光源的方向和环境光强度来添加浮雕效果。
								要应用一个MaskFilter，可以使用setMaskFilter方法，并传递给它一个MaskFilter对象。*/
		private float strokeWidth;
	}

}
