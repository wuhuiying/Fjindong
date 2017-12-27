package one.bw.com.jingdong.shouye.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.stx.xhb.xbanner.XBanner;

import java.util.ArrayList;
import java.util.List;

import one.bw.com.jingdong.R;
import one.bw.com.jingdong.shouye.bean.MyDataBean;
import one.bw.com.jingdong.shouye.bean.PaoMaBean;
import one.bw.com.jingdong.shouye.util.ImageLoaderUtil;
import one.bw.com.jingdong.shouye.view.MyPaoMaView;

/**
 * Created by Administrator on 2017/12/5/005.
 */

public class MyshouyeAdapter extends XRecyclerView.Adapter {
    Context context;
    List<Object> list;
    private List<String> strings;
    private LinearLayout shouyelunbo2yuandian;
    private ArrayList<ImageView> images;

    public MyshouyeAdapter(Context context, List<Object> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==0){
            View view = LayoutInflater.from(context).inflate(R.layout.shouyelunbo, parent,false);

            ShouYeLunBo shouYeLunBo = new ShouYeLunBo(view);
            return shouYeLunBo;
        }else if(viewType==1){
            View view = LayoutInflater.from(context).inflate(R.layout.shouyeguang, parent,false);
            ShouYeGuang shouYeGuang = new ShouYeGuang(view);
            return shouYeGuang;
        }else if(viewType==2){
            View view = LayoutInflater.from(context).inflate(R.layout.shouyelunbo2, parent,false);
            ShouYeLunBo2 shouYeLunBo2 = new ShouYeLunBo2(view);
            return shouYeLunBo2;
        }else if(viewType==3){
            View view = LayoutInflater.from(context).inflate(R.layout.shouyeguang2, parent,false);
            ShouYeGuang3 shouYeGuang3 = new ShouYeGuang3(view);
            return shouYeGuang3;
        }else if(viewType==4){
            View view = LayoutInflater.from(context).inflate(R.layout.shouyemiaosha, parent,false);
            ShouYeMiaoSha shouYeMiaoSha = new ShouYeMiaoSha(view);
            return shouYeMiaoSha;
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.shouyetuijian, parent,false);
            ShouYeTuiJian shouYeTuiJian = new ShouYeTuiJian(view);
            return shouYeTuiJian;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(getItemViewType(position)==0){
            XBanner shouyeXBanner = ((ShouYeLunBo) holder).shouyeXBanner;
            List<MyDataBean.DataBean> o = (List<MyDataBean.DataBean>) list.get(position);
            strings = new ArrayList<>();
            strings.clear();
            for (MyDataBean.DataBean d :o ) {
                strings.add(d.getIcon());
            }
            shouyeXBanner.setData(strings,null);//设置数据源
            shouyeXBanner.setPoinstPosition(XBanner.CENTER);//设置指示器的显示位置
            shouyeXBanner.setmAdapter(new XBanner.XBannerAdapter() {//xbanner的适配器，加载图片
                @Override
                public void loadBanner(XBanner banner, Object model, View view, int position) {
                    ImageLoader.getInstance().displayImage(strings.get(position), (ImageView) view, ImageLoaderUtil.showImag());
                }

            });

        }else if(getItemViewType(position)==1){
        }else if(getItemViewType(position)==2){
            ViewPager shouyeViewPager = ((ShouYeLunBo2) holder).shouyeViewPager;
            shouyelunbo2yuandian = ((ShouYeLunBo2) holder).shouyelunbo2yuandian;

            final ArrayList<Integer> imglist = (ArrayList<Integer>) list.get(position);
            initDoc(imglist);
            shouyeViewPager.setAdapter(new PagerAdapter() {
                @Override
                public int getCount() {
                    return imglist.size();
                }

                @Override
                public boolean isViewFromObject(View view, Object object) {
                    return view==object;
                }

                @Override
                public Object instantiateItem(ViewGroup container, int position) {
                    ImageView imageView = new ImageView(context);
                    imageView.setImageResource(imglist.get(position));
                    container.addView(imageView);
                    return imageView;
                }
                @Override
                public void destroyItem(ViewGroup container, int position, Object object) {
                    container.removeView((View) object);
                }
            });
            shouyeViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }
                @Override
                public void onPageSelected(int position) {
                    for (int i=0;i<images.size();i++){
                        if (i == position%images.size()){
                            images.get(i).setImageResource(R.drawable.shape_hei);
                        }else {
                            images.get(i).setImageResource(R.drawable.shape_hui);
                        }
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });


        }else if(getItemViewType(position)==3){
            MyPaoMaView shouyePaoMa = ((ShouYeGuang3) holder).shouyePaoMa;
            ArrayList<PaoMaBean> mList = new ArrayList<>();
            mList.add(new PaoMaBean("","国货PK美国货,结果让人震惊", "连接1"));
            mList.add(new PaoMaBean("","这次XiPhone,可能让你迷路", "连接2"));
            mList.add(new PaoMaBean("", "为什么吉普,奥巴马都爱钓鱼", "连接3"));
            mList.add(new PaoMaBean("", "虽然我字难看,但我钢笔好看啊", "连接4"));
            shouyePaoMa.setFrontColor(Color.RED);
            shouyePaoMa.setBackColor(Color.BLACK);
            shouyePaoMa.setmTexts(mList);
            shouyePaoMa.setmDuration(1000);
            shouyePaoMa.setmInterval(1000);

        }else if(getItemViewType(position)==4){
            RecyclerView miaoRecyclerView = ((ShouYeMiaoSha) holder).miaoRecyclerView;
            MyDataBean.MiaoshaBean miaosha = (MyDataBean.MiaoshaBean) list.get(position);
            miaoRecyclerView.setAdapter(new MyShouYeMiaoAdapter(context,miaosha));
            miaoRecyclerView.setLayoutManager(new LinearLayoutManager(context, OrientationHelper.HORIZONTAL,false));
        }else if(getItemViewType(position)==5){
            RecyclerView tuiRecyclerView = ((ShouYeTuiJian) holder).tuiRecyclerView;
            MyDataBean.TuijianBean tuijian = (MyDataBean.TuijianBean) list.get(position);
            tuiRecyclerView.setAdapter(new MyTuiJianAdapter(context,tuijian));
            tuiRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,OrientationHelper.VERTICAL));
        }
    }

    private void initDoc(List<Integer> list) {
        //1.需要一个集合记录一下小圆点的imageView控件
        images = new ArrayList<ImageView>();
        //2...linearLayout上面的视图清空一下再去添加
        shouyelunbo2yuandian.removeAllViews();
        for (int i=0;i<list.size();i++){
            ImageView imageView = new ImageView(context);
            if (i==0){
                imageView.setImageResource(R.drawable.shape_hei);
            }else {
                imageView.setImageResource(R.drawable.shape_hui);
            }
            //添加到集合去
            images.add(imageView);
            //添加到线性布局上
            //这是布局参数,,刚开始小圆点之间没有距离,所以使用java代码指定宽度高度,并且指定小圆点之间的距离
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(5,0,5,0);
            shouyelunbo2yuandian.addView(imageView,params);
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return 0;
        }else if(position==1){
            return 1;
        }else if(position==2){
            return 2;
        }else if(position==3){
            return 3;
        }else if(position==4){
            return 4;
        }else{
            return 5;
        }

    }
}
class ShouYeLunBo extends RecyclerView.ViewHolder{

