package one.bw.com.jingdong.shouye.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import one.bw.com.jingdong.R;
import one.bw.com.jingdong.gouwu.gouwuche.bean.MyGouWuBean;
import one.bw.com.jingdong.gouwu.gouwuche.view.ShangpingXiangQing;
import one.bw.com.jingdong.shouye.bean.MyDataBean;
import one.bw.com.jingdong.shouye.util.ImageLoaderUtil;

/**
 * Created by Administrator on 2017/12/5/005.
 */

public class MyTuiJianAdapter extends RecyclerView.Adapter<TuiJianHolder> {
    Context context;
    MyDataBean.TuijianBean tuijian;

    public MyTuiJianAdapter(Context context, MyDataBean.TuijianBean tuijian) {
        this.context = context;
        this.tuijian = tuijian;
    }

    @Override
    public TuiJianHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tuijianbuju, parent, false);
        TuiJianHolder tuiJianHolder = new TuiJianHolder(view);

        return tuiJianHolder;
    }

    @Override
    public void onBindViewHolder(TuiJianHolder holder, final int position) {
        ImageView tuijianimg = holder.tuijianimg;
        String[] split = tuijian.getList().get(position).getImages().split("\\|");
        ImageLoader.getInstance().displayImage(split[0],tuijianimg, ImageLoaderUtil.showImag());
        holder.tuijiantitle.setText(tuijian.getList().get(position).getSubhead().substring(0,15)+"...");
        holder.tuijianprice.setText("￥"+tuijian.getList().get(position).getPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * bargainPrice : 22.9
                 * createtime : 2017-10-14T21:38:26
                 * detailUrl : https://item.m.jd.com/product/2542855.html?utm_source=androidapp&utm_medium=appshare&utm_campaign=t_335139774&utm_term=QQfriends
                 * images : https://m.360buyimg.com/n0/jfs/t1930/284/2865629620/390243/e3ade9c4/56f0a08fNbd3a1235.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t2137/336/2802996626/155915/e5e90d7a/56f0a09cN33e01bd0.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t1882/31/2772215910/389956/c8dbf370/56f0a0a2Na0c86ea6.jpg!q70.jpg|https://m.360buyimg.com/n0/jfs/t2620/166/2703833710/312660/531aa913/57709035N33857877.jpg!q70.jpg
                 * itemtype : 1
                 * pid : 34
                 * price : 9.0
                 * pscid : 2
                 * salenum : 667
                 * sellerid : 11
                 * subhead : 三只松鼠零食特惠，专区满99减50，满199减100，火速抢购》
                 * title : 三只松鼠 坚果炒货 零食奶油味 碧根果225g/袋
                 */
                MyDataBean.TuijianBean.ListBean li = tuijian.getList().get(position);
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
        return tuijian.getList().size();
    }
}
class TuiJianHolder extends RecyclerView.ViewHolder{

    public ImageView tuijianimg;
    public TextView tuijianprice;
    public TextView tuijiantitle;

    public TuiJianHolder(View itemView) {
        super(itemView);
        tuijianimg = itemView.findViewById(R.id.tuijianimg);
        tuijianprice = itemView.findViewById(R.id.tuijianprice);
        tuijiantitle = itemView.findViewById(R.id.tuijiantitle);
    }
}