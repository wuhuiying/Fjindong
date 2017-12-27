package one.bw.com.jingdong.fenlei.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import one.bw.com.jingdong.R;
import one.bw.com.jingdong.fenlei.bean.MyZiFenFeiBean;

/**
 * Created by Administrator on 2017/12/15/015.
 */

public class MyFeiLeiMyListAdapter extends BaseAdapter {
    Context context;
    List<MyZiFenFeiBean.DataBean> data;

    public MyFeiLeiMyListAdapter(Context context, List<MyZiFenFeiBean.DataBean> data) {
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            view=View.inflate(context, R.layout.zifenleitiaomubuju,null);
        }
        MyZiFenFeiBean.DataBean dataBean = data.get(i);
        TextView zifenleitiaomuname = view.findViewById(R.id.zifenleitiaomuname);
        RecyclerView myZiFeiLeitiaomuRecyclerView = view.findViewById(R.id.myZiFeiLeitiaomuRecyclerView);
        zifenleitiaomuname.setText(dataBean.getName());
        myZiFeiLeitiaomuRecyclerView.setLayoutManager(new GridLayoutManager(context,3));
        myZiFeiLeitiaomuRecyclerView.setAdapter(new MyziFenleiTiaomuAdapter(context,dataBean.getList()));

        return view;
    }
}
