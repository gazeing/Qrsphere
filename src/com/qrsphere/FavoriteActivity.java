package com.qrsphere;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.google.zxing.BarcodeFormat;
import com.qrsphere.database.Qrcode;
import com.qrsphere.database.QrcodeJSONData;
import com.qrsphere.database.QrcodeList;
import com.qrsphere.login.LoginActivity;
import com.qrsphere.network.DeleteFavorite;
import com.qrsphere.network.GetHistoryList;
import com.qrsphere.network.QPageProcess;
import com.qrsphere.network.SuccessCode;
import com.qrsphere.scan.Contents;
import com.qrsphere.scan.Intents;
import com.qrsphere.widget.MyLog;
import com.qrsphere.widget.ScanDetail;
import com.qrsphere.widget.SeparatedListAdapter;
import com.qrsphere.widget.StartBrowser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("HandlerLeak")
public class FavoriteActivity extends Activity {
	private List<String> data = new ArrayList<String>();
	ListView lView;

	// protected String getDataElement(int index){
	// return data.get(index);
	// }
	String qrDataGlobal = "";

	Qrcode qrcodeGlobal = null;
	ArrayList<SeparatedList> lists = new ArrayList<SeparatedList>();

	// QrcodeDataOperator qdo;
	//
	// private QrcodeDataOperator getQdo() {
	// return qdo;
	// }

	SeparatedListAdapter adapter;

	SeparatedList origin_sl;
	QPageProcess qpGlobal = null;
	ProgressDialog pd;
	GetHistoryList ghhGlobal = null;
	DeleteFavorite dfGlobal = null;
	QrcodeList qrcodeListGlobal = null;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {

			if (pd != null && pd.isShowing()) {
				pd.dismiss();

				MyLog.i("Dialog",
						"pd.dismiss(); pd.getProgress() = " + pd.getProgress());

			}
			if (msg.what == SuccessCode.DETAIL_SENT_SUCCESS) {

			} else if (msg.what == SuccessCode.GET_LIST_SUCCESS) {
				qrcodeListGlobal.update();
				classfyListByCatalogue();
				lView.invalidateViews();

			} else if (msg.what == SuccessCode.ERROR) {
				Toast.makeText(FavoriteActivity.this, "Network error!",
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == SuccessCode.DELETE_FAVORITE_SUCCESS) {
				pd = qrcodeListGlobal.sendListRequest();
			} else if (msg.what == SuccessCode.QPAGE_SUCCESS) {
				qpGlobal.startQPage(FavoriteActivity.this);

			} else {
				LoginActivity.showNetworkAlert(FavoriteActivity.this);
			}
		}
	};

	protected SeparatedListAdapter addSections(SeparatedListAdapter adapter) {
		adapter.clear();

		for (SeparatedList sl : lists) {
			SimpleAdapter simpAdp1 = new SimpleAdapter(this, sl.getList(),
					R.layout.listitem, new String[] { "ItemTitle", "Time" },
					new int[] { R.id.ItemTitle, R.id.ItemText });
			adapter.addSection(sl.getCatalogue(), simpAdp1);
		}
		return adapter;
	}

	protected void classfyListByCatalogue() {

		clearAllListAndRestore();
		List<Qrcode> qrs = getQrcodeList();

		for (Qrcode q : qrs) {
			QrcodeJSONData qJson = q.getQrcodeJSONData();

			String catalogue = qJson.getCatalogue();
			if (catalogue.length() == 0)
				origin_sl.addItem(q);// if it has none category, it will be put
										// under "my best code".

			boolean isfound = false; // it is the flag show if the catalog
										// exists now.
			for (SeparatedList sl : lists) {
				if (catalogue.compareTo(sl.getCatalogue()) == 0) {// if exist
																	// add the
																	// item to
																	// the list.
					sl.addItem(q);
					isfound = true;
				}

			}
			if (isfound == false) {// is not exist create a new list and add
									// item to the list.
				SeparatedList newsl = new SeparatedList(catalogue);
				newsl.addItem(q);
				lists.add(newsl);
			}

		}
		addSections(adapter);
		lView.setAdapter(adapter);
	}

