package com.njupt.bean.toparsejson;

import com.njupt.bean.SongInfo;

import java.util.ArrayList;

/**
 {"code":200,
 "message":"success",
 "data":{"songList":[
            {"songId":6,"songname":"曾经的你","singername":"许巍","duration":"04:21:00",
        "songUrl":"http://192.168.240.125:8080/music/6.mp3","albumUrl":"http://192.168.240.125:8080/album/6.webp"},
            {"songId":9,"songname":"玫瑰花的葬礼","singername":"许嵩","duration":"04:18:00",
        "songUrl":"http://192.168.240.125:8080/music/9.mp3","albumUrl":"http://192.168.240.125:8080/album/9.webp"}
                ]
        }
 }

 */

//解析json数据
public class SearchResultList {
    public int code;

    public InnerClass data;

    public class InnerClass {
        public  ArrayList<SongInfo> songList;
    }
}