    public XBanner shouyeXBanner;

    public ShouYeLunBo(View itemView) {
        super(itemView);
        shouyeXBanner = itemView.findViewById(R.id.shouyeXBanner);
    }
}
class ShouYeGuang extends RecyclerView.ViewHolder{

    public ShouYeGuang(View itemView) {
        super(itemView);
    }
}
class ShouYeLunBo2 extends RecyclerView.ViewHolder{

    public ViewPager shouyeViewPager;
    public LinearLayout shouyelunbo2yuandian;

    public ShouYeLunBo2(View itemView) {
        super(itemView);
        shouyeViewPager = itemView.findViewById(R.id.shouyeViewPager);
        shouyelunbo2yuandian = itemView.findViewById(R.id.shouyelunbo2yuandian);

    }
}
class ShouYeGuang3 extends RecyclerView.ViewHolder{

    public MyPaoMaView shouyePaoMa;

    public ShouYeGuang3(View itemView) {
        super(itemView);
        shouyePaoMa = itemView.findViewById(R.id.shouyePaoMa);
    }
}

class ShouYeMiaoSha extends RecyclerView.ViewHolder{

    public RecyclerView miaoRecyclerView;

    public ShouYeMiaoSha(View itemView) {
        super(itemView);
        miaoRecyclerView = itemView.findViewById(R.id.miaoRecyclerView);
    }
}
class ShouYeTuiJian extends RecyclerView.ViewHolder{

    public RecyclerView tuiRecyclerView;

    public ShouYeTuiJian(View itemView) {
        super(itemView);
        tuiRecyclerView = itemView.findViewById(R.id.tuiRecyclerView);
    }
}