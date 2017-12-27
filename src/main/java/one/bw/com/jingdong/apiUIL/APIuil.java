package one.bw.com.jingdong.apiUIL;

import one.bw.com.jingdong.gouwu.gouwuche.bean.MyGouBean;

/**
 * Created by Administrator on 2017/12/6/006.
 */

public class APIuil {
    public static String shouye="https://www.zhaoapi.cn/ad/getAd?source=android";

    //http://120.27.23.105/product/searchProducts?keywords=德龙咖啡机&source=android
    //德龙咖啡机
    //搜索
    public static String sousuo(String s){
        String ss="http://120.27.23.105/product/searchProducts?keywords="+s+"&source=android";
        return ss;
    }
    //商品数据
    public static String gouwu(int page){
        String gouwu="https://www.zhaoapi.cn/product/getProducts?pscid=2&page="+page+"&source=android";
        return gouwu;
    }
    //注册
    public static String zhuceJieKou(String mobile,String pass){
        String zhuce="https://www.zhaoapi.cn/user/reg?mobile="+mobile+"&password="+pass+"&source=android";
        return zhuce;
    }
    //登录
    public static String dengluJieKou(String mobile,String pass){
        String denglu="https://www.zhaoapi.cn/user/login?mobile="+mobile+"&password="+pass+"&source=android";
        return denglu;
    }

    //查看个人信息
    public static String getRen(String uid){
        String geren="https://www.zhaoapi.cn/user/getUserInfo?uid="+uid+"&source=android";
        return geren;
    }


    //商品详情  https://www.zhaoapi.cn/product/getProductDetail?pid=34
    public static String shangping(String pid){
        String ss="https://www.zhaoapi.cn/product/getProductDetail?pid="+pid+"&source=android";
        return ss;
    }

    //添加购物车
    public static String addCar(String uid, int pid){
        String addCarJiekou="https://www.zhaoapi.cn/product/addCart?uid="+uid+"&pid="+pid+"&source=android";
        return addCarJiekou;
    }

    //查询车   https://www.zhaoapi.cn/product/getCarts
    public static String getCar(String uid){
        String addCarJiekou="http://120.27.23.105/product/getCarts?Uid="+uid+"&source=android";
        return addCarJiekou;
    }
    //删除数据  http://120.27.23.105/product/deleteCart?uid=72&pid=1
    public static String deleteCar(String uid,String pid){
        String ss="http://120.27.23.105/product/deleteCart?uid="+uid+"&pid="+pid+"&source=android";
        return ss;
    }

    //更新购物车
    public static String updataCart(String uid,MyGouBean.DataBean.ListBean l){
        String ss="https://www.zhaoapi.cn/product/updateCarts?uid="+uid+"&sellerid="+l.getSellerid()+"&pid="+l.getPid()+"&selected="+l.getSelected()+"&num="+l.getNum()+"&source=android";
        return ss;
    }

    //分类左侧列表
    public static String fenlei="http://120.27.23.105/product/getCatagory";

    //分类布局...右侧..商品子分类接口
    public static String zifenlei(String cid){
         String zifenlei="http://120.27.23.105/product/getProductCatagory?cid="+cid+"&source=android";
        return zifenlei;
    }

    //上传文件
    public static String chuanFile(String file){
        String chuan="https://www.zhaoapi.cn/file/upload?uid=3470&file="+file+"&source=android";
        return chuan;
    }

}
