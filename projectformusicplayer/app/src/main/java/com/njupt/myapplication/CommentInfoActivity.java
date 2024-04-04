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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.njupt.bean.DataForMulRecycleView;
import com.njupt.bean.SongInfo;
import com.njupt.bean.UserCommentItem;
import com.njupt.bean.toparsejson.ComentResult;
import com.njupt.service.MediaPlayerService;
import com.njupt.utils.CustomAdapter;
import com.njupt.utils.HttpManager;
import com.njupt.utils.LoginManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommentInfoActivity extends AppCompatActivity {

    //绑定服务时赋值
    ImageView songAlbum;
    TextView songName,singerName;

    MediaPlayerService.MusicControl control ;
    //点击后发起网络请求
    EditText commentEditText;
    Button commentButton;
    //联网获取数据后赋值
    RecyclerView recyclerView;



    CustomAdapter adapter;
    ArrayList<UserCommentItem> userComments;
    ArrayList<DataForMulRecycleView> userCommentForMulRecycleView;

    //联网工具，和通信工具
    HttpManager httpManager ;
    InnerHandler innerHandler;


    private ArrayList<DataForMulRecycleView> preProcess(ArrayList<UserCommentItem> userComments) {
        ArrayList<DataForMulRecycleView> data = new ArrayList<>();
        for(UserCommentItem item : userComments){
            DataForMulRecycleView tmp = new DataForMulRecycleView(5, item);
            data.add(tmp);
        }
        return data;
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            control = (MediaPlayerService.MusicControl) service;
            SongInfo songInfo = MediaPlayerService.getCurrentMusicList().get(MediaPlayerService.getCurIndex());
            Glide.with(CommentInfoActivity.this)
                    .load(HttpManager.IP_ADDRESS + songInfo.albumUrl)
                    .into(songAlbum);
            songName.setText(MediaPlayerService.getCurrentMusicList().get(MediaPlayerService.getCurIndex()).songname);
            singerName.setText(MediaPlayerService.getCurrentMusicList().get(MediaPlayerService.getCurIndex()).singername);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        initView();

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = commentEditText.getText().toString();
                if(msg.equals("")){
                    Toast.makeText(CommentInfoActivity.this, "评论为空！！", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(CommentInfoActivity.this, "增加评论", Toast.LENGTH_SHORT).show();
                    try {
                        httpManager.uploadComment(LoginManager.getInstance().getUid(),
                                MediaPlayerService.getCurrentMusicList().get(MediaPlayerService.getCurIndex()).songId,
                                msg,innerHandler);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });


        //获取评论数据
        try {
            httpManager.findCommentBySongId(MediaPlayerService.getCurrentMusicList().get(MediaPlayerService.getCurIndex()).songId,innerHandler);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    void initView(){
        //绑定服务时赋值
        songAlbum = findViewById(R.id.comment_activity_album);
        songName = findViewById(R.id.comment_activity_songname);
        singerName = findViewById(R.id.comment_activity_singername);
        //点击后发起网络请求
        commentEditText = findViewById(R.id.commentActivity_commentEditText);
        commentButton = findViewById(R.id.commentActivity_commentButton) ;
        //联网获取数据后赋值
        recyclerView = findViewById(R.id.comment_activity_recycle_view) ;
        userComments = new ArrayList<>();
        userCommentForMulRecycleView = new ArrayList<>();


        adapter = CustomAdapter.configureAdapter(CommentInfoActivity.this,userCommentForMulRecycleView);
        recyclerView.setLayoutManager(new GridLayoutManager(CommentInfoActivity.this, 12));
        recyclerView.setAdapter(adapter);

        //联网工具，和通信工具
        httpManager = new HttpManager();
        innerHandler = new InnerHandler();

        //启动服务
        Intent intent = new Intent(getApplicationContext(), MediaPlayerService.class);
        bindService(intent,connection,BIND_AUTO_CREATE);
    }
    public class InnerHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what==200){//获取评论成功
                //接收数据，notifyadapter
//                Toast.makeText(CommentInfoActivity.this, "数据获取成功", Toast.LENGTH_SHORT).show();

                userComments = (ArrayList<UserCommentItem>) ((ComentResult.InnerClass)msg.obj).userCommentList;
                userCommentForMulRecycleView = (preProcess(userComments));
                adapter.setNewData(userCommentForMulRecycleView);
                adapter.notifyDataSetChanged();

            }else if (msg.what==300){//添加评论成功
                Toast.makeText(CommentInfoActivity.this, "评论添加成功", Toast.LENGTH_SHORT).show();
                //获取评论数据
                try {
                    httpManager.findCommentBySongId(MediaPlayerService.getCurrentMusicList().get(MediaPlayerService.getCurIndex()).songId,innerHandler);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                commentEditText.setText("");
                //如果变量为空，new
            }else {
                Toast.makeText(CommentInfoActivity.this, "请检查网络连接", Toast.LENGTH_SHORT).show();

            }
        }
    }
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}
