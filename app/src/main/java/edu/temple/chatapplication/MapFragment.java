package edu.temple.chatapplication;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class MapFragment extends Fragment implements OnMapReadyCallback {
    MapView mapView;
    View mView;
    Context mContext;

    ArrayList<Partner> partners;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_map, container, false);
        Bundle args = getArguments();
        if (args != null) {
            partners = (ArrayList<Partner>) args.getSerializable("MAP_PARTNERS");
        }
        return mView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = mView.findViewById(R.id.map_view);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(mContext);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

       // LocationManager locManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        CameraPosition camPos = CameraPosition.builder().target(new LatLng(41.044089, -75.301557)).zoom(15).bearing(0).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(camPos));

        for (int i = 0; i < partners.size(); i++) {
            googleMap.addMarker(new MarkerOptions().position(partners.get(i).getCoordinates()).title(partners.get(i).getName()));
        }
    }
}
