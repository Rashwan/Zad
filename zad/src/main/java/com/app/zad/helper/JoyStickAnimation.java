package com.app.zad.helper;

import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.SeekBar;

public class JoyStickAnimation extends Animation {
	private SeekBar Seeko;
	private float from;
	private float to;

	public JoyStickAnimation(SeekBar progressBar, float from, float to) {
		super();
		this.Seeko = progressBar;
		this.from = from;
		this.to = to;
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		super.applyTransformation(interpolatedTime, t);
		float value = from + (to - from) * interpolatedTime;
		Seeko.setProgress((int) value);
	}

}