package one.bw.com.jingdong.fenlei.fraem;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import one.bw.com.jingdong.R;
import one.bw.com.jingdong.apiUIL.APIuil;
import one.bw.com.jingdong.fenlei.adapter.MyFeiLeiMyListAdapter;
import one.bw.com.jingdong.fenlei.bean.MyZiFenFeiBean;
import one.bw.com.jingdong.shouye.frame.FraFen;
import one.bw.com.jingdong.shouye.inters.MyJieKou;
import one.bw.com.jingdong.shouye.presenter.MyPresenter;
import one.bw.com.jingdong.sousuo.view.MyListView;

/**
 * Created by Administrator on 2017/12/10/010.
 */

public class FraJingCheng extends Fragment{

    private ImageView fenleiTouimg;
    private MyListView fenleiMyListView;
    private MyPresenter myPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frajingcheng, container, false);
        fenleiTouimg = view.findViewById(R.id.fenleiTouimg);
        fenleiMyListView = view.findViewById(R.id.fenleiMyListView);

        //获取分类的cid与头部的图片
        int fenleicid = FraFen.fenleicid;
        int tu = FraFen.tu;
        fenleiTouimg.setImageResource(tu);//为头部图片赋值

        myPresenter = new MyPresenter();
        myPresenter.getContent(APIuil.zifenlei(fenleicid + ""), new MyJieKou() {
            @Override
            public void onChengGong(final String json) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        MyZiFenFeiBean myZiFenFei = gson.fromJson(json, MyZiFenFeiBean.class);
                        List<MyZiFenFeiBean.DataBean> data = myZiFenFei.getData();
                        fenleiMyListView.setAdapter(new MyFeiLeiMyListAdapter(getActivity(),data));
                    }
                });
            }

            @Override
            public void onShiBai(final String ss) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),ss,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return view;
    }
}
