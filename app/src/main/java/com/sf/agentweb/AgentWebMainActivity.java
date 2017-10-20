package com.sf.agentweb;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.just.library.AgentWeb;
import com.sf.sftestforas.MainActivity;
import com.sf.sftestforas.R;

public class AgentWebMainActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private TextView mTitleTextView;

    private Button btn_agent_activity;
    private Button btn_agent_fragment;
    private Button btn_agent_filedownload;
    private Button btn_agent_fileupload;
    private Button btn_agent_js;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_web_test);

        initViews();
    }

    private void initViews() {
        btn_agent_activity = (Button) findViewById(R.id.btn_agent_activity);
        btn_agent_fragment = (Button) findViewById(R.id.btn_agent_fragment);
        btn_agent_filedownload = (Button) findViewById(R.id.btn_agent_filedownload);
        btn_agent_fileupload = (Button) findViewById(R.id.btn_agent_fileupload);
        btn_agent_js = (Button) findViewById(R.id.btn_agent_js);

        btn_agent_activity.setOnClickListener(this);
        btn_agent_fragment.setOnClickListener(this);
        btn_agent_filedownload.setOnClickListener(this);
        btn_agent_fileupload.setOnClickListener(this);
        btn_agent_js.setOnClickListener(this);

        mToolbar = (Toolbar) this.findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle("");
        mTitleTextView = (TextView) this.findViewById(R.id.toolbar_title);
        mTitleTextView.setText("AgentWeb 使用指南");
        this.setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null)
            // Enable the Up button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AgentWebMainActivity.this.finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_agent_activity:
                agentWebInActivity();
                break;

            case R.id.btn_agent_fragment:
                startActivity(new Intent(this, AgentWebActivity.class).putExtra("type_key", 0));
                break;

            case R.id.btn_agent_filedownload:
                startActivity(new Intent(this, AgentWebActivity.class).putExtra("type_key", 1));
                break;

            case R.id.btn_agent_fileupload:
                startActivity(new Intent(this, AgentWebActivity.class).putExtra("type_key", 2));
                break;

            case R.id.btn_agent_js:
                startActivity(new Intent(this, AgentWebActivity.class).putExtra("type_key", 3));
                break;
        }
    }

    /*
    * AgentWeb在activity中的使用
    * */
    private LinearLayout mLinearLayout;
    private AgentWeb mAgentWeb;

    private void agentWebInActivity() {
        try {
            mAgentWeb = AgentWeb.with(this)//传入Activity
                    //传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ，
                    // 那么第二参数需要传入 RelativeLayout.LayoutParams
                    .setAgentWebParent(mLinearLayout, new LinearLayout.LayoutParams(-1, -1))
                    .useDefaultIndicator()// 使用默认进度条
                    .defaultProgressBarColor() // 使用默认进度条颜色
                    .setReceivedTitleCallback(null) //设置 Web 页面的 title 回调
                    .setWebChromeClient(mWebChromeClient)
                    .createAgentWeb()//
                    .ready()
                    .go("file:///android_asset/fivefu.html");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    * 通过onJsPrompt实现JS通信
    * */
    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            Log.i("sf", "message=" + message);

            if ("callByJs".equals(message)) {
                callByJS(defaultValue, result);
            }

            return true;
        }
    };

    private void callByJS(String defaultValue, JsPromptResult result) {
        Log.i("sf", "defaultValue=" + defaultValue);

        result.confirm("客户端已经接受到了数据");
    }

}
