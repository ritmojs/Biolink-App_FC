package com.retical.biolink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class WhatsApp extends AppCompatActivity {
EditText email,code;
Button mbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_app);
        email=findViewById(R.id.text_link);
        mbtn=findViewById(R.id.Save);
        code=findViewById(R.id.text_input);
        mbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text="https://wa.me/"+code.getText().toString()+email.getText().toString();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", text);
                clipboard.setPrimaryClip(clip);
                email.setText("");
                code.setText("");
                Toast.makeText(WhatsApp.this, "Copied", Toast.LENGTH_SHORT).show();
            }
        });
    }
}