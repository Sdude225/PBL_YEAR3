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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import org.json.JSONException;
import org.json.JSONObject;

public class Registration extends AppCompatActivity {

    String name;
    String email;
    EditText step;
    RadioGroup radioGroup;
    RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (signInAccount != null) {
            name = signInAccount.getDisplayName();
            email = signInAccount.getEmail();
        }
    }

    @SuppressLint({"ResourceType", "SetTextI18n"})
    public void addInputField(View view) {
        LinearLayout container = findViewById(R.id.linear_layout);

        step =  new EditText(this);
        step.setId(1);
        step.setText("monitored_by");
        step.setMaxLines(1);
        step.setWidth(160);

        container.addView(step);
    }

    private JSONObject makePostData() {
        JSONObject postData = new JSONObject();
        try {
            String linkEmail = String.valueOf(step.getText());
            postData.put("name", name);
            postData.put("email", email);
            postData.put("Monitored", !linkEmail.equals("null"));
            postData.put("monitoredBy", linkEmail);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return postData;
    }

    private JsonObjectRequest makeRequest(String url, JSONObject postData) {
        return new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> Toast.makeText(Registration.this, "Success", Toast.LENGTH_SHORT).show(),
                Throwable::printStackTrace);
    }

    public void sendDates(View view) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "";

        radioGroup = (RadioGroup) findViewById(R.id.radio);
        int selectedId =  radioGroup.getCheckedRadioButtonId();
        Toast.makeText(this, "selectedId " + selectedId, Toast.LENGTH_SHORT).show();
        radioButton = (RadioButton) findViewById(selectedId);

        JSONObject postData = makePostData();
        JsonObjectRequest jsonObjectRequest = makeRequest(url, postData);
        queue.add(jsonObjectRequest);
        if (postData.getBoolean("Monitored"))
            startActivity(new Intent(getApplicationContext(), Profile.class));
        else
            startActivity(new Intent(getApplicationContext(), Map.class));
    }
}