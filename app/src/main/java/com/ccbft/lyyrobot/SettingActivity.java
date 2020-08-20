package com.ccbft.lyyrobot;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends BaseActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private Button api,clear,feedback;
    private Toolbar toolbar;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        toolbar=findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setTitle("设置");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this,MainActivity.class));
            }
        });

        setSupportActionBar(toolbar);
        MySQLiteOpenhelper mySQLiteOpenhelper = new MySQLiteOpenhelper(this, "robot.db", null, 1);
        sqLiteDatabase = mySQLiteOpenhelper.getReadableDatabase();
        api=findViewById(R.id.api);
        clear=findViewById(R.id.clear);
        feedback=findViewById(R.id.feedback);
        sharedPreferences=getSharedPreferences("setting",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        String [] arr={"背景1","背景2","背景3","背景4","背景5"};
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,R.layout.item_select,arr);
        arrayAdapter.setDropDownViewResource(R.layout.item_dropdown);
        Spinner spinner=findViewById(R.id.sp_dropdown);
        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(sharedPreferences.getInt("bgImageIndex",0));//默认选中那一项
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        commitData(editor,0);
                        break;
                    case 1:
                        commitData(editor,1);
                        break;
                    case 2:
                        commitData(editor,2);
                        break;
                    case 3:
                        commitData(editor,3);
                        break;
                    case 4:
                        commitData(editor,4);
                        break;
                    default:
                }
            }

            private void commitData(SharedPreferences.Editor editor,int i) {
                editor.putInt("bgImageIndex",i);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spinner2=findViewById(R.id.sp_dropdown2);
        String [] arr2={"白色","灰色","黑色"};
        ArrayAdapter<String> arrayAdapter2=new ArrayAdapter<String>(this,R.layout.item_select,arr2);
        arrayAdapter2.setDropDownViewResource(R.layout.item_dropdown);
        spinner2.setAdapter(arrayAdapter2);
        spinner2.setSelection(sharedPreferences.getInt("timeColor",0));
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        editor.putInt("timeColor",0);
                        editor.apply();
                        break;
                    case 1:
                        editor.putInt("timeColor",1);
                        editor.apply();
                        break;
                    case 2:
                        editor.putInt("timeColor",2);
                        editor.apply();
                        break;
                    default:
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spinner3=findViewById(R.id.sp_dropdown3);
        String [] arr3={"白色","灰色","黑色"};
        ArrayAdapter<String> arrayAdapter3=new ArrayAdapter<String>(this,R.layout.item_select,arr3);
        arrayAdapter3.setDropDownViewResource(R.layout.item_dropdown);
        spinner3.setAdapter(arrayAdapter3);
        spinner3.setSelection(sharedPreferences.getInt("nameColor",0));
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        editor.putInt("nameColor",0);
                        editor.apply();
                        break;
                    case 1:
                        editor.putInt("nameColor",1);
                        editor.apply();
                        break;
                    case 2:
                        editor.putInt("nameColor",2);
                        editor.apply();
                        break;
                    default:
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        AlertDialog.Builder builder=new AlertDialog.Builder(SettingActivity.this);
        final View view = SettingActivity.this.getLayoutInflater().inflate(R.layout.dialog, null, false);
        builder.setCancelable(false);
        builder.setView(view);
        final EditText et1 = view.findViewById(R.id.et1);
        final EditText et2=view.findViewById(R.id.et2);
        final Button btn1=view.findViewById(R.id.btn1);
        final Button btn2=view.findViewById(R.id.btn2);
        final Button btn3=view.findViewById(R.id.btn3);
        final AlertDialog alertDialog = builder.create();
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("apiKey",et1.getText().toString().trim());
                editor.putString("apiSecret",et2.getText().toString().trim());
                editor.apply();
                alertDialog.dismiss();
                Toast.makeText(SettingActivity.this, "保存成功！", Toast.LENGTH_SHORT).show();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et1.setText("fe6ed258c8faf18e6400bd7a9d401f16");
                et2.setText("jwxa3c845wxb");
            }
        });
        api.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1=new AlertDialog.Builder(SettingActivity.this);
                builder1.setTitle("警告!")
                        .setMessage("您确定要从本地数据库清空所有的聊天记录吗？（注意：删除后不可恢复，如需要，可先将聊天记录导出为txt文件）");
                builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder1.setCancelable(false);
                builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String sql="delete from msg";
                        sqLiteDatabase.execSQL(sql);
                        Toast.makeText(SettingActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                    }
                });
                builder1.create().show();

            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this,FeedbackActivity.class));
            }
        });
    }
}
