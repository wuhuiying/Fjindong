package one.bw.com.jingdong.sousuo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import one.bw.com.jingdong.R;
import one.bw.com.jingdong.gouwu.gouwuche.inters.DianjiJieKou;

/**
 * Created by Administrator on 2017/12/12/012.
 */

public class MyReSouAdapter extends RecyclerView.Adapter<ReSouHolde> {
    Context context;
    String[] mNames;

    public DianjiJieKou dianjie;

    public void setDianjie(DianjiJieKou dianjie) {
        this.dianjie = dianjie;
    }

    public MyReSouAdapter(Context context, String[] mNames) {
        this.context = context;
        this.mNames = mNames;
    }



    @Override
    public ReSouHolde onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.resouliushihengbuju, parent, false);
        ReSouHolde reSouHolde = new ReSouHolde(view);

        return reSouHolde;
    }

    @Override
    public void onBindViewHolder(ReSouHolde holder, final int position) {
        holder.resouname.setText(mNames[position]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dianjie.onItemClike(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNames.length;
    }
}
class ReSouHolde extends RecyclerView.ViewHolder{

    public TextView resouname;

    public ReSouHolde(View itemView) {
        super(itemView);
        resouname = itemView.findViewById(R.id.resouname);
    }
}