	protected void clearAllListAndRestore() {
		for (SeparatedList sl : lists) {
			sl.clear();
		}
		lists.clear();
		origin_sl = new SeparatedList("My Best Codes");
		lists.add(origin_sl);
	}

	public void deleteSelectedRecord() {
		Qrcode qc = qrcodeGlobal;
		if (qc != null) {
			pd = dfGlobal.postData(qc);
		}
	}

	protected void feedback() {

		Qrcode qc = qrcodeGlobal;
		if (qc != null) {
			Intent intent = new Intent(new Intent(FavoriteActivity.this,
					FeedbackActivity.class));
			Bundle b = new Bundle();
			b.putString("rawdata", qc.getRawdata());
			intent.putExtras(b);
			startActivity(intent);
		}
	}

	protected List<String> getData() {
		return data;
	}


	protected List<Qrcode> getQrcodeList() {

		List<Qrcode> list = null;
		if (qrcodeListGlobal != null) {
			list = qrcodeListGlobal.getFavoriteList();
		}
		return list;
	}

	public void goToTheWeb() {

		Qrcode qc = qrcodeGlobal;
		if (qc != null) {
			StartBrowser sb = new StartBrowser(qc.getQrcodeJSONData().getUrl(),
					this);
			sb.startBrowse();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// qdo = new QrcodeDataOperator(this);

		qpGlobal = new QPageProcess(pd, handler);
		ghhGlobal = new GetHistoryList(FavoriteActivity.this, handler);
		dfGlobal = new DeleteFavorite(FavoriteActivity.this, handler);
		String listString = getIntent().getExtras().getString("ListString");
		if (listString != null)
			qrcodeListGlobal = new QrcodeList(this, ghhGlobal, pd, listString);

		setContentView(R.layout.favorite);
		lView = (ListView) findViewById(R.id.favorite_list);

		TextView tv = (TextView) findViewById(R.id.tvtitle);
		// Drawable back = getResources().
		// getDrawable(R.drawable.banner_favorite);
		tv.setBackgroundResource(R.drawable.banner_favorite);

		Button btn_back = (Button) findViewById(R.id.btn_title_left);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FavoriteActivity.this.finish();
			}
		});

		Button btn_history = (Button) findViewById(R.id.btn_title_right);
		// back = getResources(). getDrawable(R.drawable.icon_history);
		btn_history.setBackgroundResource(R.drawable.icon_history);
		btn_history.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				FavoriteActivity.this.finish();
				Intent intent = new Intent(FavoriteActivity.this,
						HistoryActivity.class);
				intent.putExtra("ListString",
						qrcodeListGlobal.getJSONArrayString());
				startActivity(intent);
			}
		});

		// /clearAllListAndRestore();

		// for test
		// int i = 3;
		// while(i-->0){
		// JSONObject json = new JSONObject();
		// try {
		// json.put("Catalogue", "test");
		// json.put("URL", "test");
		// json.put("IsFavorite", true);
		// } catch (JSONException e) {
		//
		// e.printStackTrace();
		// }
		// Qrcode q1 = new Qrcode(json.toString());
		//
		// ;
		// sl.addItem(q1.getQrcodeJSONData().getUrl(),
		// TransferTimeFormat(q1.getTimeStamp()));
		// }
		// test end

		adapter = new SeparatedListAdapter(this);

		addSections(adapter);

		lView.setAdapter(adapter);
		lView.setBackgroundResource(R.drawable.background);
		lView.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> parentView, View childView,
					int position, long id) {

				qrDataGlobal = (String) ((HashMap<String, Object>) adapter
						.getItem(position)).get("ItemTitle");
				qrcodeGlobal = (Qrcode) ((HashMap<String, Object>) adapter
						.getItem(position)).get("Qrcode");
				showPopup(childView);
			}
		});

		classfyListByCatalogue();
	}


	protected void onResume() {

		super.onResume();

		// classfyListByCatalogue();
		lView.invalidateViews();
	}

	protected void setPopupMenuAction(Qrcode q) {
		qrcodeGlobal = q;
		android.content.DialogInterface.OnClickListener onselect = new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				switch (which) {
				case 0:

					showQPage();
					break;
				case 1:

					share();
					break;
				case 2:

					showScanDetails();
					break;
				case 3:

					feedback();
					break;
				case 4:

					deleteSelectedRecord();
					break;
				default:
					break;

				}

			}

		};
		if (q != null) {

			showPopup(q.getQrcodeJSONData().getUrl(), onselect);

		}
	}

	protected void share() {
		Qrcode qc = qrcodeGlobal;
		if (qc != null) {
			Intent intent = new Intent(new Intent(FavoriteActivity.this,
					ShareActivity.class));
			Bundle b = new Bundle();
			b.putString("rawdata", qc.getRawdata());
			intent.putExtras(b);
			intent.setAction(Intents.Encode.ACTION);
			intent.putExtra(Intents.Encode.DATA, qc.getQrcodeJSONData()
					.getUrl());
			intent.putExtra(Intents.Encode.TYPE, Contents.Type.TEXT);
			intent.putExtra(Intents.Encode.FORMAT, BarcodeFormat.QR_CODE);
			startActivity(intent);
		}

	}

	protected void showPopup(String str,
			android.content.DialogInterface.OnClickListener oc) {

		String[] choices = { this.getString(R.string.qpage),
				this.getString(R.string.send_out),
				this.getString(R.string.scan_detail),
				this.getString(R.string.feedback),
				this.getString(R.string.delete_item) };

		AlertDialog dialog = new AlertDialog.Builder(FavoriteActivity.this)
				.setIcon(R.drawable.qrcode_icon)
				.setTitle(str)
				.setItems(choices,
						(android.content.DialogInterface.OnClickListener) oc)
				.create();
		dialog.show();

	}

	protected void showPopup(View childView) {
		// if (CheckVersion.CheckVersion11()){
		//
		// PopupMenu popup = new PopupMenu(this, childView);
		// MenuInflater inflater = popup.getMenuInflater();
		// inflater.inflate(R.menu.favor_chose, popup.getMenu());
		// popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
		//
		// @Override
		// //the logic to deal with the menu selection
		// public boolean onMenuItemClick(MenuItem item) {
		// Toast.makeText(getBaseContext(), "You selected the action : " +
		// item.getTitle(), Toast.LENGTH_SHORT).show();
		// switch(item.getItemId()){
		// case R.id.go_to_web:
		// showQPage();
		// break;
		//
		// case R.id.feedback:
		// feedback();
		// ;
		// break;
		// case R.id.scan_detail:
		//
		// showScanDetails();
		// break;
		// case R.id.send_out:
		// share();
		// ;
		// break;
		// case R.id.delete_item:
		// deleteSelectedRecord();
		// break;
		// default:
		// break;
		// }
		// return true;
		// }
		//
		//
		// });
		// popup.show();
		// }
		// else{
		if (qrcodeGlobal != null)
			setPopupMenuAction(qrcodeGlobal);
		// }

	}

	private void showQPage() {
		// TODO Auto-generated method stub
		Qrcode qc = qrcodeGlobal;
		if (qc != null) {
		//	this.pd = qpGlobal.sentToServer(FavoriteActivity.this, qc);
		}
	}

	@SuppressLint("SimpleDateFormat")
	public void showScanDetails() {

		Qrcode qc = qrcodeGlobal;
		if (qc != null) {
			Intent intent = new Intent(FavoriteActivity.this,
					ScanDetailActivity.class);
			Bundle b = new Bundle();
			b.putString("Qrcode", qc.getRawdata());
			intent.putExtras(b);
			startActivity(intent);
		}
	}
}

class SeparatedList {
	ArrayList<HashMap<String, Object>> list;
	String catalogue = "";

	public SeparatedList(String catalogue) {
		super();
		list = new ArrayList<HashMap<String, Object>>();
		this.catalogue = catalogue;
	}

	public void addItem(Qrcode q) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("Qrcode", q);
		map.put("ItemTitle", q.getQrcodeJSONData().getUrl());
		map.put("Time", ScanDetail.TransferTimeFormat(ScanDetail
				.getLongFromServerTimeFormat(q.getQrcodeJSONData()
						.getDateTime())));

		list.add(map);
	}

	public void clear() {
		list.clear();
	}

	public String getCatalogue() {
		return catalogue;
	}

	public ArrayList<HashMap<String, Object>> getList() {
		return list;
	}

}
