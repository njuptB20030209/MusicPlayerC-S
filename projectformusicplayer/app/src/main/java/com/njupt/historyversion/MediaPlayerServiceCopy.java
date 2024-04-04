//package com.njupt.service;
//
//import android.app.Service;
//import android.content.Intent;
//import android.media.MediaPlayer;
//import android.net.Uri;
//import android.os.Binder;
//import android.os.Bundle;
//import android.os.IBinder;
//import android.os.Message;
//
//import androidx.annotation.Nullable;
//
//import com.njupt.message.MessageEvent;
//import com.njupt.myapplication.MusicPlayerMachineActivity;
//
//import java.util.Timer;
//import java.util.TimerTask;
//
//public class MediaPlayerServiceCopy extends Service {
//    MediaPlayer mediaPlayer;//多媒体对象
//    Timer timer;//时钟
//
//
//    public MediaPlayerServiceCopy(){
//
//    }
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return new MusicControl();//绑定服务的时候，把音乐控制类实例化
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        mediaPlayer = new MediaPlayer();//或许可以用mainAcyivity的对象，不可以只能通过Bind
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }
//
//    public void addTimer(){
//        if(timer == null){
//            timer = new Timer();
//            TimerTask task = new TimerTask() {
//                @Override
//                public void run() {
//                    int duration = mediaPlayer.getDuration();//获得歌曲总时长
//                    int currentPos = mediaPlayer.getCurrentPosition();//获得当前播放进度
//                    Message message = MusicPlayerMachineActivity.halder.obtainMessage();
//                    Bundle bundle = new Bundle();
//                    bundle.putInt("duration",duration);
//                    bundle.putInt("currentPosition",currentPos);
//                    message.setData(bundle);
//                    MusicPlayerMachineActivity.halder.sendMessage(message);
//                }
//            };
//            timer.schedule(task,5,500);
//        }
//    }
//    public class MusicControl extends Binder{
//        public void play(){
//            mediaPlayer.reset();
//            mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse("http://192.168.240.125:8080/album/1.webp"));
//            mediaPlayer.start();
//            addTimer();
//        }
//        public void pause(){
//            mediaPlayer.pause();
////            try {
////                timer.wait();//
////            } catch (InterruptedException e) {
////                throw new RuntimeException(e);
////            }//不需要
//        }
//        public void resume(){
//            mediaPlayer.start();

//        }
//        public void stop(){
//            mediaPlayer.stop();
//            timer.cancel();
//        }
//        public void seekTo(int ms){
//            mediaPlayer.seekTo(ms);
//        }
//    }
//
//}
