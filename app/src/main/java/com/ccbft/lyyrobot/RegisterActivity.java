package com.ccbft.lyyrobot;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ccbft.lyyrobot.domain.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private EditText accountEt,passwordET,passwordEt2,verificationCodeEt;
    private Button registerBtn;
    private TextView loginTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        registerBtn.setOnClickListener(this);
        loginTv.setOnClickListener(this);
    }

    private void initView() {
        ActionBar actionBar = getSupportActionBar();
        if ((actionBar!=null)){
            actionBar.hide();
        }
        accountEt=findViewById(R.id.accountEt);
        passwordET=findViewById(R.id.passwordEt);
        passwordEt2=findViewById(R.id.passwordEt2);
        registerBtn=findViewById(R.id.registerBtn);
        loginTv=findViewById(R.id.loginTv);
        verificationCodeEt=findViewById(R.id.verificationCodeEt);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerBtn:
                register();
                break;
            case R.id.loginTv:
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                break;
            default:
        }
    }

    private void register() {
        String account = accountEt.getText().toString().trim();
        String password = passwordET.getText().toString().trim();
        String password2 = passwordEt2.getText().toString().trim();
        MyApplication.currentTime=System.currentTimeMillis();
        if(MyApplication.currentTime-MyApplication.lastTime<3600000*24){//除非卸载重装，否则一天只能注册一次
            Toast.makeText(this, "请不要频繁恶意注册！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!password.equals(password2)){
            Toast.makeText(this, "注册失败，两次输入的密码不一致！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!VerificationCodeView.verificationCode.equalsIgnoreCase(verificationCodeEt.getText().toString().trim())){
            Toast.makeText(this, "注册失败，验证码输入错误！", Toast.LENGTH_SHORT).show();
            return;
        }
        User user=new User();
        user.setUsername(account);
        user.setPassword(password);
        user.setAge(18);
        user.setNickname("新用户"+System.currentTimeMillis());
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if(e==null){
                    AlertDialog.Builder builder=new AlertDialog.Builder(RegisterActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("注册成功！您想现在马上登陆吗？");
                    builder.setPositiveButton("马上登陆", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putString("u",account);
                            bundle.putString("p",password);
                            intent.putExtras(bundle);
                            setResult(Activity.RESULT_OK,intent);
                            finish();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.setCancelable(false);
                    builder.create().show();
                    MyApplication.lastTime=MyApplication.currentTime;//将注册成功时的那个时间点保存下来
                }else{
                    String msg=e.getMessage();
                    String result="";
                    if(msg.contains(" is null")){
                        result="\n用户名或密码不能为空！";
                    }else if(msg.contains("already taken")){
                        result="\n用户名"+"\""+account+"\""+"已经存在!";
                    }

                    Toast.makeText(RegisterActivity.this, "注册失败!"+result, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
