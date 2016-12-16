package com.app.zad.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.vending.billing.IabHelper;
import com.android.vending.billing.IabResult;
import com.android.vending.billing.Inventory;
import com.android.vending.billing.Purchase;
import com.app.zad.R;

public class Billing extends Activity {

	protected static final String TAG = "Billing";
	IabHelper mHelper;
	boolean mIsPremium = false;
	private String base64EncodedPublicKey;
	private String SKU;
	private SharedPreferences IapPrefrences;
	private ConnectivityManager connManager;
	private NetworkInfo mWifi;
	private NetworkInfo mData;
	private Boolean wificon = false;
	private Boolean datacon = false;
	private String flag = "none" ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_billing);
		base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsHb48P8sao5Z5hFB6J7kInN6EdUbNsvhfNw6VuRmz6RvdE1vWnvSoiSZFFwtZmvVFzIQftWy"+getMiddle4Bits()+"1bbP8LqQxvy/8V/qLnbkwnns+nBNZzIChQORLYLOUWSbnAk0JiFwavIuusvmmoptGha2oYVkWpY2No5HgPkkVRmNLJ9tXeRe6BnmldoNbNEzzSwJ7g67ZqF/od91LD+EV2k0qbMxEonQX5NPtxCBNXkgBTBuHpt0L6hn60ix8JCz224zzIB8Xu4liqGj+OQLV8KLvyjKHt0rPzcXlivCXFIUZD770zK5dvaP7BwO8mfPmufb48xrWPROCz5VCLsLDjmcMwweyQIDAQAB";
		SKU = "premiu"+getMiddle2BitsSku()+"00";

		mHelper = new IabHelper(this, base64EncodedPublicKey);
		mHelper.enableDebugLogging(true);
		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				if (!result.isSuccess()) {
					// Oh noes, there was a problem.
					Log.d(TAG, "error setting up IAP");
					return;
				}

				// Have we been disposed of in the meantime? If so, quit.
				if (mHelper == null)
					return;
				ArrayList<String> skus = new ArrayList<String>();
				skus.add(SKU);
				// IAB is fully set up. Now, let's get an inventory of stuff we
				// own.
				Log.d(TAG, "Setup successful. Querying inventory.");
				mHelper.queryInventoryAsync(true, skus, mQueryFinishedListener);
				flag = "in progress";
			}

		});

	}

	IabHelper.QueryInventoryFinishedListener mQueryFinishedListener = new IabHelper.QueryInventoryFinishedListener() {

		public void onQueryInventoryFinished(IabResult result,
				Inventory inventory) {
			Log.d(TAG, "Query inventory finished.");

			// Have we been disposed of in the meantime? If so, quit.
			if (mHelper == null)
				return;

			// Is it a failure?
			if (result.isFailure()) {

				Log.d(TAG, "Failed to query inventory: " + result);
				return;
			}

			Log.d(TAG, "Query inventory was successful.");

			// Do we have the premium upgrade?
			Purchase premiumPurchase = inventory.getPurchase(SKU);
			mIsPremium = (premiumPurchase != null);
			Log.d(TAG, "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));
			flag = "finished";
		}
	};

	// User Clicked To be Premium
	public void onPayButtonClicked(View arg0) {

		if (mIsPremium) {

			Toast.makeText(Billing.this, R.string.alreadyPremium,
					Toast.LENGTH_SHORT).show();
			return;
		} else {
			connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			mData = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mWifi!=null) wificon = mWifi.isConnected();
			if (mData!=null) datacon = mData.isConnected();
			//04/03/2015
			if ((wificon || datacon) && flag.equals("finished")) {
				String payload = "";
				mHelper.launchPurchaseFlow(Billing.this, SKU, 1001,
						mPurchaseFinishedListener, payload);
			}else{
				Toast.makeText(this,R.string.failure_toast, Toast.LENGTH_LONG).show();
			}
		}
	}

	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			Log.d(TAG, "Purchase finished: " + result + ", purchase: "
					+ purchase);

			// if we were disposed of in the meantime, quit.
			if (mHelper == null)
				return;

			if (result.isFailure()) {
				
				Toast.makeText(Billing.this, R.string.error_purchasing,
						Toast.LENGTH_LONG).show();				

				return;
			}

			Log.d(TAG, "Purchase successful.");

			if (purchase.getSku().equals(SKU)) {
				// bought the premium upgrade!
				Log.d(TAG, "Purchase is premium upgrade. Congratulating user.");
				Toast.makeText(Billing.this, R.string.becamePremium,
						Toast.LENGTH_LONG).show();
				mHelper.queryInventoryAsync(false, mQueryFinishedListener);

				mIsPremium = true;
				IapPrefrences = getSharedPreferences(
						getString(R.string.IAPPreference), MODE_PRIVATE);
				Editor editor = IapPrefrences.edit();
				editor.putBoolean(getString(R.string.isPremium), mIsPremium);
				editor.commit();

			}

		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + ","
				+ data);
		if (mHelper == null)
			return;

		// Pass on the activity result to the helper for handling
		if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
			// not handled, so handle it ourselves (here's where you'd
			// perform any handling of activity results not related to in-app
			// billing...
			super.onActivityResult(requestCode, resultCode, data);
		} else {
			Log.d(TAG, "onActivityResult handled by IABUtil.");
		}
	}

	private String getMiddle4Bits(){
		return "2oYT";
	}

	private String getMiddle2BitsSku() {
		return "m1";
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "Destroying helper.");
		if (mHelper != null) {
			mHelper.dispose();
			mHelper = null;
		}
	}

}
