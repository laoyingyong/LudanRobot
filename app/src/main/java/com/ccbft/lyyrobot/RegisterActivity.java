package com.ccbft.lyyrobot;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ccbft.lyyrobot.domain.RegisterCheck;
import com.ccbft.lyyrobot.domain.User;
import com.ccbft.lyyrobot.util.RegisterUtils;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private EditText accountEt,passwordET,passwordEt2,verificationCodeEt;
    private Button registerBtn;
    private TextView loginTv;
    private static final String TAG = "RegisterActivity";

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
                register(v);
                break;
            case R.id.loginTv:
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                break;
            default:
        }
    }

    private void register(View v) {
        String account = accountEt.getText().toString().trim();
        String password = passwordET.getText().toString().trim();
        String password2 = passwordEt2.getText().toString().trim();

        if(!password.equals(password2)){
            Snackbar.make(v,"注册失败，两次输入的密码不一致！",Snackbar.LENGTH_LONG).show();
            return;
        }
        if(!VerificationCodeView.verificationCode.equalsIgnoreCase(verificationCodeEt.getText().toString().trim())){
            Snackbar.make(v,"注册失败，验证码输入错误！",Snackbar.LENGTH_LONG).show();
            return;
        }

        BmobQuery<RegisterCheck> query1=new BmobQuery<>();
        query1.addWhereEqualTo("ip",RegisterUtils.getIpAddress());
        BmobQuery<RegisterCheck> query2=new BmobQuery<>();
        query2.addWhereEqualTo("mac",RegisterUtils.getMachineHardwareAddress());
        long yesterday=System.currentTimeMillis()-1000*60*60*24;//昨天的这个时刻
        Date yerterdayTime=new Date(yesterday);
        BmobDate bmobCreatedAtDate = new BmobDate(yerterdayTime);
        BmobQuery<RegisterCheck> query3=new BmobQuery<>();
        query3.addWhereGreaterThan("registerTime",bmobCreatedAtDate);//大于昨天的这个时刻
        List<BmobQuery<RegisterCheck>> list=new ArrayList<>();
        list.add(query1);
        list.add(query2);
        list.add(query3);
        BmobQuery<RegisterCheck> mainQuery=new BmobQuery<>();
        mainQuery.and(list);
        mainQuery.findObjects(new FindListener<RegisterCheck>() {
            @Override
            public void done(List<RegisterCheck> list, BmobException e) {
                if(e==null){
                    if(list.size()!=0){
                        Snackbar.make(v,"请不要频繁注册！",Snackbar.LENGTH_LONG).show();
                    }else {//24小时之内，查找不到相同的ip地址和mac地址，允许注册。

                        User user=new User();
                        user.setUsername(account);
                        user.setPassword(password);

                        Random random=new Random();
                        StringBuilder stringBuilder=new StringBuilder("1");
                        for (int i = 0; i < 10; i++) {
                            int n = random.nextInt(10);
                            stringBuilder.append(""+n);
                        }
                        user.setMobilePhoneNumber(stringBuilder.toString().trim());
                        user.setAge(18);
                        user.setNickname("新用户"+System.currentTimeMillis());
                        user.signUp(new SaveListener<User>() {
                            @Override
                            public void done(User user, BmobException e) {
                                if(e==null){
                                    //将用户注册时的ip地址和mac地址保存下来
                                    BmobDate bmobDate=new BmobDate(new Date());
                                    RegisterCheck registerCheck=new RegisterCheck(RegisterUtils.getIpAddress(),RegisterUtils.getMachineHardwareAddress(),bmobDate);
                                    registerCheck.save(new SaveListener<String>() {
                                        @Override
                                        public void done(String s, BmobException e) {
                                            if(e==null){
                                            }else {
                                                Log.e(TAG, "done: "+e.getMessage());
                                            }
                                        }
                                    });
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
                                }else{
                                    String msg=e.getMessage();
                                    Snackbar.make(v,"注册失败!"+msg,Snackbar.LENGTH_LONG).show();
                                }

                            }
                        });

                    }
                }else{
                    Snackbar.make(v,e.getMessage(),Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }//register()   end~

}
