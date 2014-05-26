package com.testtreecore.ui;

import java.io.File;

import com.testtreecore.R;
import com.treecore.UIBroadcast;
import com.treecore.activity.TActivity;
import com.treecore.activity.annotation.TInjectView;
import com.treecore.cache.TCacheManager;
import com.treecore.cache.disc.TIDiscCacheAware;
import com.treecore.cache.disc.naming.TIFileNameGenerator;
import com.treecore.cache.memory.TIMemoryCacheAware;
import com.treecore.download.DownloadInfo;
import com.treecore.download.TIDownloadTaskListener;
import com.treecore.download.TMDownloadManager;
import com.treecore.filepath.TFilePathManager;
import com.treecore.utils.TUrlParserUtils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TestCache extends TActivity implements OnClickListener {
	private String TAG = TestCache.class.getCanonicalName();
	@TInjectView(id = R.id.Button_test1)
	private Button mTest1Button;
	@TInjectView(id = R.id.Button_test2)
	private Button mTest2Button;
	@TInjectView(id = R.id.Button_test3)
	private Button mTest3Button;

	private int mMemoryCacheSize = 0;
	private int mDiscCacheSize = 0;
	private int mDiscCacheFileCount = 0;
	private TIMemoryCacheAware<String, Bitmap> mMemoryCache = null;
	private TIDiscCacheAware mDiscCache1 = null;
	private TIDiscCacheAware mDiscCache2 = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_cache);
		initControl();

		mMemoryCacheSize = 2 * 1024 * 1024;// 2M
		mDiscCacheSize = 2 * 1024 * 1024;// 2M
		mDiscCacheFileCount = 100;// 一百个文件
		mMemoryCache = TCacheManager.createLruMemoryCache(mMemoryCacheSize);

		try {
			mDiscCache1 = TCacheManager.createReserveDiscCache(TFilePathManager
					.getInstance().getCachePath(), "test");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			mDiscCache2 = TCacheManager.createFileCountLimitedDiscCache(
					mDiscCacheFileCount, TFilePathManager.getInstance()
							.getCachePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View arg0) {
		try {
			Drawable demo = getResources().getDrawable(R.drawable.ic_launcher);
			Bitmap bitmap = ((BitmapDrawable) demo).getBitmap();
			if (arg0 == mTest1Button) {
				mMemoryCache.put("123", bitmap);
				mMemoryCache.put("1234", bitmap);

				mMemoryCache.get("123");
				mMemoryCache.get("1234");
			} else if (arg0 == mTest2Button) {
				mDiscCache1.put("123", mContext.getCacheDir());
				mDiscCache1.get("123");
			} else if (arg0 == mTest3Button) {
				mDiscCache2.put("123", mContext.getCacheDir());
				mDiscCache2.get("123");
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

	private void initControl() {
		mTest1Button.setOnClickListener(this);
		mTest2Button.setOnClickListener(this);
		mTest3Button.setOnClickListener(this);
	}
}
