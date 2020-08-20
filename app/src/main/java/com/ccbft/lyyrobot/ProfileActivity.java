package com.ccbft.lyyrobot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ccbft.lyyrobot.domain.User;

import cn.bmob.v3.BmobUser;

public class ProfileActivity extends AppCompatActivity {
    private TextView usernameTv,nicknameTv,ageTv,phoneNum,emailTv;
    private Button editBtn;
    private Toolbar toolbar;
    private static final String TAG = "ProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initView();
        newThread();
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this,EditProfileActivity.class);
                User user=new User();
                user.setUsername(usernameTv.getText().toString());
                user.setNickname(nicknameTv.getText().toString());
                try {
                    user.setAge(Integer.parseInt(ageTv.getText().toString()));
                } catch (NumberFormatException e) {
                    Log.d(TAG, e.getMessage());
                }
                user.setMobilePhoneNumber(phoneNum.getText().toString());
                user.setEmail(emailTv.getText().toString());
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });

    }

    private void newThread(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                User currentUser = BmobUser.getCurrentUser(User.class);
                usernameTv.setText(currentUser.getUsername());
                nicknameTv.setText(currentUser.getNickname());
                ageTv.setText(""+currentUser.getAge());//注意参数的类型，否则会出现难以调试的闪退错误
                phoneNum.setText(currentUser.getMobilePhoneNumber());
                emailTv.setText(currentUser.getEmail());

            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        newThread();
    }

    private void initView() {
        usernameTv=findViewById(R.id.usernameTv);
        nicknameTv=findViewById(R.id.nicknameTv);
        ageTv=findViewById(R.id.ageTv);
        phoneNum=findViewById(R.id.phoneNum);
        emailTv=findViewById(R.id.emailTv);
        editBtn=findViewById(R.id.editBtn);
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("个人资料");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,MainActivity.class));
            }
        });
    }


}
