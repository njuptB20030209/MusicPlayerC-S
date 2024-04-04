package com.njupt.utils;

import android.os.Message;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.njupt.bean.toparsejson.ComentResult;
import com.njupt.bean.toparsejson.LoginOrRegistResult;
import com.njupt.bean.toparsejson.MulStyleListData;
import com.njupt.bean.toparsejson.SearchResultList;
import com.njupt.myapplication.CommentInfoActivity;
import com.njupt.myapplication.LikeSongListActivity;
import com.njupt.myapplication.LoginActivity;
import com.njupt.myapplication.MainActivity;
import com.njupt.myapplication.MusicPlayerMachineActivity;
import com.njupt.myapplication.RankingPlaylistActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpManager {

   public static final String IP_ADDRESS = "http://192.168.240.125:8080/";
    private Gson gson = new Gson();
    private OkHttpClient okHttpClient = new OkHttpClient()
            .newBuilder()
            .build();

    //根据歌名搜索音乐
    public void findSongByName(String songname, MainActivity.InnerHandler handler) throws IOException {

        String URL = IP_ADDRESS + "SongInfo/findSongByName";
        Message obtain = new Message();
        Request request = new Request.Builder().get()
                .url(URL).build();
        //加入搜索参数
        Request.Builder requestBuilder = request.newBuilder();
        HttpUrl.Builder httpUrlBuilder = request.url().newBuilder();
        httpUrlBuilder.addQueryParameter("songname",songname);//msg=晴天&type=mv&n=0
        requestBuilder.url(httpUrlBuilder.build());
        request = requestBuilder.build();

        //结果在回调里面，放入handler消息队列
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                obtain.what=-1;
                obtain.obj = null;
                handler.sendMessage(obtain);
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    ResponseBody body = response.body();
                    if (body != null) {
                        String string = body.string();
                        SearchResultList searchResultList = new SearchResultList();
                        searchResultList = gson.fromJson(string, SearchResultList.class);
                        //传送数据
                        obtain.what=200;
                        obtain.obj = searchResultList.data;
                        handler.sendMessage(obtain);
                    }else {
                        obtain.what=-1;
                        obtain.obj = null;
                        handler.sendMessage(obtain);
                    }
                }else {
                    obtain.what=-1;
                    obtain.obj = null;
                    handler.sendMessage(obtain);
                }
            }
        });
    }


    //得到排行榜数据
    public void getSongRanking(int id, RankingPlaylistActivity.InnerHandler handler) throws IOException {
        String URL = IP_ADDRESS + "SongInfo/getSongRanking";
        Message obtain = new Message();
        Request request = new Request.Builder().get()
                .url(URL).build();
        //加入搜索参数
        Request.Builder requestBuilder = request.newBuilder();
        HttpUrl.Builder httpUrlBuilder = request.url().newBuilder();
        httpUrlBuilder.addQueryParameter("id", String.valueOf(id));
        requestBuilder.url(httpUrlBuilder.build());
        request = requestBuilder.build();

        //结果在回调里面，放入handler消息队列
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                obtain.what=-1;
                obtain.obj = null;
                handler.sendMessage(obtain);
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if(response.isSuccessful()){
                    ResponseBody body = response.body();
                    if (body != null) {
                        String string = body.string();
                        SearchResultList searchResultList = new SearchResultList();
                        searchResultList = gson.fromJson(string, SearchResultList.class);
                        //传送数据
                        obtain.what=200;
                        obtain.obj = searchResultList.data;
                        handler.sendMessage(obtain);
                    }else {
                        obtain.what=-1;
                        obtain.obj = null;
                        handler.sendMessage(obtain);
                    }
                }else {
                    obtain.what=-1;
                    obtain.obj = null;
                    handler.sendMessage(obtain);
                }
            }
        });
    }

    //获取我喜欢歌单
    public void findSongByUid(int uid, LikeSongListActivity.InnerHandler handler) throws IOException {
        String URL = IP_ADDRESS +  "SongInfo/findSongByUid";
        Message obtain = new Message();
        Request request = new Request.Builder().get()
                .url(URL).build();
        //加入搜索参数
        Request.Builder requestBuilder = request.newBuilder();
        HttpUrl.Builder httpUrlBuilder = request.url().newBuilder();
        httpUrlBuilder.addQueryParameter("uid", String.valueOf(uid));//msg=晴天&type=mv&n=0
        requestBuilder.url(httpUrlBuilder.build());
        request = requestBuilder.build();

        //结果在回调里面，放入handler消息队列
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                obtain.what=-1;
                obtain.obj = null;
                handler.sendMessage(obtain);
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if(response.isSuccessful()){
                    ResponseBody body = response.body();
                    if (body != null) {
                        String string = body.string();
                        SearchResultList searchResultList = new SearchResultList();
                        searchResultList = gson.fromJson(string, SearchResultList.class);
                        //传送数据
                        obtain.what=200;
                        obtain.obj = searchResultList.data;
                        handler.sendMessage(obtain);
                    }else {
                        obtain.what=-1;
                        obtain.obj = null;
                        handler.sendMessage(obtain);
                    }
                }else {
                    obtain.what=-1;
                    obtain.obj = null;
                    handler.sendMessage(obtain);
                }
            }
        });
    }

    //获取多类型列表，使用新的解析数据bean
    public void getMulSongList(int page, MainActivity.InnerHandler handler) throws IOException {
        String URL = IP_ADDRESS +  "SongInfo/getMulSongList";
        Message obtain = new Message();
        Request request = new Request.Builder().get()
                .url(URL).build();
        //加入搜索参数
        Request.Builder requestBuilder = request.newBuilder();
        HttpUrl.Builder httpUrlBuilder = request.url().newBuilder();
        httpUrlBuilder.addQueryParameter("page", String.valueOf(page));
        requestBuilder.url(httpUrlBuilder.build());
        request = requestBuilder.build();

        //结果在回调里面，放入handler消息队列
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                obtain.what=-1;
                obtain.obj = null;
                handler.sendMessage(obtain);
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if(response.isSuccessful()){
                    ResponseBody body = response.body();
                    if (body != null) {
                        String string = body.string();
                        MulStyleListData mulStyleListData = new MulStyleListData();
                        mulStyleListData = gson.fromJson(string, MulStyleListData.class);
//                        //传送数据
                        obtain.what=201;
                        obtain.obj = mulStyleListData.data;
                        handler.sendMessage(obtain);
                    }else {
                        obtain.what=-1;
                        obtain.obj = null;
                        handler.sendMessage(obtain);
                    }
                }else {
                    obtain.what=-1;
                    obtain.obj = null;
                    handler.sendMessage(obtain);
                }
            }
        });
    }

    //登录,返回新的dataBean
    public void login(String username , String  userPwd, LoginActivity.InnerHandler handler) throws IOException {
        String URL = IP_ADDRESS + "AppUser/login";
        Message obtain = new Message();
        Request request = new Request.Builder().get()
                .url(URL).build();
        //加入搜索参数
        Request.Builder requestBuilder = request.newBuilder();
        HttpUrl.Builder httpUrlBuilder = request.url().newBuilder();
        httpUrlBuilder.addQueryParameter("username", username);
        httpUrlBuilder.addQueryParameter("userPwd", userPwd);
        requestBuilder.url(httpUrlBuilder.build());
        request = requestBuilder.build();

        //结果在回调里面，放入handler消息队列
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                obtain.what=-1;
                obtain.obj = null;
                handler.sendMessage(obtain);
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    ResponseBody body = response.body();
                    if (body != null) {
                        String string = body.string();
                        LoginOrRegistResult loginOrRegistResult = new LoginOrRegistResult();
                        loginOrRegistResult = gson.fromJson(string, LoginOrRegistResult.class);
//                        //传送数据
                        int code = loginOrRegistResult.code;
                        if(code==200) obtain.what=200;
                        else if (code == 501) obtain.what=201;
                        else obtain.what=202;
                        //返回结果时自己判断，跟据loginOrRegistResult的状态码
                        obtain.obj = loginOrRegistResult.data;//错误，完整传入包括状态码
                        handler.sendMessage(obtain);
                    }else {
                        obtain.what=-1;
                        obtain.obj = null;
                        //返回联网失败的相关
                        handler.sendMessage(obtain);
                    }
                }else {
                    obtain.what=-1;
                    obtain.obj = null;
                    //返回联网失败的相关
                    handler.sendMessage(obtain);
                }
            }
        });
    }

    //注册,不返回数据，只返回状态码
    public void register(String username , String  userPwd, LoginActivity.InnerHandler handler) throws IOException {
        String URL = IP_ADDRESS + "AppUser/regist";
        Message obtain = new Message();
        Request request = new Request.Builder().get()
                .url(URL).build();
        //加入搜索参数
        Request.Builder requestBuilder = request.newBuilder();
        HttpUrl.Builder httpUrlBuilder = request.url().newBuilder();
        httpUrlBuilder.addQueryParameter("username", username);
        httpUrlBuilder.addQueryParameter("userPwd", userPwd);
        requestBuilder.url(httpUrlBuilder.build());
        request = requestBuilder.build();

        //结果在回调里面，放入handler消息队列
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                obtain.what=-1;
                obtain.obj = null;
                handler.sendMessage(obtain);
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if(response.isSuccessful()){
                    ResponseBody body = response.body();
                    if (body != null) {
                        String string = body.string();
                        LoginOrRegistResult loginOrRegistResult = new LoginOrRegistResult();
                        loginOrRegistResult = gson.fromJson(string, LoginOrRegistResult.class);
                        //传送数据
                        int code = loginOrRegistResult.code;
                        if(code==200) obtain.what=300;
                        else  obtain.what=301;
                        //返回结果时自己判断，跟据loginOrRegistResult的状态码
                        obtain.obj = loginOrRegistResult.data;//错误，完整传入包括状态码
                        handler.sendMessage(obtain);
                    }else {
                        obtain.what=-1;
                        obtain.obj = null;
                        handler.sendMessage(obtain);
                    }
                }else {
                    obtain.what=-1;
                    obtain.obj = null;
                    handler.sendMessage(obtain);
                }
            }
        });
    }

    //不返回数据，返回状态码
    public void uploadComment(int uid, int songId , String comment, CommentInfoActivity.InnerHandler handler) throws IOException {
        String URL = IP_ADDRESS + "UserComment/uploadComment";
        Message obtain = new Message();
        Request request = new Request.Builder().get()
                .url(URL).build();
        //加入搜索参数
        Request.Builder requestBuilder = request.newBuilder();
        HttpUrl.Builder httpUrlBuilder = request.url().newBuilder();
        httpUrlBuilder.addQueryParameter("uid", String.valueOf(uid));
        httpUrlBuilder.addQueryParameter("songId", String.valueOf(songId));
        httpUrlBuilder.addQueryParameter("comment", comment);
        requestBuilder.url(httpUrlBuilder.build());
        request = requestBuilder.build();

        //结果在回调里面，放入handler消息队列
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                obtain.what=-1;
                obtain.obj = null;
                handler.sendMessage(obtain);
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    ResponseBody body = response.body();
                    if (body != null) {
                        String string = body.string();
                        ComentResult comentResult = new ComentResult();
                        comentResult = gson.fromJson(string, ComentResult.class);
                        //传送数据
                        obtain.what=300;//添加成功
                        obtain.obj = comentResult.data;
                        handler.sendMessage(obtain);
                    }else {
                        obtain.what=-1;
                        obtain.obj = null;
                        handler.sendMessage(obtain);
                    }
                }else {
                    obtain.what=-1;
                    obtain.obj = null;
                    handler.sendMessage(obtain);
                }
            }
        });
    }

    //返回新的databean
    public void findCommentBySongId(int songId, CommentInfoActivity.InnerHandler handler) throws IOException {
        String URL = IP_ADDRESS + "UserComment/findCommentBySongId";
        Message obtain = new Message();
        Request request = new Request.Builder().get()
                .url(URL).build();
        //加入搜索参数
        Request.Builder requestBuilder = request.newBuilder();
        HttpUrl.Builder httpUrlBuilder = request.url().newBuilder();
        httpUrlBuilder.addQueryParameter("songId", String.valueOf(songId));
        requestBuilder.url(httpUrlBuilder.build());
        request = requestBuilder.build();

        //结果在回调里面，放入handler消息队列
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                obtain.what=-1;
                obtain.obj = null;
                handler.sendMessage(obtain);
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if(response.isSuccessful()){
                    ResponseBody body = response.body();
                    if (body != null) {
                        String string = body.string();
                        ComentResult comentResult = new ComentResult();
                        comentResult = gson.fromJson(string, ComentResult.class);
                        //传送数据
                        obtain.what=200;//获取评论成功
                        obtain.obj = comentResult.data;
                        handler.sendMessage(obtain);
                    }else {
                        obtain.what=-1;
                        obtain.obj = null;
                        handler.sendMessage(obtain);
                    }
                }else {
                    obtain.what=-1;
                    obtain.obj = null;
                    handler.sendMessage(obtain);
                }
            }
        });
    }

    public void addToLike(int uid, int songId, MusicPlayerMachineActivity.InnerHandler handler) throws IOException {
        String URL = IP_ADDRESS + "UserComment/addToLike";
        Message obtain = new Message();
        Request request = new Request.Builder().get()
                .url(URL).build();
        //加入搜索参数
        Request.Builder requestBuilder = request.newBuilder();
        HttpUrl.Builder httpUrlBuilder = request.url().newBuilder();
        httpUrlBuilder.addQueryParameter("uid", String.valueOf(uid));
        httpUrlBuilder.addQueryParameter("songId", String.valueOf(songId));
        requestBuilder.url(httpUrlBuilder.build());
        request = requestBuilder.build();

        //结果在回调里面，放入handler消息队列
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                obtain.what=-1;
                obtain.obj = null;
                handler.sendMessage(obtain);
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    ResponseBody body = response.body();
                    if (body != null) {
                        String string = body.string();
                        ComentResult comentResult = new ComentResult();
                        comentResult = gson.fromJson(string, ComentResult.class);
                        //传送数据
                        if( comentResult.code == 200 ) {
                            obtain.what = 200;//根据实际情况调整
                        }else {
                            obtain.what=201;//已经添加过了
                        }
                        obtain.obj = comentResult.data;
                        handler.sendMessage(obtain);
                    }else {
                        obtain.what=-1;
                        obtain.obj = null;
                        handler.sendMessage(obtain);
                    }
                }else {
                    obtain.what=-1;
                    obtain.obj = null;
                    handler.sendMessage(obtain);
                }
            }
        });
    }
}
