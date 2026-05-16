package com.example.a111.mycontrol;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/*
带有边框的标签
 */
public class MyTextView extends androidx.appcompat.widget.AppCompatTextView {
    Paint paint=new Paint();
    public MyTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.BLACK);  //设置对象颜色
        /*
        canvas.drawLine(0,4,this.getWidth(),4,paint); //上方
        canvas.drawLine(0,this.getHeight()+2,this.getWidth()+2,this.getHeight(),paint);
        canvas.drawLine(-2,-4,this.getHeight(),-4,paint);
        canvas.drawLine(this.getWidth()-2,3,this.getWidth()-2,this.getHeight(),paint);
         */
    }
}
