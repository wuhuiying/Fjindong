package one.bw.com.jingdong.gouwu.gouwuche.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ExpandableListView;

/**
 * Created by Administrator on 2017/12/15/015.
 */

public class MyExpandableListView extends ExpandableListView {

    public MyExDianji myExDianji;

    public void setMyExDianji(MyExDianji myExDianji) {
        this.myExDianji = myExDianji;
    }

    public MyExpandableListView(Context context) {
        super(context);
    }

    public MyExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyExpandableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(ev.ACTION_MOVE==MotionEvent.ACTION_DOWN){
            Log.i("jiab","onTouchEvent==="+1);
            return true;
        }
        return false;
    }

    public interface MyExDianji{
        void dianji(int i);
    }

}
