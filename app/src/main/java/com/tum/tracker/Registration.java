package com.tum.tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class Registration extends AppCompatActivity {

    TextView name, mail;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        name = findViewById(R.id.name);
        mail = findViewById(R.id.mail);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (signInAccount != null) {
            name.setText(signInAccount.getDisplayName());
            mail.setText(signInAccount.getEmail());
        }
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Map.class);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("ResourceType")
    public void addInputField(View view) {
        ConstraintLayout container = findViewById(R.id.constrain_layout);

        EditText step =  new EditText(this);
        step.setId(1);
        step.setText("monitored_by");
        step.setMaxLines(1);
        step.setWidth(container.getWidth());

        container.addView(step);
    }

    public void sendDates(View view) {
//        RequestQueue queue = Volley.newRequestQueue(this);
//        String url = "";
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
//                new Response.Listener<>()
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                })
    }
}