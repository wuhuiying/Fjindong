package one.bw.com.jingdong.sousuo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import one.bw.com.jingdong.R;

/**
 * Created by Administrator on 2017/12/12/012.
 */

public class MySouSuoLvAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> list;

    public MySouSuoLvAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
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
        if (view==null){
            view=View.inflate(context, R.layout.sousuolishibuju,null);
        }
        TextView sousuolishiname = view.findViewById(R.id.sousuolishiname);
        sousuolishiname.setText(list.get(i));
        return view;
    }
}
