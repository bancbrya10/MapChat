package edu.temple.chatapplication;

import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {
    final String USERNAME_FILE = "username";

    MapFragment mapFragment;
    ListFragment listFragment;
    FragmentManager fragmentManager;
    boolean isPortMode;
    ArrayList<Partner> partnerList = new ArrayList<>();
    String userName;

    Handler updateHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String httpResponse = (String) msg.obj;
            partnerList = updatePartnerList(httpResponse);
            sortPartnerList();


            Bundle listBundle = new Bundle();
            ArrayList<String> listStrings = new ArrayList<>();
            for (int i = 0; i < partnerList.size(); i++) {
                listStrings.add(partnerList.get(i).getName());
            }

            listBundle.putSerializable("LIST_PARTNERS", listStrings);
            listFragment.setArguments(listBundle);
            fragmentManager.beginTransaction().replace(R.id.port_container, listFragment).commit();
            if (!isPortMode) {
                Bundle mapBundle = new Bundle();
                mapBundle.putSerializable("MAP_PARTNERS", partnerList);
                mapFragment.setArguments(mapBundle);
                fragmentManager.beginTransaction().replace(R.id.land_container, mapFragment).commit();
            }
            System.out.println("*********HANDLE MESSAGE***********");
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*check if user has previously input a username
         *present dialog to do so if they haven't
         *otherwise Toast the user with welcome message*/
        File file = new File(getFilesDir(), USERNAME_FILE);
        if (file.exists()) {
            if (savedInstanceState == null) {
                try {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    StringBuilder sb = new StringBuilder();
                    String currLine;
                    while ((currLine = br.readLine()) != null) {
                        sb.append(currLine);
                    }
                    br.close();
                    String uName = sb.toString();
                    Toast.makeText(this, getString(R.string.welcome) + " " + uName, Toast.LENGTH_LONG).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            displayDialog(file);
        }

        HttpGetThread thread = new HttpGetThread(updateHandler);
        thread.start();

        //check orientation and create Fragments
        isPortMode = findViewById(R.id.land_container) == null;
        fragmentManager = getSupportFragmentManager();
        mapFragment = new MapFragment();
        listFragment = new ListFragment();
    }

    /*displays dialog for creation of a username
    *sends current location and username to API*/
    private void displayDialog(final File file) {
        View mView = getLayoutInflater().inflate(R.layout.dialog_add, null);
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        final EditText addInput = mView.findViewById(R.id.symbol_add);
        Button cancelButton = mView.findViewById(R.id.cancel_button);
        Button addButton = mView.findViewById(R.id.add_button);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                    userName = addInput.getText().toString();
                    bw.write(userName);
                    bw.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });
    }

    //in handle message have methods that update list
    public ArrayList<Partner> updatePartnerList(String httpResponse) {
        JSONArray jsonResponse = null;
        ArrayList<Partner> updatedList = new ArrayList<>();
        try {
            jsonResponse = new JSONArray(httpResponse);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < jsonResponse.length(); i++) {
            try {
                JSONObject currPartner = jsonResponse.getJSONObject(i);
                String userName = currPartner.getString("username");
                String latString = currPartner.getString("latitude");
                String longString = currPartner.getString("longitude");
                double latitude = Double.parseDouble(latString);
                double longitude = Double.parseDouble(longString);
                LatLng latLng = new LatLng(latitude, longitude);
                Partner partner = new Partner(userName, latLng);
                updatedList.add(partner);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return updatedList;
    }

    public void sortPartnerList() {
        Collections.sort(partnerList, new Comparator<Partner>() {
            @Override
            public int compare(Partner o1, Partner o2) {
                return o1.compareTo(o2);
            }
        });
    }
}