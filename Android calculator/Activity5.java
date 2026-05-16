package com.example.a111.mycontrol;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.a111.databinding.Activity5Binding; // 导入绑定类

public class Activity5 extends AppCompatActivity {
    private Activity5Binding binding; // 声明绑定对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // 初始化绑定类并设置布局
        binding = Activity5Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 通过 binding 对象访问控件并设置点击事件
        binding.kaiqi.setOnClickListener(v -> {
            Toast.makeText(Activity5.this, "恭喜你", Toast.LENGTH_SHORT).show();
            StartGame.showFloatWindow(Activity5.this);
        });
        /*
        binding.群聊.setOnClickListener(v -> {
            // 处理群聊按钮点击逻辑
            Toast.makeText(Activity5.this, "群聊功能", Toast.LENGTH_SHORT).show();
        });*/
    }
}