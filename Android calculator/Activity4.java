package com.example.a111.mycontrol;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import com.example.a111.util.Http; // 自动导入或手动添加
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.a111.R;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import com.example.a111.util.Rc4Util; // 导入RC4工具类
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Activity4 extends AppCompatActivity {

    // 验证配置参数（需与你的云验证平台一致）
    String WEIURL = "https://daocaorenn.xyz"; // 云验证地址
    String WEIAID = "first"; // 应用ID
    String WEIKEY = "xxxx"; // 应用key
    String RC4KEY = "z998b25de15ff880143b7c2f1xxxxx"; // RC4密钥
    String DLCODE = "x"; // 登录状态码
    String EDITION = "x"; // 当前版本号

    // 控件
    EditText e001; // 输入卡密的输入框
    Button button; // 验证按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity4); // 关联你的布局

        // 初始化控件
        e001 = findViewById(R.id.e001);
        button = findViewById(R.id.button);

        // 初始化验证相关功能
        Notice(); // 显示公告
        update(); // 检测更新

        // 绑定按钮点击事件（触发验证）
        button.setOnClickListener(v -> {
            try {
                login(); // 调用验证方法
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Toast.makeText(Activity4.this, "验证失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 显示公告（复用MainActivity的逻辑）
    public void Notice() {
        Http.get(WEIURL + "/api/?id=notice&app=" + WEIAID, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(() -> Toast.makeText(Activity4.this, "公告加载失败", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String WEINotice = Rc4Util.decryRC4(Objects.requireNonNull(response.body()).string(), RC4KEY, "UTF-8");
                try {
                    JSONObject jsonObject = new JSONObject(WEINotice);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("msg");
                    JSONObject json = new JSONObject(msg);
                    String app_gg = json.getString("app_gg");  // 公告内容
                    if(code.equals("200")){
                        if (!app_gg.isEmpty()) {
                            runOnUiThread(() -> new AlertDialog.Builder(Activity4.this)
                                    .setTitle("公告")
                                    .setMessage(app_gg)
                                    .setPositiveButton("朕已阅", null)
                                    .show());
                        }else{

                            runOnUiThread(() -> Toast.makeText(Activity4.this, "暂无公告", Toast.LENGTH_LONG).show());
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(Activity4.this, "公告解析失败", Toast.LENGTH_SHORT).show());
                    //   runOnUiThread(() -> Toast.makeText(Activity4.this, , Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    // 检测更新（复用MainActivity的逻辑）
    public void update() {
        Http.get(WEIURL + "/api/?id=ini&app=" + WEIAID, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(() -> Toast.makeText(Activity4.this, "更新检测失败", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String WEIINI = Rc4Util.decryRC4(Objects.requireNonNull(response.body()).string(), RC4KEY, "UTF-8");
                try {
                    JSONObject jsonObject = new JSONObject(WEIINI);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("msg");

                    if (code.equals("200")) {
                        JSONObject json = new JSONObject(msg);
                        String version = json.getString("version");
                        String app_update_show = json.getString("app_update_show");
                        String app_update_url = json.getString("app_update_url");
                        String app_update_must = json.getString("app_update_must");

                        runOnUiThread(() -> {
                            if (!version.equals(EDITION)) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Activity4.this)
                                        .setTitle("发现新版本")
                                        .setMessage(app_update_show)
                                        .setPositiveButton("更新", (dialog, which) -> {
                                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(app_update_url));
                                            startActivity(intent);
                                        });

                                if (app_update_must.equals("y")) {
                                    builder.setCancelable(false); // 强制更新
                                }

                                builder.show();
                            } else {
                                // 修复：移除多余的 runOnUiThread
                                new AlertDialog.Builder(Activity4.this)
                                        .setTitle("更新")
                                        .setMessage("已是最新版本")
                                        .setPositiveButton("确定", null)
                                        .setNeutralButton("查看更新日志", (dialog, which) -> {
                                            // 构建更新日志内容
                                            String updateLog = "";
                                            try {
                                                // 假设 app_update_log 是 JSON 格式
                                                JSONObject jsonLog = new JSONObject(app_update_show);
                                              //  updateLog = jsonLog.getString("content");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                                // 解析失败时使用默认内容
                                                updateLog = app_update_show;

                                            }
                                            new AlertDialog.Builder(Activity4.this)
                                                    .setTitle("版本更新日志")
                                                    .setMessage(app_update_show) // 修复：设置日志内容
                                                    .setPositiveButton("关闭", null)
                                                    .show();
                                        })
                                        .show(); // 修复：添加缺失的 show()
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(Activity4.this, "更新解析失败", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    // 卡密验证核心方法（适配当前布局的输入框）
    public void login() throws UnsupportedEncodingException {
        String kami = e001.getText().toString().trim(); // 从e001获取卡密
        if (kami.isEmpty()) {
            Toast.makeText(this, "请输入卡密", Toast.LENGTH_SHORT).show();
            return;
        }

        // 构建验证参数（复用MainActivity的签名逻辑）
        String ANDROID_ID = Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID); // 设备码
        long TIME = new Date().getTime(); // 时间戳
        double VALUE = 1 + Math.random() * (10 - 1 + 1) + TIME; // 随机数+时间戳
        String SIGN = stringToMD5("kami=" + kami + "&markcode=" + ANDROID_ID + "&t=" + TIME + "&" + WEIKEY); // 签名
        String DATA = "data=" + Rc4Util.encryRC4String("kami=" + kami + "&markcode=" + ANDROID_ID + "&t=" + TIME + "&sign=" + SIGN + "&value=" + VALUE, RC4KEY, "UTF-8");

        // 发送验证请求
        Http.get(WEIURL + "/api/?id=kmlogon&app=" + WEIAID + "&" + DATA, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(() -> Toast.makeText(Activity4.this, "网络异常", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String WEIDATA = Rc4Util.decryRC4(Objects.requireNonNull(response.body()).string(), RC4KEY, "UTF-8");
                try {
                    JSONObject jsonObject = new JSONObject(WEIDATA);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("msg");
                    String check = jsonObject.getString("check");
                    String time = jsonObject.getString("time");

                    runOnUiThread(() -> {
                        if (code.equals(DLCODE)) { // 验证成功
                            try {
                                JSONObject json = new JSONObject(msg);
                                String vip = json.getString("vip"); // 到期时间
                                if (check.equals(stringToMD5(time + WEIKEY + VALUE))) {
                                    // 验证通过，可跳转其他页面或执行后续操作
                                    Toast.makeText(Activity4.this, "验证成功", Toast.LENGTH_SHORT).show();
                                   // Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.bilibili.com/bangumi/play/ep831714?bsource=bing_ogv"));
                                   // startActivity(intent);
                                     Intent intent = new Intent(Activity4.this, com.example.a111.mycontrol.Activity5.class);
                                     startActivity(intent);
                                } else {
                                    Toast.makeText(Activity4.this, "验证数据异常", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(Activity4.this, "验证解析失败", Toast.LENGTH_SHORT).show();
                            }
                        } else { // 验证失败（如卡密无效）
                            //Toast.makeText(Activity4.this, msg, Toast.LENGTH_SHORT).show();
                            Toast.makeText(Activity4.this, "密码错误", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(Activity4.this, "验证失败", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    // MD5加密工具（复用）
    public static String stringToMD5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }
}