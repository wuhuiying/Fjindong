package one.bw.com.jingdong.shouye.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import one.bw.com.jingdong.R;
import one.bw.com.jingdong.gouwu.gouwuche.bean.MyGouWuBean;
import one.bw.com.jingdong.gouwu.gouwuche.view.ShangpingXiangQing;
import one.bw.com.jingdong.shouye.bean.MyDataBean;
import one.bw.com.jingdong.shouye.util.ImageLoaderUtil;

/**
 * Created by Administrator on 2017/12/5/005.
 */

public class MyShouYeMiaoAdapter extends RecyclerView.Adapter<MiaoShaHolder> {
    Context context;
    MyDataBean.MiaoshaBean miaosha;

    public MyShouYeMiaoAdapter(Context context, MyDataBean.MiaoshaBean miaosha) {
        this.context = context;
        this.miaosha = miaosha;
    }

    @Override
    public MiaoShaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.miaoshabuju, parent, false);
        MiaoShaHolder miaoShaHolder = new MiaoShaHolder(view);
        return miaoShaHolder;
    }

    @Override
    public void onBindViewHolder(MiaoShaHolder holder, final int position) {
        ImageView miaoshaimg = holder.miaoshaimg;
        String images = miaosha.getList().get(position).getImages();
        String[] split = images.split("\\|");
        ImageLoader.getInstance().displayImage(split[0],miaoshaimg, ImageLoaderUtil.showImag());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDataBean.MiaoshaBean.ListBeanX li = miaosha.getList().get(position);
                MyGouWuBean.DataBean d=new MyGouWuBean.DataBean();
                d.setDetailUrl(li.getDetailUrl());
                d.setCreatetime(li.getCreatetime());
                d.setBargainPrice(li.getBargainPrice());
                d.setImages(li.getImages());
                d.setItemtype(li.getItemtype());
                d.setPid(li.getPid());
                d.setPrice(li.getPrice());
                d.setPscid(li.getPscid());
                d.setSellerid(li.getSellerid());
                d.setSalenum(li.getSalenum());
                d.setSubhead(li.getSubhead());
                d.setTitle(li.getTitle());
                Intent intent = new Intent(context, ShangpingXiangQing.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("dataBean",d);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return miaosha.getList().size();
    }
}
class MiaoShaHolder extends RecyclerView.ViewHolder{

    public ImageView miaoshaimg;

    public MiaoShaHolder(View itemView) {
        super(itemView);
        miaoshaimg = itemView.findViewById(R.id.miaoshaimg);
    }
}