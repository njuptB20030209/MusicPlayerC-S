package com.njupt.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.njupt.bean.SongInfo;
import com.njupt.utils.HttpManager;

import java.io.IOException;
import java.util.ArrayList;

public class MediaPlayerService extends Service {
    MediaPlayer mediaPlayer;//多媒体对象
    static ArrayList<SongInfo> currentMusicList;
    static int curIndex ;
    public MediaPlayerService(){

    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MusicControl();//绑定服务的时候，把音乐控制类实例化
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();//或许可以用mainAcyivity的对象，不可以只能通过Bind
        currentMusicList = new ArrayList<>();
        curIndex = -1;

        addListnerEvent();
    }

    private void addListnerEvent() {
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // 准备完成后开始播放
                mp.start();
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(currentMusicList != null && currentMusicList.size()>0) {
                    curIndex = (curIndex + 1) % currentMusicList.size();
                    //让media变更,带实现
                    if (mp != null ) {
                        mp.reset();
                    }
                    try {
                        mp.setDataSource(HttpManager.IP_ADDRESS + currentMusicList.get(curIndex).songUrl);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    mp.prepareAsync();
                }else {
                    Toast.makeText(getApplicationContext(), "无可播放歌曲", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //非绑定服务时启动
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class MusicControl extends Binder{
        public void play() {
            if (currentMusicList!=null && currentMusicList.size() > 0) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                try {
                    mediaPlayer.setDataSource(HttpManager.IP_ADDRESS + currentMusicList.get(curIndex).songUrl);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                mediaPlayer.prepareAsync();
            }else {
                Toast.makeText(MediaPlayerService.this, "列表无歌曲", Toast.LENGTH_SHORT).show();
            }
        }
        public boolean playNext(){
            if(currentMusicList != null && currentMusicList.size()>0) {
                curIndex = (curIndex + 1) % currentMusicList.size();
                //让media变更,带实现
                if (mediaPlayer != null ) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                }
                try {
                    mediaPlayer.setDataSource(HttpManager.IP_ADDRESS + currentMusicList.get(curIndex).songUrl);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
//                timer.cancel();//给旧歌去掉监听
                mediaPlayer.prepareAsync();
                return true;
            }else {
                Toast.makeText(getApplicationContext(), "无可播放歌曲", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
        public Boolean pauseOrResume(){
            if(currentMusicList != null && currentMusicList.size()>0) {
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        return false;//放启动图标
                    } else {
                        mediaPlayer.start();
                        return true;//放停止图标
                    }
                }
            }else {
                Toast.makeText(getApplicationContext(), "无可播放歌曲", Toast.LENGTH_SHORT).show();
            }
            return false;//放启动图标
        }
        public boolean playPre(){
            if(currentMusicList != null && currentMusicList.size()>0 ) {
                curIndex = ((curIndex - 1) + currentMusicList.size()) % currentMusicList.size();
                //让media变更,带实现
                if (mediaPlayer != null ) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                }
                try {
                    mediaPlayer.setDataSource(HttpManager.IP_ADDRESS + currentMusicList.get(curIndex).songUrl);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                mediaPlayer.prepareAsync();
                return true;
            }else {
                Toast.makeText(getApplicationContext(), "无可播放歌曲", Toast.LENGTH_SHORT).show();
            }
            return false;
        }

        public void pause() {
            mediaPlayer.pause();
        }

        public void resume() {
            mediaPlayer.start();
        }

        public void seekTo(int progress) {
            mediaPlayer.seekTo(progress);
        }

        public boolean isPlaying() {
            return mediaPlayer.isPlaying();
        }

        public int getDuration() {
            return mediaPlayer.getDuration();
        }
        public int getCurrentDuration(){
            return mediaPlayer.getCurrentPosition();
        }

        public void playLocal() {
            if (currentMusicList!=null && currentMusicList.size() > 0) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                try {
                    mediaPlayer.setDataSource(currentMusicList.get(curIndex).songUrl);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                mediaPlayer.prepareAsync();
            }else {
                Toast.makeText(MediaPlayerService.this, "列表无歌曲", Toast.LENGTH_SHORT).show();
            }
        }

        public boolean playPreLocal() {
            if(currentMusicList != null && currentMusicList.size()>0 ) {
                curIndex = ((curIndex - 1) + currentMusicList.size()) % currentMusicList.size();
                //让media变更,带实现
                if (mediaPlayer != null ) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                }
                try {
                    mediaPlayer.setDataSource(currentMusicList.get(curIndex).songUrl);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                mediaPlayer.prepareAsync();
                return true;
            }else {
                Toast.makeText(getApplicationContext(), "无可播放歌曲", Toast.LENGTH_SHORT).show();
            }
            return false;
        }

        public boolean playNextLocal() {
            if(currentMusicList != null && currentMusicList.size()>0) {
                curIndex = (curIndex + 1) % currentMusicList.size();
                //让media变更,带实现
                if (mediaPlayer != null ) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                }
                try {
                    mediaPlayer.setDataSource(currentMusicList.get(curIndex).songUrl);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
//                timer.cancel();//给旧歌去掉监听
                mediaPlayer.prepareAsync();
                return true;
            }else {
                Toast.makeText(getApplicationContext(), "无可播放歌曲", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    }

    //任何涉及播放的都要先调用！！！
    public static void setCurrentMusicListAndCurIndex(int cur,ArrayList<SongInfo> curList){
        curIndex = cur;
        currentMusicList = curList;
    }
    public static ArrayList<SongInfo> getCurrentMusicList() {
        return currentMusicList;
    }

    public static int getCurIndex() {
        return curIndex;
    }
}
