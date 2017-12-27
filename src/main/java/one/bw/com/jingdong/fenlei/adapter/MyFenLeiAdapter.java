package one.bw.com.jingdong.fenlei.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import one.bw.com.jingdong.R;
import one.bw.com.jingdong.fenlei.bean.MyFenLeiBean;
import one.bw.com.jingdong.shouye.frame.FraFen;

/**
 * Created by Administrator on 2017/12/8/008.
 */

public class MyFenLeiAdapter extends BaseAdapter {
    Context context;
    List<MyFenLeiBean.DataBean> data;
    public MyFenLeiAdapter(Context context, List<MyFenLeiBean.DataBean> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        view=View.inflate(context, R.layout.fenleilvbuju,null);
        final TextView fenleiName = view.findViewById(R.id.fenleiName);
        fenleiName.setText(data.get(i).getName());
        if(i== FraFen.item){
            view.setBackgroundColor(context.getResources().getColor(R.color.fenleilvbeijing));
            fenleiName.setTextColor(Color.RED);
        }else{
            view.setBackgroundColor(Color.WHITE);
            fenleiName.setTextColor(Color.BLACK);
        }
        return view;
    }



}
