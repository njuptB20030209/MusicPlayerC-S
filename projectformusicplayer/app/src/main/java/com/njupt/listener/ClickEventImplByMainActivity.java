package com.njupt.listener;

import com.njupt.bean.DataForMulRecycleView;

import java.util.ArrayList;

public interface ClickEventImplByMainActivity {
    void onClickToSearchBySongname(String songname);//实现搜索框网络请求
    void onClickToPlayAndSetSearchListToCurrentPlayerList(int position);//实现搜索结果点击播放

    void onClickToJumpToMusicPlayerMachineActivity(int curIndex);//实现跳转音乐播放器页面

    void onClickToPlayAndSetMulSongListToCurrentPlayerList(ArrayList<DataForMulRecycleView> recycleViewArrayList, int position);//实现多布局列表点击播放

    void onLoadingNextPageDataToMulSongList();//加载下一页多布局数据

    void onClickToJumpToRankingPlaylist();

    void onClickToJumpToLoginActivity();

    void onClickToJumpToRecommendActivity(String type);//跳转每日推荐页面

    void onClickToJumpToLocalMusicActivity();//跳转每日推荐页面
}
