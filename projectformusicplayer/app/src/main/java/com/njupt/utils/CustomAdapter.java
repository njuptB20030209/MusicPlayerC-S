package com.njupt.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.GridSpanSizeLookup;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.njupt.bean.DataForMulRecycleView;

import com.njupt.listener.ClickEventImplByMainActivity;
import com.njupt.myapplication.R;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class CustomAdapter extends BaseMultiItemQuickAdapter<DataForMulRecycleView, BaseViewHolder> {
    public static final int ITEM_FIRST_LEVEL = 1;//1*1
    public static final int ITEM_SECOND_LEVEL = 2;//1*3

    public static final int ITEM_Third_LEVEL = 3;//1*4
    public static final int ITEM_FORTH_LEVEL = 4;//模块布局

    public static final int ITEM_FIFTH_LEVEL = 5;//评论布局
    Context mContext;

    public CustomAdapter(@Nullable List<DataForMulRecycleView> data, Context Context) {

        super(data);

        mContext = Context;

        addItemType(ITEM_FIRST_LEVEL, R.layout.listitem_1_1_layout);

        addItemType(ITEM_SECOND_LEVEL, R.layout.listitem_1_3_layout);

        addItemType(ITEM_Third_LEVEL, R.layout.listitem_1_4_layout);

        addItemType(ITEM_FORTH_LEVEL, R.layout.listitem_1_1_module_layout);

        addItemType(ITEM_FIFTH_LEVEL, R.layout.listitem_1_1_comment_layout);

    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DataForMulRecycleView dataForMulRecycleView) {

                switch (dataForMulRecycleView.getItemType()) {
                    case ITEM_FIRST_LEVEL:
                        ImageView itemImg = helper.getView(R.id.list_item_1_1_album);
                        Glide.with(mContext)
                                .load(HttpManager.IP_ADDRESS + dataForMulRecycleView.albumUrl)
                                .into(itemImg);
                        helper.setText(R.id.list_item_1_1_songname,dataForMulRecycleView.songname);
                        helper.setText(R.id.list_item_1_1_singername,dataForMulRecycleView.singername);
                        break;
                    case ITEM_SECOND_LEVEL:
                        helper.setText(R.id.list_item_1_3_songname, dataForMulRecycleView.songname);
                        ImageView itemImgb = helper.getView(R.id.list_item_1_3_album);
                        Glide.with(mContext)
                                .load(HttpManager.IP_ADDRESS + dataForMulRecycleView.albumUrl)
                                .into(itemImgb);
                        helper.setText(R.id.list_item_1_3_singername,dataForMulRecycleView.singername);
                        break;
                    case ITEM_Third_LEVEL:
                        helper.setText(R.id.list_item_1_4_songname, dataForMulRecycleView.songname);
                        ImageView itemImgc = helper.getView(R.id.list_item_1_4_album);
                        Glide.with(mContext)
                                .load(HttpManager.IP_ADDRESS + dataForMulRecycleView.albumUrl)
                                .into(itemImgc);
                        helper.setText(R.id.list_item_1_4_singername,dataForMulRecycleView.singername);
                        break;
                    case ITEM_FORTH_LEVEL:
                        helper.setText(R.id.list_item_1_1_module_name, dataForMulRecycleView.moduleName);
                        helper.setBackgroundColor(R.id.item_1_1_module_cardview, Color.TRANSPARENT);
                        break;
                    case ITEM_FIFTH_LEVEL:
                        helper.setText(R.id.list_item_1_1_comment_username, dataForMulRecycleView.singername+":");
                        helper.setText(R.id.list_item_1_1_comment_text,dataForMulRecycleView.moduleName);
                        helper.setBackgroundColor(R.id.item_1_1_comment_cardview, Color.TRANSPARENT);
                        break;
                }
    }

    public static CustomAdapter configureAdapter(Context context, List<DataForMulRecycleView> dataList) {
        // 这里进行 Adapter 的配置
        CustomAdapter adapter = new CustomAdapter(dataList,context);
        adapter.setGridSpanSizeLookup(new GridSpanSizeLookup() {
            @Override
            public int getSpanSize(@NonNull GridLayoutManager gridLayoutManager, int viewType, int position) {
                int spanCount = gridLayoutManager.getSpanCount();
                if(viewType == 1 || viewType == 4 || viewType == 5){
                   //一行一列
                    return spanCount;
                } else if (viewType == 3 ) {
                    //一行4列
                    return spanCount/4;
                }else {
                    return spanCount/3;//一行三列
                }
            }
        });
        return adapter;
    }
}

