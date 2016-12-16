package com.app.zad.widget;

import android.content.Context;

import com.app.zad.help.AbstractWizardModel;
import com.app.zad.help.PageList;

public class ConfigurationWizard extends AbstractWizardModel {

	public ConfigurationWizard(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected PageList onNewRootPageList() {
		// TODO Auto-generated method stub
		return new PageList(new ChooseAuthorPage(this, "ZAD WELCOME"),
				new ChooseCategoryPage(this, "AAS"),
				new ChooseUpdateFrequencyPage(this, "Update"));
	}

}
