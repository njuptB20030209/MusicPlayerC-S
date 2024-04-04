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
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.njupt.bean.DataForMulRecycleView;
import com.njupt.bean.SongInfo;
import com.njupt.listener.ClickEventImplByMainActivity;
import com.njupt.myapplication.R;
import com.njupt.utils.CustomAdapter;

import java.util.ArrayList;

public class MainActivityMainPageFragment extends Fragment {

    private Context mContext;
    private ArrayList<DataForMulRecycleView> recycleViewArrayList;
    EditText searchEditText;
    Button searchButton;

    RecyclerView recyclerView;
    CustomAdapter adapter;

    SwipeRefreshLayout refreshLayout;

    public MainActivityMainPageFragment(Context context, ArrayList<SongInfo> searchMusicResultList) {
        super(R.layout.fragment_activity_main_mainpage);
        mContext = context;
        recycleViewArrayList = preProcess(searchMusicResultList);
        adapter = (CustomAdapter) CustomAdapter.configureAdapter(this.mContext, recycleViewArrayList);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
//                Toast.makeText(mContext, "点击了第"+position+"个位置", Toast.LENGTH_SHORT).show();
                ((ClickEventImplByMainActivity) mContext).onClickToPlayAndSetSearchListToCurrentPlayerList(position);
            }
        });
    }

    public void setSearchMusicResultList(ArrayList<SongInfo> searchMusicResultList) {
        if(searchMusicResultList != null) {
            this.recycleViewArrayList = preProcess(searchMusicResultList);
            if (!this.recycleViewArrayList.isEmpty()){
                adapter.setNewData(this.recycleViewArrayList);
                adapter.notifyDataSetChanged();
            }
        }
        else this.recycleViewArrayList = new ArrayList<>();
    }

    /**
     * 对从网络中获取的数据进行解析，使其符合BaseMultiItemQuickAdapter的数据格式
     * @param searchMusicResultList
     * @return
     */
    private ArrayList<DataForMulRecycleView> preProcess(ArrayList<SongInfo> searchMusicResultList) {
        ArrayList<DataForMulRecycleView> data = new ArrayList<>();
        for(SongInfo item : searchMusicResultList){
            DataForMulRecycleView tmp = new DataForMulRecycleView(1,item);
            data.add(tmp);
        }
        return data;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        //获取activity上下文
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //获取layout的view控件
        searchEditText = view.findViewById(R.id.mainActivity_mainPageFragment_SearchEditText);
        searchButton = view.findViewById(R.id.mainActivity_mainPageFragment_SearchButton);
        refreshLayout = view.findViewById(R.id.main_page_fragment_refresh_layout);
        recyclerView = view.findViewById(R.id.mainActivity_mainPageFragment_recyclerview);

        setListenerEvent();

        recyclerView.setLayoutManager(new GridLayoutManager(this.mContext, 12));
        recyclerView.setAdapter(adapter);
    }

    private void setListenerEvent(){
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String songName = String.valueOf(searchEditText.getText());
                if(songName.equals("")){
                    songName = searchEditText.getHint().toString();
                }
                ((ClickEventImplByMainActivity)mContext).onClickToSearchBySongname(songName);
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

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}
