package com.example.a111.mycontrol;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a111.R;

/*
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
*/
public class Activity3 extends AppCompatActivity {

    EditText e1,e2;
    String s1,s2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity3);
        AlertDialog.Builder b=new AlertDialog.Builder(Activity3.this);
        e1=findViewById(R.id.e1);
        e2=findViewById(R.id.e2);
        Button button=findViewById(R.id.b1);
        Intent intent=new Intent(this, Activity2.class);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s1=e1.getText().toString();

                s2=e2.getText().toString();
                if(s1.isEmpty()||s2.isEmpty()){
                    return;
                }
                Log.e("button",s1);
                intent.putExtra("ee1",s1);
                intent.putExtra("ee2",s2);
                startActivity(intent);
            }
        });
    }
}