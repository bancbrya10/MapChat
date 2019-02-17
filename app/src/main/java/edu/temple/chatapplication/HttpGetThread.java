package edu.temple.chatapplication;

import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpGetThread extends Thread {
    private final String GET_URL = "https://kamorris.com/lab/get_locations.php";
    private final long THIRTY_SECS = 30000;

    Handler handler;

    public HttpGetThread(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        while (true) {
            try {
                URL url = new URL(GET_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer sb = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    sb.append(inputLine);
                }
                in.close();
                connection.disconnect();

                String response = sb.toString();
                Message msg = Message.obtain();
                msg.obj = response;
                handler.sendMessage(msg);
                System.out.println("******THREAD COMPLETE*************");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                System.out.println("*********THREAD SLEEP*************");
                sleep(THIRTY_SECS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}