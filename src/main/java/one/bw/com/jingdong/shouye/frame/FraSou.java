package one.bw.com.jingdong.shouye.frame;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import one.bw.com.jingdong.MyErWeiMa;
import one.bw.com.jingdong.R;
import one.bw.com.jingdong.apiUIL.APIuil;
import one.bw.com.jingdong.shouye.adapter.MyshouyeAdapter;
import one.bw.com.jingdong.shouye.bean.MyDataBean;
import one.bw.com.jingdong.sousuo.MySouSuo;

/**
 * Created by Administrator on 2017/12/4/004.
 */

public class FraSou extends Fragment {

    private XRecyclerView shouXRecyclerView;
    ArrayList<Object> list = new ArrayList<>();
    ArrayList<Integer> imglist=new ArrayList<>();
    private LinearLayout dianjiSouSuo;
    private LinearLayout saoyisaso;
    private LinearLayout xiaoxi;
    private LinearLayout sousuo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.shouye, container, false);
        imglist.add(R.drawable.guang2);
        imglist.add(R.drawable.guang3);
        shouXRecyclerView = view.findViewById(R.id.shouXRecyclerView);
        dianjiSouSuo = view.findViewById(R.id.dianjiSouSuo);
        saoyisaso = view.findViewById(R.id.ll_sao);
        xiaoxi = view.findViewById(R.id.ll_msg);
        sousuo = view.findViewById(R.id.sousuo);
        shouXRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL,false));
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onResume() {
        super.onResume();
        OkHttpClient okHttpClient = new OkHttpClient();
        Request build = new Request.Builder().url(APIuil.shouye).build();
        Call call = okHttpClient.newCall(build);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String json = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        MyDataBean myDataBean = gson.fromJson(json, MyDataBean.class);
                        list.add(myDataBean.getData());
                        list.add(R.drawable.guang1);
                        list.add(imglist);
                        list.add(R.drawable.guang4);
                        list.add(myDataBean.getMiaosha());
                        list.add(myDataBean.getTuijian());
                        shouXRecyclerView.setAdapter(new MyshouyeAdapter(getActivity(),list));
                    }
                });
            }
        });
        //设置下拉刷新功能
        shouXRecyclerView.setPullRefreshEnabled(true);
        shouXRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                shouXRecyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                Toast.makeText(getActivity(),"上拉成功！",Toast.LENGTH_SHORT).show();
                shouXRecyclerView.loadMoreComplete();
            }
        });
        //点击搜索跳转
        dianjiSouSuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MySouSuo.class);
                getActivity().startActivity(intent);
            }
        });
        saoyisaso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"正在打开二维码...",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                startActivityForResult(intent, 5);
            }
        });
        xiaoxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"正在打开消息界面...",Toast.LENGTH_SHORT).show();
            }
        });

        Log.i("jiabab","===="+isEmulator(getActivity()));
        if(isEmulator(getActivity())){
           // XRecyclerView的滑动，，判断向上滑动的距离是否大于600，，true=灰色，，false=透明
            shouXRecyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    //获取滑动距离，，通过布局管理器
                    //1.获得视图的第一条木的下标
                    //2.根据下标获得view条目,,,在获得条目的高度
                    //3.下标*条目高度-可见视图距离顶部的高度
                    LinearLayoutManager layoutManager = (LinearLayoutManager) shouXRecyclerView.getLayoutManager();
                    int position = layoutManager.findFirstVisibleItemPosition();
                    View firstVisiableChildView = layoutManager.findViewByPosition(position);
                    int itemHeight = firstVisiableChildView.getHeight();
                    int i4 = (position) * itemHeight - firstVisiableChildView.getTop();
                    Log.i("jibbb","==="+i4);
                    if(i4>600){
                        sousuo.setBackgroundColor(getResources().getColor(R.color.fenleilvbeijing));
                    }else{
                        sousuo.setBackgroundColor(getResources().getColor(R.color.touming));
                    }
                }
            });
        }


    }
    //判断方法,是模拟器就返回true
    public boolean isEmulator(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        if (imei == null || imei.equals("000000000000000")) {
            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5) {
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Toast.makeText(getActivity(), "解析结果:" + result, Toast.LENGTH_LONG).show();

                    //二维码解析完成，就跳转webview页面
                    Intent intent = new Intent(getActivity(), MyErWeiMa.class);
                    intent.putExtra("url", result);
                    getActivity().startActivity(intent);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(getActivity(), "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}