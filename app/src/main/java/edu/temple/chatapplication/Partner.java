package edu.temple.chatapplication;

import android.location.Location;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

public class Partner implements Comparable<Partner>{
    private String name;
    private LatLng coordinates;

    public Partner(String name, LatLng coordinates) {
        this.name = name;
        this.coordinates = coordinates;
    }

    public String getName() {
        return name;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    @Override
    public int compareTo(@NonNull Partner other) {
        float[] distance = new float[2];

        double startLatitude = other.coordinates.latitude;
        double startLongitude = other.coordinates.longitude;
        double endLatitude = this.coordinates.latitude;
        double endLongitude = this.coordinates.longitude;
        Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude, distance);

        return (int) distance[0];
    }
}
