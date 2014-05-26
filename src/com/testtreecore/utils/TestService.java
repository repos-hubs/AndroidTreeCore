package com.testtreecore.utils;

import java.util.Timer;
import java.util.TimerTask;

import com.sqlcrypt.database.AssetsDatabaseManager;
import com.sqlcrypt.database.Cursor;
import com.sqlcrypt.database.sqlite.SQLiteDatabase;
import com.testtreecore.R;
import com.treecore.utils.TCursorUtils;
import com.treecore.utils.log.TLog;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class TestService extends Service {
	private static final String TAG = TestService.class.getCanonicalName();
	public final static String Intent_Action = "com.test.TestService.SipService";
	public static boolean mStarted = false;

	private Context mContext;
	private Timer mTimer;
	private SQLiteDatabase mDB;

	@Override
	public void onCreate() {
		Log.i(TAG, "onCreate");
		super.onCreate();
		mContext = this;

		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				if (mDB == null) {
					TLog.i(this, "无数据库");
					return;
				}

				long startTime = System.currentTimeMillis();
				int size = 0;
				Cursor cursor = null;

				try {
					cursor = mDB.query("test", null, null, null, null, null,
							null);
					size = cursor.getCount();

					if (cursor.moveToFirst()) {
						do {
							try {
								int id = cursor.getInt(cursor
										.getColumnIndex("id"));
								if (id == -1 || id == 0)
									continue;

								// mDB.execSQL("delete from test where id =" +
								// id);
								TLog.i(this, "查看某id:" + id);
							} catch (Exception e) {
								e.printStackTrace();
							}
						} while (cursor.moveToNext());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (cursor != null)
					cursor.close();

				long second = (System.currentTimeMillis() - startTime) / 1000;
				TLog.i(this, "test表个数:" + size + " 所花时间：" + second);
			}
		}, 5000, 5000);

		AssetsDatabaseManager.initManager(mContext.getApplicationContext());

		AssetsDatabaseManager manager = AssetsDatabaseManager.getManager();
		try {
			if (mDB != null && mDB.isOpen()) {
				mDB.close();
			}
			mDB = manager.getDatabase("sample.sqlite", "");
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, R.string.error_tip2, Toast.LENGTH_SHORT)
					.show();
			return;
		}
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "onDestroy");
		mStarted = false;
		AssetsDatabaseManager.closeAllDatabase();
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log.i(TAG, "onStart");
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "XMPPService onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.i(TAG, "onUnbind");
		return super.onUnbind(intent);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	public static void bindService(Context context,
			ServiceConnection serviceConnection) { //
		Intent intent = new Intent(Intent_Action);
		context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
	}

	public static void startService(Context context) {//
		Intent localIntent = new Intent();
		localIntent.setClass(context, TestService.class);
		context.startService(localIntent);
	}

	public static void stopService(Context context) {//
		Intent localIntent = new Intent();
		localIntent.setClass(context, TestService.class);
		context.stopService(localIntent);
	}

	public static void unbindService(Context context,
			ServiceConnection serviceConnection) { //
		context.unbindService(serviceConnection);
	}
}
