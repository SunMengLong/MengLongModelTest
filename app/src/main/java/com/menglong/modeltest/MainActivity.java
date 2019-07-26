package com.menglong.modeltest;

import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import com.menglong.http.ResponseObj;
import com.menglong.http.StarHttpsUtil;
import com.menglong.json.MlJson;
import com.menglong.update.MlAppUpdate;
import com.menglong.videoplayer.analytics.PlayerBaseInfo;
import com.menglong.videoplayer.ijkplayer.IjkVideoView;
import com.menglong.videoplayer.ijkplayer.PlayerOptions;

import org.codehaus.jackson.type.TypeReference;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;
import tv.danmaku.ijk.media.player.IMediaPlayer;

public class MainActivity extends BaseActivity {

    private String url = "http://192.168.32.249:10081/tacs-service/v2/categories";
    private IjkVideoView ijkVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
        updateApk();
    }

    private void updateApk() {
        requestRuntimePermision(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissioLitener() {
            @Override
            public void onGranted() {
                MlAppUpdate.getInstence().downloadAndInstall("http://192.168.32.249:8090/group1/M00/00/95/wKgg-V06kUiARGIBAEEGBiaPiGM987.apk",MainActivity.this);
            }

            @Override
            public void onDenied(ArrayList<String> list) {

            }
        },this);
    }

    private void initView() {
        ijkVideoView = findViewById(R.id.life_cir_item_ijkplayer);
        //设置播放器
        //如：视频启动时要seek到的位置、播放器的最大尺寸
        PlayerOptions playerOptions = new PlayerOptions();
        ijkVideoView.setPlayerOptions(playerOptions);
        PlayerBaseInfo baseInfo = new PlayerBaseInfo();
        baseInfo.setBufferMaxSizeRate(0);
        baseInfo.setPlayerBufferMaxSize(10 * 1024 * 1024);
        ijkVideoView.setPlayerBaseInfo(baseInfo);
        //播放出错监听
        ijkVideoView.setmSendErrorListener(new IjkVideoView.sendErrorListener() {
            @Override
            public void sendError(int errorType, int errorcode) {
            }
        });
        //播放器准备好的监听
        ijkVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {

            }
        });
        //播放器播放完成监听
        ijkVideoView.setmFinishListener(new IjkVideoView.FinishListener() {
            @Override
            public void show() {
                ijkVideoView.start();
            }
        });
        //设置播放的视频地址
        ijkVideoView.setVideoURI(Uri.parse("http://ivi.bupt.edu.cn/hls/cctv3hd.m3u8"));
        ijkVideoView.start();
    }

    private void initData() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    ResponseObj responseObj = StarHttpsUtil.getSyncToResponseObj(url, Headers.of("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJ0ZW5hbnRpZCI6IjciLCJleHAiOjE1NjU5MzM3NjUsImlhdCI6MTU2MzM0MTc2NSwiZGV2aWNlaWQiOiI3MTgiLCJlbnZpcm9uZW1lbnRzdGF0ZSI6Ik9OTElORSIsInRlcm1pbmFsdHlwZSI6IlNUQiJ9.7J71tj1AfTWJF7CnI9P3rm6GAwk7A07Hr6CRF83cYud1WzmKeGl_SW6NWjXHsBu2uLWEg7ZTgRMzqtwjqaj-UQ"));
                    List<CategoryObjectV1> categoriesList = MlJson.parseStringToListObject(responseObj.getJsonStr(), new TypeReference<List<CategoryObjectV1>>() {
                    });
                    Log.i("sml", "run: ........."+categoriesList.get(0).getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
