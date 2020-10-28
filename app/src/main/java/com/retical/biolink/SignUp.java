package com.retical.biolink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.retical.biolink.UtilsService.SharedPreferenceClass;
import com.retical.biolink.UtilsService.UtilService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    private Button loginBtn, registerBtn;
    private EditText name_ET, email_ET, password_ET,username_ET;
    ProgressBar progressBar;

    private String name, email, password,username;
    UtilService utilService;
    SharedPreferenceClass sharedPreferenceClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        loginBtn = findViewById(R.id.loginBtn);
        name_ET = findViewById(R.id.name_ET);
        email_ET = findViewById(R.id.email_ET);
        password_ET = findViewById(R.id.password_ET);
        progressBar = findViewById(R.id.progress_bar);
        registerBtn = findViewById(R.id.registerBtn);
        username_ET=findViewById(R.id.username_ET);
        utilService = new UtilService();

        sharedPreferenceClass = new SharedPreferenceClass(this);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utilService.hideKeyboard(view, SignUp.this);
                name = name_ET.getText().toString();
                email = email_ET.getText().toString();
                password = password_ET.getText().toString();
                username=username_ET.getText().toString();

                if(validate(view)) {
                    registerUser(view);
                }
            }
        });
    }

    private void registerUser(View view) {
        progressBar.setVisibility(View.VISIBLE);

        final HashMap<String, String> params = new HashMap<>();
        params.put("username", name);
        params.put("email", email);
        params.put("password", password);
        params.put("SharedId",username);

        String apiKey = "https://biolink-app.herokuapp.com/api/signup/";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                apiKey, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                if(response.has("User"))
                {

                    Toast.makeText(SignUp.this,"Success", Toast.LENGTH_SHORT).show();
                    Log.d("USER DATA",response.toString());
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(SignUp.this, Login.class));

                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse response = error.networkResponse;
                if(error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers,  "utf-8"));

                        JSONObject obj = new JSONObject(res);
                        Toast.makeText(SignUp.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    } catch (JSONException | UnsupportedEncodingException je) {
                        final int code=response.statusCode;
                        if(code==400)
                        {
                            Toast.makeText(SignUp.this, "Unable to Signup", Toast.LENGTH_SHORT).show();
                        }
                        je.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return params;
            }
        };

        // set retry policy
        int socketTime = 3000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTime,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        // request add
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }


    public boolean validate(View view) {
        boolean isValid;

        if(!TextUtils.isEmpty(name)) {
            if(!TextUtils.isEmpty(email)) {
                if(!TextUtils.isEmpty(password)) {
                    isValid = true;
                } else {
                    utilService.showSnackBar(view,"please enter password....");
                    isValid = false;
                }
            } else {
                utilService.showSnackBar(view,"please enter email....");
                isValid = false;
            }
        } else {
            utilService.showSnackBar(view,"please enter name....");
            isValid = false;
        }

        return  isValid;
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences todo_pref = getSharedPreferences("user_todo", MODE_PRIVATE);
        if(todo_pref.contains("token")) {
            startActivity(new Intent(SignUp.this, MainActivity.class));
            finish();
        }
    }
}