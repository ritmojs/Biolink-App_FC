package com.retical.biolink;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RetailerBioFragment extends Fragment {
    TextView mEmail,mUsername,mlogout,mName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_retailer_bio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEmail=view.findViewById(R.id.what);
        mUsername=view.findViewById(R.id.Help);
        mlogout=view.findViewById(R.id.TelegramLink);
        mName=view.findViewById(R.id.name);
        final SharedPreferences mPref = getActivity().getSharedPreferences("user_todo", getActivity().MODE_PRIVATE);
        mName.setText(mPref.getString("name",""));
        mEmail.setText(mPref.getString("email",""));
        mUsername.setText(mPref.getString("SharedId",""));
        mlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mPref.edit();
                Intent intent=new Intent(getActivity(),Login.class);
                startActivity(intent);
                editor.clear();
                editor.apply();
            }
        });



    }
}