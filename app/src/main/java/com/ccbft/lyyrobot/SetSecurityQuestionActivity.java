package com.ccbft.lyyrobot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.ccbft.lyyrobot.domain.User;
import com.ccbft.lyyrobot.util.ViewUtils;
import com.google.android.material.snackbar.Snackbar;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class SetSecurityQuestionActivity extends BaseActivity {
    private EditText questionEt;
    private EditText answerEt;
    private Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_security_question);
        initView();
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = questionEt.getText().toString();
                String s1 = answerEt.getText().toString();
                User user=BmobUser.getCurrentUser(User.class);
                user.setSecurityQuestion(s);
                user.setSecretAnswer(s1);
                user.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            ViewUtils.hideAllInputMethod(SetSecurityQuestionActivity.this);
                            Snackbar.make(v,"设置成功！",Snackbar.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(SetSecurityQuestionActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void initView() {
        questionEt=findViewById(R.id.questionEt);
        answerEt=findViewById(R.id.answerEt);
        saveBtn=findViewById(R.id.saveBtn);
        Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("设置密保问题");
        toolbar.setNavigationIcon(R.drawable.back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SetSecurityQuestionActivity.this,SettingActivity.class));
            }
        });
        User currentUser = BmobUser.getCurrentUser(User.class);
        String securityQuestion = currentUser.getSecurityQuestion();
        String secretAnswer = currentUser.getSecretAnswer();
        questionEt.setText(securityQuestion);
        answerEt.setText(secretAnswer);

    }
}
