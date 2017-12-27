package one.bw.com.jingdong;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyErWeiMa extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_er_wei_ma);
        WebView webView = (WebView) findViewById(R.id.webView);
        String url = getIntent().getStringExtra("url");
        webView.loadUrl(url);//加载本地路径文件，，url

//设置
        webView.setWebViewClient(new WebViewClient());//不去跳转到浏览器

        WebSettings settings = webView.getSettings();//获得设置页面的权限

        settings.setJavaScriptCanOpenWindowsAutomatically(true);//s设置javascript弹窗

        settings.setJavaScriptEnabled(true);//是安卓支持js脚本

        webView.setWebChromeClient(new WebChromeClient());//使安卓支持网页的弹出框
    }
}
