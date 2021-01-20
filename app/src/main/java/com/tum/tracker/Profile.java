package com.tum.tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.auth0.android.jwt.*;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {
    LocationManager locationManager;
    TextView name, mail;
    Button logout;
    String myJwtToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        logout = findViewById(R.id.logout);
        name = findViewById(R.id.name);
        mail = findViewById(R.id.mail);
        Bundle bundle = getIntent().getExtras();
        myJwtToken = bundle.getString("myJwtToken");
        RequestQueue queue = Volley.newRequestQueue(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 1, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                JsonObjectRequest req = makeRequest("http://92.115.190.64:8080/user/cor",makePostData(location.getLongitude(), location.getLatitude()));
                queue.add(req);
            }
        });

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (signInAccount != null) {
            name.setText(signInAccount.getDisplayName());
            mail.setText(signInAccount.getEmail());
        }
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                Toast.makeText(Profile.this, "Have a good day", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
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

    private JsonObjectRequest makeRequest(String url, JSONObject postData) {
        return new JsonObjectRequest(Request.Method.POST, url, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + myJwtToken);
                return params;
            }
        };
    };

    private JsonObjectRequest makeGetRequest(String url) {
        return new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println(response.get("cor") + "and iopta " + response.get("lastSeen"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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
}