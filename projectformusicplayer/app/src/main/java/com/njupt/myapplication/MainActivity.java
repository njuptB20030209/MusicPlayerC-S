package com.njupt.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.njupt.bean.DataForMulRecycleView;
import com.njupt.bean.MulSongListItem;
import com.njupt.bean.toparsejson.MulStyleListData;
import com.njupt.bean.toparsejson.SearchResultList;
import com.njupt.bean.SongInfo;
import com.njupt.fragment.MainActivityFindPageFragment;
import com.njupt.fragment.MainActivityMainPageFragment;
import com.njupt.fragment.MainActivityMyPageFragment;
import com.njupt.fragment.MusicPlayerBarFragment;
import com.njupt.listener.ClickEventImplByMainActivity;
import com.njupt.service.MediaPlayerService;
import com.njupt.utils.HttpManager;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ClickEventImplByMainActivity {
    ArrayList<SongInfo> currentMusicPlayerList;//传给musicplayerfragment和MainPageFragment
    ArrayList<SongInfo> searchMusicResultList;//传给搜索音乐后的播放列表

    ArrayList<MulSongListItem> mulSongListItemArrayList;//从网络获取，传给发现fragment页面
    Button clickToMainPage,clickToFindPage,clickToMyPage;

    MainActivityMainPageFragment mainActivityMainPageFragment;
    MainActivityFindPageFragment mainActivityFindPageFragment;

    MainActivityMyPageFragment mainActivityMyPageFragment;

    MusicPlayerBarFragment musicPlayerBarFragment;
    HttpManager httpManager ;
    private InnerHandler handler;
//    MediaPlayerService.MusicControl control;
//    private ServiceConnection connection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//             control = (MediaPlayerService.MusicControl) service;
//        }
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//
//        }
//    };
    int page_mulSongList = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        addListenerEvent();

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.main_fragment_top, mainActivityMainPageFragment)
                .commit();


        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_musicPlayerBar, musicPlayerBarFragment)
                .commit();
    }

    private void initView() {

        clickToMainPage = findViewById(R.id.clickToMainPage);
        clickToFindPage = findViewById(R.id.clickToFindPage);
        clickToMyPage = findViewById(R.id.clickToMyPage);

        searchMusicResultList = new ArrayList<>();
        currentMusicPlayerList = new ArrayList<>();
        mulSongListItemArrayList = new ArrayList<>();


        mainActivityMainPageFragment = new MainActivityMainPageFragment(this,this.currentMusicPlayerList);

        mainActivityFindPageFragment = new MainActivityFindPageFragment(this,this.mulSongListItemArrayList);

        mainActivityMyPageFragment = new MainActivityMyPageFragment();

        musicPlayerBarFragment = new MusicPlayerBarFragment();

        handler = new InnerHandler();
        httpManager = new HttpManager();
    }
    private void addListenerEvent(){
        //切换mainpage的fragment
        clickToMainPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.main_fragment_top, mainActivityMainPageFragment)
                        .commit();
                mainActivityMainPageFragment.setSearchMusicResultList(currentMusicPlayerList);
            }
        });
        clickToFindPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    httpManager.getMulSongList(page_mulSongList+1,handler);//传个参数page_mulSongList=1
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.main_fragment_top, mainActivityFindPageFragment)
                        .commit();
            }
        });
        clickToMyPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "我的", Toast.LENGTH_SHORT).show();
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.main_fragment_top, mainActivityMyPageFragment)
                    .commit();

            }
        });
    }

    @Override
    public void onClickToSearchBySongname(String songname) {
        //发起网络请求,给mainfragment搜索框的
        try {
            httpManager.findSongByName(songname,handler);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onClickToPlayAndSetSearchListToCurrentPlayerList(int position) {
        currentMusicPlayerList = searchMusicResultList;//两者其实一样
        //当前音乐传入播放器fragment,让音乐播放
        musicPlayerBarFragment.setCurrentMusicPlayerAndPlay(position,currentMusicPlayerList);
        //如何让它播放？
    }

    @Override
    public void onClickToJumpToMusicPlayerMachineActivity(int curIndex) {
        //跳转音乐播放器activity,同时保持音乐播放
        if(curIndex != -1) {
            Intent intent = new Intent(MainActivity.this, MusicPlayerMachineActivity.class);
            startActivity(intent);
        }else {
            Toast.makeText(this, "无歌曲播放，不可跳转", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClickToPlayAndSetMulSongListToCurrentPlayerList(ArrayList<DataForMulRecycleView> recycleViewArrayList, int position) {
//        Toast.makeText(this, "将点击的项目判断，歌曲追加，模块名无反应", Toast.LENGTH_SHORT).show();
        DataForMulRecycleView dataForMulRecycleView = recycleViewArrayList.get(position);
        if (dataForMulRecycleView.style != 4) {
            SongInfo item = new SongInfo(dataForMulRecycleView);
            if (currentMusicPlayerList == null) currentMusicPlayerList = new ArrayList<>();
            //去重
            if (currentMusicPlayerList.contains(item)) {
                // 如果元素已经存在于列表中，则将其移除
                currentMusicPlayerList.remove(item);
            }
            // 将元素添加到列表末尾
            currentMusicPlayerList.add(item);
            musicPlayerBarFragment.setCurrentMusicPlayerAndPlay(currentMusicPlayerList.size() - 1, currentMusicPlayerList);
        }
    }

    @Override
    public void onLoadingNextPageDataToMulSongList() {
        if(page_mulSongList >= 3){
            Toast.makeText(this, "数据已全部加载", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "加载第"+(page_mulSongList+1)+"页", Toast.LENGTH_SHORT).show();
            try {
                httpManager.getMulSongList(page_mulSongList+1,handler);//page_mulSongList
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onClickToJumpToRankingPlaylist() {
        Intent intent = new Intent(MainActivity.this, RankingPlaylistActivity.class);
        startActivity(intent);
        //之后应该没什么操作
    }

    @Override
    public void onClickToJumpToLoginActivity() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClickToJumpToRecommendActivity(String type) {
        Intent intent = new Intent(MainActivity.this, LikeSongListActivity.class);
        intent.putExtra("TYPE", type); // 发送信息，
        startActivity(intent);
    }

    @Override
    public void onClickToJumpToLocalMusicActivity() {
        Intent intent = new Intent(MainActivity.this, LocalMusicActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        currentMusicPlayerList = MediaPlayerService.getCurrentMusicList();
        searchMusicResultList = currentMusicPlayerList;
    }



    //处理网络请求
    public class InnerHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 200) {
                //网络将数据放入message
                searchMusicResultList = ((SearchResultList.InnerClass) msg.obj).songList;
                //处理数据
                currentMusicPlayerList = searchMusicResultList;

                if(currentMusicPlayerList.size()>0) {
                    mainActivityMainPageFragment.setSearchMusicResultList(currentMusicPlayerList);
                }else{
                    Toast.makeText(MainActivity.this, "未搜到歌曲", Toast.LENGTH_SHORT).show();
                }
            } else if (msg.what == 201) {
                mulSongListItemArrayList = ((MulStyleListData.InnerClass) msg.obj).records;
                if(mulSongListItemArrayList.size()>0) {
                    //此方法会产生空指针异常
                    page_mulSongList++;
                    mainActivityFindPageFragment.setMulSongListItemArrayList(mulSongListItemArrayList);
                }else{
                    Toast.makeText(MainActivity.this, "未获取到数据", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
            }
        }
    }
}