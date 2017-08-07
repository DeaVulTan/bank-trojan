package com.example.livemusay.myapplication;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class request
{
    public String go_post(String url, String parametr)
    {
        secFunctions SF = new secFunctions();
            SendLoginData SendLoginData = new SendLoginData();
            SendLoginData.execute(url,parametr);

            try
            {
             return SF.return_plain_text(SendLoginData.get(),"<tag>","</tag>");
            }
            catch (Exception x)
            {
                return "";
            }
        }

    class SendLoginData extends AsyncTask<String, String, String> {

        String resultString = null;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls)
        {
        try
        {
            String myURL = urls[0];
            String parammetrs = urls[1];
            byte[] data = null;
            InputStream is = null;

            try {
                URL url = new URL(myURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");//POST
                conn.setDoOutput(true);
                conn.setDoInput(true);

                conn.setRequestProperty("Content-Length", //Content-Length
                        "" + Integer.toString(parammetrs.getBytes().length));
                OutputStream os = conn.getOutputStream();
                data = parammetrs.getBytes("UTF-8");//UTF-8
                os.write(data);
                data = null;

                conn.connect();
                int responseCode= conn.getResponseCode();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                if (responseCode == 200) {
                    is = conn.getInputStream();

                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) != -1) {
                        baos.write(buffer, 0, bytesRead);
                    }
                    data = baos.toByteArray();
                    resultString = new String(data, "UTF-8");//UTF-8
                } else {
                }
            } catch (MalformedURLException e)
            {
                //MalformedURLException
                Log.e("Request","MalformedURLException");
            }
            catch (IOException e)
            {
                //IOException
                Log.e("Request","IOException");
            }
            catch (Exception e)
            {
                //Exception
                Log.e("Request","Exception");
            }
        } catch (Exception e) {
                e.printStackTrace();
            }
            return resultString;
        }

        @Override
        protected void onPostExecute(String result){super.onPostExecute(result);}
    }
}
