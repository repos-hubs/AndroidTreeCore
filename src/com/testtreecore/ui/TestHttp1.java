package com.testtreecore.ui;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.testtreecore.R;
import com.testtreecore.utils.SampleJSON;
import com.treecore.activity.TActivity;
import com.treecore.activity.annotation.TInjectView;
import com.treecore.http.TAsyncHttpClient;
import com.treecore.http.TSyncHttpClient;
import com.treecore.http.core.AsyncHttpResponseHandler;
import com.treecore.http.core.BaseJsonHttpResponseHandler;
import com.treecore.http.core.BinaryHttpResponseHandler;
import com.treecore.http.core.FileAsyncHttpResponseHandler;
import com.treecore.http.core.RequestParams;
import com.treecore.http.core.ResponseHandlerInterface;
import com.treecore.utils.TFileUtils;
import com.treecore.utils.log.TLog;

public class TestHttp1 extends TActivity implements OnClickListener {
	private String TAG = TestHttp1.class.getSimpleName();

	@TInjectView(id = R.id.EditText_headers)
	private EditText mHeadersEditText;
	@TInjectView(id = R.id.EditText_body)
	private EditText mBodyEditText;
	@TInjectView(id = R.id.EditText_status_code)
	private EditText mStatusCodeEditText;
	@TInjectView(id = R.id.EditText_url)
	private EditText mUrlEditText;

	@TInjectView(id = R.id.button_cancel)
	private Button mCancelButton;
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
	@TInjectView(id = R.id.button_gzip)
	private Button mGZipButton;
	@TInjectView(id = R.id.button_redirect302)
	private Button mRedirect302Button;
	@TInjectView(id = R.id.button_json)
	private Button mJsonButton;
	@TInjectView(id = R.id.button_login)
	private Button mLoginButton;
	@TInjectView(id = R.id.button_binary)
	private Button mBinaryButton;
	@TInjectView(id = R.id.button_get2)
	private Button mSynGetButton;
	@TInjectView(id = R.id.button_timeout)
	private Button mTimeOutButton;

