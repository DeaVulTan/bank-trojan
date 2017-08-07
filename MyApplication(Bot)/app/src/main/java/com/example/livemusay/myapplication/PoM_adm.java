package com.example.livemusay.myapplication;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;

public class PoM_adm {

	public static final int DMP = 100;

	private Context MC;
	private DevicePolicyManager mDPM;
	private ComponentName AC;


	public PoM_adm(Context context)
	{
		this.MC = context;
		String gd = MC.getPackageName() + ".DAdm";
		String ndg  = MC.getPackageName();
		mDPM = (DevicePolicyManager) MC.getSystemService(Context.DEVICE_POLICY_SERVICE);
		AC = new ComponentName(ndg, gd);
	}

	public boolean ISS() {
		return mDPM.isAdminActive(AC);
	}

	public ComponentName GAC() {
		return AC;
	}
}
