package com.testtreecore.ui;

import java.io.File;

import com.testtreecore.R;
import com.treecore.activity.TActivity;
import com.treecore.filepath.TFilePathManager;
import com.treecore.utils.TCompressUtils;
import com.treecore.utils.config.TIConfig;
import com.treecore.utils.config.TPreferenceConfig;
import com.treecore.utils.config.TPropertiesConfig;
import com.treecore.utils.encryption.TAes;
import com.treecore.utils.encryption.TBase64;
import com.treecore.utils.encryption.TDes;
import com.treecore.utils.log.TLog;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

public class TestTree extends TActivity {
	protected String TAG = TestTree.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_tree);

		TFilePathManager.getInstance().initConfig(null); // 默认目录（下载�?图片、视频�?缓存等目�?
															// 优先考虑内存条，其次手机内存�?
	}

	public void onClickTestPath(View view) { // 测试路径
		TFilePathManager.getInstance().initConfig(
				Environment.getExternalStorageDirectory().getPath()
						+ File.pathSeparator + "test");

		// 程序目录里面分了图片、缓存区、下载图、音频区、视频区
		TFilePathManager.getInstance().getAppPath();
		TFilePathManager.getInstance().getAudioPath();
		TFilePathManager.getInstance().getCachePath();
		TFilePathManager.getInstance().getDownloadPath();
		TFilePathManager.getInstance().getImagePath();
	}

	public void onClickTestLog(View view) { // 测试日志
		TLog.enablePrintToFileLogger(true);
		for (int i = 0; i < 100; i++)
			TLog.i(TAG, "123456" + i);
		// TLog.release(); //关闭程序可释�?

		// 日志是放在程序目录下

		// TLog.enableIgnoreAll(enable);
		// TLog.enableIgnoreWarn(enable);
	}

	public void onClickTestTActivity(View view) { // 测试TActivity
		Intent intent = new Intent();
		intent.setClass(this, TestTActivity.class);
		startActivity(intent);
	}

	public void onClickTestHttp1(View view) { // 测试Http1
		Intent intent = new Intent();
		intent.setClass(this, TestHttp1.class);
		startActivity(intent);
	}

	public void onClickTestHttp2(View view) { // 测试Http2
		Intent intent = new Intent();
		intent.setClass(this, TestHttp2.class);
		startActivity(intent);
	}

	public void onClickTestThreadsdown(View view) { // 测试多线程下载
		Intent intent = new Intent();
		intent.setClass(this, TestDowns.class);
		startActivity(intent);
	}

	public void onClickTestCrash(View view) { // 崩溃
		int result = 1 / 0;
	}

	public void onClickTestSqlEncrypt(View view) { // 测试sql加密
		Intent intent = new Intent();
		intent.setClass(this, TestSqlEncrypt.class);
		startActivity(intent);
	}

	public void onClickTestDAO(View view) { // 测试orm数据库
		Intent intent = new Intent();
		intent.setClass(this, TestDAO.class);
		startActivity(intent);
	}

	public void onClickTestDB(View view) { // 测试Think数据库
		Intent intent = new Intent();
		intent.setClass(this, TestDB.class);
		startActivity(intent);
	}

	public void onClickTestCache(View view) { // 测试缓存
		Intent intent = new Intent();
		intent.setClass(this, TestCache.class);
		startActivity(intent);
	}

	public void onClickTConfig(View view) { // 测试配置信息
		TPreferenceConfig.getInstance().initConfig(this);
		TPropertiesConfig.getInstance().initConfig(this);

		TIConfig iConfig = TPreferenceConfig.getInstance();
		iConfig.setBoolean("123", true);
		boolean result = iConfig.getBoolean("123", false);

		iConfig = TPropertiesConfig.getInstance();
		iConfig.setBoolean("1234", true);
		result = iConfig.getBoolean("1234", false);
	}

	public void onClickEncryption(View view) { // 测试加密
		String src = "banketree@qq.com";
		String encrypted = "";
		String key = "banketree";

		try {
			encrypted = TAes.encrypt(key, src);
			String tempString = TAes.decrypt(key, encrypted);

			encrypted = TBase64.encode(src.getBytes());
			tempString = TBase64.decode(encrypted).toString();

			encrypted = TDes.encrypt(key, src);
			tempString = TDes.decrypt(key, encrypted);
			TLog.i(this, tempString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onClickZip(View view) { // 测试解压缩
		try {
			TCompressUtils.compressJar("");
			TCompressUtils.compressZip("");
			TCompressUtils.uncompressJar("");
			TCompressUtils.uncompressZip("");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
