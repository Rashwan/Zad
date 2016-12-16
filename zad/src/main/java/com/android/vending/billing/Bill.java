package com.android.vending.billing;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.app.zad.R;

public class Bill extends Activity{
	protected static final String TAG = "Bill";
	private IabHelper iabHelper;
	private Boolean mIsPremium = false;
	private  Context mContext;
	private String base64EncodedPublicKey;
	private String SKU;
	public SharedPreferences iapPrefrence;
	@SuppressWarnings("unused")
	private String flag;
	
	public void init(Context context){
		mContext = context;
		
		base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsHb48P8sao5Z5hFB6J7kInN6EdUbNsvhfNw6VuRmz6RvdE1vWnvSoiSZFFwtZmvVFzIQftWy"+getMiddle4Bits()+"1bbP8LqQxvy/8V/qLnbkwnns+nBNZzIChQORLYLOUWSbnAk0JiFwavIuusvmmoptGha2oYVkWpY2No5HgPkkVRmNLJ9tXeRe6BnmldoNbNEzzSwJ7g67ZqF/od91LD+EV2k0qbMxEonQX5NPtxCBNXkgBTBuHpt0L6hn60ix8JCz224zzIB8Xu4liqGj+OQLV8KLvyjKHt0rPzcXlivCXFIUZD770zK5dvaP7BwO8mfPmufb48xrWPROCz5VCLsLDjmcMwweyQIDAQAB";
		SKU  = "premiu"+getMiddle2BitsSku()+"00";
		
		iabHelper = new IabHelper(mContext, base64EncodedPublicKey);
		
		iabHelper.enableDebugLogging(true);
		
		iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			   public void onIabSetupFinished(IabResult result) {
				   if (!result.isSuccess()) {
	                    // Oh noes, there was a problem.
	                    Log.d(TAG, "error setting up IAP");
	                    return;
	                }

	                // Have we been disposed of in the meantime? If so, quit.
	                if (iabHelper == null) return;
	                
	                // IAB is fully set up. Now, let's get an inventory of stuff we own.
	                Log.d(TAG, "Setup successful. Querying inventory.");
	                iabHelper.queryInventoryAsync(false,mQueryFinishedListener);
	                flag = "inProgress";
	                
	              	                
			}
		
		
		});
		
	}
	
	

	IabHelper.QueryInventoryFinishedListener 
	   mQueryFinishedListener = new IabHelper.QueryInventoryFinishedListener() {
	   
		public void onQueryInventoryFinished(IabResult result, Inventory inventory)   
		   {
			   Log.d(TAG, "Query inventory finished.");
			   
		        // Have we been disposed of in the meantime? If so, quit.
		        if (iabHelper == null) return;
		
		        // Is it a failure?
		        if (result.isFailure()) {
		     	    
		            Log.d(TAG,"Failed to query inventory: " + result);
		            return;
		        }
		        
		        Log.d(TAG, "Query inventory was successful.");
		
		
		        // Do we have the premium upgrade?
		        Purchase premiumPurchase = inventory.getPurchase(SKU);
		        mIsPremium = (premiumPurchase != null);
		        Log.d(TAG, "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));
		    	

				iapPrefrence = mContext.getSharedPreferences(mContext.getString(R.string.IAPPreference),
						MODE_PRIVATE);
				Editor editor = iapPrefrence.edit();
				editor.putBoolean(mContext.getString(R.string.isPremium), mIsPremium);
				editor.commit();
				
				flag = "finished";
		       return;
		}
	};
	
	//Secure Our Public Key
	private String getMiddle4Bits(){
		
		return "2oYT";
	}

	private String getMiddle2BitsSku() {
		return "m1";
	}
	
}
