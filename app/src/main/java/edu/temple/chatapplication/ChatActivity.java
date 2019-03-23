package edu.temple.chatapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private final String MESSAGE_URL = "http://kamorris.com/lab/send_message.php";

    private ArrayList<Message> rawMessages;
    private ChatAdapter chatAdapter;
    private String thisUser;
    private String partner;
    private EncryptorDecryptor encryptor;
    private KeyPair userKeyPair;
    private String partnerPublicKey;

    //layout objects
    private ListView messageList;
    private Button sendButton;
    private EditText messageInput;

    //receive messages
    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Message message = new Message();
            message.setBelongsToThisUser(false);
            message.setData(encryptor.getDecrypted(intent.getExtras().getString("encrypted_message")));
            chatAdapter.addMessage(message);
            chatAdapter.notifyDataSetChanged();
            //enable scrolling for new messages
            messageList.setSelection(messageList.getCount() - 1);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, new IntentFilter("incoming_message"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        userKeyPair = (KeyPair) intent.getSerializableExtra("user_keys");
        partnerPublicKey = intent.getStringExtra("partner_key");
        encryptor = new EncryptorDecryptor(userKeyPair, partnerPublicKey);

        chatAdapter = new ChatAdapter(this, rawMessages);
        messageList.setAdapter(chatAdapter);
    }

    //For sending of messages to Messaging API
    private class SendMessage extends AsyncTask {
        public Context context;
        private String userName;
        private String partnerName;
        private String encryptedMessage;

        public SendMessage(Context context, String userName, String partnerName, String plainText) {
            this.context = context;
            this.userName = userName;
            this.partnerName = partnerName;
            encryptedMessage = encryptor.getEncrypted(plainText);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                //assemble parameters for HTTP POST request
                Map<String, String> params = new LinkedHashMap<>();
                params.put("user", userName);
                params.put("partneruser", partnerName);
                params.put("message", encryptedMessage);

                StringBuilder sb = new StringBuilder();
                for (Map.Entry<String, String> param : params.entrySet()) {
                    if (sb.length() != 0) {
                        sb.append('&');
                    }
                    sb.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    sb.append('=');
                    sb.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                byte[] postDataBytes = sb.toString().getBytes("UTF-8");

                URL url = new URL(MESSAGE_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                connection.setDoOutput(true);
                connection.getOutputStream().write(postDataBytes);

                Reader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                for (int c; (c = in.read()) >= 0;) {
                    System.out.println((char) c);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
