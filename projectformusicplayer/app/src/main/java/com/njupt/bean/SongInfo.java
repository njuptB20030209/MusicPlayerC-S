package com.njupt.bean;

import androidx.annotation.Nullable;

import java.util.Objects;

//基本歌曲信息,需要被process转换成DataForMulRecycleview
//style=1
public class SongInfo {
    public int songId;
    public String songname;
    public String singername;
    public String duration;
    public String songUrl;
    public String albumUrl;

    public SongInfo() {
    }

    public SongInfo(int songId, String songname, String singername, String duration, String songUrl, String albumUrl) {
        this.songId = songId;
        this.songname = songname;
        this.singername = singername;
        this.duration = duration;
        this.songUrl = songUrl;
        this.albumUrl = albumUrl;
    }

    public SongInfo(DataForMulRecycleView item){
        songId = item.songId;
        songname = item.songname;
        singername = item.singername;
        duration = item.duration;
        songUrl = item.songUrl;
        albumUrl = item.albumUrl;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SongInfo songInfo = (SongInfo) o;
        return songId == songInfo.songId && Objects.equals(songname, songInfo.songname) && Objects.equals(singername, songInfo.singername) && Objects.equals(duration, songInfo.duration) && Objects.equals(songUrl, songInfo.songUrl) && Objects.equals(albumUrl, songInfo.albumUrl);
    }
    @Override
    public int hashCode() {
        return Objects.hash(songId, songname, singername, duration, songUrl, albumUrl);
    }



}
