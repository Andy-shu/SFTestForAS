package com.sf.sftestforas;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class HttpTestActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_get_syn;
    private Button btn_get_asyn;
    private Button btn_post_string;
    private Button btn_post_stream;
    private Button btn_post_file;
    private Button btn_post_form;
    private Button btn_post_multi;
    private Button btn_header;
    private Button btn_gson;

    private final OkHttpClient client = new OkHttpClient();
    private static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");
    private static final MediaType MEDIA_TYPE_TXT
            = MediaType.parse("text/plain; charset=utf-8");
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private static final String IMGUR_CLIENT_ID = "...";
    private final Gson gson = new Gson();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_test);

        initViews();

    }

    private void initViews() {
        btn_get_syn = (Button) findViewById(R.id.btn_get_syn);
        btn_get_asyn = (Button) findViewById(R.id.btn_get_asyn);
        btn_post_string = (Button) findViewById(R.id.btn_post_string);
        btn_post_stream = (Button) findViewById(R.id.btn_post_stream);
        btn_post_file = (Button) findViewById(R.id.btn_post_file);
        btn_post_form = (Button) findViewById(R.id.btn_post_form);
        btn_post_multi = (Button) findViewById(R.id.btn_post_multi);
        btn_header = (Button) findViewById(R.id.btn_header);
        btn_gson = (Button) findViewById(R.id.btn_gson);

        btn_get_syn.setOnClickListener(this);
        btn_get_asyn.setOnClickListener(this);
        btn_post_string.setOnClickListener(this);
        btn_post_stream.setOnClickListener(this);
        btn_post_file.setOnClickListener(this);
        btn_post_form.setOnClickListener(this);
        btn_post_multi.setOnClickListener(this);
        btn_header.setOnClickListener(this);
        btn_gson.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_get_syn:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            synOkHttp();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;

            case R.id.btn_get_asyn:
                try {
                    asynOkHttp();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btn_post_string:
                break;

            case R.id.btn_post_stream:
                break;

            case R.id.btn_post_file:
                break;

            case R.id.btn_post_form:
                break;

            case R.id.btn_post_multi:
                break;

            case R.id.btn_header:
                break;

            case R.id.btn_gson:
                break;

        }
    }

    /*
    * 同步get请求
    */
    private void synOkHttp() throws Exception {
        //创建一个request请求对象
        final Request request = new Request.Builder().url("http://publicobject.com/helloworld.txt").build();

        //new call
        Response response = null;
        try {
            Call call = client.newCall(request);
            response = call.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response.isSuccessful()) {
            Headers headers = response.headers();
            Log.i("sf", headers.toString());
        }
    }

    /*
     * 异步get请求
     */
    private void asynOkHttp() {
        //创建一个request请求对象
        final Request request = new Request.Builder().url("http://publicobject.com/helloworld.txt").build();

        //new call
        Call call = client.newCall(request);

        try {
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    Log.i("sf", result);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
    * Post方式提交String
    * */
    private void postString() throws IOException {
        String postBody = ""
                + "Releases\n"
                + "--------\n"
                + "\n"
                + " * _1.0_ May 6, 2013\n"
                + " * _1.1_ June 15, 2013\n"
                + " * _1.2_ August 11, 2013\n";

        Request.Builder builder = new Request.Builder();
        builder.url("https://api.github.com/markdown/raw");
        builder.post(RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"), postBody));

        Request request = builder.build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            Log.i("sf", response.body().string());
        }
    }

    /*
    * Post方式提交流
    *
    * okhttp对流和字节的处理都是基于okio库
    *
    * 如果需要用到OutputStream，可以使用BufferedSink.outputSteam()来获取
    * */
    private void postStream() throws Exception {
        RequestBody requestBody = new RequestBody() {
            @Nullable
            @Override
            public MediaType contentType() {
                return null;
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                sink.writeUtf8("Numbers\n");
                sink.writeUtf8("-------\n");
                for (int i = 2; i <= 997; i++) {
                    sink.writeUtf8(String.format(" * %s = %s\n", i, factor(i)));
                }
            }

            private String factor(int n) {
                for (int i = 2; i < n; i++) {
                    int x = n / i;
                    if (x * i == n)
                        return factor(x) + " × " + i;
                }
                return Integer.toString(n);
            }
        };

        Request request = new Request.Builder().url("https://api.github.com/markdown/raw").post(requestBody).build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            Log.i("sf", response.body().string());
        }
    }

    /*
    * post方式提交文件
    * */
    public void postFile() throws Exception {
        File file = new File(Environment.getExternalStorageDirectory() + "/README.txt");

        if (!file.exists()) {
            Log.i("sf", "文件不存在");
        }

        Request request = new Request.Builder()
                .url("http://192.168.0.165:8080/SSH/uploadAction!upload.action")
                .post(RequestBody.create(MEDIA_TYPE_TXT, file))
                .build();

        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            Log.i("sf", response.body().string());
        }
    }

    /*
    * post提交表单
    * */
    public void postForm() throws Exception {
        RequestBody formBody = new FormBody.Builder()
                .add("search", "Jurassic Park")
                .build();
        Request request = new Request.Builder()
                .url("https://en.wikipedia.org/w/index.php")
                .post(formBody)
                .build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful())
            Log.i("sf", response.body().string());

    }

    /*
    * post方式提交分块请求
    * */
    public void postMulti() throws Exception {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title", "Square Logo")
                .addFormDataPart("image", "logo-square.png",
                        RequestBody.create(MEDIA_TYPE_PNG, new File("website/static/logo-square.png")))
                .build();

        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                .url("https://api.imgur.com/3/image")
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            Log.i("sf", response.body().string());
        }
    }

    public void test() throws Exception {
        Request request = new Request.Builder()
                .url("https://api.github.com/repos/square/okhttp/issues")
                .header("User-Agent", "OkHttp Headers.java")
                .addHeader("Accept", "application/json; q=0.5")
                .addHeader("Accept", "application/vnd.github.v3+json")
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful())
            throw new IOException("Unexpected code " + response);

        System.out.println("Server: " + response.header("Server"));
        System.out.println("Date: " + response.header("Date"));
        System.out.println("Vary: " + response.headers("Vary"));
    }


    public void test2() throws Exception {
        Request request = new Request.Builder()
                .url("https://api.github.com/gists/c2a7c39532239ff261be")
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful())
            throw new IOException("Unexpected code " + response);

        Gist gist = gson.fromJson(response.body().charStream(), Gist.class);
        for (Map.Entry<String, GistFile> entry : gist.files.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue().content);
        }
    }

    static class Gist {
        Map<String, GistFile> files;
    }

    static class GistFile {
        String content;
    }
}
