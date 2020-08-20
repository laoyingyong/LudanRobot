package com.ccbft.lyyrobot;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ccbft.lyyrobot.domain.Msg;
import com.ccbft.lyyrobot.domain.User;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobUser;

public class MainActivity extends BaseActivity {
    //http://i.itpk.cn/api.php?question=%22%E6%97%A0%E8%AF%AD%22&api_key=fe6ed258c8faf18e6400bd7a9d401f16&api_secret=jwxa3c845wxb
    private List<Msg> msgList=new ArrayList<>();
    private RecyclerView recyclerView;
    private EditText editText;
    private Button button;
    private Toolbar toolbar;
    private MsgAdapter msgAdapter;
    private static final String TAG = "MainActivity";
    private SQLiteDatabase sqLiteDatabase;
    private LinearLayout linearLayout;
    public static final int [] imageArr={R.drawable.bg1,R.drawable.bg2,R.drawable.bg3,R.drawable.bg4,R.drawable.bg5};
    public static String apiKey="fe6ed258c8faf18e6400bd7a9d401f16";
    public static String apiSecrect="jwxa3c845wxb";
    SharedPreferences sharedPreferences;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MySQLiteOpenhelper mySQLiteOpenhelper = new MySQLiteOpenhelper(this, "robot.db", null, 1);
        sqLiteDatabase = mySQLiteOpenhelper.getReadableDatabase();
        msgList.add(new Msg("欢迎回来",Msg.TYPE_RECEIVED));
        recyclerView=findViewById(R.id.rv);
        editText=findViewById(R.id.et);
        button=findViewById(R.id.btn);
        linearLayout=findViewById(R.id.ll);
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("卤蛋机器人");
        setSupportActionBar(toolbar);

        setBg();
        setApiKeyAndSecret();

        setColor();
        recyclerView.setAdapter(msgAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = editText.getText().toString();
                User currentUser = BmobUser.getCurrentUser(User.class);
                String nickname = currentUser.getNickname();
                Msg mm = new Msg(s, Msg.TYPE_SENT, R.drawable.kai, getCurrentTime(), "我", 1);
                msgList.add(mm);
                String sql="insert into msg values(?,?,?,?,?,?)";
                int num=R.drawable.kai;
                sqLiteDatabase.execSQL(sql,new String [] {null,s,"1",""+num,getCurrentTime(),nickname});
                msgAdapter.notifyItemInserted(msgList.size()-1);
                recyclerView.scrollToPosition(msgList.size()-1);
                sendRequest(editText.getText().toString());
                editText.setText("");
                InputMethodManager inputMethodManager  =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if(inputMethodManager.isActive()){//如果软件盘已经打开则关闭之
                    inputMethodManager.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setApiKeyAndSecret() {
        String key = sharedPreferences.getString("apiKey", "fe6ed258c8faf18e6400bd7a9d401f16");
        String secret = sharedPreferences.getString("apiSecret", "jwxa3c845wxb");
        apiKey=key;
        apiSecrect=secret;

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onRestart() {//活动重新启动的时候，也要让背景更新。不需要重启应用，用户就能看到设置的生效。
        super.onRestart();
        setBg();
        setApiKeyAndSecret();
        setColor();
    }

    private void setColor() {
        int [] colorArr={Color.WHITE,Color.GRAY,Color.BLACK};
        int timeColorIndex = sharedPreferences.getInt("timeColor", 0);
        int nameColorIndex=sharedPreferences.getInt("nameColor",0);
        msgAdapter=new MsgAdapter(msgList,this, colorArr[timeColorIndex],colorArr[nameColorIndex]);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setBg(){
        sharedPreferences = getSharedPreferences("setting",MODE_PRIVATE);
        int bgImageIndex = sharedPreferences.getInt("bgImageIndex", 0);
        linearLayout.setBackground(getDrawable(imageArr[bgImageIndex]));

    }

    private void sendRequest(final String question){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url=new URL("http://i.itpk.cn/api.php?question="+question+"&api_key="+apiKey+"&api_secret="+apiSecrect);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
                    final StringBuilder response=new StringBuilder();
                    String line;
                    while ((line=reader.readLine())!=null){
                        response.append(line);
                    }
                    Log.d(TAG, "响应回来的数据是： "+response.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(question.contains("笑话")){
                                String title=null;
                                String content=null;
                                try {
                                    JSONObject jsonObject=new JSONObject(response.toString());
                                    title = jsonObject.getString("title");
                                    content = jsonObject.getString("content");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                msgList.add(new Msg("《"+title+"》:"+content,Msg.TYPE_RECEIVED));
                                String sql="insert into msg values (?,?,?,?,?,?)";
                                sqLiteDatabase.execSQL(sql,new String []{null,"《"+title+"》:"+content,"0","700091",getCurrentTime(),"卤蛋"});

                            }else {//如果返回来的不是笑话
                                Msg msg = new Msg(response.toString(), Msg.TYPE_RECEIVED, R.drawable.ludan, getCurrentTime(), "卤蛋", 0);
                                msgList.add(msg);
                                String sql="insert into msg values (?,?,?,?,?,?)";
                                sqLiteDatabase.execSQL(sql,new String []{null,response.toString(),"0","700091",getCurrentTime(),"卤蛋"});

                            }
                            msgAdapter.notifyItemInserted(msgList.size()-1);
                            recyclerView.scrollToPosition(msgList.size()-1);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "连接超时，服务器可能崩了~", Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        }).start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.history:
                startActivity(new Intent(this,HistoryActivity.class));
                break;
            case R.id.profile:
                startActivity(new Intent(this,ProfileActivity.class));
                break;
            case R.id.setting:
                startActivity(new Intent(this,SettingActivity.class));
                break;
            case R.id.logout:
                BmobUser.logOut();//退出登录，同时清除缓存用户对象。
                finish();//销毁当前活动
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                break;
            case R.id.exit:
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("提示");
                builder.setMessage("您确定要退出程序吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCollector.finishAll();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setCancelable(false);
                builder.create().show();
                break;
            default:
        }
        return true;
    }

    private String getCurrentTime(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(date);
        return format;
    }

}
