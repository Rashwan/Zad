package com.app.zad.notboring;

import java.util.Random;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.app.zad.R;

public class ActionBarMagicView extends FrameLayout {

	private final Handler mHandler;
	private int[] mResourceIds;
	private ImageView[] mImageViews;
	private int mActiveImageIndex = -1;

	private final Random random = new Random();
	private int mSwapMs = 10000;
	private int mFadeInOutMs = 400;

	private float maxScaleFactor = 1.5F;
	private float minScaleFactor = 1.2F;

	private Runnable mSwapImageRunnable = new Runnable() {
		@Override
		public void run() {
			swapImage();
			mHandler.postDelayed(mSwapImageRunnable, mSwapMs - mFadeInOutMs * 2);
		}
	};

	public ActionBarMagicView(Context context) {
		this(context, null);
	}

	public ActionBarMagicView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ActionBarMagicView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mHandler = new Handler();
	}

	public void setResourceIds(int... resourceIds) {
		mResourceIds = resourceIds;
		try {
			fillImageViews();
		} catch (OutOfMemoryError o) {
			fillImageViewsForLowMemories();
		}

	}

	private void swapImage() {
		if (mActiveImageIndex == -1) {
			mActiveImageIndex = 1;
			animate(mImageViews[mActiveImageIndex]);
			return;
		}

		int inactiveIndex = mActiveImageIndex;
		mActiveImageIndex = (1 + mActiveImageIndex) % mImageViews.length;

		final ImageView activeImageView = mImageViews[mActiveImageIndex];
		activeImageView.setAlpha(0.0f);
		ImageView inactiveImageView = mImageViews[inactiveIndex];

		animate(activeImageView);

		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.setDuration(mFadeInOutMs);
		animatorSet.playTogether(
				ObjectAnimator.ofFloat(inactiveImageView, "alpha", 1.0f, 0.0f),
				ObjectAnimator.ofFloat(activeImageView, "alpha", 0.0f, 1.0f));
		animatorSet.start();
	}

	private void start(View view, long duration, float fromScale,
			float toScale, float fromTranslationX, float fromTranslationY,
			float toTranslationX, float toTranslationY) {
		view.setScaleX(fromScale);
		view.setScaleY(fromScale);
		view.setTranslationX(fromTranslationX);
		view.setTranslationY(fromTranslationY);
		ViewPropertyAnimator propertyAnimator = view.animate()
				.translationX(toTranslationX).translationY(toTranslationY)
				.scaleX(toScale).scaleY(toScale).setDuration(duration);
		propertyAnimator.start();
	}

	private float pickScale() {
		return this.minScaleFactor + this.random.nextFloat()
				* (this.maxScaleFactor - this.minScaleFactor);
	}

	private float pickTranslation(int value, float ratio) {
		return value * (ratio - 1.0f) * (this.random.nextFloat() - 0.5f);
	}

	public void animate(View view) {
		float fromScale = pickScale();
		float toScale = pickScale();
		float fromTranslationX = pickTranslation(view.getWidth(), fromScale);
		float fromTranslationY = pickTranslation(view.getHeight(), fromScale);
		float toTranslationX = pickTranslation(view.getWidth(), toScale);
		float toTranslationY = pickTranslation(view.getHeight(), toScale);
		start(view, this.mSwapMs, fromScale, toScale, fromTranslationX,
				fromTranslationY, toTranslationX, toTranslationY);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		startKenBurnsAnimation();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		mHandler.removeCallbacks(mSwapImageRunnable);
	}

	private void startKenBurnsAnimation() {
		mHandler.post(mSwapImageRunnable);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		View view = inflate(getContext(), R.layout.view_kenburns, this);

		mImageViews = new ImageView[2];
		mImageViews[0] = (ImageView) view.findViewById(R.id.image0);
		mImageViews[1] = (ImageView) view.findViewById(R.id.image1);
	}

	private void fillImageViews() {
		for (int i = 0; i < mImageViews.length; i++) {
			mImageViews[i].setImageResource(mResourceIds[i]);
		}
	}

	private void fillImageViewsForLowMemories() {
		for (int i = 0; i < mImageViews.length; i++) {
			try {
				mImageViews[i].setImageBitmap(decodeSampledBitmapFromResource(
						getResources(), mResourceIds[i], 100, 100));
			} catch (OutOfMemoryError o) {
				mImageViews[i].setImageBitmap(decodeSampledBitmapFromResource(
						getResources(), mResourceIds[i], 40, 40));
			}
		}
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

}
