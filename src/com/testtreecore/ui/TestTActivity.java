package com.testtreecore.ui;

import com.testtreecore.R;
import com.treecore.UIBroadcast;
import com.treecore.activity.TActivity;
import com.treecore.activity.annotation.TInjectView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.TextView;

public class TestTActivity extends TActivity implements OnClickListener {
	private String TAG = TestTActivity.class.getCanonicalName();
	@TInjectView(id = R.id.Button_test)
	private Button testComparatorButton;
	@TInjectView(id = R.id.TextView_test)
	private TextView showViewTextView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		final View view = View.inflate(this, R.layout.splash, null);
		setContentView(view);
		// 渐变展示启动�?
		AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
		aa.setDuration(5000);
		view.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				finish();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}
		});

		testComparatorButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		if (testComparatorButton == arg0) {
			Log.i("", "");
			UIBroadcast.sentEvent(mContext, 1001, 1002, "");
		}
	}

	@Override
	public void processEvent(Intent intent) {
		super.processEvent(intent);
		int mainEvent = intent.getIntExtra(UIBroadcast.MAINEVENT, -1); // 主事�?
		int subEvent = intent.getIntExtra(UIBroadcast.EVENT, -1);// 次事�?

		makeText("哈哈触发�?" + mainEvent + subEvent);// 每个Activity可接收广�?
	}
}
