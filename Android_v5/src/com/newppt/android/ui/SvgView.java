package com.newppt.android.ui;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.newppt.android.data.AnimUtils2;
import com.newppt.android.data.ClientSocket;
import com.newppt.android.data.SaveJpgThread;
import com.newppt.android.data.SendFileClient;
import com.newppt.android.entity.HMessage;
import com.newppt.android.entity.MyPath;
import com.newppt.android.logical.FileInfo;
import com.newppt1.android_v5.R;

public class SvgView extends Activity {

	Button _renewButton = null;
//	ImageView _connect = null;
	
	Button _loadButton = null;
	Button _controlButton = null;
	Button _handPaintButton = null;
	Button _clearButton = null;
	Button _eraserButton = null;
	Button _undobButton = null;
	Button _redobButton = null;
	Button _paintbButton = null;
	ProgressBar _greBar;
	ImageView _yincanLayoutLeft;
	LinearLayout _layoutLeft;
	
	Button _bijipptButton;
	Button _yuanbanpptButton;

	private boolean _touchTip = true;
	private boolean _layoutLeftInTip = false;
	
	private AnimUtils2 _animUtils2;
	LinearLayout _layoutButtom;
	LinearLayout _layoutTop;
	LayoutParams layoutParams;
	
	private boolean _contralTip = true; // 鍙楁帶鏍囧織
	private boolean _paintTip = false;
//	private boolean _loadSucceedTip = false;

	HMessage hMessage = null; // JPG鍚屾绾跨▼浼犺緭淇℃伅瀵硅薄

	private ClientSocket _clientSocket = null;

	private String _ip;

	private int _port = 8888;

	private String sendMessage = "Android";
	Handler _handler;
	Handler _loadHandler;
	ScaleImage _imageView = null;
	NewImage _drawView = null;
	// record the heigth and width of the imageview
	int heigth;
	int width;
	
//	String currentFileName;
	
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imageview);
		
		iniView();
	}

	private void iniView() {
		_imageView = (ScaleImage) this.findViewById(R.id.image);
		_layoutTop = (LinearLayout) findViewById(R.id.layoutTop);
		_layoutButtom = (LinearLayout) findViewById(R.id.layoutButtom);
		

		_yincanLayoutLeft = (ImageView) findViewById(R.id.yincanLayout_left);
		_layoutLeft = (LinearLayout) findViewById(R.id.layoutLeft);
		
		_bijipptButton = (Button) findViewById(R.id.goto_bijippt);
		_yuanbanpptButton = (Button) findViewById(R.id.goto_yuanbanppt);
		
		_drawView = (NewImage) this.findViewById(R.id.image2);
		layoutParams = _imageView.getLayoutParams();
		_drawView.setLayoutParams(layoutParams);
		
		_animUtils2 = new AnimUtils2();
		_animUtils2.drawViewOut(_drawView, 10);
		_animUtils2.layoutButtomOut(_layoutButtom, 10);
		_animUtils2.layoutLeftOut(_layoutLeft, 10);

		_renewButton = (Button) findViewById(R.id.renew);
//		_connect = (ImageView) findViewById(R.id.renew);
		_loadButton = (Button) findViewById(R.id.load);
		_controlButton = (Button) findViewById(R.id.contral);
		_handPaintButton = (Button) findViewById(R.id.paintMap);
		_greBar = (ProgressBar) findViewById(R.id.loadprogress);
		
		Intent intent = this.getIntent();
		_ip = intent.getStringExtra("ip");

		//鑾峰彇_imageview鐨勫楂�
		ViewTreeObserver vto2 = _imageView.getViewTreeObserver();
		vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				_imageView.getViewTreeObserver().removeGlobalOnLayoutListener(
						this);
				heigth = _imageView.getHeight();
				width = _imageView.getWidth();

				resetCanvas();
			}
		});
		
		messageEvent();
		conectAndRenew();
	}

