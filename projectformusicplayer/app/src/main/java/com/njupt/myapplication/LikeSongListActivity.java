package com.njupt.myapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.njupt.bean.DataForMulRecycleView;
import com.njupt.bean.SongInfo;
import com.njupt.bean.toparsejson.SearchResultList;
import com.njupt.fragment.MusicPlayerBarFragment;
import com.njupt.listener.ClickEventImplByLikeSongActivity;
import com.njupt.service.MediaPlayerService;
import com.njupt.utils.CustomAdapter;
import com.njupt.utils.HttpManager;
import com.njupt.utils.LoginManager;

import java.io.IOException;
import java.util.ArrayList;

public class LikeSongListActivity extends AppCompatActivity implements ClickEventImplByLikeSongActivity {
    InnerHandler handler;
    HttpManager httpManager;

    TextView title;
    RecyclerView recyclerView;
    CustomAdapter adapter;

    ArrayList<DataForMulRecycleView> dataForMulRecycleViews ; //交给adapter的数据
    ArrayList<SongInfo> songInfos; //接收联网数据

    MusicPlayerBarFragment musicPlayerBarFragment;
    MediaPlayerService.MusicControl control;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            control = (MediaPlayerService.MusicControl) service;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_song_list);

        title = findViewById(R.id.activity_like_text);
        recyclerView = findViewById(R.id.likeActivity_recyclerview);
        dataForMulRecycleViews = new ArrayList<>();
        songInfos  = new ArrayList<>();

        musicPlayerBarFragment = new MusicPlayerBarFragment();

        //设置了事件
        adapter = CustomAdapter.configureAdapter(LikeSongListActivity.this,dataForMulRecycleViews);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                //当前音乐传入播放器fragment,让音乐播放
                musicPlayerBarFragment.setCurrentMusicPlayerAndPlay(position,songInfos);
                //如何让它播放？
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(LikeSongListActivity.this, 12));
        recyclerView.setAdapter(adapter);


        handler = new InnerHandler();
        httpManager = new HttpManager() ;

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.like_activity_musicPlayerBar, musicPlayerBarFragment)
                .commit();

        //根据传过来的数据，决定是每日推荐还是我喜欢。
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String type = extras.getString("TYPE"); // 根据键获取值
            if(type != null && type.equals("recommend")){
                //设置标题为每日推荐，获取每日推荐数据
                String s = LoginManager.getInstance().getUsername()+"的每日推荐";
                title.setText(s);
                try {
                    httpManager.findSongByUid(LoginManager.getInstance().getUid(),handler);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else   {
                //设置标题为我喜欢，获取我喜欢数据
                String s = LoginManager.getInstance().getUsername()+"的我喜欢歌单";
                title.setText(s);
                try {
                    httpManager.findSongByUid(LoginManager.getInstance().getUid(), handler);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        //绑定服务
        Intent intent = new Intent(LikeSongListActivity.this, MediaPlayerService.class);
        bindService(intent,connection, Context.BIND_AUTO_CREATE);
    }

    private ArrayList<DataForMulRecycleView> preProcess(ArrayList<SongInfo> searchMusicResultList) {
        ArrayList<DataForMulRecycleView> data = new ArrayList<>();
        for(SongInfo item : searchMusicResultList){
            DataForMulRecycleView tmp = new DataForMulRecycleView(1,item);
            data.add(tmp);
        }
        return data;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解绑服务
        unbindService(connection);
    }

    @Override
    public void onClickToJumpToMusicPlayerMachineActivity(int curIndex) {
        if(curIndex != -1) {
            Intent intent = new Intent(LikeSongListActivity.this, MusicPlayerMachineActivity.class);
            startActivity(intent);
        }else {
            Toast.makeText(this, "无歌曲播放，不可跳转", Toast.LENGTH_SHORT).show();
        }    //
    }

    public class InnerHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
              songInfos = ((SearchResultList.InnerClass)  msg.obj).songList;
              dataForMulRecycleViews = preProcess(songInfos);
              adapter.setNewData(dataForMulRecycleViews);
              adapter.notifyDataSetChanged();
            }else {
                Toast.makeText(LikeSongListActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
