
package com.app.zad.ui;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.app.zad.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;




public class Facebook_Share extends Activity {
	protected static final String TAG = Facebook_Share.class.getName();

	String shareAuthor;
	String shareQuote;
	CallbackManager callbackManager;
	Context context;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facebook__share);
		context = this;
		Intent in = getIntent();
		shareAuthor = in.getExtras().getString("shareAuthor");
		shareQuote = in.getExtras().getString("shareQuote");

		prepareFbShareButton();


	}
	private void prepareFbShareButton() {
		callbackManager = CallbackManager.Factory.create();
		ShareDialog shareDialog = new ShareDialog(this);
		shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
			@Override
			public void onSuccess(Sharer.Result result) {
				Log.d(TAG,"success");
				Toast.makeText(context, getString(R.string.shared),Toast.LENGTH_SHORT).show();
				finish();
			}

			@Override
			public void onCancel() {
				Log.d(TAG,"canceled");
			}

			@Override
			public void onError(FacebookException error) {
				Log.d(TAG,"error");
				Toast.makeText(context, getString(R.string.not_shared),Toast.LENGTH_SHORT).show();

			}
		});

		if (ShareDialog.canShow(ShareLinkContent.class)){
			ShareLinkContent content = prepareFbShareContent();
			shareDialog.show(content);
		}
	}

	@NonNull
	private ShareLinkContent prepareFbShareContent() {
		return new ShareLinkContent.Builder()
				.setContentTitle(getString(R.string.zad_for) + shareAuthor)
				.setContentDescription(shareQuote)
				.setImageUrl(Uri.parse(getString(R.string.facebook_image)))
				.setContentUrl(Uri.parse("http://www.appzad.com"))
				.build();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode,resultCode,data);
	}



	
}
