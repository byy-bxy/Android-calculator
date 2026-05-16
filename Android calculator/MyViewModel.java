package com.example.a111;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyViewModel extends ViewModel {
    private MutableLiveData<String> mainNum;
    public String num []={"",""};
    public String sing1="",sing2="";
    public boolean havePoint=false;
    public MutableLiveData<String> getMainNum(){
        if(mainNum == null){
            mainNum = new MutableLiveData<>();
            mainNum.setValue("0");
        }
        return mainNum;
    }
    public void setMainNum(String n){
        if(mainNum.getValue().equals("0")){
            mainNum.setValue(n);
        }else {
            mainNum.setValue(mainNum.getValue()+n);
        }
    }
    public String mainNumWithNum_0_Tocal(){
        String value="0";
        if(mainNum.getValue().contains(".")){
            switch (sing1){
                case "+":
                    value=String.valueOf(Double.valueOf(num[0])+Double.valueOf(mainNum.getValue()));

                    break;
                case "-":
                    value=String.valueOf(Double.valueOf(num[0])-Double.valueOf(mainNum.getValue()));
                    break;
                case "*":
                    value=String.valueOf(Double.valueOf(num[0])*Double.valueOf(mainNum.getValue()));
                    break;
                case"/":
                    if(mainNum.getValue().equals("0")){
                        mainNum.setValue("再见");
                    }
                    value=String.valueOf(Double.valueOf(num[0])/Double.valueOf(mainNum.getValue()));
                    break;
            }
        }else{
            switch (sing1){
                case "+":
                    value=String.valueOf(Integer.valueOf(num[0])+Integer.valueOf(mainNum.getValue()));

                    break;
                case "-":
                    value=String.valueOf(Integer.valueOf(num[0])-Integer.valueOf(mainNum.getValue()));
                    break;
                case "*":
                    value=String.valueOf(Integer.valueOf(num[0])*Integer.valueOf(mainNum.getValue()));
                    break;
                case"/":
                    if(mainNum.getValue().equals("0")){
                        mainNum.setValue("再见");
                    }
                    value=String.valueOf(Double.valueOf(num[0])/Double.valueOf(mainNum.getValue()));
                    break;
            }
        }
        return value;
    }
    public String mainNumWith_1_Tocal() {
        String value = "0";
        if (mainNum.getValue().contains(".") || num[1].contains(".")) {
            switch (sing1) {
                case "*":
                    value = String.valueOf(Double.valueOf(num[1]) * Double.valueOf(mainNum.getValue()));
                    break;
                case "/":
                    if (mainNum.getValue().equals("0")) {
                        mainNum.setValue("再见");
                    }
                    value = String.valueOf(Double.valueOf(num[1]) / Double.valueOf(mainNum.getValue()));
                    break;
                case "%":
                    if (mainNum.getValue().equals("0")) {
                        mainNum.setValue("再见");
                        return "0"; // 模零错误时返回0
                    }
                    value = String.valueOf(Double.valueOf(num[1]) % Double.valueOf(mainNum.getValue())); // 取余计算
                    break;
            }
        } else {
            switch (sing2) {
                case "*":
                    value = String.valueOf(Integer.valueOf(num[1]) * Integer.valueOf(mainNum.getValue()));
                    break;
                case "/":
                    if (mainNum.getValue().equals("0")) {
                        mainNum.setValue("再见");
                        return "再见";
                    }
                    value = String.valueOf(Double.valueOf(num[1])/Double.valueOf(mainNum.getValue()));
                    break;
            }
        }
        if (value.contains(".") && value.endsWith("0")) {
            value = value.substring(0, value.indexOf("."));
        }
        return value;

    }
    public String calculateSquare() {
        String currentValue = mainNum.getValue();
        try {
            double num = Double.parseDouble(currentValue);
            double result = num * num;

            // 格式化结果：整数去小数点，小数保留精度
            if (result % 1 == 0) {
                return String.valueOf((long) result); // 整数去掉小数点
            } else {
                return String.valueOf(result);
            }
        } catch (NumberFormatException e) {
            return "错误";
        }
    }
}
