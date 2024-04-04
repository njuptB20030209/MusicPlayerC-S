package com.njupt.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.njupt.bean.SongInfo;

import java.util.ArrayList;
import java.util.List;

public class MusicUtils {

    // 获取本地音乐
    public static List<SongInfo> getAllLocalMusic(Context context) {
        List<SongInfo> musicList = new ArrayList<>();
//        String selection = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
//        String[] selectionArgs = new String[]{"audio/mpeg"};

        // 查询音乐
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI; // 修改为外部存储的 URI
//        Cursor cursor = contentResolver.query(uri, null, selection, selectionArgs, null);
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // 获取音乐信息
                @SuppressLint("Range")
                long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                @SuppressLint("Range")
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                @SuppressLint("Range")
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                @SuppressLint("Range")
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                @SuppressLint("Range")
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                @SuppressLint("Range")
                long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));

                // 创建 Music 对象并添加到列表中
                if(duration > 36000) {
                    SongInfo music = new SongInfo((int) id, title, artist, duration + "", path, album);
                    musicList.add(music);
                }
            } while (cursor.moveToNext() && musicList.size()<10);

            // 关闭游标
            cursor.close();
        }

        return musicList;
    }
}