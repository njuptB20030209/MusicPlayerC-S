package com.njupt.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
 public class SongInfo {
        public int songId;
        public String songname;
        public  String singername;
        public String duration;
        public  String songUrl;
        public  String albumUrl;

    }
 */

//用于显示recycleview列表
public class DataForMulRecycleView implements MultiItemEntity, Serializable {

    public DataForMulRecycleView(){}


    public DataForMulRecycleView(int style,SongInfo item){
        this.style = style;
        moduleName = "默认";
        songname = item.songname;
        singername = item.singername;
        songId = item.songId;
        duration = item.duration;
        albumUrl = item.albumUrl;
        songUrl = item.songUrl;
    }


    public DataForMulRecycleView(int style,String moduleName){
        this.style = style;
        this.moduleName = moduleName;
        songname = " ";
        singername = " ";
        songId = -1;
        duration = " ";
        albumUrl = " ";
        songUrl = " ";
    }

    @SerializedName("style")
    public int  style;//布局风格
    @SerializedName("moduleName")
    public String moduleName;//模块名，兼做评论内容
    @SerializedName("songId")
    public int songId;
    @SerializedName("songname")
    public String songname;
    @SerializedName("singername")//歌手，兼做评论用户名
    public  String singername;
    @SerializedName("duration")
    public String duration;
    @SerializedName("songUrl")
    public  String songUrl;
    @SerializedName("albumUrl")
    public  String albumUrl;

    public DataForMulRecycleView(int style, UserCommentItem item) {
        this.style = style;
        this.moduleName = item.comment;
        songname = " ";
        singername = item.username;
        songId = item.songId;
        duration = " ";
        albumUrl = " ";
        songUrl = " ";
    }

    @Override
    public int getItemType() {
        return style;
    }
}
