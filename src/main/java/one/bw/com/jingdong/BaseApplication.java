package one.bw.com.jingdong;

import android.app.Application;

import one.bw.com.jingdong.shouye.util.ImageLoaderUtil;

/**
 * Created by Administrator on 2017/12/5/005.
 */

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ImageLoaderUtil.getImag(this);
    }
}
