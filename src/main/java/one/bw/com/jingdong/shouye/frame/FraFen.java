package one.bw.com.jingdong.shouye.frame;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import one.bw.com.jingdong.R;
import one.bw.com.jingdong.apiUIL.APIuil;
import one.bw.com.jingdong.fenlei.adapter.MyFenLeiAdapter;
import one.bw.com.jingdong.fenlei.bean.MyFenLeiBean;
import one.bw.com.jingdong.fenlei.fraem.FraJingCheng;
import one.bw.com.jingdong.shouye.inters.MyJieKou;
import one.bw.com.jingdong.shouye.presenter.MyPresenter;
import one.bw.com.jingdong.sousuo.MySouSuo;

/**
 * Created by Administrator on 2017/12/4/004.
 */

public class FraFen extends Fragment {

    private ListView fenleilv;
    private MyPresenter myPresenter;
    private List<MyFenLeiBean.DataBean> list=new ArrayList<>();
    private MyFenLeiAdapter myFenLeiAdapter;
    public static int item=0;
    private int firstVisiblePosition;
    public static int fenleicid=1;
    public static int tu=R.drawable.zifenleitu;
    int[] fenleiimg={R.drawable.zifenleitu,R.drawable.zifenleitu2,R.drawable.zifenleitu3,R.drawable.zifenleitu4};
    private LinearLayout dianjiSouSuo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fenlei, container, false);
        fenleilv = view.findViewById(R.id.fenleilv);
        dianjiSouSuo = view.findViewById(R.id.dianjiSouSuo);
        //点击跳到搜索页面
        dianjiSouSuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MySouSuo.class);
                getActivity().startActivity(intent);
            }
        });
        myPresenter = new MyPresenter();
        //获取左边分类条目
        getCeBian();
        //设置默认展示页面
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fenlaiFrameLayout,new FraJingCheng()).commit();
        //为分类条目设置点击滑动，，动画
        fenleilv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                firstVisiblePosition = fenleilv.getFirstVisiblePosition();

                /**
                 * listview.smoothScrollToPosition(position);//平滑到某个item
                 listview.setSelection(position);//滑动到某个item
                 listview.scrollListBy(600);//向下滑动600px。向上是-600
                 listview.smoothScrollBy(600, 2000);//向下平滑1000px，在2s内。向上是-600
                 */

//                fenleilv.smoothScrollBy(view.getHeight(),1000);
                fenleilv.smoothScrollBy((view.getHeight())* (i-firstVisiblePosition)-view.getHeight(),1000);
                fenleilv.smoothScrollToPosition(i);
                item=i;
                int cid = list.get(i).getCid();
                int i1 = i % fenleiimg.length;
                tu=fenleiimg[i1];
                fenleicid=cid;
                setAdapter();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fenlaiFrameLayout,new FraJingCheng()).commit();
            }
        });

        return view;
    }

    private void getCeBian() {
        myPresenter.getContent(APIuil.fenlei, new MyJieKou() {
            @Override
            public void onChengGong(final String json) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        MyFenLeiBean myFenLeiBean = gson.fromJson(json, MyFenLeiBean.class);
                        if(myFenLeiBean.getCode().equals("0")){
                            List<MyFenLeiBean.DataBean> data = myFenLeiBean.getData();
                            list.addAll(data);
                            setAdapter();
                        }else{
                            Toast.makeText(getActivity(),myFenLeiBean.getMsg(),Toast.LENGTH_SHORT).show();
                        }
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
    }


    public void setAdapter(){
        if(myFenLeiAdapter==null){
            myFenLeiAdapter = new MyFenLeiAdapter(getActivity(), list);
            fenleilv.setAdapter(myFenLeiAdapter);
        }else{
            myFenLeiAdapter.notifyDataSetChanged();
        }
    }
}
