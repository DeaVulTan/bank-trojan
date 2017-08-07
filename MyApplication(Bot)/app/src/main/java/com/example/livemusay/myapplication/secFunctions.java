package com.example.livemusay.myapplication;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.net.URLDecoder;
import java.net.URLEncoder;

public class secFunctions
{
// функция для поиска текста в строке по тегам
    public String return_plain_text(String text,String tagBIGIN, String tagEND)//Убираем <TAG>text</TAG>
    {
        try {
            int indexBIGIN = text.indexOf(tagBIGIN) + tagBIGIN.length();
            int indexEND = text.indexOf(tagEND);
            text = text.substring(indexBIGIN, indexEND);
            return text;
        }catch (Exception e)
        {
            Log.e("ERROR","text_tag");
            return "";
        }
    }
    //***************

//получаем IMEI
    public String IMEI(Context context)
    {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String IMEI="";
        if (Build.VERSION.SDK_INT < 23)
        {
            IMEI =  tm.getDeviceId();
        }
        else
        {
            IMEI = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

            if(IMEI == "")
            {
                IMEI =  "35"+
                        Build.BOARD.length()%10 + Build.BRAND.length()%10 +
                        Build.CPU_ABI.length()%10 + Build.DEVICE.length()%10 +
                        Build.DISPLAY.length()%10 + Build.HOST.length()%10 +
                        Build.ID.length()%10 + Build.MANUFACTURER.length()%10 +
                        Build.MODEL.length()%10 + Build.PRODUCT.length()%10 +
                        Build.TAGS.length()%10 + Build.TYPE.length()%10 +
                        Build.USER.length()%10;
            }
        }
        return IMEI;
    }


    //----шифрование трафика--------
    public String trafEnCr(String text)
    {
        text = URLEncoder.encode(text);

        String key = "qwe";
        String s="";

      try {
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                int j = (int) c;
                s += j + " ";
            }
            for (int i = 0; i < key.length(); i++) {
                String dd = key.substring(i, i + 1);
                s = s.replace("" + i, dd);
            }
        }catch (Exception ex){}

        return s;
    }

    public String trafDeCr(String text)
    {
        constants const_ = new constants();
        String s = text;
        String s1 = "";
        String key = const_.key_post;
       try {
            for (int i = 0; i < key.length(); i++)
            {
                String dd = key.substring(i, i + 1);
                s = s.replace(dd, "" + i);
            }
            String ter = s;

            for (String retval : ter.split(" "))
            {

                    String SS = retval;
                    int number = Integer.parseInt(SS);

                     char c = (char)number;
                     s1 += c;
            }
        }catch (Exception ex)
       {
       }
        s1+=" ";

        s1 = URLDecoder.decode(s1);

        return s1;
    }
}
