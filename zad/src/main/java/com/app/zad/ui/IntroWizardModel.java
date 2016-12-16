package com.app.zad.ui;

import android.content.Context;

import com.app.zad.help.AbstractWizardModel;
import com.app.zad.help.PageList;
import com.app.zad.help.Welcome1Page;
import com.app.zad.help.Welcome2Page;
import com.app.zad.help.Welcome3Page;
import com.app.zad.help.Welcome4Page;

public class IntroWizardModel extends AbstractWizardModel {

	public IntroWizardModel(Context context) {
		super(context);
	}

	@Override
	protected PageList onNewRootPageList() {
		return new PageList(new Welcome1Page(this, ""), new Welcome2Page(this,
				""), new Welcome4Page(this, ""), new Welcome3Page(this, ""));
	}

}
