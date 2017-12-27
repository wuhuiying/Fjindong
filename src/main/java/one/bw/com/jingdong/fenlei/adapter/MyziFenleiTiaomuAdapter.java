package one.bw.com.jingdong.fenlei.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import one.bw.com.jingdong.R;
import one.bw.com.jingdong.apiUIL.APIuil;
import one.bw.com.jingdong.fenlei.bean.MyZiFenFeiBean;
import one.bw.com.jingdong.shouye.inters.MyJieKou;
import one.bw.com.jingdong.shouye.presenter.MyPresenter;
import one.bw.com.jingdong.shouye.util.ImageLoaderUtil;
import one.bw.com.jingdong.sousuo.MySouSuoShow;
import one.bw.com.jingdong.sousuo.bean.MySousuoBean;

/**
 * Created by Administrator on 2017/12/10/010.
 */

class MyziFenleiTiaomuAdapter extends RecyclerView.Adapter<ZiFenleiTiaomuHolder> {
    Context context;
    List<MyZiFenFeiBean.DataBean.ListBean> list;
    private final MyPresenter myPresenter;
    Handler h=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){
                Toast.makeText(context,"没有该类商品！",Toast.LENGTH_SHORT).show();
            }else if(msg.what==1){
                String json = (String) msg.obj;
                Intent intent = new Intent(context, MySouSuoShow.class);
                intent.putExtra("json",json);
                intent.putExtra("keywords",name);
                context.startActivity(intent);
            }else if(msg.what==2){
                Toast.makeText(context,"数据请求错误！",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context,"请求失败！",Toast.LENGTH_SHORT).show();
            }

        }
    };
    private String name;

    public MyziFenleiTiaomuAdapter(Context context, List<MyZiFenFeiBean.DataBean.ListBean> list) {
        this.context = context;
        this.list = list;
        myPresenter = new MyPresenter();
    }

    @Override
    public ZiFenleiTiaomuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.zifenleitiaomurecybuju, parent, false);
        ZiFenleiTiaomuHolder ziFenleiTiaomuHolder = new ZiFenleiTiaomuHolder(view);
        return ziFenleiTiaomuHolder;
    }

    @Override
    public void onBindViewHolder(ZiFenleiTiaomuHolder holder, final int position) {
        ImageView zifenleitiaomurecyimg = holder.zifenleitiaomurecyimg;
        ImageLoader.getInstance().displayImage(list.get(position).getIcon(),zifenleitiaomurecyimg, ImageLoaderUtil.showImag());
        holder.zifenleitiaomurecyname.setText(list.get(position).getName());
        //分类自条目的点击事件，，，跳转到搜索详情页面
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = list.get(position).getName();
                String sousuo = APIuil.sousuo(name);
                getSouSuoContent(name,sousuo);//跳转传值的方法，，
            }
        });
    }

    //跳转传值，，解析获取数据，，适配器中不可以直接用runOnUiThread方法来直接操作主线程，所以只能用handle机制
    private void getSouSuoContent(final String name, String sousuo) {
        myPresenter.getContent(sousuo, new MyJieKou() {
            @Override
            public void onChengGong(final String json) {

               new Thread() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        MySousuoBean mySousuoBean = gson.fromJson(json, MySousuoBean.class);
                        if(mySousuoBean.getCode().equals("0")){
                            if(mySousuoBean.getData().toString().equals("[]")){
                                h.sendEmptyMessage(0);
                            }else{
                                //跳转，，将解析出来的对象穿走
                                Message obtain = Message.obtain();
                                obtain.obj=json;
                                obtain.what=1;
                                h.sendMessage(obtain);
                            }
                        }else{
                            h.sendEmptyMessage(2);
                        }
                    }
                }.start();
            }

            @Override
            public void onShiBai(final String ss) {
                new Thread() {
                    @Override
                    public void run() {
                        h.sendEmptyMessage(3);
                    }
                }.start();

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
class ZiFenleiTiaomuHolder extends RecyclerView.ViewHolder{

    public ImageView zifenleitiaomurecyimg;
    public TextView zifenleitiaomurecyname;

    public ZiFenleiTiaomuHolder(View itemView) {
        super(itemView);
        zifenleitiaomurecyimg = itemView.findViewById(R.id.zifenleitiaomurecyimg);
        zifenleitiaomurecyname = itemView.findViewById(R.id.zifenleitiaomurecyname);
    }
}
