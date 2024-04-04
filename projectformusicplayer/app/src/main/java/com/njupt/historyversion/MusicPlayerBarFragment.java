//package com.njupt.historyversion;
//
//import android.content.Context;
//import android.media.MediaPlayer;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import com.bumptech.glide.Glide;
//import com.njupt.bean.toparsejson.SearchResultList;
//import com.njupt.listener.ClickEventImplByMainActivity;
//import com.njupt.myapplication.R;
//
//import java.io.IOException;
//import java.util.ArrayList;
//
//public class MusicPlayerBarFragment extends Fragment {
//
//    Button clickToPre,clickToNext,clickToStartOrPause;
//    ImageView albumAndToPlayerMachine;
//
//    MediaPlayer mediaPlayer;
//
//    Context mContext;
//    int curIndex = -1;
//    ArrayList<SearchResultList.SongInfo> currentMusicList;
//
//    public MusicPlayerBarFragment(){
//        super(R.layout.fragment_musicplayerbar);
//    }
//
//    public void setCurrentMusicPlayerAndPlay(int index, ArrayList<SearchResultList.SongInfo> cur){
//
//        currentMusicList = cur;
//        if(currentMusicList == null || currentMusicList.size()==0){
//            currentMusicList = new ArrayList<>();
//        }
//        curIndex = index;
//
//        //播放下一首
//        if (mediaPlayer != null ) {
//            mediaPlayer.stop();
//            mediaPlayer.reset();
//        }
//        try {
//            mediaPlayer.setDataSource(currentMusicList.get(curIndex).songUrl);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        mediaPlayer.prepareAsync();
//    }
//
//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        mContext = context;
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        clickToPre = view.findViewById(R.id.clickToPlayPre);
//        clickToStartOrPause = view.findViewById(R.id.clickToPlayOrPause);
//        clickToNext = view.findViewById(R.id.clickToPlayNext);
//        albumAndToPlayerMachine = view.findViewById(R.id.albumAndClickToMusicPlayerMachine);
//
//        mediaPlayer = new MediaPlayer();
//
//
//        AddListenerEvents();
//
//
//    }
//
//    private void AddListenerEvents() {
//        //歌曲的监听
//        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                // 准备完成后开始播放
//                mp.start();
//                Glide.with(mContext)
//                        .load(currentMusicList.get(curIndex).albumUrl)
//                        .into(albumAndToPlayerMachine);
//                clickToStartOrPause.setBackgroundResource(R.drawable.music_bar_pause_btn);
//            }
//        });
//        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                if(currentMusicList != null && currentMusicList.size()>0) {
//                    curIndex = (curIndex + 1) % currentMusicList.size();
//                    //让media变更,带实现
//                    if (mp != null ) {
//                        mp.reset();
//                    }
//                    try {
//                        mp.setDataSource(currentMusicList.get(curIndex).songUrl);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                    mp.prepareAsync();
//                }else {
//                    Toast.makeText(mContext, "无可播放歌曲", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        clickToNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(currentMusicList != null && currentMusicList.size()>0) {
//                    curIndex = (curIndex + 1) % currentMusicList.size();
//                    //让media变更,带实现
//                    if (mediaPlayer != null ) {
//                        mediaPlayer.stop();
//                        mediaPlayer.reset();
//                    }
//                    try {
//                        mediaPlayer.setDataSource(currentMusicList.get(curIndex).songUrl);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                    mediaPlayer.prepareAsync();
//                }else {
//                    Toast.makeText(mContext, "无可播放歌曲", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        clickToPre.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(currentMusicList != null && currentMusicList.size()>0 ) {
//                    curIndex = ((curIndex - 1) + currentMusicList.size()) % currentMusicList.size();
//                    //让media变更,带实现
//                    if (mediaPlayer != null ) {
//                        mediaPlayer.stop();
//                        mediaPlayer.reset();
//                    }
//                    try {
//                        mediaPlayer.setDataSource(currentMusicList.get(curIndex).songUrl);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                    mediaPlayer.prepareAsync();
//                }else {
//                    Toast.makeText(mContext, "无可播放歌曲", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        clickToStartOrPause.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(currentMusicList != null && currentMusicList.size()>0) {
//                    if (mediaPlayer != null) {
//                        if (mediaPlayer.isPlaying()) {
//                            mediaPlayer.pause();
//                            clickToStartOrPause.setBackgroundResource(R.drawable.music_bar_start_btn);
//                        } else {
//                            mediaPlayer.start();
//                            clickToStartOrPause.setBackgroundResource(R.drawable.music_bar_pause_btn);
//                        }
//                    }
//                }else {
//                    Toast.makeText(mContext, "无可播放歌曲", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        albumAndToPlayerMachine.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(mContext, currentMusicList.get(curIndex).songname+"被点击", Toast.LENGTH_SHORT).show();
//                ((ClickEventImplByMainActivity)mContext).onClickToJumpToMusicPlayerMachineActivity(curIndex);
//            }
//        });
//    }
//
//
//
//
//
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        if (mediaPlayer != null) {
//            mediaPlayer.release();
//            mediaPlayer = null;
//        }
//    }
//}
