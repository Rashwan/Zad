package com.app.zad.widgetPremium;

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
		return new PageList(new ChooseUpdateFrequencyPage(this, "Update"));
	}

}