	private TAsyncHttpClient mTAsyncHttpClient = new TAsyncHttpClient();// 异步
	private TSyncHttpClient mTSyncHttpClient = new TSyncHttpClient();// 同步

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_http1);
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
		if (view == mCancelButton) {
			mTAsyncHttpClient.cancelRequests(this, true);
			mTAsyncHttpClient.cancelAllRequests(true);
		} else if (view == mGetButton) {
			url = "https://httpbin.org/get";
			mTAsyncHttpClient.get(url, new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					debugHeaders(headers);
					debugStatusCode(statusCode);
					debugResponse(new String(responseBody));
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					debugHeaders(headers);
					debugStatusCode(statusCode);
					if (responseBody != null) {
						debugResponse(new String(responseBody));
					}

					TLog.e(this, error.getMessage());
				}

				@Override
				public void onProgress(int bytesWritten, int totalSize) {
					super.onProgress(bytesWritten, totalSize);
				}

				@Override
				public void onRetry(int retryNo) {
					makeText(String.format("Request is retried, retry no. %d",
							retryNo));
				}
			});
		} else if (view == mPostButton) {
			url = "http://httpbin.org/post";
			mTAsyncHttpClient.post(mContext, url, getRequestHeaders(),
					getRequestEntity(), null, new AsyncHttpResponseHandler() {

						@Override
						public void onStart() {
						}

						@Override
						public void onSuccess(int statusCode, Header[] headers,
								byte[] response) {
							debugHeaders(headers);
							debugStatusCode(statusCode);
							debugResponse(new String(response));
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								byte[] errorResponse, Throwable e) {
							debugHeaders(headers);
							debugStatusCode(statusCode);
							debugThrowable(e);
							if (errorResponse != null) {
								debugResponse(new String(errorResponse));
							}
						}
					});

		} else if (view == mPutButton) {
			url = "http://httpbin.org/put";

			mTAsyncHttpClient.put(this, url, getRequestHeaders(),
					getRequestEntity(), null, new AsyncHttpResponseHandler() {

						@Override
						public void onStart() {
						}

						@Override
						public void onSuccess(int statusCode, Header[] headers,
								byte[] response) {
							debugHeaders(headers);
							debugStatusCode(statusCode);
							debugResponse(new String(response));
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								byte[] errorResponse, Throwable e) {
							debugHeaders(headers);
							debugStatusCode(statusCode);
							debugThrowable(e);
							if (errorResponse != null) {
								debugResponse(new String(errorResponse));
							}
						}
					});
		} else if (view == mDeleteButton) {
			url = "http://httpbin.org/delete";

			mTAsyncHttpClient.delete(this, url, getRequestHeaders(), null,
					new AsyncHttpResponseHandler() {

						@Override
						public void onStart() {
						}

						@Override
						public void onSuccess(int statusCode, Header[] headers,
								byte[] response) {
							debugHeaders(headers);
							debugStatusCode(statusCode);
							debugResponse(new String(response));
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								byte[] errorResponse, Throwable e) {
							debugHeaders(headers);
							debugStatusCode(statusCode);
							debugThrowable(e);
							if (errorResponse != null) {
								debugResponse(new String(errorResponse));
							}
						}
					});
		} else if (view == mFileButton) {
			url = "https://httpbin.org/robots.txt";

			mTAsyncHttpClient.get(this, url, getRequestHeaders(), null,
					new FileAsyncHttpResponseHandler(this) {
						@Override
						public void onStart() {
						}

						@Override
						public void onSuccess(int statusCode, Header[] headers,
								File response) {
							debugHeaders(headers);
							debugStatusCode(statusCode);
							debugFile(response);
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, File file) {
							debugHeaders(headers);
							debugStatusCode(statusCode);
							debugThrowable(throwable);
							debugFile(file);
						}

						private void debugFile(File file) {
							if (file == null || !file.exists()) {
								debugResponse("Response is null");
								return;
							}
							try {
								debugResponse(file.getAbsolutePath()
										+ "\r\n\r\n"
										+ TFileUtils.getStringFromFile(file));
							} catch (Throwable t) {
								TLog.e(TAG,
										"Cannot debug file contents"
												+ t.getMessage());
							}
							if (!deleteTargetFile()) {
								TLog.d(TAG, "Could not delete response file "
										+ file.getAbsolutePath());
							}
						}
					});
		} else if (view == mGZipButton) {
			url = "http://httpbin.org/gzip";
			mTAsyncHttpClient.get(this, url, null, null,
					new BaseJsonHttpResponseHandler<SampleJSON>() {

						@Override
						public void onStart() {
						}

						@Override
						public void onSuccess(int statusCode, Header[] headers,
								String rawJsonResponse, SampleJSON response) {
							debugHeaders(headers);
							debugStatusCode(statusCode);
							if (response != null) {
								debugResponse(rawJsonResponse);
							}
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, String rawJsonData,
								SampleJSON errorResponse) {
							debugHeaders(headers);
							debugStatusCode(statusCode);
							debugThrowable(throwable);
							if (errorResponse != null) {
								debugResponse(rawJsonData);
							}
						}

						@Override
						protected SampleJSON parseResponse(String rawJsonData,
								boolean isFailure) throws Throwable {
							return null;
							// return new ObjectMapper().readValues(
							// new JsonFactory().createParser(rawJsonData),
							// SampleJSON.class).next();
						}

					});

		} else if (view == mRedirect302Button) {
			url = "http://httpbin.org/redirect/6";

			HttpClient client = mTAsyncHttpClient.getHttpClient();
			if (client instanceof DefaultHttpClient) {
				// enableRedirects/enableRelativeRedirects/enableCircularRedirects
				mTAsyncHttpClient.setEnableRedirects(true, true, true);
			}

			mTAsyncHttpClient.get(url, new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					debugHeaders(headers);
					debugStatusCode(statusCode);
					debugResponse(new String(responseBody));
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					debugHeaders(headers);
					debugStatusCode(statusCode);
					if (responseBody != null) {
						debugResponse(new String(responseBody));
					}

					TLog.e(this, error.getMessage());
				}

				@Override
				public void onProgress(int bytesWritten, int totalSize) {
					super.onProgress(bytesWritten, totalSize);
				}

				@Override
				public void onRetry(int retryNo) {
					makeText(String.format("Request is retried, retry no. %d",
							retryNo));
				}
			});
		} else if (view == mJsonButton) {
			url = "http://httpbin.org/headers";

			mTAsyncHttpClient.get(this, url, null, null,
					new BaseJsonHttpResponseHandler<SampleJSON>() {

						@Override
						public void onStart() {
						}

						@Override
						public void onSuccess(int statusCode, Header[] headers,
								String rawJsonResponse, SampleJSON response) {
							debugHeaders(headers);
							debugStatusCode(statusCode);
							if (response != null) {
								debugResponse(rawJsonResponse);
							}
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, String rawJsonData,
								SampleJSON errorResponse) {
							debugHeaders(headers);
							debugStatusCode(statusCode);
							debugThrowable(throwable);
							if (errorResponse != null) {
								debugResponse(rawJsonData);
							}
						}

						@Override
						protected SampleJSON parseResponse(String rawJsonData,
								boolean isFailure) throws Throwable {
							return null;
							// return new ObjectMapper().readValues(
							// new JsonFactory().createParser(rawJsonData),
							// SampleJSON.class).next();
						}

					});
		} else if (view == mLoginButton) {
			url = "http://myendpoint.com";
			RequestParams params = new RequestParams();
			params.put("username", "banketree");
			params.put("password", "111111");
			params.put("email", "banketree@qq.com");
			// params.put("profile_picture", new File("pic.jpg")); // Upload a
			// File
			// params.put("profile_picture2", someInputStream); // Upload
			// anInputStream
			// map = new HashMap&lt;String, String&gt;();
			// map.put("first_name", "James");
			// map.put("last_name", "Smith");
			// params.put("user", map);
			// * Set&lt;String&gt; set = new HashSet&lt;String&gt;(); //
			// unordered collection
			// * set.add("music");
			// * set.add("art");
			// * params.put("like", set); // url params:
			// "like=music&amp;like=art"
			//
			// * List&lt;String&gt; list = new ArrayList&lt;String&gt;(); //
			// Ordered collection
			// * list.add("Java");<>
			// * list.add("C");
			// * params.put("languages", list); // url params:
			// "languages[]=Java&amp;languages[]=C"
			//
			// * String[] colors = { "blue", "yellow" }; // Ordered collection
			// * params.put("colors", colors); // url params:
			// "colors[]=blue&amp;colors[]=yellow"
			// *
			// * List&lt;Map&lt;String, String&gt;&gt; listOfMaps = new
			// ArrayList&lt;Map&lt;String,
			// * String&gt;&gt;();
			// * Map&lt;String, String&gt; user1 = new HashMap&lt;String,
			// String&gt;();
			// * user1.put("age", "30");
			// * user1.put("gender", "male");
			// * Map&lt;String, String&gt; user2 = new HashMap&lt;String,
			// String&gt;();
			// * user2.put("age", "25");
			// * user2.put("gender", "female");
			// * listOfMaps.add(user1);
			// * listOfMaps.add(user2);
			// * params.put("users", listOfMaps); // url params:
			// "users[][age]=30&amp;users[][gender]=male&amp;users[][age]=25&amp;users[][gender]=female"
			// *

			mTAsyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {

				@Override
				public void onStart() {
				}

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] response) {
					debugHeaders(headers);
					debugStatusCode(statusCode);
					debugResponse(new String(response));
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] errorResponse, Throwable e) {
					debugHeaders(headers);
					debugStatusCode(statusCode);
					debugThrowable(e);
					if (errorResponse != null) {
						debugResponse(new String(errorResponse));
					}
				}
			});
		} else if (view == mBinaryButton) {
			url = "http://httpbin.org/gzip";

			mTAsyncHttpClient.get(this, url, getRequestHeaders(), null,
					new BinaryHttpResponseHandler() {
						@Override
						public void onStart() {
						}

						@Override
						public String[] getAllowedContentTypes() {
							// Allowing all data for debug purposes
							return new String[] { ".*" };
						}

						public void onSuccess(int statusCode, Header[] headers,
								byte[] binaryData) {
							debugStatusCode(statusCode);
							debugHeaders(headers);
							debugResponse("Received response is "
									+ binaryData.length + " bytes");
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								byte[] errorResponse, Throwable e) {
							debugHeaders(headers);
							debugStatusCode(statusCode);
							debugThrowable(e);
							if (errorResponse != null) {
								debugResponse("Received response is "
										+ errorResponse.length + " bytes");
							}
						}
					});
		} else if (view == mSynGetButton) {
			url = "https://httpbin.org/delay/6";

			mTSyncHttpClient.get(this, url, getRequestHeaders(), null,
					new AsyncHttpResponseHandler() {

						@Override
						public void onStart() {
						}

						@Override
						public void onSuccess(final int statusCode,
								final Header[] headers, final byte[] response) {
							debugHeaders(headers);
							debugStatusCode(statusCode);
							debugResponse(new String(response));
						}

						@Override
						public void onFailure(final int statusCode,
								final Header[] headers,
								final byte[] errorResponse, final Throwable e) {
							debugHeaders(headers);
							debugStatusCode(statusCode);
							debugThrowable(e);
							if (errorResponse != null) {
								debugResponse(new String(errorResponse));
							}
						}
					});
		} else if (view == mTimeOutButton) {
			url = "http://httpbin.org/delay/6";

			mTAsyncHttpClient.get(this, url, getRequestHeaders(), null,
					new AsyncHttpResponseHandler() {
						private int counter = 0;
						private int id = counter++;
						private SparseArray<String> states = new SparseArray<String>();

						@Override
						public void onStart() {
							setStatus(id, "START");
						}

						@Override
						public void onSuccess(int statusCode, Header[] headers,
								byte[] responseBody) {
							setStatus(id, "SUCCESS");
						}

						@Override
						public void onFinish() {
							setStatus(id, "FINISH");
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								byte[] responseBody, Throwable error) {
							setStatus(id, "FAILURE");
						}

						@Override
						public void onCancel() {
							setStatus(id, "CANCEL");
						}

						private synchronized void setStatus(int id,
								String status) {
							String current = states.get(id, null);
							states.put(id, current == null ? status : current
									+ "," + status);
							for (int i = 0; i < states.size(); i++) {
								debugResponse(String.format("%d (from %d): %s",
										states.keyAt(i), counter,
										states.get(states.keyAt(i))));
							}
						}
					});
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
		mCancelButton.setOnClickListener(this);
		mGetButton.setOnClickListener(this);
		mPostButton.setOnClickListener(this);
		mPutButton.setOnClickListener(this);
		mDeleteButton.setOnClickListener(this);
		mFileButton.setOnClickListener(this);
		mGZipButton.setOnClickListener(this);
		mRedirect302Button.setOnClickListener(this);
		mJsonButton.setOnClickListener(this);
		mBinaryButton.setOnClickListener(this);
		mLoginButton.setOnClickListener(this);
		mSynGetButton.setOnClickListener(this);
		mTimeOutButton.setOnClickListener(this);

	}
}
