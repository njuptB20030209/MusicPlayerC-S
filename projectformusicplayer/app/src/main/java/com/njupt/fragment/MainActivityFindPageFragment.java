package com.njupt.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.GridSpanSizeLookup;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.njupt.bean.DataForMulRecycleView;
import com.njupt.bean.MulSongListItem;
import com.njupt.bean.SongInfo;
import com.njupt.listener.ClickEventImplByMainActivity;
import com.njupt.myapplication.R;
import com.njupt.utils.CustomAdapter;
import com.njupt.utils.LoginManager;

import java.util.ArrayList;

public class MainActivityFindPageFragment extends Fragment {
    Button btn_recommend,btn_ranking;
    Context mContext;//跳转新的activity

    private ArrayList<DataForMulRecycleView> recycleViewArrayList;
    RecyclerView recyclerView;
    CustomAdapter adapter;
   SwipeRefreshLayout refreshLayout;


    //设置数据，activity使用
    public void setMulSongListItemArrayList(ArrayList<MulSongListItem> mulSongListItemArrayList) {
        if(mulSongListItemArrayList != null) {
            ArrayList<DataForMulRecycleView> tmp = preProcess(mulSongListItemArrayList);
            this.recycleViewArrayList.addAll(tmp);
            if (  !this.recycleViewArrayList.isEmpty()){
                adapter.setNewData(this.recycleViewArrayList);
                adapter.notifyDataSetChanged();
            }
        }
        else this.recycleViewArrayList = new ArrayList<>();
    }

    private ArrayList<DataForMulRecycleView> preProcess(ArrayList<MulSongListItem> mulSongListItemArrayList) {
            //  对数据重组
        ArrayList<DataForMulRecycleView> dataForMulRecycleViewArrayList = new ArrayList<>();
        for(MulSongListItem mulSongListItem : mulSongListItemArrayList){
            DataForMulRecycleView item = new DataForMulRecycleView(4,mulSongListItem.moduleName);
            dataForMulRecycleViewArrayList.add(item);
            for(SongInfo songInfo : mulSongListItem.songInfoList){
                DataForMulRecycleView tmp = new DataForMulRecycleView(mulSongListItem.style,songInfo);
                dataForMulRecycleViewArrayList.add(tmp);
            }
        }
        return dataForMulRecycleViewArrayList;
    }

    public MainActivityFindPageFragment(Context context,ArrayList<MulSongListItem> mulSongListItemArrayList){
        super(R.layout.fragment_activity_main_findpage);
        this.mContext = context;
        this.recycleViewArrayList = preProcess(mulSongListItemArrayList);
        //解决adapter为null的问题
        adapter = (CustomAdapter) CustomAdapter.configureAdapter(this.mContext, recycleViewArrayList);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
//                Toast.makeText(mContext, "点击了第"+position+"个位置", Toast.LENGTH_SHORT).show();

                ((ClickEventImplByMainActivity) mContext).onClickToPlayAndSetMulSongListToCurrentPlayerList(recycleViewArrayList,position);
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn_ranking = view.findViewById(R.id.find_page_btn_ranking);
        btn_recommend = view.findViewById(R.id.find_page_btn_recommend);
        recyclerView = view.findViewById(R.id.mainActivity_findPageFragment_recyclerview);
        refreshLayout = view.findViewById(R.id.find_page_fragment_refresh_layout);

        setListenerEvent();

        recyclerView.setLayoutManager(new GridLayoutManager(this.mContext, 12));
        recyclerView.setAdapter(adapter);

    }

    private void setListenerEvent(){
        btn_ranking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //写一个接口,让mianactivity实现
                ((ClickEventImplByMainActivity)mContext).onClickToJumpToRankingPlaylist();
            }
        });
        btn_recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(LoginManager.getInstance().isLoggedIn()) {
//                    Toast.makeText(mContext, "跳转每日推荐页面", Toast.LENGTH_SHORT).show();
                    ((ClickEventImplByMainActivity)mContext).onClickToJumpToRecommendActivity("recommend");
                }else{
                    Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show();
                    ((ClickEventImplByMainActivity)mContext).onClickToJumpToLoginActivity();
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // canScrollVertically参数是1是无法上拉
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    ((ClickEventImplByMainActivity)mContext).onLoadingNextPageDataToMulSongList();
                }
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 刷新完成后，通知 SwipeRefreshLayout 停止刷新状态
                        refreshLayout.setRefreshing(false);
                        // 在这里更新数据或者执行其他操作
                        adapter.notifyDataSetChanged();
                        Toast.makeText(mContext, "刷新完成", Toast.LENGTH_SHORT).show();
                    }
                }, 1000); // 1 秒后停止刷新，模拟刷新过程
            }
        });

    }
}
