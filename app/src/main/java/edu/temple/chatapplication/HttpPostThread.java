package edu.temple.chatapplication;

import android.os.Handler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class HttpPostThread extends Thread {
    final String POST_URL = "https://kamorris.com/lab/register_location.php";

    String userName;
    double latitude;
    double longitude;

    public HttpPostThread(String userName, double latitude, double longitude) {
        this.userName = userName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public void run() {
        try {
            URL url = new URL(POST_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            Map<String, String> parameters = new HashMap<>();
            parameters.put("username", userName);
            parameters.put("latitude", Double.toString(latitude));
            parameters.put("longitude", Double.toString(longitude));
            connection.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());

            StringBuilder builder = new StringBuilder();
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                builder.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                builder.append("=");
                builder.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                builder.append("&");
            }
            String resultString = builder.toString();
            String paramsString = resultString.length() > 0 ? resultString
                    .substring(0, resultString.length() - 1) : resultString;

            out.writeBytes(paramsString);
            out.flush();
            out.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
