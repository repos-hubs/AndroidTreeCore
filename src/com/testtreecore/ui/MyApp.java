package com.testtreecore.ui;

import android.content.Intent;

import com.testtreecore.crash.sent.CrashReportSender;
import com.treecore.TApplication;
import com.treecore.crash.TCrash;
import com.treecore.utils.log.TLog;

public class MyApp extends TApplication {
	private static final String TAG = MyApp.class.getSimpleName();

	@Override
	public void onCreate() {
		super.onCreate();

		// 奔溃处理
		TCrash.getInstance().removeAllReportSenders();
		TCrash.getInstance().setReportSender(
				new CrashReportSender(getApplicationContext()));
	}

	@Override
	public void onAppCrash(String crashFile) {

		TLog.d(TAG, "Creating Dialog for " + crashFile);
		Intent dialogIntent = new Intent(this, TestCrash.class);
		dialogIntent.putExtra(TCrash.EXTRA_REPORT_FILE_NAME, crashFile);
		dialogIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(dialogIntent);
	}
}
