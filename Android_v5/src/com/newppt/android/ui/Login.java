package com.newppt.android.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.newppt.android.data.GetIpThread;
import com.newppt.android.entity.MyPath;
import com.newppt.android.logical.FileInfo;
import com.newppt1.android_v5.R;

public class Login extends Activity {

	// private Button ok;
	private ImageView _enter;
	private Button _iPButton;
	private ImageView _gotoImageView;
	private EditText _ipEditView;
	private String _ipString;
	private Handler _handler;
	private ImageView _clearEdit;

	// ip地址验证正则表达式    //?!\\d\\d\\d所在缝隙的右侧必须不能匹配三个ddd
	Pattern pattern = Pattern
			.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\." +
					"((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\." +
					"((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\." +
					"((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");

	public Login() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		iniView();
	}

	private void iniView() {
		// 新建文件夹
		FileInfo.CreateFile(MyPath.rootPath);
		FileInfo.CreateFile(MyPath.savePptFilePath);

		_ipEditView = (EditText) this.findViewById(R.id.ip);
		// ok=(Button)this.findViewById(R.id.ok);

		_enter = (ImageView) findViewById(R.id.ok);
		_gotoImageView = (ImageView) this.findViewById(R.id.golist);
		_clearEdit = (ImageView) findViewById(R.id.clearTip);
		_iPButton = (Button) this.findViewById(R.id.getIP);
		optionEvent();
	}
	
   //操作事件函数
	private void optionEvent() {

		_gotoImageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Login.this, PLFragmentActivity.class);
				
				startActivity(intent);
			}
		});

		_iPButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				new GetIpThread(_handler).start();
			}
		});

		_enter.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String ipStr = _ipEditView.getText().toString().trim();
				Matcher matcher = pattern.matcher(ipStr);
				if (!matcher.matches()) {
					Toast.makeText(Login.this, "IP地址格式不对", 2).show();
					return;
				}

				if (ipStr == null || ipStr.equals("")) {
					Toast.makeText(Login.this, "IP地址为空", 2).show();
					return;
				}
				String portString = "8888";
				Intent intent = new Intent(Login.this, SvgView.class);
				intent.putExtra("ip", ipStr);
				intent.putExtra("port", portString);
				startActivity(intent);
				

			}
		});

		_clearEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				_ipEditView.setText("");
			}
		});

		_handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 0x123)
					_ipString = (String) msg.obj;
				_ipEditView.setText(_ipString);
			}
		};

	}

	private long exitTime = 0;

	// 退出提示
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);			
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
