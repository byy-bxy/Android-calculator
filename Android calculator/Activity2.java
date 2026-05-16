package com.example.a111.mycontrol;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a111.R;

public class Activity2 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity2);
        Intent intent=this.getIntent();
        String ss1=intent.getStringExtra("ee1");
        String ss2=intent.getStringExtra("ee2");
        double i=Double.parseDouble(ss1);
        double i1=Double.parseDouble(ss2);
        double sum=i+i1;
        TextView textView=findViewById(R.id.t1);
        textView.setText("                                                 计算结果是"+sum);

    }
}