//	private void resetButtonClickable() {
//		_clearButton.setClickable(_paintTip);
//		_redobButton.setClickable(_paintTip);
//		_undobButton.setClickable(_paintTip);
//		_paintbButton.setClickable(_paintTip);
//		_eraserButton.setClickable(_paintTip);
//	}

	/**
	 * 娑堟伅澶勭悊
	 */
	private void messageEvent() {

		// _handler 鍝嶅簲鍥剧墖鏇存柊
		_handler = new Handler() {
			@SuppressLint("NewApi")
			@Override
			public void handleMessage(Message msg) {
					
				if(msg.what == 0x110) {
					Toast.makeText(SvgView.this, msg.obj.toString(), 1).show();
					_greBar.setVisibility(View.GONE);
					
				}
				
				if (msg.what == 0x123 && _contralTip) {

					_greBar.setVisibility(View.GONE);
					 
//						resetCanvas();
					
					savePPTNoteJpg();

					hMessage = (HMessage) msg.obj;
					byte[] data = hMessage.getBuffer();
					Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
							data.length);
					
					_imageView.setImageBitmap(bitmap);					
//					_animUtils2.rotateView(_imageView, 400);
//					_animUtils2.drawViewIn(_imageView, 400);
					
					resetCanvas();
					_drawView.clear();

					Toast.makeText(SvgView.this, "加载成功", Toast.LENGTH_SHORT).show();
					
				}
			}
		};

		// _handler 鍝嶅簲涓嬭浇鎻愮ず
		_loadHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if (msg.what == 0x124) {
					Toast.makeText(SvgView.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
				}
				if (msg.what == 0x125) {
					System.out.println("---------55");
					Toast.makeText(SvgView.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
					MediaPlayer mediaPlayer = MediaPlayer.create(SvgView.this,
							R.raw.tip);
					mediaPlayer.start();
//					Vibrator vib = (Vibrator) SvgView.this.getSystemService(Service.VIBRATOR_SERVICE);   
//			            vib.vibrate(100);
				}
				
				if(msg.what == 0x126) {
					Toast.makeText(SvgView.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
				}
			}
		};

		// 缁樺浘
		_handPaintButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (_handPaintButton.getText().toString().equals("手写了啦")) {
//					_handPaintButton.setText("取消手写");
					_handPaintButton.setText(R.string.not_paintMap);
					_animUtils2.drawViewIn(_drawView, 10);
					_animUtils2.layoutButtomIn(_layoutButtom, 200 );

					 _paintTip = true; //璁剧疆鎵嬬粯鏍囧織涓簍rue
//					 resetButtonClickable();

				} else {
//					_handPaintButton.setText("手写了啦");
					_handPaintButton.setText(R.string.paintMap);
					_animUtils2.drawViewOut(_drawView, 10);
					_animUtils2.layoutButtomOut(_layoutButtom, 200 );
					_paintTip = false;
					// resetButtonClickable();
				}

			}
		});

		// renew
		_renewButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// webview.goBack();
				conectAndRenew();
			}

			
		});

		// load
		_loadButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (_ip.length() > 0) {
					SendFileClient sendFileClient = new SendFileClient(_ip,
							_loadHandler, "PPT");
					new Thread(sendFileClient).start();
				} else {
					Toast.makeText(SvgView.this, "ip was null", Toast.LENGTH_SHORT).show();
				}
			}
		});

		// control
		_controlButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (_controlButton.getText().toString().equals("同步哟")) {
//					_controlButton.setText("不同步");
					_controlButton.setText(R.string.contralButton);
					_contralTip = true;
					conectAndRenew();
				} else {
//					_controlButton.setText("同步哟");
					_controlButton.setText(R.string.tongbuyo);
					_contralTip = false;
				}

			}
		});

		// clear
		_clearButton = (Button) this.findViewById(R.id.clear);
		_clearButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				resetCanvas();
				_drawView.clear();

			}
		});

		// eraser
		_eraserButton = (Button) this.findViewById(R.id.eraser);
		_eraserButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				_drawView.initEraserPaint();
			}
		});

		// undo
		_undobButton = (Button) this.findViewById(R.id.undo);
		_undobButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				resetCanvas();
				_drawView.undo();
			}
		});

		// redo
		_redobButton = (Button) this.findViewById(R.id.redo);
		_redobButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				resetCanvas();
				_drawView.redo();
			}
		});

		// pen
		_paintbButton = (Button) this.findViewById(R.id.paint);
		_paintbButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				_drawView.setPaint();

			}
		});
		
		_yincanLayoutLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(_layoutLeftInTip) {
					_animUtils2.layoutLeftOut(_layoutLeft, 100);
					_yincanLayoutLeft.setImageResource(R.drawable.yuan_1);
				}
				else {
					_animUtils2.layoutLeftIn(_layoutLeft, 100);
					_yincanLayoutLeft.setImageResource(R.drawable.yuan_2);
				}
				
				_layoutLeftInTip = !_layoutLeftInTip;
			}
		});
		
		_bijipptButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(_clientSocket!=null && _clientSocket.connectSucceedTip) {
					
					savePPTNoteJpg();//save note
					
				    String currentFilePath = MyPath.rootPath + _clientSocket.getFileName();
				    Intent intent = new Intent(SvgView.this, PPTView.class);
					intent.putExtra("path", currentFilePath);
					startActivity(intent);
				}
				
				else {
					Toast.makeText(SvgView.this, "亲，您还没连接服务端呢", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		_yuanbanpptButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(_clientSocket != null) {
					String currenFilePath = MyPath.savePptFilePath + _clientSocket.getFileName();
					if(FileInfo.fileExist(currenFilePath) && !_clientSocket.getFileName().equals("")) {
						
						_animUtils2.layoutLeftOut(_layoutLeft, 10);
						_layoutLeftInTip = false;
						_yincanLayoutLeft.setImageResource(R.drawable.yuan_1);
						
						savePPTNoteJpg();//save note
						
						
						Intent intent = new Intent();
						intent.setAction(android.content.Intent.ACTION_VIEW);
						File file =new File(currenFilePath);
						intent.setDataAndType(Uri.fromFile(file), "text/*");
						startActivity(intent);						
					}
					else {
						Toast.makeText(SvgView.this, "亲，你没下载原版PPT", Toast.LENGTH_SHORT).show();
					}
				}
				else {
					Toast.makeText(SvgView.this, "亲，您还没连接服务端呢", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

   private void savePPTNoteJpg() {
	 
	if (_drawView._paths.size() == 0) {
		return ;
	}
	String rootPath = MyPath.rootPath + hMessage.getMsgDmt().getFilename();
	String savePath = rootPath+MyPath.noteJpg;
	
	FileInfo.CreateFile(rootPath);
	FileInfo.CreateFile(savePath);
	
	String filePath = savePath+"/ppt-"
			+ hMessage.getMsgDmt().getCurrentPage()
			+ ".jpg";						
	
	new SaveJpgThread(
			_drawView.Bitmap2Bytes(_drawView._bitmapDraw),
			filePath).start();
   }
	
	private void conectAndRenew() {
		if (_clientSocket != null) {
			_clientSocket.setStopTip();
			
		}

		if (_ip.length() > 0) {						
			_clientSocket = new ClientSocket(_ip, _port, _handler);
			_clientSocket.start();	
			
//			Intent intent = new Intent (SvgView.this,LoadingActivity.class);			
//			startActivity(intent);	
			_greBar.setVisibility(View.VISIBLE);

		} else {
			Toast.makeText(SvgView.this, "ip was null", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * reset Canvas
	 * 
	 */
	private void resetCanvas() {
		_drawView._bitmapDraw = Bitmap.createBitmap(width, heigth,
				Config.ARGB_8888);
		_drawView._canvas.setBitmap(_drawView._bitmapDraw);
	}

	private long exitTime = 0;

	// 閫�嚭鎻愮ず
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出到主界面",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				
				savePPTNoteJpg();//save note
				
				this.finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// 閲嶅啓瑙﹀睆浜嬩欢
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			System.out.println("------->succeed");
			if (_touchTip) {
				
				if(_paintTip) {
				   _animUtils2.startAnimOut(_layoutTop, _layoutButtom, 200);
				}
				else {
				  _animUtils2.layoutTopOut(_layoutTop, 200);
				}
				System.out.println("---------111" + _touchTip);
				_touchTip = false;

			} else {
				
				if(_paintTip) {
				  _animUtils2.startAnimIn(_layoutTop, _layoutButtom, 200);
				}
				else {
					  _animUtils2.layoutTopIn(_layoutTop, 200);
				}
				System.out.println("---------222" + _touchTip);
				_touchTip = true;

			}
		}

		return true;
	}
}
