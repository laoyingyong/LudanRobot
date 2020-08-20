package com.ccbft.lyyrobot;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.ccbft.lyyrobot.domain.User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class EditProfileActivity extends BaseActivity implements View.OnClickListener {
    private EditText usernameEt,nicknameEt,ageEt,phonenumEt,emailEt;
    private Button saveBtn;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initView();
        saveBtn.setOnClickListener(this);
    }

    private void initView() {
        usernameEt=findViewById(R.id.usernameEt);
        nicknameEt=findViewById(R.id.nicknameEt);
        ageEt=findViewById(R.id.ageEt);
        phonenumEt=findViewById(R.id.phoneNumEt);
        emailEt=findViewById(R.id.emailEt);
        saveBtn=findViewById(R.id.saveBtn);

        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("编辑资料");
        toolbar.setNavigationIcon(R.drawable.back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this,ProfileActivity.class));
            }
        });

        Intent intent = getIntent();
        User user= (User) intent.getSerializableExtra("user");

        usernameEt.setText(user.getUsername());
        nicknameEt.setText(user.getNickname());
        ageEt.setText(""+user.getAge());
        phonenumEt.setText(user.getMobilePhoneNumber());
        emailEt.setText(user.getEmail());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.saveBtn:
                //String username = usernameEt.getText().toString().trim();
                String nickname=nicknameEt.getText().toString().trim();
                String a=ageEt.getText().toString().trim();
                Integer age=0;
                try {
                    age=Integer.parseInt(a);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                String phoneNum=phonenumEt.getText().toString().trim();
                String email=emailEt.getText().toString().trim();
                User currentUser = BmobUser.getCurrentUser(User.class);
                currentUser.setNickname(nickname);
                currentUser.setAge(age);
                currentUser.setMobilePhoneNumber(phoneNum);
                currentUser.setEmail(email);
                currentUser.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            Toast.makeText(EditProfileActivity.this, "更新资料成功！", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(EditProfileActivity.this, "更新失败！\n"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                break;
            default:
        }
    }
}
