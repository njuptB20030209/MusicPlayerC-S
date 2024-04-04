package com.njupt.myapplication;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.njupt.bean.SongInfo;
import com.njupt.fragment.MusicPlayerBarFragment;
import com.njupt.service.MediaPlayerService;
import com.njupt.utils.HttpManager;
import com.njupt.utils.LoginManager;

import java.io.IOException;

public class MusicPlayerMachineActivity extends AppCompatActivity {

    MusicPlayerBarFragment musicPlayerBarFragment ;//内部按钮自己处理
    ImageView album;//封面
    TextView curDuration;//歌曲当前时间
    TextView totalDuration;//歌曲总时间
    SeekBar musicProgressBar;//进度条

    private Handler handler;//定时事件
    private InnerHandler innerHandler;//处理收藏请求
    HttpManager httpManager;
    Button clickToCollect,clickToComment;

    private MediaPlayerService.MusicControl control;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            control = (MediaPlayerService.MusicControl) service;
            SongInfo songInfo = MediaPlayerService.getCurrentMusicList().get(MediaPlayerService.getCurIndex());
            Glide.with(MusicPlayerMachineActivity.this)
                    .load(HttpManager.IP_ADDRESS + songInfo.albumUrl)
                    .into(album);

            int duration = control.getDuration();//总时长
            int currentPosition = control.getCurrentDuration();//当前时长

            musicProgressBar.setProgress((int) ((currentPosition*100.0) / duration));
            String totalTime = msToMinSec(duration);
            String currentTime = msToMinSec(currentPosition);
            totalDuration.setText(totalTime);
            curDuration.setText(currentTime);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private Runnable changeView = new Runnable() {
        @Override
        public void run() {
            if(control.isPlaying()) {
                SongInfo songInfo = MediaPlayerService.getCurrentMusicList().get(MediaPlayerService.getCurIndex());
                Glide.with(MusicPlayerMachineActivity.this)
                        .load(HttpManager.IP_ADDRESS + songInfo.albumUrl)
                        .into(album);
                int duration = control.getDuration();//总时长
                int currentPosition = control.getCurrentDuration();//当前时长

                musicProgressBar.setProgress((int) ((currentPosition*100.0) / duration));
                String totalTime = msToMinSec(duration);
                String currentTime = msToMinSec(currentPosition);
                totalDuration.setText(totalTime);
                curDuration.setText(currentTime);
            }
            handler.postDelayed(this, 500);
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player_machine);

        initView();

        addListnerEvent();
        //也许要重写fragment方法
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.machine_musicPlayerBar, musicPlayerBarFragment)
                .commit();

        //准备计时
        //出现问题，与进度条拖拽冲突 --已经解决
        handler.postDelayed(changeView,500);
    }

    private void addListnerEvent() {
        musicProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    //是用户自己拖动的，
                    int duration = control.getDuration();
                    int newPosition = (int) ((duration * progress) / 100.0);
                    control.seekTo(newPosition);
                    String currentTime = msToMinSec(newPosition);
                    curDuration.setText(currentTime);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {// 拖动时停
                control.pause();
                //让handler定时事件暂停
                handler.removeCallbacks(changeView);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                control.resume();
                //让handler定时事件恢复
                handler.postDelayed(changeView,500);
            }
        });
        clickToCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(MusicPlayerMachineActivity.this, "点击收藏", Toast.LENGTH_SHORT).show();
                if(LoginManager.getInstance().isLoggedIn()){
                    //收藏
                    //忘记了
                    if(MediaPlayerService.getCurrentMusicList()==null
                            || MediaPlayerService.getCurrentMusicList().isEmpty()
                    || MediaPlayerService.getCurIndex() < 0){
                        Toast.makeText(MusicPlayerMachineActivity.this, "当前无歌曲播放！", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(MusicPlayerMachineActivity.this, "正在添加", Toast.LENGTH_SHORT).show();
                        try {
                            httpManager.addToLike(LoginManager.getInstance().getUid(),MediaPlayerService.getCurrentMusicList().get(MediaPlayerService.getCurIndex()).songId,innerHandler);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }else{
                    //跳转登录页
                    Toast.makeText(MusicPlayerMachineActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MusicPlayerMachineActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        clickToComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MusicPlayerMachineActivity.this, "点击评论", Toast.LENGTH_SHORT).show();
                if(LoginManager.getInstance().isLoggedIn()){
                    //收藏
                    //忘记了
                    if(MediaPlayerService.getCurrentMusicList()==null
                            || MediaPlayerService.getCurrentMusicList().isEmpty()
                            || MediaPlayerService.getCurIndex() < 0){
                        Toast.makeText(MusicPlayerMachineActivity.this, "当前无歌曲播放！", Toast.LENGTH_SHORT).show();
                    }else {
//                        Toast.makeText(MusicPlayerMachineActivity.this, "正在跳转评论页", Toast.LENGTH_SHORT).show();
                        //跳转评论activity
                        Intent intent = new Intent(MusicPlayerMachineActivity.this, CommentInfoActivity.class);
                        startActivity(intent);
                    }
                }else{
                    //跳转登录页
                    Toast.makeText(MusicPlayerMachineActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MusicPlayerMachineActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void initView() {
        //初始化组件
        album = findViewById(R.id.music_player_machine_album);
        curDuration = findViewById(R.id.music_player_current_duration);
        totalDuration = findViewById(R.id.music_player_machine_duration);
        musicProgressBar = findViewById(R.id.music_player_machine_seek_bar);
        clickToCollect = findViewById(R.id.music_player_machine_btn_to_collect);
        clickToComment = findViewById(R.id.music_player_machine_btn_to_comment);
        handler = new Handler();
        innerHandler = new InnerHandler();
        httpManager = new HttpManager();
        //初始化fragment
        musicPlayerBarFragment = new MusicPlayerBarFragment();

        //绑定服务
        Intent intent = new Intent(getApplicationContext(), MediaPlayerService.class);
        bindService(intent,connection,BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
//        control.stop();
        super.onDestroy();
        unbindService(connection);
        handler.removeCallbacksAndMessages(null);
    }

    public static String msToMinSec(int ms){
        int sec = ms/1000;
        int min = sec/60;
        sec -= min*60;
        return String.format("%02d:%02d",min,sec);
    }

    public class InnerHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what==200){
                Toast.makeText(MusicPlayerMachineActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 201) {
                Toast.makeText(MusicPlayerMachineActivity.this, "已经收藏过了！！", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(MusicPlayerMachineActivity.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
