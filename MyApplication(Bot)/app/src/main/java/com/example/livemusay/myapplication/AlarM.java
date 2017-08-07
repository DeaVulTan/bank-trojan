package com.example.livemusay.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarM extends BroadcastReceiver {

    public AlarM() {}

    @Override
    public void onReceive(Context context, Intent intent)
    {
        //запускаем сервис
       context.startService(new Intent(context, StartWhile.class));
    }

}
