/*
 * Copyright (C) 2011 Markus Junginger, greenrobot (http://greenrobot.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.testtreecore.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.testtreecore.Account;
import com.testtreecore.AccountDao;
import com.testtreecore.DaoMaster;
import com.testtreecore.User;
import com.testtreecore.UserDao;
import com.testtreecore.DaoMaster.DevOpenHelper;
import com.testtreecore.DaoSession;
import com.testtreecore.R;
import com.treecore.activity.TActivity;
import com.treecore.activity.annotation.TInjectView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class TestDAO extends TActivity implements OnClickListener {

	@TInjectView(id = R.id.Button_add)
	private Button mAddButton;
	@TInjectView(id = R.id.Button_del)
	private Button mDelButton;
	@TInjectView(id = R.id.Button_look)
	private Button mLookButton;
	@TInjectView(id = R.id.ListView_cursor)
	private ListView mCursorListView;

	private DaoMaster mDaoMaster;
	private DaoSession mDaoSession;
	private SQLiteDatabase mSQLiteDatabase;
	private AccountDao mAccountDao;
	private UserDao mUserDao;
	private Cursor mCursor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_dao);
		initControl();

		try {
			DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "test-db",
					null);
			mSQLiteDatabase = helper.getWritableDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (mSQLiteDatabase == null) {
			finish();
			return;
		}

		mDaoMaster = new DaoMaster(mSQLiteDatabase);
		mDaoSession = mDaoMaster.newSession();
		mAccountDao = mDaoSession.getAccountDao();
		mUserDao = mDaoSession.getUserDao();
	}

	private void initControl() {
		mAddButton.setOnClickListener(this);
		mDelButton.setOnClickListener(this);
		mLookButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		long startTime = System.currentTimeMillis();

		try {
			if (v == mAddButton) {
				int iCount = 0;
				while (iCount < 100) {
					Account account = new Account();
					account.setNumber("" + System.currentTimeMillis());
					account.setName("" + iCount);
					account.setComment("" + iCount);
					account.setDate(new Date());
					account.setText("" + iCount);
					mAccountDao.insert(account);
					iCount++;
				}

				iCount = 0;
				while (iCount < 100) {
					User user = new User();
					user.setNumber("" + System.currentTimeMillis());
					user.setName("" + iCount);
					user.setComment("" + iCount);
					user.setDate(new Date());
					user.setText("" + iCount);
					mUserDao.insert(user);
					iCount++;
				}
			} else if (v == mDelButton) {
				mAccountDao.deleteAll();
				mUserDao.deleteAll();
			} else if (v == mLookButton) {
				if (mCursor != null)
					mCursor.close();
				String textColumn = AccountDao.Properties.Number.columnName;
				String orderBy = textColumn + " COLLATE LOCALIZED ASC";
				mCursor = mSQLiteDatabase.query(mAccountDao.getTablename(),
						mAccountDao.getAllColumns(), null, null, null, null,
						orderBy);

				if (mCursor != null && mCursor.moveToFirst()) {

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		int second = (int) ((System.currentTimeMillis() - startTime) / 1000);
		makeText("本次用时：" + second);
	}
}