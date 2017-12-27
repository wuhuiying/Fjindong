package one.bw.com.jingdong.shouye.presenter;

import one.bw.com.jingdong.shouye.inters.MyJieKou;
import one.bw.com.jingdong.shouye.model.MyModel;

/**
 * Created by Administrator on 2017/12/6/006.
 */

public class MyPresenter {

    private final MyModel myModel;

    public MyPresenter() {
        myModel = new MyModel();
    }

    public void getContent(String url, final MyJieKou jie){
        myModel.getContentMessge(url, new MyJieKou() {
            @Override
            public void onChengGong(String json) {
                jie.onChengGong(json);
            }

            @Override
            public void onShiBai(String ss) {
                jie.onShiBai(ss);
            }
        });
    }
}
