package com.retical.biolink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class EditLink extends AppCompatActivity {
    EditText mLink;
    TextView mtext;
    Button msave;
    String id,text,Link,token,SharedId;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_link);
        SharedPreferences mPref=getSharedPreferences("user_todo",MODE_PRIVATE);
        id=mPref.getString("id","");
        token=mPref.getString("token","");
        SharedId=mPref.getString("SharedId","");
        progressBar=findViewById(R.id.progress_add);
        mtext=findViewById(R.id.text_input);
        mLink=findViewById(R.id.text_link);
        msave=findViewById(R.id.update);
        mtext.setText(SharedId);


        msave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Link=mLink.getText().toString();
                if(Link !="")
                {
                    addLink(view);
                }
                else
                {
                    mLink.setError("Required Field");
                }
            }
        });





    }

    private void addLink(View view) {
        progressBar.setVisibility(View.VISIBLE);

        final HashMap<String, String> params = new HashMap<>();
        params.put("SharedId", Link);

        String apiKey = "https://biolink-app.herokuapp.com/api/"+id+"/update/SharedId/";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT,
                apiKey, new JSONObject(params), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("DATA:",response.toString());

                if(response.has("Links"))
                {
                    mtext.setText("");
                    mLink.setText("");
                    Toast.makeText(EditLink.this, "Update Successful", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
                else {
                    Toast.makeText(EditLink.this, "Update Failed", Toast.LENGTH_SHORT).show();
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

                        progressBar.setVisibility(View.GONE);
                    } catch (JSONException | UnsupportedEncodingException je) {
                        je.printStackTrace();
                        final int code=response.statusCode;
                        Toast.makeText(EditLink.this, code+"", Toast.LENGTH_SHORT).show();

                        progressBar.setVisibility(View.GONE);
                    }
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization","Bearer "+ token);

                return headers;
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
}