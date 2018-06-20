package com.flj.latte.ec.detail;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.flj.latte.delegates.LatteDelegate;
import com.flj.latte.ec.R;

/**
 * Created by yb
 */

public class VideoDelegate extends LatteDelegate {

    private static final String ARG_VIDEO_TVURL = "ARG_VIDEO_TVURL";
    private String tvUrl;
    private VideoView videoView;

    @Override
    public Object setLayout() {
        return R.layout.delegate_video;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args != null) {
            tvUrl = args.getString(ARG_VIDEO_TVURL);

        }
    }

    public static VideoDelegate create(String vedioInfo) {
        final Bundle args = new Bundle();
        args.putString(ARG_VIDEO_TVURL, vedioInfo);
        final VideoDelegate delegate = new VideoDelegate();
        delegate.setArguments(args);
        return delegate;
    }


    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

        /*** 创建VideoView对象*/

        final VideoView videoView= $(R.id.vv_video);
        /*** 网络视频播放* urlPath 将网络地址path 转化为Uri*/
        videoView.setVideoURI(Uri.parse(tvUrl));

        /***通过系统MediaController 播放 停止  暂停 视屏*/

        MediaController mediaController=new MediaController(this.getContext());

        /*** 设置VideoView与MediaController关联*/

        videoView.setMediaController(mediaController);

        /*** 设置MediaController与VideoView关联*/

        mediaController.setMediaPlayer(videoView);


    }
}
