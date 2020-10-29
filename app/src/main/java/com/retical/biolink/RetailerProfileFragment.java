package com.retical.biolink;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RetailerProfileFragment extends Fragment {
TextView mWhatsApp,mTelegram,mPhone,mEmail,bio,help;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_retailer_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mWhatsApp=view.findViewById(R.id.whatsApp);
        mPhone=view.findViewById(R.id.PhoneLink);
        mEmail=view.findViewById(R.id.EmailLink);
        bio=view.findViewById(R.id.what);
        help=view.findViewById(R.id.Help);
        bio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),Webpage.class);
                String  url="https://biolink-app.herokuapp.com/";
                intent.putExtra("url",url);
                startActivity(intent);
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),Webpage.class);
                String  url="https://biolink-app.herokuapp.com/";
                intent.putExtra("url",url);
                startActivity(intent);
            }
        });
        mTelegram=view.findViewById(R.id.TelegramLink);


        mWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),WhatsApp.class);
                startActivity(intent);
            }
        });
        mTelegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),Telegram.class);
                startActivity(intent);
            }
        });
        mPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),Phone.class);
                startActivity(intent);
            }
        });
        mEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),Email.class);
                startActivity(intent);
            }
        });

    }
}