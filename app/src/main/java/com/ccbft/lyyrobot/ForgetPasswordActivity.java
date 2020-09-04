package com.ccbft.lyyrobot;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ccbft.lyyrobot.domain.User;
import com.ccbft.lyyrobot.util.ViewUtils;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener {
    private EditText phoneEt;
    private TextView questionTv;
    private EditText answerEt;
    private Button resetPasswordBtn;
    private TextView loginTv;
    private static final String TAG = "ForgetPasswordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        initView();
        phoneEt.addTextChangedListener(new HideTextWatcher(phoneEt));
        resetPasswordBtn.setOnClickListener(this);
        loginTv.setOnClickListener(this);
    }

    private void initView() {
        phoneEt=findViewById(R.id.phoneEt);
        questionTv=findViewById(R.id.questionTv);
        answerEt=findViewById(R.id.answerEt);
        resetPasswordBtn=findViewById(R.id.resetPasswordBtn);
        loginTv=findViewById(R.id.loginTv);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.resetPasswordBtn:
                String phoneNum = phoneEt.getText().toString().trim();
                if(TextUtils.isEmpty(phoneNum)){
                    Snackbar.make(v,"手机号不能为空",Snackbar.LENGTH_LONG).show();
                    return;
                }
                BmobQuery<User> bmobQuery=new BmobQuery<>();
                bmobQuery.addWhereEqualTo("secretAnswer",answerEt.getText().toString().trim());

                BmobQuery<User> bmobQuery2=new BmobQuery<>();
                bmobQuery2.addWhereEqualTo("mobilePhoneNumber",phoneNum);

                List<BmobQuery<User>> list=new ArrayList<>();

                list.add(bmobQuery);
                list.add(bmobQuery2);

                BmobQuery<User> mainQuery=new BmobQuery<>();
                mainQuery.and(list);
                mainQuery.findObjects(new FindListener<User>() {
                    @Override
                    public void done(List<User> list, BmobException e) {
                        if(e==null){
                            if(list.size()==1){
                                User user = list.get(0);
                                String viewedPassword = user.getViewedPassword();
                                String username = user.getUsername();
                                Snackbar.make(v,"您的用户名是："+username+"\n您的密码是："+viewedPassword,Snackbar.LENGTH_LONG).show();
                            }else{
                                Snackbar.make(v,"答案错误！",Snackbar.LENGTH_LONG).show();
                            }

                        }else {
                            Toast.makeText(ForgetPasswordActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                break;
            case R.id.loginTv:
                startActivity(new Intent(ForgetPasswordActivity.this,LoginActivity.class));
                break;
            default:
        }
    }

    private class HideTextWatcher implements TextWatcher {
        private EditText mView;
        private int mMaxLength;
        private CharSequence mStr;

        public HideTextWatcher(EditText v) {
            super();
            mView = v;
            mMaxLength = ViewUtils.getMaxLength(v);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mStr = s;
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mStr.length() == 11 && mMaxLength == 11) {
                BmobQuery<User> bmobQuery=new BmobQuery<>();
                bmobQuery.addWhereEqualTo("mobilePhoneNumber",phoneEt.getText().toString());
                bmobQuery.findObjects(new FindListener<User>() {
                    @Override
                    public void done(List<User> list, BmobException e) {
                        if(e==null){
                            int size = list.size();
                            Log.e(TAG, "大小是： "+size);
                            if(size==1){
                                User user = list.get(0);
                                String securityQuestion = user.getSecurityQuestion();
                                Log.e(TAG, "done: "+securityQuestion);
                                questionTv.setText(securityQuestion);
                            }else {
                                Toast.makeText(ForgetPasswordActivity.this, "该手机号没有对应的用户！", Toast.LENGTH_SHORT).show();
                            }

                        }else {
                            Toast.makeText(MyApplication.getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }
}
