package com.tum.tracker;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Map extends FragmentActivity implements OnMapReadyCallback {
    LocationManager locationManager;
    GoogleMap mMap;
    String myJwtToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Bundle bundle = getIntent().getExtras();
        myJwtToken = bundle.getString("myJwtToken");
        RequestQueue queue = Volley.newRequestQueue(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 1, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(location.getLatitude(), location.getLongitude()))
                        .title("Latitude: " + location.getLatitude() + " Longitude: " + location.getLongitude()));
                JsonObjectRequest gereq = makeGetRequest();
                queue.add(gereq);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private JSONObject makePostData(double lng, double lat) {
        JSONObject postData = new JSONObject();
        try {
            postData.put("corX", lng);
            postData.put("corY", lat);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return postData;
    }
    private JsonObjectRequest makeGetRequest() {
        return new JsonObjectRequest(Request.Method.GET, "http://92.115.190.64:8080/user/cor", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray cor = response.getJSONArray("cor");
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(cor.getDouble(0), cor.getDouble(1)))
                            .title("Lat: " + cor.getDouble(1) + " Long: " + cor.getDouble(1)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            public java.util.Map<String, String> getHeaders() throws AuthFailureError {
                java.util.Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + myJwtToken);
                params.put("Content-Type", "application/json; charset=UTF-8");
                return params;
            }
        };
    }
    private String[] getLngLat(JSONObject json) throws JSONException {
        return json.getString("cor").split("\\s+");
    }
}