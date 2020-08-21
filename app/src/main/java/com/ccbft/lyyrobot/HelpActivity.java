package com.ccbft.lyyrobot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class HelpActivity extends BaseActivity {
    private TextView feetbackTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        feetbackTv=findViewById(R.id.feedbackTv);
        Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("使用帮助");
        toolbar.setNavigationIcon(R.drawable.back);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HelpActivity.this,SettingActivity.class));
            }
        });

        feetbackTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HelpActivity.this,FeedbackActivity.class));
            }
        });
    }
}
