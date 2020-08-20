package com.ccbft.lyyrobot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VerificationCodeView extends View {
    public static String verificationCode="";//验证码
    private List<Line> lineList=new ArrayList<>();//用来装放干扰线的集合

    public VerificationCodeView(Context context) {
        super(context);
    }

    public VerificationCodeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VerificationCodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public VerificationCodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint=new Paint();
        paint.setColor(Color.parseColor("#FF9999"));
        canvas.drawRect(0,0,300,130,paint);
        String str="0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random=new Random();
        StringBuilder sb=new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int index = random.nextInt(str.length());
            String s=""+str.charAt(index);
            sb.append(s);
        }
        if (TextUtils.isEmpty(verificationCode)){//如果验证码没有被赋过值
            verificationCode=sb.toString();
        }
        paint.setColor(Color.parseColor("#FFFF66"));
        paint.setTextSize(80);
        canvas.drawText(verificationCode,40,80,paint);//画验证码

        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(5);

        if(lineList.size()==0){//如果线的集合没有被重新赋值
            for (int i = 0; i < 5; i++) {
                int point1X=random.nextInt(300);
                int point1Y=random.nextInt(300);
                int point2X=random.nextInt(130);
                int point2Y=random.nextInt(130);
                lineList.add(new Line(point1X,point1Y,point2X,point2Y));
                canvas.drawLine(point1X,point1Y,point2X,point2Y,paint);//画干扰线
            }
        }else {
            for (int i = 0; i < 5; i++) {
                Line line = lineList.get(i);
                int point1X = line.getPoint1X();
                int point1Y = line.getPoint1Y();
                int point2X = line.getPoint2X();
                int point2Y = line.getPoint2Y();
                canvas.drawLine(point1X,point1Y,point2X,point2Y,paint);//画干扰线
            }
        }


    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {//保存信息
        Bundle bundle = new Bundle();
        bundle.putString("verificationCode", verificationCode);
        for (int i = 0; i < 5; i++) {
            Line line = lineList.get(i);
            bundle.putInt("startPoitnX"+i,line.getPoint1X());
            bundle.putInt("startPoitnY"+i,line.getPoint1Y());
            bundle.putInt("endPoitnX"+i,line.getPoint2X());
            bundle.putInt("endPoitnY"+i,line.getPoint2Y());
        }
        bundle.putParcelable("instance",super.onSaveInstanceState());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {//恢复信息
        if(state instanceof Bundle){
            Bundle bundle=(Bundle)state;
            Parcelable parcelable = bundle.getParcelable("instance");
            super.onRestoreInstanceState(parcelable);
            verificationCode=bundle.getString("verificationCode");

            List<Line> list=new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                int startPoitnX = bundle.getInt("startPoitnX" + i);
                int startPoitnY = bundle.getInt("startPoitnY" + i);
                int endPoitnX = bundle.getInt("endPoitnX" + i);
                int endPoitnY = bundle.getInt("endPoitnY" + i);
                list.add(new Line(startPoitnX,startPoitnY,endPoitnX,endPoitnY));
            }
            lineList=list;
            return;
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {//验证码的触摸事件，点击后可以更新验证码
        String str="0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random=new Random();
        StringBuilder sb=new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int index = random.nextInt(str.length());
            String s=""+str.charAt(index);
            sb.append(s);
        }
        verificationCode=sb.toString();
        lineList.clear();
        for (int i = 0; i < 5; i++) {
            int point1X=random.nextInt(300);
            int point1Y=random.nextInt(300);
            int point2X=random.nextInt(130);
            int point2Y=random.nextInt(130);
            lineList.add(new Line(point1X,point1Y,point2X,point2Y));
        }


        invalidate();//重新绘图
        return true;
    }
}

class Line{
    private int point1X;//起始点的X坐标
    private int point1Y;//起始点的Y坐标
    private int point2X;//终点的X坐标
    private int point2Y;//终点的Y坐标

    public Line() {
    }

    public Line(int point1X, int point1Y, int point2X, int point2Y) {
        this.point1X = point1X;
        this.point1Y = point1Y;
        this.point2X = point2X;
        this.point2Y = point2Y;
    }

    public int getPoint1X() {
        return point1X;
    }

    public void setPoint1X(int point1X) {
        this.point1X = point1X;
    }

    public int getPoint1Y() {
        return point1Y;
    }

    public void setPoint1Y(int point1Y) {
        this.point1Y = point1Y;
    }

    public int getPoint2X() {
        return point2X;
    }

    public void setPoint2X(int point2X) {
        this.point2X = point2X;
    }

    public int getPoint2Y() {
        return point2Y;
    }

    public void setPoint2Y(int point2Y) {
        this.point2Y = point2Y;
    }
}
