package one.bw.com.jingdong.sousuo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ScrollView;

import one.bw.com.jingdong.sousuo.inerts.ScrollViewListener;

/**
 * Created by Administrator on 2017/12/13/013.
 */

public class MyScrollView extends ScrollView {
    private ScrollViewListener scrollViewListener = null;

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //对外提供接口的访问方法
    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        //scrollview的起始点+总高度==crollView的computeVerticalScrollRange
        if(getScrollY() + getHeight() ==  computeVerticalScrollRange())
        {
            Log.d("jiaba","------滚动到最下方------");
            if(scrollViewListener!=null){
                scrollViewListener.onScrollChanged(this, l, t, oldl, oldt);
            }
        }
    }
}
