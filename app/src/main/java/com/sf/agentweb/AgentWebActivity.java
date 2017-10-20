package com.sf.agentweb;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.sf.sftestforas.R;

public class AgentWebActivity extends AppCompatActivity {
    private FrameLayout agentweb_frame;
    private FragmentManager mFragmentManager;
    private AgentWebFragment mAgentWebFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_web);

        agentweb_frame = (FrameLayout) findViewById(R.id.container_framelayout);
        mFragmentManager = getSupportFragmentManager();

        int key = getIntent().getIntExtra("type_key", -1);

        openFragment(key);
    }

    private void openFragment(int key) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Bundle mBundle = null;

        switch (key) {
            /*Fragment 使用AgentWeb*/
            case 0:
                mBundle = new Bundle();
                mBundle.putString("url_key", "https://m.vip.com/?source=www&jump_https=1");
                mAgentWebFragment = AgentWebFragment.newInstance(mBundle);
                ft.add(R.id.container_framelayout, mAgentWebFragment, AgentWebFragment.class.getName());
                break;

            /*AgentWeb文件下载*/
            case 1:
                mBundle = new Bundle();
                mBundle.putString("url_key", "http://android.myapp.com/");
                mAgentWebFragment = AgentWebFragment.newInstance(mBundle);
                ft.add(R.id.container_framelayout, mAgentWebFragment, AgentWebFragment.class.getName());
                break;

            /*AgentWeb文件上传*/
            case 2:
                mBundle = new Bundle();
                mBundle.putString("url_key", "file:///android_asset/upload_file/uploadfile.html");
                mAgentWebFragment = AgentWebFragment.newInstance(mBundle);
                ft.add(R.id.container_framelayout, mAgentWebFragment, AgentWebFragment.class.getName());
                break;
        }

        ft.commit();
    }
}
