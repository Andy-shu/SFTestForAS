package com.sf.sftestforas;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sf.agentweb.AgentWebMainActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private TextView mTitleTextView;

    private Button button_okhttp;
    private Button button_agentweb;
    private Button button_rxjava;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_okhttp = (Button) findViewById(R.id.button_okhttp);
        button_agentweb = (Button) findViewById(R.id.button_agentweb);
        button_rxjava = (Button) findViewById(R.id.button_rxjava);

        button_okhttp.setOnClickListener(this);
        button_agentweb.setOnClickListener(this);
        button_rxjava.setOnClickListener(this);

        mToolbar = (Toolbar) this.findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle("");
        mTitleTextView = (TextView) this.findViewById(R.id.toolbar_title);
        mTitleTextView.setText("舒飞综合测试");
        this.setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null)
            // Enable the Up button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_okhttp:
                Intent intent = new Intent(this, HttpTestActivity.class);
                startActivity(intent);
                break;

            case R.id.button_agentweb:
                Intent agentIntent = new Intent(this, AgentWebMainActivity.class);
                startActivity(agentIntent);
                break;

            case R.id.button_rxjava:
                break;

            default:
                break;
        }
    }
}
