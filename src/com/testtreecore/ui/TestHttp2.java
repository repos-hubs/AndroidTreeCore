package com.testtreecore.ui;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.testtreecore.R;
import com.treecore.activity.TActivity;
import com.treecore.activity.annotation.TInjectView;
import com.treecore.http.TAsyncHttpClient;
import com.treecore.http.TSyncHttpClient;
import com.treecore.http2.HttpClientMethod;
import com.treecore.http2.TBsonHttpClient;
import com.treecore.utils.log.TLog;

public class TestHttp2 extends TActivity implements OnClickListener {
	private String TAG = TestHttp2.class.getSimpleName();

	@TInjectView(id = R.id.EditText_headers)
	private EditText mHeadersEditText;
	@TInjectView(id = R.id.EditText_body)
	private EditText mBodyEditText;
	@TInjectView(id = R.id.EditText_status_code)
	private EditText mStatusCodeEditText;
	@TInjectView(id = R.id.EditText_url)
	private EditText mUrlEditText;

	@TInjectView(id = R.id.button_get)
	private Button mGetButton;
	@TInjectView(id = R.id.button_post)
	private Button mPostButton;
	@TInjectView(id = R.id.button_put)
	private Button mPutButton;
	@TInjectView(id = R.id.button_delete)
	private Button mDeleteButton;
	@TInjectView(id = R.id.button_file)
	private Button mFileButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_http2);
		initControl();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View view) {
		String url = "";
		clearData();
		if (view == mGetButton) {
			url = "https://httpbin.org/get";

			TBsonHttpClient client = new TBsonHttpClient(url);
			try {
				client.execute(HttpClientMethod.GET);
				debugResponse(client.getResponse().toString());
			} catch (Exception e) {
			}

			if (client != null) {
				debugStatusCode(client.getResponseCode());
			}
		} else if (view == mPostButton) {
			url = "http://httpbin.org/post";

			TBsonHttpClient client = new TBsonHttpClient(url);
			try {
				client.execute(HttpClientMethod.POST);
				debugResponse(client.getResponse().toString());
			} catch (Exception e) {
			}

			if (client != null) {
				debugStatusCode(client.getResponseCode());
			}
		} else if (view == mPutButton) {
			url = "http://httpbin.org/put";

			TBsonHttpClient client = new TBsonHttpClient(url);
			try {
				client.execute(HttpClientMethod.PUT);
				debugResponse(client.getResponse().toString());
			} catch (Exception e) {
			}

			if (client != null) {
				debugStatusCode(client.getResponseCode());
			}
		} else if (view == mDeleteButton) {
			url = "http://httpbin.org/delete";

			TBsonHttpClient client = new TBsonHttpClient(url);
			try {
				client.execute(HttpClientMethod.DELETE);
				debugResponse(client.getResponse().toString());
			} catch (Exception e) {
			}

			if (client != null) {
				debugStatusCode(client.getResponseCode());
			}
		} else if (view == mFileButton) {
			url = "https://httpbin.org/robots.txt";

			TBsonHttpClient client = new TBsonHttpClient(url);
			try {
				client.execute(HttpClientMethod.GET);
				debugResponse(client.getResponse().toString());
			} catch (Exception e) {
			}

			if (client != null) {
				debugStatusCode(client.getResponseCode());
			}
		}

		mUrlEditText.setText(url);
	}

	private void clearData() {
		mHeadersEditText.setText("");
		mBodyEditText.setText("");
		mStatusCodeEditText.setText("");
		mUrlEditText.setText("");
	}

	public Header[] getRequestHeaders() {
		List<Header> headers = new ArrayList<Header>();
		String headersRaw = mHeadersEditText.getText() == null ? null
				: mHeadersEditText.getText().toString();

		if (headersRaw != null && headersRaw.length() > 3) {
			String[] lines = headersRaw.split("\\r?\\n");
			for (String line : lines) {
				try {
					String[] kv = line.split("=");
					if (kv.length != 2)
						throw new IllegalArgumentException(
								"Wrong header format, may be 'Key=Value' only");
					headers.add(new BasicHeader(kv[0].trim(), kv[1].trim()));
				} catch (Throwable t) {
					TLog.e(this,
							"Not a valid header line: " + line + t.getMessage());
				}
			}
		}
		return headers.toArray(new Header[headers.size()]);
	}

	public HttpEntity getRequestEntity() {
		if (mBodyEditText.getText() != null) {
			try {
				return new StringEntity(mBodyEditText.getText().toString());
			} catch (UnsupportedEncodingException e) {
				TLog.e(this, "cannot create String entity" + e.getMessage());
			}
		}
		return null;
	}

	protected final void debugHeaders(Header[] headers) {
		if (headers != null) {
			TLog.d(TAG, "Return Headers:");
			StringBuilder builder = new StringBuilder();
			for (Header h : headers) {
				String _h = String.format(Locale.US, "%s : %s", h.getName(),
						h.getValue());
				TLog.d(TAG, _h);
				builder.append(_h);
				builder.append("\n");
			}

			mHeadersEditText.setText(builder.toString());
		}
	}

	protected final void debugThrowable(Throwable e) {
		TLog.e(TAG, e.getMessage());
	}

	protected final void debugResponse(String response) {
		if (response != null) {
			TLog.d(TAG, "Response data:");
			TLog.d(TAG, response);
			mBodyEditText.setText(response);
		}
	}

	protected final void debugStatusCode(int statusCode) {
		String msg = String.format(Locale.US, "Return Status Code: %d",
				statusCode);
		TLog.d(TAG, msg);
		mStatusCodeEditText.setText(msg);
	}

	private void initControl() {
		mGetButton.setOnClickListener(this);
		mPostButton.setOnClickListener(this);
		mPutButton.setOnClickListener(this);
		mDeleteButton.setOnClickListener(this);
		mFileButton.setOnClickListener(this);
	}
}
