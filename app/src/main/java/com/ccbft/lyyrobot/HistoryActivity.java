package com.ccbft.lyyrobot;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class HistoryActivity extends BaseActivity {
    private TextView tv;
    private Toolbar toolbar;
    private SQLiteDatabase sqLiteDatabase;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        tv=findViewById(R.id.tv);
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("聊天记录");
        toolbar.setNavigationIcon(getDrawable(R.drawable.back));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        MySQLiteOpenhelper mySQLiteOpenhelper = new MySQLiteOpenhelper(this, "robot.db", null, 1);
        sqLiteDatabase = mySQLiteOpenhelper.getReadableDatabase();
        tv.setText(getRecord());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option2,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.export:
                exportFile();
                break;
            default:
        }
        return true;
    }
    //导出聊天记录
    private void exportFile() {
        BufferedWriter writer=null;
        try {
            FileOutputStream fileOutputStream = openFileOutput("history.txt", Context.MODE_PRIVATE);
            writer=new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            writer.write(getRecord());
            File files = getExternalFilesDir("history.txt");
            String s = files.toString();
            Toast.makeText(this, "导出成功！文件路径位于\n"+s, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(writer!=null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //查询数据库获取所有的聊天记录
    private String getRecord(){
        Cursor cursor = sqLiteDatabase.rawQuery("select * from msg", null);
        StringBuilder sb=new StringBuilder();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String content = cursor.getString(1);
            int type = cursor.getInt(2);
            String time = cursor.getString(4);
            String name = cursor.getString(5);
            sb.append(id+"\t"+content+"\t"+type+"\t"+time+"\t"+name+"\n");
        }
        return sb.toString();
    }
}
