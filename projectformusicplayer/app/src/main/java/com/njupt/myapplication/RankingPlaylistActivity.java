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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.njupt.bean.DataForMulRecycleView;
import com.njupt.bean.SongInfo;
import com.njupt.bean.toparsejson.MulStyleListData;
import com.njupt.bean.toparsejson.SearchResultList;
import com.njupt.fragment.MusicPlayerBarFragment;
import com.njupt.listener.CilckEventImplByRankingActivity;
import com.njupt.listener.ClickEventImplByMainActivity;
import com.njupt.service.MediaPlayerService;
import com.njupt.utils.CustomAdapter;
import com.njupt.utils.HttpManager;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 排行榜activity，默认显示热歌榜信息
 */
public class RankingPlaylistActivity extends AppCompatActivity implements CilckEventImplByRankingActivity {

    ArrayList<SongInfo> currentMusicPlayerList;
    ArrayList<SongInfo> searchMusicResultList;//传给搜索音乐后的播放列表
    ArrayList<DataForMulRecycleView> recycleViewArrayList;//用于显示列表
    CustomAdapter adapter;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    InnerHandler handler;
    HttpManager httpManager ;

    MusicPlayerBarFragment musicPlayerBarFragment;

    Button btn_hot,btn_new,btn_innovation;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_playlist);

        initView();

        setListenrEvent();

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.activity_ranking_musicPlayerBar, musicPlayerBarFragment)
                .commit();

        try {
            httpManager.getSongRanking(1,handler);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //要写个preProcess函数
        adapter = (CustomAdapter) CustomAdapter.configureAdapter(this, recycleViewArrayList);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
//                Toast.makeText(RankingPlaylistActivity.this, "点击了第"+position+"个位置", Toast.LENGTH_SHORT).show();
                musicPlayerBarFragment.setCurrentMusicPlayerAndPlay(position,currentMusicPlayerList);
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(this, 12));
        recyclerView.setAdapter(adapter);

    }

    private void initView() {
        btn_hot=findViewById(R.id.activity_ranking_playlist_btn_hot);
        btn_new=findViewById(R.id.activity_ranking_playlist_btn_new);
        btn_innovation=findViewById(R.id.activity_ranking_playlist_btn_innovation);
        recyclerView = findViewById(R.id.rankingActivity_recyclerview);
        swipeRefreshLayout = findViewById(R.id.ranking_playlist_activity_refresh_layout);
        handler = new InnerHandler();
        httpManager = new HttpManager();
        musicPlayerBarFragment = new MusicPlayerBarFragment();
        recycleViewArrayList = new ArrayList<>();
        searchMusicResultList = new ArrayList<>();
    }

    private void setListenrEvent() {
        btn_hot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    httpManager.getSongRanking(1,handler);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    httpManager.getSongRanking(2,handler);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        btn_innovation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    httpManager.getSongRanking(3,handler);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 刷新完成后，通知 SwipeRefreshLayout 停止刷新状态
                        swipeRefreshLayout.setRefreshing(false);
                        // 在这里更新数据或者执行其他操作
                        Toast.makeText(RankingPlaylistActivity.this, "刷新完成", Toast.LENGTH_SHORT).show();
                    }
                }, 1000); // 1 秒后停止刷新，模拟刷新过程
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClickToJumpToMusicPlayerMachineActivity(int curIndex) {
        if(curIndex != -1) {
            Intent intent = new Intent(RankingPlaylistActivity.this, MusicPlayerMachineActivity.class);
            startActivity(intent);
        }else {
            Toast.makeText(this, "无歌曲播放，不可跳转", Toast.LENGTH_SHORT).show();
        }        //跳到专辑页
    }

    public class InnerHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 200) {
              //返回数据
                //网络将数据放入message
                searchMusicResultList = ((SearchResultList.InnerClass) msg.obj).songList;
                //处理数据
                currentMusicPlayerList = searchMusicResultList;

                if(searchMusicResultList!=null && searchMusicResultList.size()>0) {
                    //将数据放入adapter,notify
                    recycleViewArrayList = preProcess(searchMusicResultList);
                    if(recycleViewArrayList != null) {
                        adapter.setNewData(recycleViewArrayList);
                        adapter.notifyDataSetChanged();
                    }
                }else{
                    Toast.makeText(RankingPlaylistActivity.this, "未搜到歌曲", Toast.LENGTH_SHORT).show();
                }
            } else if (msg.what == 201) {

            } else {

            }
        }
    }

    private ArrayList<DataForMulRecycleView> preProcess(ArrayList<SongInfo> searchMusicResultList) {
        ArrayList<DataForMulRecycleView> data = new ArrayList<>();
        for(SongInfo item : searchMusicResultList){
            DataForMulRecycleView tmp = new DataForMulRecycleView(1,item);
            data.add(tmp);
        }
        return data;
    }
}
