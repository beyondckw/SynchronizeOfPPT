package com.newppt.android.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.newppt.android.entity.MyPath;
import com.newppt.android.logical.ApplyListAdapter;
import com.newppt.android.logical.FileInfo;
import com.newppt1.android_v5.R;

public class XiazaiTagFragment extends Fragment implements OnItemClickListener, OnItemLongClickListener {
	
	private ListView _xiazaiListView;
	private List<String> _pptList = new ArrayList<String>();
	private List<String> _timeList = new ArrayList<String>();
	
	private ApplyListAdapter _adapter;
	String _path;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.tablist, container, false);

		
		iniView(inflater, rootView);
		
		return rootView;
	}

	private void iniView(LayoutInflater inflater, View rootView) {
		_xiazaiListView = (ListView) rootView.findViewById(R.id.pptlist);
		try {
			_path = MyPath.savePptFilePath;
			Map<String, Object> map = FileInfo.loadPPTInfo(_path);
			_pptList = (List<String>) map.get("pptList");
			_timeList = (List<String>) map.get("timeList");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		_adapter = new ApplyListAdapter(inflater, _pptList, _timeList);
		_xiazaiListView.setAdapter(_adapter);

		_xiazaiListView.setOnItemClickListener(this);
		_xiazaiListView.setOnItemLongClickListener(this);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
		final String filePath = _path + _adapter.getItem(position);

		Intent intent = new Intent();
		intent.setAction(android.content.Intent.ACTION_VIEW);
		File file =new File(filePath);
		intent.setDataAndType(Uri.fromFile(file), "text/*");
		startActivity(intent);
		
	}
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
		int position, long id) {
		// TODO Auto-generated method stub
		final int index = position;
		
		Builder dialog = new AlertDialog.Builder(getActivity());
		dialog.setTitle("删除对话框");
		dialog.setIcon(R.drawable.delete);		
		dialog.setMessage("确定要删除么？");
		
		dialog.setPositiveButton("确定", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				final String filePath = _path + _adapter.getItem(index);
				if (FileInfo.DeleteFolder(filePath)) {
					Toast.makeText(getActivity(), "删除成功", 2).show();
					_pptList.remove(index);
					_adapter.notifyDataSetChanged();
					_xiazaiListView.setAdapter(_adapter);
				} else {
					Toast.makeText(getActivity(), "删除失败", 2).show();
				}
			}
		});
		
		dialog.setNegativeButton("取消",null);
		
		dialog.create();
		dialog.show();

		return true;
	}
}
