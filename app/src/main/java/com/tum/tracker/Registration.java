package com.tum.tracker;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {

    String token;
    EditText step;
    RadioGroup radioGroup;
    RadioButton radioButton;
    int selectedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        Bundle bundle = getIntent().getExtras();
        token = bundle.getString("token");
        radioGroup = (RadioGroup) findViewById(R.id.radio);
    }

    @SuppressLint({"ResourceType", "SetTextI18n"})
    public void addInputField(View view) {
        LinearLayout container = findViewById(R.id.linear_layout);

        step =  new EditText(this);
        step.setId(1);
        step.setText("Liked Email");
        step.setMaxLines(1);
        step.setWidth(160);
        container.addView(step);
    }

    private JSONObject makePostData() {
        JSONObject postData = new JSONObject();
        try {
            postData.put("token", token);
            if(selectedId == 1) {
                postData.put("monitor", true);
                postData.put("linkedEmail", ""); }
            else
            {
                postData.put("monitor", false);
                postData.put("linkedEmail", String.valueOf(step.getText())); }
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
        return postData;
    }

    private JsonObjectRequest makeRequest(String url, JSONObject postData) {
        return new JsonObjectRequest(Request.Method.POST, url, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    };

    public void sendDates(View view) throws JSONException {

        selectedId =  radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://92.115.190.64:8080/user/register";
        JSONObject postData = makePostData();
        JsonObjectRequest jsonObjectRequest = makeRequest(url, postData);
        queue.add(jsonObjectRequest);
    }
}