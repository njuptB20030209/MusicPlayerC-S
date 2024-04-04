package com.njupt.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.njupt.bean.SongInfo;
import com.njupt.listener.CilckEventImplByRankingActivity;
import com.njupt.listener.ClickEventImplByLikeSongActivity;
import com.njupt.listener.ClickEventImplByLocalActivity;
import com.njupt.listener.ClickEventImplByMainActivity;
import com.njupt.myapplication.LikeSongListActivity;
import com.njupt.myapplication.LocalMusicActivity;
import com.njupt.myapplication.MainActivity;
import com.njupt.myapplication.MusicPlayerMachineActivity;
import com.njupt.myapplication.R;
import com.njupt.myapplication.RankingPlaylistActivity;
import com.njupt.service.MediaPlayerService;
import com.njupt.utils.HttpManager;

import java.util.ArrayList;

public class MusicPlayerBarFragment extends Fragment {

    Button clickToPre,clickToNext,clickToStartOrPause;
    ImageView albumAndToPlayerMachine;

    Context mContext;
    private MediaPlayerService.MusicControl control;

    boolean isServiceBound;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            control = (MediaPlayerService.MusicControl) service;
            isServiceBound = true;
            if(control.isPlaying()){
                //封面，暂停图标要改
                SongInfo songInfo = MediaPlayerService.getCurrentMusicList().get(MediaPlayerService.getCurIndex());
                Glide.with(mContext)
                        .load(HttpManager.IP_ADDRESS + songInfo.albumUrl)
                        .placeholder(R.drawable.music_bar_default_album_vector)
                        .into(albumAndToPlayerMachine);
                clickToStartOrPause.setBackgroundResource(R.drawable.music_bar_to_stop);
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            isServiceBound = false;
        }
    };

    public MusicPlayerBarFragment(){
        super(R.layout.fragment_musicplayerbar);
    }

    public void setCurrentMusicPlayerAndPlay(int index, ArrayList<SongInfo> cur){
        if(cur!=null && cur.size()>0) {
            MediaPlayerService.setCurrentMusicListAndCurIndex(index, cur);

            if(mContext instanceof LocalMusicActivity){
                control.playLocal();
            }else {
                control.play();
            }
            SongInfo songInfo = MediaPlayerService.getCurrentMusicList().get(MediaPlayerService.getCurIndex());
            Glide.with(mContext)
                    .load(HttpManager.IP_ADDRESS+songInfo.albumUrl)
                    .placeholder(R.drawable.music_bar_default_album_vector)
                    .into(albumAndToPlayerMachine);
            clickToStartOrPause.setBackgroundResource(R.drawable.music_bar_to_stop);
        }else {
            Toast.makeText(mContext, "列表无歌曲", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        clickToPre = view.findViewById(R.id.clickToPlayPre);
        clickToStartOrPause = view.findViewById(R.id.clickToPlayOrPause);
        clickToNext = view.findViewById(R.id.clickToPlayNext);
        albumAndToPlayerMachine = view.findViewById(R.id.albumAndClickToMusicPlayerMachine);

        addListenerEvents();

        bindMusicService();

    }

    private void addListenerEvents() {
        //按钮的监听
        clickToNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSuccess ;
                if(mContext instanceof LocalMusicActivity){
                    isSuccess = control.playNextLocal();
                }else {
                    isSuccess = control.playNext();
                }
                if(isSuccess) {
                    SongInfo songInfo = MediaPlayerService.getCurrentMusicList().get(MediaPlayerService.getCurIndex());
                    Glide.with(mContext)
                            .load(HttpManager.IP_ADDRESS + songInfo.albumUrl)
                            .placeholder(R.drawable.music_bar_default_album_vector)
                            .into(albumAndToPlayerMachine);
                    clickToStartOrPause.setBackgroundResource(R.drawable.music_bar_to_stop);
                }
            }
        });
        clickToPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSuccess ;
                if(mContext instanceof LocalMusicActivity){
                    isSuccess = control.playPreLocal();
                }else {
                    isSuccess = control.playPre();
                }
                if(isSuccess) {
                    SongInfo songInfo = MediaPlayerService.getCurrentMusicList().get(MediaPlayerService.getCurIndex());
                    Glide.with(mContext)
                            .load(HttpManager.IP_ADDRESS + songInfo.albumUrl)
                            .placeholder(R.drawable.music_bar_default_album_vector)
                            .into(albumAndToPlayerMachine);
                }
            }
        });
        clickToStartOrPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isPlaying = control.pauseOrResume();
                if(isPlaying){
                    clickToStartOrPause.setBackgroundResource(R.drawable.music_bar_to_stop);
                }else {
                    clickToStartOrPause.setBackgroundResource(R.drawable.music_bar_to_start);
                }
            }
        });
        albumAndToPlayerMachine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, MediaPlayerService.getCurIndex()+"被点击", Toast.LENGTH_SHORT).show();
                //初始返回-1
                if(mContext instanceof MainActivity) {
                    ((ClickEventImplByMainActivity) mContext).onClickToJumpToMusicPlayerMachineActivity(MediaPlayerService.getCurIndex());
                }else if(mContext instanceof RankingPlaylistActivity){
                    ((CilckEventImplByRankingActivity)mContext).onClickToJumpToMusicPlayerMachineActivity(MediaPlayerService.getCurIndex());
                }else if(mContext instanceof LikeSongListActivity){
                    ((ClickEventImplByLikeSongActivity)mContext).onClickToJumpToMusicPlayerMachineActivity(MediaPlayerService.getCurIndex());
                }else if(mContext instanceof LocalMusicActivity){
                    ((ClickEventImplByLocalActivity)mContext).onClickToJumpToMusicPlayerMachineActivity(MediaPlayerService.getCurIndex());
                }else {
                    Toast.makeText(mContext, "点击了", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // 绑定音乐播放服务
    private void bindMusicService() {
        Intent intent = new Intent(mContext, MediaPlayerService.class);
        mContext.bindService(intent,connection,Context.BIND_AUTO_CREATE);
    }

    // 解绑音乐播放服务
    private void unbindMusicService() {
        if (isServiceBound) {
            mContext.unbindService(connection);
            isServiceBound = false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(control!=null && control.isPlaying()){
            //封面，暂停图标要改
            SongInfo songInfo = MediaPlayerService.getCurrentMusicList().get(MediaPlayerService.getCurIndex());
            Glide.with(mContext)
                    .load(HttpManager.IP_ADDRESS + songInfo.albumUrl)
                    .placeholder(R.drawable.music_bar_default_album_vector)
                    .into(albumAndToPlayerMachine);
            clickToStartOrPause.setBackgroundResource(R.drawable.music_bar_to_stop);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        control.stop();  //音乐将不在后台播放
        unbindMusicService();
    }

}
