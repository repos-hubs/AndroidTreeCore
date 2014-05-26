package com.testtreecore.ui;

import java.util.Date;
import java.util.List;

import com.testtreecore.R;
import com.testtreecore.db.data.TestDataEntity;
import com.treecore.activity.TActivity;
import com.treecore.activity.annotation.TInjectView;
import com.treecore.db.TSQLiteDatabase;
import com.treecore.db.TSQLiteDatabasePool;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TestDB extends TActivity {
	@TInjectView(id = R.id.insert_data)
	Button insertDataButton;
	@TInjectView(id = R.id.select_data)
	Button selectDataButton;
	@TInjectView(id = R.id.update_data)
	Button updateDataButton;
	@TInjectView(id = R.id.delete_data)
	Button deleteDataButton;
	@TInjectView(id = R.id.show_data)
	TextView showDataTextView;

	/** 数据库链接池 */
	protected TSQLiteDatabasePool mSQLiteDatabasePool;

	private TSQLiteDatabase sqLiteDatabase;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_db);
		setTitle("测试数据库");
		createDabaBase();

		initControl();
	}

	/**
	 * 这个方法只是用于测试
	 */
	private void createDabaBase() {
		getSQLiteDatabasePool();
		// TODO Auto-generated method stub
		sqLiteDatabase = mSQLiteDatabasePool.getSQLiteDatabase();
		// 创建表的操作
		// tadbAdapter.creatTable(TestDataEntity.class);
		// tadbAdapter.hasTable(TestDataEntity.class);
		// tadbAdapter.dropTable(TestDataEntity.class);
		if (sqLiteDatabase != null) {
			if (sqLiteDatabase.hasTable(TestDataEntity.class)) {
				sqLiteDatabase.dropTable(TestDataEntity.class);
			}
			sqLiteDatabase.creatTable(TestDataEntity.class);

		}
	}

	/**
	 * 释放数据链接
	 */
	private void releaseSQLiteDatabase() {
		mSQLiteDatabasePool.releaseSQLiteDatabase(sqLiteDatabase);
	}

	private void insertData() {
		// TODO Auto-generated method stub
		TestDataEntity testDataEntity = new TestDataEntity();
		testDataEntity.setName("Think Android ADD");
		testDataEntity.setB(true);
		Character c1 = new Character('c');
		testDataEntity.setC(c1);
		testDataEntity.setD(10);
		testDataEntity.setDate(new Date());
		testDataEntity.setF(2f);
		testDataEntity.setI(123);
		sqLiteDatabase.insert(testDataEntity);
		selectData();
	}

	private void selectData() {
		// TODO Auto-generated method stub
		String showString = "";
		List<TestDataEntity> list = sqLiteDatabase.query(TestDataEntity.class,
				false, null, null, null, null, null);
		for (int i = 0; i < list.size(); i++) {
			TestDataEntity testDataEntity = list.get(i);
			showString = showString + testDataEntity.toString();
		}
		show(showString);
	}

	private void updateData() {
		// TODO Auto-generated method stub
		TestDataEntity testDataEntity = new TestDataEntity();
		testDataEntity.setName("Think Android you");
		testDataEntity.setB(true);
		Character c1 = new Character('c');
		testDataEntity.setC(c1);
		testDataEntity.setD(10);
		testDataEntity.setDate(new Date());
		testDataEntity.setF(2f);
		testDataEntity.setI(123);
		sqLiteDatabase.update(testDataEntity, "username="
				+ "'Think Android ADD'");
		selectData();
	}

	private void deleteData() {
		sqLiteDatabase.delete(TestDataEntity.class, "i=" + "123");
		selectData();
	}

	private void show(String showString) {
		showDataTextView.setText(showString);
	}

	protected void initControl() {
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.insert_data:
					insertData();
					break;
				case R.id.select_data:
					selectData();
					break;
				case R.id.update_data:
					updateData();
					break;
				case R.id.delete_data:
					deleteData();
					break;
				default:
					break;
				}
			}
		};
		insertDataButton.setOnClickListener(onClickListener);
		selectDataButton.setOnClickListener(onClickListener);
		updateDataButton.setOnClickListener(onClickListener);
		deleteDataButton.setOnClickListener(onClickListener);

	}

	// 获取数据库
	public void getSQLiteDatabasePool() {
		if (mSQLiteDatabasePool == null) {
			mSQLiteDatabasePool = TSQLiteDatabasePool.getInstance(this,
					"test.db", 1, true);
			mSQLiteDatabasePool.createPool();
		}
	}
}