package edu.temple.chatapplication;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class MessagingService extends FirebaseMessagingService {
    private JSONObject jsonObject = null;
    private LocalBroadcastManager localBroadcastManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        //assign jsonObject message payload data
        String partnerName = null;
        String message = null;
        try {
            jsonObject = new JSONObject(remoteMessage.getData().get("payload"));
            partnerName = jsonObject.getString("from");
            message = jsonObject.getString("message");

            Intent intent = new Intent("new_message");
            intent.putExtra("partner", partnerName);
            intent.putExtra("message", message);
            localBroadcastManager.sendBroadcast(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }




    }
}
