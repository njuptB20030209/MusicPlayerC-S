package com.njupt.test;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.njupt.myapplication.R;
import com.njupt.utils.HttpManager;

import java.io.IOException;

public class TestGetJsonFromHttp extends AppCompatActivity {
    HttpManager httpManager = new HttpManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_gethttp);

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                httpManager.findSongByName("的");
            }
        });

//        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    httpManager.getSongRanking(1,handler);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        });
//        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    httpManager.findSongByUid(1, handler);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        });
        findViewById(R.id.btn4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                try {
//                    httpManager.getMulSongList();
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
            }
        });
//        findViewById(R.id.btn5).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    httpManager.login("test","teseet", handler);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        });
//        findViewById(R.id.btn6).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    httpManager.regist("test","test", handler);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        });
//        findViewById(R.id.btn7).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    httpManager.uploadComment(1,1,"3/15 19:25测试", innerHandler);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        });
//        findViewById(R.id.btn8).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    httpManager.findCommentBySongId(1, innerHandler);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        });

    }

}
