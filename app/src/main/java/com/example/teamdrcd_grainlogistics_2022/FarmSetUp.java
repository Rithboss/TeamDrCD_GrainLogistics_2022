package com.example.teamdrcd_grainlogistics_2022;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.firebase.auth.*;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class FarmSetUp extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String[] locs = new String[4];
    private Polygon polygon1;
    private GeoPoint geo1;
    private GeoPoint geo2;
    private GeoPoint geo3;
    private GeoPoint geo4;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_farm_set_up);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Bettendorf and move the camera
        LatLng quadcities = new LatLng(42, -90);
        mMap.addMarker(new MarkerOptions().position(quadcities).title("Marker in Bettendorf"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(quadcities));

        //on touch stores coordinates to send to make a polygon
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng point) {
                Log.d("DEBUG", point.toString());
                if(locs[0] == null){
                    locs[0] = point.toString();
                } else if(locs[1] == null){
                    locs[1] = point.toString();
                } else if(locs[2] == null){
                    locs[2] = point.toString();
                } else if(locs[3] == null){
                    locs[3] = point.toString();
                    markArea(locs);
                }
            }
        });
    }
    //takes in a list of coordinate strings and uses them to make a polygon
    public void markArea(String[] locs){
        double lat1 = 0;
        double lng1 = 0;
        double lat2 = 0;
        double lng2 = 0;
        double lat3 = 0;
        double lng3 = 0;
        double lat4 = 0;
        double lng4 = 0;
        for(int i = 0;i<locs.length;i++){
            String x = locs[i];
            double lat = Double.parseDouble(x.substring(x.indexOf("(") + 1, x.indexOf("(") + 8));
            double lng = Double.parseDouble(x.substring(x.indexOf(",")+1, x.indexOf(",") + 8));
            if(i == 0) {
                lat1 = lat;
                lng1 = lng;
            } else if(i == 1) {
                lat2 = lat;
                lng2 = lng;
            } else if(i == 2) {
                lat3 = lat;
                lng3 = lng;
            } else if(i == 3) {
                lat4 = lat;
                lng4 = lng;
            }
        }
        polygon1 = mMap.addPolygon(new PolygonOptions().clickable(true)
                .add(
                        new LatLng(lat1, lng1),
                        new LatLng(lat2, lng2),
                        new LatLng(lat3, lng3),
                        new LatLng(lat4, lng4)));
        polygon1.setStrokeColor(0xff388E3C);
        geo1 = new GeoPoint(lat1,lng1);
        geo2 = new GeoPoint(lat2,lng2);
        geo3 = new GeoPoint(lat3,lng3);
        geo4 = new GeoPoint(lat4,lng4);
    }
    //adds the polygon to a permanently stored list
    public void addPoly(View view){
        GeoPoint[] temp = {geo1,geo2,geo3,geo4};
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        String uid = user.getUid();
        DatabaseReference myRef3 = database.getReference("/users/" + uid + "/FarmTest");
        myRef3.setValue(temp);
        polygon1.remove();
        locs[0] = null;
        locs[1] = null;
        locs[2] = null;
        locs[3] = null;
        geo1 = null;
        geo2 = null;
        geo3 = null;
        geo4 = null;
    }
    //gets rid of the current polygon
    public void resetPoly(View view){
        polygon1.remove();
        locs[0] = null;
        locs[1] = null;
        locs[2] = null;
        locs[3] = null;
        geo1 = null;
        geo2 = null;
        geo3 = null;
        geo4 = null;
    }
}


