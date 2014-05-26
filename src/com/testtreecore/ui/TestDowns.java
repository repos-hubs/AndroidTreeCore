package com.testtreecore.ui;

import com.testtreecore.R;
import com.treecore.UIBroadcast;
import com.treecore.activity.TActivity;
import com.treecore.activity.annotation.TInjectView;
import com.treecore.download.DownloadInfo;
import com.treecore.download.TIDownloadTaskListener;
import com.treecore.download.TMDownloadManager;
import com.treecore.filepath.TFilePathManager;
import com.treecore.utils.TUrlParserUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;

public class TestDowns extends TActivity implements OnClickListener,
		TIDownloadTaskListener {
	private String TAG = TestDowns.class.getCanonicalName();
	@TInjectView(id = R.id.Button_test1)
	private Button mTest1Button;
	@TInjectView(id = R.id.ProgressBar_test1)
	private ProgressBar mTest1ProgressBar;
	@TInjectView(id = R.id.Button_test2)
	private Button mTest2Button;
	@TInjectView(id = R.id.ProgressBar_test2)
	private ProgressBar mTest2ProgressBar;
	@TInjectView(id = R.id.Button_test3)
	private Button mTest3Button;
	@TInjectView(id = R.id.ProgressBar_test3)
	private ProgressBar mTest3ProgressBar;

	private DownloadInfo mDownloadInfo1, mDownloadInfo2, mDownloadInfo3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_down_);
		initControl();
	}

	@Override
	public void onClick(View arg0) {
		try {
			if (arg0 == mTest1Button) {
				if (mTest1Button.getText().equals("停止")) {
					TMDownloadManager.getInstance().stopDownloadTask(
							mDownloadInfo1);
				} else {
					TMDownloadManager.getInstance().addDownloadTask(
							mDownloadInfo1);
				}
			} else if (arg0 == mTest2Button) {
				if (mTest2Button.getText().equals("停止")) {
					TMDownloadManager.getInstance().stopDownloadTask(
							mDownloadInfo2);
				} else {
					TMDownloadManager.getInstance().addDownloadTask(
							mDownloadInfo2);
				}
			} else if (arg0 == mTest3Button) {
				if (mTest3Button.getText().equals("停止")) {
					TMDownloadManager.getInstance().stopDownloadTask(
							mDownloadInfo3);
				} else {
					TMDownloadManager.getInstance().addDownloadTask(
							mDownloadInfo3);
				}
			}
		} catch (Exception e) {
			makeText(e.getMessage());
		}
	}

	@Override
	public void processEvent(Intent intent) {
		super.processEvent(intent);
		int mainEvent = intent.getIntExtra(UIBroadcast.MAINEVENT, -1); // 主事�?
		int subEvent = intent.getIntExtra(UIBroadcast.EVENT, -1);// 次事�?

	}

	@Override
	public void onDownloadTaskUpdate(DownloadInfo task) {
		if (task == mDownloadInfo1) {
			mTest1ProgressBar.setProgress((int) task.getPercent());

			mTest1Button.setText(mDownloadInfo1.isRuning() ? "停止" : "开始");
		} else if (task == mDownloadInfo2) {
			mTest2ProgressBar.setProgress((int) task.getPercent());

			mTest2Button.setText(mDownloadInfo2.isRuning() ? "停止" : "开始");
		} else if (task == mDownloadInfo3) {
			mTest3ProgressBar.setProgress((int) task.getPercent());

			mTest3Button.setText(mDownloadInfo3.isRuning() ? "停止" : "开始");
		}
	}

	@Override
	public void onDownloadTaskCancel(DownloadInfo task) {
		// if (task == mDownloadInfo1) {
		// mTest1Button.setText("开始");
		// } else if (task == mDownloadInfo2) {
		// mTest2Button.setText("开始");
		// } else if (task == mDownloadInfo3) {
		// }
	}

	private void initControl() {
		mTest1Button.setOnClickListener(this);
		mTest2Button.setOnClickListener(this);
		mTest3Button.setOnClickListener(this);

		TMDownloadManager.getInstance().setTIDownloadTaskListener(this);
		String filePathString = TFilePathManager.getInstance()
				.getDownloadPath();
		mDownloadInfo1 = new DownloadInfo(
				"http://download.haozip.com/haozip_v3.1_hj.exe",
				filePathString, "haozip_v3.1_hj"
						+ TUrlParserUtils.getFileNameByUrl("") + ".exe");
		mDownloadInfo2 = new DownloadInfo(
				"http://dlsw.baidu.com/sw-search-sp/soft/24/13406/XiuXiu_BdSetup.4173490713.exe",
				filePathString, "XiuXiu_BdSetup.4173490713"
						+ TUrlParserUtils.getFileNameByUrl("") + ".exe");
		mDownloadInfo3 = new DownloadInfo(
				"http://asmack.freakempire.de/0.8.9beta4/asmack-android-8-0.8.9beta4.jar",
				filePathString, "asmack-android-8-0.8.9beta4"
						+ TUrlParserUtils.getFileNameByUrl("") + ".jar");
	}
}
