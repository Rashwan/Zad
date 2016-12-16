
package com.app.zad.ui;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;
import com.sromku.simple.fb.entities.Feed;
import com.sromku.simple.fb.entities.Photo;
import com.sromku.simple.fb.entities.Privacy;
import com.sromku.simple.fb.entities.Privacy.PrivacySettings;
import com.sromku.simple.fb.entities.Story;
import com.sromku.simple.fb.entities.Story.StoryAction;
import com.sromku.simple.fb.entities.Story.StoryObject;
import com.sromku.simple.fb.listeners.OnCreateStoryObject;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnPublishListener;
import com.app.zad.R;




public class Facebook_Share extends Activity {
	protected static final String TAG = Facebook_Share.class.getName();

	private SimpleFacebook mSimpleFacebook;
	Feed feed;
	OnPublishListener onPublishListener1;
	Photo photo;
	OnPublishListener onPublishListener2;
	Story story;
	OnPublishListener onPublishListener3;
	StoryObject storyObject;
	OnCreateStoryObject onstory;
	ProgressDialog mProgressDialog;
	String shareAuthor;

	OnPublishListener onPublishListener4;

	StoryObject storyObject5;

	String shareQuote;

	String shareBody;
	private Boolean wificon  = false;
	private Boolean datacon = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// test local language
				/*Utils.updateLanguage(getApplicationContext(), "en");
				Utils.printHashKey(getApplicationContext());*/

		setContentView(R.layout.activity_facebook__share);
		Intent in = getIntent();
		shareAuthor = in.getExtras().getString("shareAuthor");
		shareQuote = in.getExtras().getString("shareQuote");
		shareBody = in.getExtras().getString("shareBody");
		Permission[] permissions = new Permission[] {
				Permission.PUBLISH_ACTION
		};
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo mData = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mWifi!=null) wificon = mWifi.isConnected();
		if (mData!=null) datacon = mData.isConnected();
		//04/03/2015
		if (wificon || datacon) {
		   
		
			SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
					.setAppId(getString(R.string.app_id))
					.setNamespace(getString(R.string.facebook_namespace))
					.setPermissions(permissions)
					.setAskForAllPermissionsAtOnce(false).build();
			SimpleFacebook.setConfiguration(configuration);
		}
		else{
			Toast.makeText(getApplicationContext(), R.string.not_shared, Toast.LENGTH_SHORT).show();
			finish();
		}
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSimpleFacebook = SimpleFacebook.getInstance(this);
		OnLoginListener onLoginListener = new OnLoginListener() {
		    @Override
		    public void onLogin() {
		        // change the state of the button or do whatever you want
		        Log.i(TAG, "Logged in");
		        hideDialog();
		        
		        StoryObject storyObject44 = new StoryObject.Builder()         
		        .setNoun("quote")
		        .addProperty("author", shareAuthor)
                .setDescription(shareQuote)
                .setImage(getString(R.string.facebook_image))
                .setTitle(getString(R.string.zad_for) + shareAuthor)
                .setUrl(getString(R.string.facebook_playstore)) 
	            .build();                       
		        Toast.makeText(getApplicationContext(), R.string.preparing_quote_sharing, Toast.LENGTH_LONG).show();
	            mSimpleFacebook.create(storyObject44, new OnCreateStoryObject() {
	                public void onComplete(String response) {
	                	Privacy privacy = new Privacy.Builder()
	        			.setPrivacySettings(PrivacySettings.ALL_FRIENDS)
	        			.build();
	                	
	                    StoryObject storyObject22 = new StoryObject.Builder()            
	                    .setId(response)
	                    .setNoun("quote")
	                    .setPrivacy(privacy)
	                    .build();

	                    StoryAction storyAction = new StoryAction.Builder()
	                    .setAction("share") 
	                    .addProperty("fb:explicitly_shared", "true")
	                    .build();

	                Story story = new Story.Builder()
	                    .setObject(storyObject22)
	                    .setAction(storyAction)
	                    .build();
	                
	                mSimpleFacebook.publish(story, new OnPublishListener() {
	        			
	        		    @Override
	        		    public void onComplete(String postId) {
	        		    	Log.i("Done", "yes ID= " + postId);
	        				hideDialog();
	        				Toast.makeText(getApplicationContext(), R.string.shared, Toast.LENGTH_SHORT).show();
	        				finish();
	        		    }

	        		    @Override
	        		    public void onFail(String reason) {
	        		    	Log.i("failed", reason);
	        				hideDialog();
	        				Toast.makeText(getApplicationContext(), R.string.not_shared, Toast.LENGTH_SHORT).show();
	        				finish();
	        		    }
	        		    @Override
	        		    public void onThinking() {
	        		    	Log.i("thinking", ">>>>>");
	        				showDialog();
	        		    }
	        		    @Override
	        		    public void onException(Throwable throwable) {
	        				hideDialog();
	        				Toast.makeText(getApplicationContext(), R.string.not_shared, Toast.LENGTH_SHORT).show();
	        				finish();
	        		    }});                                      

	                }
	            }); 
		    }

		    @Override
		    public void onNotAcceptingPermissions(Permission.Type type) {
		        // user didn't accept READ or WRITE permission
		        Log.w(TAG, String.format("You didn't accept %s permissions", type.name()));
		    }

			@Override
			public void onThinking() {
				// TODO Auto-generated method stub
				showDialog();
				
			}

			@Override
			public void onException(Throwable arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onFail(String arg0) {
				// TODO Auto-generated method stub
				
			}
		};
		mSimpleFacebook.login(onLoginListener);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    mSimpleFacebook.onActivityResult(this, requestCode, resultCode, data); 
	    super.onActivityResult(requestCode, resultCode, data);
	} 
	

	protected void showDialog() {
		if (mProgressDialog == null) {
			setProgressDialog();
		}
		mProgressDialog.show();
	}
	
	protected void hideDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}
	
	private void setProgressDialog() {
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setTitle(getString(R.string.sharing_quote));
		mProgressDialog.setMessage(getString(R.string.please_wait));
	}
	
	
}
