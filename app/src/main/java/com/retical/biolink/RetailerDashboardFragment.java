package com.retical.biolink;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class RetailerDashboardFragment extends Fragment {

    TextView mAdd,mNoLink,mADD,mName,mShare;
    ImageView mLink,mView,mpreview;
    ProgressBar progressBar;
    SharedPreferences mPref;
    String Id,token,SharedId,name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_retailer_dashboard, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPref=this.getActivity().getSharedPreferences("user_todo", Context.MODE_PRIVATE);
        Id=mPref.getString("id","");
        SharedId=mPref.getString("SharedId","");
        token=mPref.getString("token","");
        name=mPref.getString("name","");
        mAdd = view.findViewById(R.id.AddBio);
        mAdd.setText(name);
        mADD=view.findViewById(R.id.AddLink);
        mADD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),AddLink.class);
                startActivity(intent);
            }
        });
        mpreview=view.findViewById(R.id.preview);
        mShare=view.findViewById(R.id.Share);
        mLink = view.findViewById(R.id.link);
        mView=view.findViewById(R.id.views);
        mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT,"Hello Friend! I am Sharing my Contact details. You Can Contact me on:"+"https://biolink-app.herokuapp.com/"+SharedId);
                startActivity(i.createChooser(i, "Share using:"));

            }
        });

        mpreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),Webpage.class);
               String  url="https://biolink-app.herokuapp.com/"+SharedId+"/";
                intent.putExtra("url",url);
                startActivity(intent);
            }
        });

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewDialog();
            }
            private void ViewDialog() {
                View view = getLayoutInflater().inflate(R.layout.views, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setView(view).show();
                progressBar=view.findViewById(R.id.prog);
                mNoLink=view.findViewById(R.id.Nolink);
                //Start Fetching From NODE JS SERVER
                checkViews(view);
            }
        });

        mLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ALERT DIALOG CREATED
                checkDialog();
            }

            private void checkDialog() {
                View view = getLayoutInflater().inflate(R.layout.check_link, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setView(view).show();
                progressBar=view.findViewById(R.id.prog);
                mNoLink=view.findViewById(R.id.Nolink);
                //Start Fetching From NODE JS SERVER
                 checkLink(view);
            }
        });
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddLink.class);
                startActivity(intent);
            }
        });


    }

    private void checkViews(View view) {
        progressBar.setVisibility(View.VISIBLE);
        String apiKey = "https://biolink-app.herokuapp.com/api/user/biolink/dashboard/"+Id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                apiKey,new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("DATA:",response.toString());
                if(response.has("role"))
                {

                    String role= null;
                    try {
                         role = response.getString("role");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mNoLink.setVisibility(View.VISIBLE);
                    mNoLink.setText(role);
                    progressBar.setVisibility(View.GONE);

                }
                else {
                    Toast.makeText(getActivity(), "Not Success", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getContext(), obj.getString("msg"), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    } catch (JSONException | UnsupportedEncodingException je) {
                        je.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer "+token);
                return headers;
            }
        };
        // set retry policy
        int socketTime = 3000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTime,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        // request add
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonObjectRequest);
    }

//FUNCTION TO FETCH DATA FROM NODE BACKEND FOR NO OF LINKS
    private void checkLink(View view) {
        progressBar.setVisibility(View.VISIBLE);
        String apiKey = "https://biolink-app.herokuapp.com/api/user/biolink/dashboard/"+Id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                apiKey,new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("DATA:",response.toString());
                if(response.has("Links"))
                {
                    try {
                        JSONArray link=response.getJSONArray("Links");
                        mNoLink.setVisibility(View.VISIBLE);
                        mNoLink.setText(link.length()+"");
                        progressBar.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }
                else {
                    Toast.makeText(getActivity(), "Not Success", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getContext(), obj.getString("msg"), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    } catch (JSONException | UnsupportedEncodingException je) {
                        je.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer "+token);
                return headers;
            }
        };
        // set retry policy
        int socketTime = 3000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTime,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        // request add
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonObjectRequest);
    }
}