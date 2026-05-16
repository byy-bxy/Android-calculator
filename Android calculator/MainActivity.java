package com.example.a111;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.DialogInterface;

import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.a111.databinding.ActivityMainBinding;
import com.example.a111.mycontrol.MyTextView;

public class MainActivity extends AppCompatActivity {
    int num=0;
    private ActivityMainBinding binding;
    private MyViewModel myViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        myViewModel = new ViewModelProvider(this,new ViewModelProvider.NewInstanceFactory()).get(MyViewModel.class);
        myViewModel.getMainNum().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.myTextView.setText(myViewModel.getMainNum().getValue());
            if(myViewModel.sing2.equals("")){


                if(myViewModel.sing1.equals("")){
                    binding.textView.setText(myViewModel.getMainNum().getValue());
                }else{
                    binding.textView.setText(myViewModel.num[0] + myViewModel.sing1 + myViewModel.getMainNum().getValue());
                }
            }else {
                binding.textView.setText(myViewModel.num[0]+myViewModel.sing1+myViewModel.num[1]+myViewModel.sing2+myViewModel.getMainNum().getValue());
            }
                //binding.textView.setText(myViewModel.getMainNum().getValue());
            }
        });
        binding.button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myViewModel.setMainNum("0");
            }
        });
        binding.button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myViewModel.setMainNum("1");
            }
        });
        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myViewModel.setMainNum("2");
            }
        });
        binding.button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myViewModel.setMainNum("3");
            }
        });
        binding.button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myViewModel.setMainNum("4");
            }
        });
        binding.button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myViewModel.setMainNum("5");
            }
        });
        binding.button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myViewModel.setMainNum("6");
            }
        });
        binding.button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myViewModel.setMainNum("7");
            }
        });
        binding.button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myViewModel.setMainNum("8");
            }
        });
        binding.button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myViewModel.setMainNum("9");
            }
        });
        binding.button19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!myViewModel.havePoint){
                    myViewModel.getMainNum().setValue(myViewModel.getMainNum().getValue()+".");
                    myViewModel.havePoint=true;
                }
            }
        });
        binding.button12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myViewModel.sing1.equals("")){
                    myViewModel.sing1="+";
                    myViewModel.num[0] = myViewModel.getMainNum().getValue();
                    myViewModel.getMainNum().setValue("0");
                    myViewModel.havePoint=false;

                }else if(myViewModel.sing2.equals("")){
                    myViewModel.num[0] = myViewModel.mainNumWithNum_0_Tocal();
                    myViewModel.sing1="+";
                    myViewModel.getMainNum().setValue("0");
                    myViewModel.havePoint=false;
                }else{
                    myViewModel.getMainNum().setValue(myViewModel.mainNumWith_1_Tocal());
                    myViewModel.num[1]="";
                    myViewModel.sing2="";
                    myViewModel.num[0]=myViewModel.mainNumWithNum_0_Tocal();
                    myViewModel.sing1="+";
                    myViewModel.getMainNum().setValue("0");
                    myViewModel.havePoint=false;

                }
            }
        });
        binding.button11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myViewModel.sing1.equals("")){
                    myViewModel.sing1="*";
                    myViewModel.num[0] = myViewModel.getMainNum().getValue();
                    myViewModel.getMainNum().setValue("0");
                    myViewModel.havePoint=false;
                }else if(myViewModel.sing2.equals("")){
                    if(myViewModel.sing1.equals("*") || myViewModel.sing1.equals("/")){
                        myViewModel.num[0] = myViewModel.mainNumWithNum_0_Tocal();
                        myViewModel.sing1="*";
                        myViewModel.getMainNum().setValue("0");
                        myViewModel.havePoint=false;
                    }else{
                         myViewModel.num[1]=myViewModel.getMainNum().getValue();
                         myViewModel.sing2="*";
                         myViewModel.getMainNum().setValue("0");
                         myViewModel.havePoint=false;
                    }
                }else{
                    myViewModel.num[1] = myViewModel.mainNumWith_1_Tocal();
                    myViewModel.sing2="*";
                    myViewModel.getMainNum().setValue("0");
                    myViewModel.havePoint=false;
                }
            }
        });
        binding.button13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myViewModel.sing1.equals("")){
                    myViewModel.sing1="-";
                    myViewModel.num[0]=myViewModel.getMainNum().getValue();//将当前的主数值存放到num[0]当中
                    myViewModel.getMainNum().setValue("0");
                    myViewModel.havePoint=false;

                }else if(myViewModel.sing2.equals("")){
                    myViewModel.num[0] = myViewModel.mainNumWithNum_0_Tocal();
                    myViewModel.sing1="-";
                    myViewModel.getMainNum().setValue("0");
                    myViewModel.havePoint=false;

                }else{//如果是像 a+b*c 这种形式的式子
                    myViewModel.getMainNum().setValue(myViewModel.mainNumWith_1_Tocal());//计算 d=b*c
                    myViewModel.num[1]="";
                    myViewModel.sing2="";
                    myViewModel.num[0]=myViewModel.mainNumWithNum_0_Tocal();//计算a+d
                    myViewModel.sing1="-";
                    myViewModel.getMainNum().setValue("0");
                    myViewModel.havePoint=false;

                }
            }
        });
        binding.button17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myViewModel.sing1.equals("")){
                    myViewModel.sing1="/";
                    myViewModel.num[0]=myViewModel.getMainNum().getValue();//将当前的主数值存放到num[0]当中
                    myViewModel.getMainNum().setValue("0");
                    myViewModel.havePoint=false;

                }else if(myViewModel.sing2.equals("")){
                    if(myViewModel.sing1.equals("*") || myViewModel.sing1.equals("/")){//按照正常运算顺序进行计算
                        myViewModel.num[0] = myViewModel.mainNumWithNum_0_Tocal();
                        myViewModel.sing1="/";
                        myViewModel.getMainNum().setValue("0");
                        myViewModel.havePoint=false;

                    }else{
                        myViewModel.sing2="/";
                        myViewModel.num[1] = myViewModel.getMainNum().getValue();//将当前的主数值存放到num[1]当中
                        myViewModel.getMainNum().setValue("0");
                        myViewModel.havePoint=false;
                    }
                }else{
                    myViewModel.num[1] = myViewModel.mainNumWith_1_Tocal();
                    myViewModel.sing2="/";
                    myViewModel.getMainNum().setValue("0");
                    myViewModel.havePoint=false;

                }
            }
        });
        binding.button66.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myViewModel.sing2="";
                myViewModel.num[1]="";
                myViewModel.sing1="";
                myViewModel.num[0]="";
                myViewModel.getMainNum().setValue("0");
                myViewModel.havePoint=false;

            }
        });
        binding.button18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myViewModel.sing2.equals("")){
                    if(!myViewModel.sing1.equals("")){
                        myViewModel.getMainNum().setValue(myViewModel.mainNumWithNum_0_Tocal());
                        if(myViewModel.getMainNum().getValue().contains(".")){
                            myViewModel.havePoint=true;
                        }else{
                            myViewModel.havePoint=false;
                        }
                        myViewModel.num[0]="";
                        myViewModel.sing1="";
                    }
                }else{
                    myViewModel.getMainNum().setValue(myViewModel.mainNumWith_1_Tocal());
                    myViewModel.num[1]="";
                    myViewModel.sing2="";
                    myViewModel.getMainNum().setValue(myViewModel.mainNumWithNum_0_Tocal());;
                    if(myViewModel.getMainNum().getValue().contains(".")){
                        myViewModel.havePoint=true;
                    }else{
                        myViewModel.havePoint=false;
                    }
                    myViewModel.num[0]="";
                    myViewModel.sing1="";
                }

                binding.textView.setText(myViewModel.num[0] + myViewModel.sing1 + myViewModel.getMainNum().getValue());
            }
        });
        binding.button20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!myViewModel.getMainNum().getValue().equals("0")){
                    myViewModel.getMainNum().setValue(myViewModel.getMainNum().getValue().substring(0,myViewModel.getMainNum().getValue().length()-1));
                    if(myViewModel.getMainNum().getValue().equals(""))
                        myViewModel.getMainNum().setValue("0");
                }
            }
        });
        binding.button21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myViewModel.sing1.equals("")) {
                    // 场景1：无任何运算符（如输入"5"后点%）
                    myViewModel.sing1 = "%"; // 设为一级运算符（此时实际是二级，逻辑同*、/）
                    myViewModel.num[0] = myViewModel.getMainNum().getValue(); // 存储当前值到num[0]
                    myViewModel.getMainNum().setValue("0"); // 重置当前输入
                    myViewModel.havePoint = false; // 重置小数点状态
                } else if (myViewModel.sing2.equals("")) {
                    // 场景2：已有一级运算符，但无二级运算符
                    if (myViewModel.sing1.equals("*") || myViewModel.sing1.equals("/") || myViewModel.sing1.equals("%")) {
                        // 若一级运算符是二级运算（*、/、%），直接计算并更新num[0]
                        myViewModel.num[0] = myViewModel.mainNumWithNum_0_Tocal(); // 计算num[0] sign1 当前值
                        myViewModel.sing1 = "%"; // 更新一级运算符为%
                        myViewModel.getMainNum().setValue("0");
                        myViewModel.havePoint = false;
                    } else {
                        // 若一级运算符是加减（+、-），将%设为二级运算符，存储中间值到num[1]
                        myViewModel.sing2 = "%";
                        myViewModel.num[1] = myViewModel.getMainNum().getValue(); // 存储当前值到num[1]
                        myViewModel.getMainNum().setValue("0");
                        myViewModel.havePoint = false;
                    }
                } else {
                    // 场景3：已有二级运算符（如a+b*c后点%），先算当前二级运算，再更新
                    myViewModel.num[1] = myViewModel.mainNumWith_1_Tocal(); // 计算num[1] sign2 当前值
                    myViewModel.sing2 = "%"; // 更新二级运算符为%
                    myViewModel.getMainNum().setValue("0");
                    myViewModel.havePoint = false;
                }
            }
        });
        binding.button33.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder b=new AlertDialog.Builder(MainActivity.this);
                b.setTitle("提示");
                b.setMessage("确定切换老版本? \n(点击空白区域可取消)");
                b.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this,"已切换",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, com.example.a111.mycontrol.Activity3.class);
                        startActivity(intent); // 启动Activity3
                    }


                });
                b.show();
            }
        });
        binding.button32.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取当前显示的数值
                String currentValue = myViewModel.getMainNum().getValue();

                // 检查是否为有效数字
                if (currentValue.equals("0") || currentValue.isEmpty()) {
                    return; // 无需计算0的平方
                }

                // 调用 ViewModel 的平方计算方法
                String squaredValue = myViewModel.calculateSquare();

                // 更新显示
                myViewModel.getMainNum().setValue(squaredValue);

                myViewModel.sing1 = "";
                myViewModel.sing2 = "";
                myViewModel.num[0] = "";
                myViewModel.num[1] = "";
                myViewModel.havePoint = squaredValue.contains(".");
            }
        });
        binding.button55.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, com.example.a111.mycontrol.Activity4.class);
                startActivity(intent);

            }
        });

        }

    }
