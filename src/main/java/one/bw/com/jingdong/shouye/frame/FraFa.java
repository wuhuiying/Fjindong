package one.bw.com.jingdong.shouye.frame;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import one.bw.com.jingdong.R;

/**
 * Created by Administrator on 2017/12/4/004.
 */

public class FraFa extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.faxian, container, false);
        VideoView vv =  view.findViewById(R.id.vv);
        MediaController mc = new MediaController(getActivity());
        vv.setVideoPath(Environment.getExternalStorageDirectory()+"/meinv.mp4");
        mc.setMediaPlayer(vv);
        vv.setMediaController(mc);
        vv.requestFocus();

        return view;
    }
}
