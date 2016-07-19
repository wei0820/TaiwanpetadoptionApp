package com.jackpan.TaiwanpetadoptionApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.util.IabHelper;
import com.util.IabResult;
import com.util.Inventory;

import java.util.Timer;
import java.util.TimerTask;


public class HeadpageActivity extends Activity {
	static final String ITEM_MY_FREE = "my_free";
	static final String ITEM_SPONSOR_MONth = "sponsor_month";
	static final String ITEM_SPONSOR_YEARS = "sponsor_years";
	static final  String ITEM_SPOMSOR_OTHER ="sponsor_other";
	static final String ITEM_MY_VIP = "my_vip";
	static final String ITEM_1000 = "1000";
	static final String ITEM_100 = "100";
	private boolean change = false;
	private Button but1;
	public ProgressDialog myDialog;
	IabHelper mHelper;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 開啟全螢幕	
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 設定隱藏APP標題
		setContentView(R.layout.activity_headpage);

		ConnectivityManager conManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo networInfo = conManager.getActiveNetworkInfo();       

		if (networInfo == null || !networInfo.isAvailable()){ 
			new AlertDialog.Builder(HeadpageActivity.this)
			.setTitle(getString(R.string.Network_status))
			.setMessage(getString(R.string.no_network))
			.setCancelable(false)
			.setPositiveButton(getString(R.string.setting), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Intent settintIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
					startActivity(settintIntent);

				}
			})
			.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					HeadpageActivity.this.finish();
				}
			})
			.show();

		}else{
			final ProgressDialog progressDialog =ProgressDialog.show(HeadpageActivity.this, getString(R.string.Network_in),getString(R.string.waitting));
			final Handler handler = new Handler();
			final Runnable runnable = new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					new AlertDialog.Builder(HeadpageActivity.this);

					progressDialog.dismiss();

				}
			};	
			

			Thread thread = new Thread(){

				@Override
				public void run() {
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					handler.post(runnable);
				}
			};
			thread.start();	

		}

		final TextView test = (TextView) findViewById(R.id.textView1);
		Timer timer = new Timer();

		TimerTask task = new TimerTask() {
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						if (change) {
							change = false;
							test.setTextColor(Color.TRANSPARENT); // 讓文字透明
						} else {
							change = true;
							test.setTextColor(Color.DKGRAY);
						}
					}
				});
			}
		};
		timer.schedule(task, 1, 800); // 參數分別是(timer需要做什麼事情，執行後多久開始執行，閃爍速度多快)



		mHelper = new IabHelper(this, getString(R.string.my_license_key));
		mHelper.startSetup(new
								   IabHelper.OnIabSetupFinishedListener() {
									   public void onIabSetupFinished(IabResult result) {
										   if (!result.isSuccess()) {

											   MySharedPrefernces.saveIsBuyed(HeadpageActivity.this,false);
										   } else {

											   MySharedPrefernces.saveIsBuyed(HeadpageActivity.this,false);
											   mHelper.queryInventoryAsync(mGotInventoryListener);
										   }

									   }
								   });

		GetButtonView();
		setButtonEvent();

	}

	public void GetButtonView() {
		but1 = (Button) findViewById(R.id.button1);
	}

	public void setButtonEvent() {
		but1.setOnClickListener(buttonListener);
	}

	private Button.OnClickListener buttonListener = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
//			
//			Intent intent = new Intent();
//			intent.setClass(HeadpageActivity.this, MainActivity.class);
//			startActivity(intent);
//			finish();
			myDialog = ProgressDialog.show(HeadpageActivity.this, "請稍後片刻....",
					"正在載入中", true);
			new Thread() {
				public void run() {
					try {
						sleep(1000);
						Intent intent = new Intent();
						intent.setClass(HeadpageActivity.this, MainActivity.class);// Testone跳到Testtwo這個Activity
						startActivity(intent);
						finish();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {

						myDialog.dismiss();
					}
				}
			}.start();

		}

	};

	IabHelper.QueryInventoryFinishedListener mGotInventoryListener
			= new IabHelper.QueryInventoryFinishedListener() {
		public void onQueryInventoryFinished(IabResult result,
											 Inventory inventory) {

			if (result.isFailure())
			{


				// handle error here
//				Toast.makeText(getApplicationContext(),result.getMessage()+","+result.getResponse(),Toast.LENGTH_SHORT).show();
				return;
			}
			else
			{



				if(inventory.hasPurchase(ITEM_MY_FREE))
				{
					Toast.makeText(getApplicationContext(),ITEM_MY_FREE,Toast.LENGTH_SHORT).show();
					MySharedPrefernces.saveIsBuyed(HeadpageActivity.this, true);


				}
				if(inventory.hasPurchase(ITEM_SPONSOR_MONth))
				{
					MySharedPrefernces.saveIsBuyed(HeadpageActivity.this, true);
					Toast.makeText(getApplicationContext(),ITEM_SPONSOR_MONth,Toast.LENGTH_SHORT).show();
				}
				if(inventory.hasPurchase(ITEM_SPOMSOR_OTHER))
				{
					Toast.makeText(getApplicationContext(),ITEM_SPOMSOR_OTHER,Toast.LENGTH_SHORT).show();
					MySharedPrefernces.saveIsBuyed(HeadpageActivity.this, true);


				}
				if(inventory.hasPurchase(ITEM_SPONSOR_YEARS)){
					MySharedPrefernces.saveIsBuyed(HeadpageActivity.this, true);
					Toast.makeText(getApplicationContext(),ITEM_SPONSOR_YEARS,Toast.LENGTH_SHORT).show();

				}
				if(inventory.hasPurchase(ITEM_MY_VIP)){
					MySharedPrefernces.saveIsBuyed(HeadpageActivity.this, true);
					Toast.makeText(getApplicationContext(),ITEM_MY_VIP,Toast.LENGTH_SHORT).show();

				}
				if(inventory.hasPurchase(ITEM_100)){

					MySharedPrefernces.saveIsBuyed(HeadpageActivity.this, true);
					Toast.makeText(getApplicationContext(),ITEM_100,Toast.LENGTH_SHORT).show();


				}
				if(inventory.hasPurchase(ITEM_1000)){
					MySharedPrefernces.saveIsBuyed(HeadpageActivity.this, true);
					Toast.makeText(getApplicationContext(),ITEM_1000,Toast.LENGTH_SHORT).show();				}



			}
		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mHelper != null) mHelper.dispose();
		mHelper = null;
	}

}