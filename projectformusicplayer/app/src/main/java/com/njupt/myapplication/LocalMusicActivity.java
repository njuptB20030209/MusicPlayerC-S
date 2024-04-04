package com.njupt.myapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings;
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
import com.njupt.fragment.MusicPlayerBarFragment;
import com.njupt.listener.ClickEventImplByLocalActivity;
import com.njupt.service.MediaPlayerService;
import com.njupt.utils.CustomAdapter;
import com.njupt.utils.HttpManager;
import com.njupt.utils.MusicUtils;

import java.util.ArrayList;
import java.util.List;

public class LocalMusicActivity extends AppCompatActivity implements ClickEventImplByLocalActivity {

    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    TextView title;
    RecyclerView recyclerView;
    CustomAdapter adapter;

    ArrayList<DataForMulRecycleView> dataForMulRecycleViews ; //交给adapter的数据
    ArrayList<SongInfo> songInfos; //获取本地数据

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

        if(hasStoragePermission()){
             queryAudioFiles();
        }else {
            requestStoragePermission();
        }
        if(songInfos != null && songInfos.size()>0){
            Toast.makeText(this, "查询到"+songInfos.size()+"条音乐", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "无本地音乐", Toast.LENGTH_SHORT).show();
        }

        title = findViewById(R.id.activity_like_text);
        recyclerView = findViewById(R.id.likeActivity_recyclerview);
        dataForMulRecycleViews = preProcess(songInfos);
        musicPlayerBarFragment = new MusicPlayerBarFragment();

        title.setText("本地音乐");

        //设置了事件
        adapter = CustomAdapter.configureAdapter(LocalMusicActivity.this,dataForMulRecycleViews);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                //当前音乐传入播放器fragment,让音乐播放
                musicPlayerBarFragment.setCurrentMusicPlayerAndPlay(position,songInfos);
                //如何让它播放？
//                Toast.makeText(LocalMusicActivity.this, "clickitem", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(LocalMusicActivity.this, 12));
        recyclerView.setAdapter(adapter);

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.like_activity_musicPlayerBar, musicPlayerBarFragment)
                .commit();

        //绑定服务
        Intent intent = new Intent(LocalMusicActivity.this, MediaPlayerService.class);
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
    // 处理权限请求结果
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION) {
            if (hasStoragePermission()) {
                // 已经获取了管理外部存储的权限
                // 执行需要存储权限的操作
                queryAudioFiles();
            } else {
                // 未获取权限
                // 可以根据需要进行处理
                Toast.makeText(this, "未获取权限", Toast.LENGTH_SHORT).show();
            }
        }
    }
    // 检查是否具有管理外部存储的权限
    private boolean hasStoragePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            return true; // 在Android 10及之前的版本中，无需检查权限
        }
        return Environment.isExternalStorageManager();
    }

    // 请求管理外部存储的权限
    private void requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, REQUEST_CODE_STORAGE_PERMISSION);
            } catch (Exception e) {
                // 处理异常
                e.printStackTrace();
            }
        } else {
            // 在Android 10及之前的版本中，无需请求权限
            // 可以直接执行需要存储权限的操作
            queryAudioFiles();
        }
    }

    // 查询音频文件的方法
    private void queryAudioFiles() {
        songInfos = (ArrayList<SongInfo>) MusicUtils.getAllLocalMusic(getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    @Override
    public void onClickToJumpToMusicPlayerMachineActivity(int curIndex) {
        if(curIndex != -1) {
            Intent intent = new Intent(LocalMusicActivity.this, MusicPlayerMachineActivity.class);
            startActivity(intent);
        }else {
            Toast.makeText(this, "无歌曲播放，不可跳转", Toast.LENGTH_SHORT).show();
        }
    }
}
