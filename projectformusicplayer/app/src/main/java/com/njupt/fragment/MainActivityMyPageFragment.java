package com.njupt.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.njupt.listener.ClickEventImplByMainActivity;
import com.njupt.myapplication.R;
import com.njupt.utils.LoginManager;

public class MainActivityMyPageFragment extends Fragment {
    Button btn_login,btn_like,btn_local;
    Context mContext;

    public MainActivityMyPageFragment() {
        super(R.layout.fragment_activity_main_mypage);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_login = view.findViewById(R.id.my_page_btn_login);
        btn_like = view.findViewById(R.id.my_page_btn_like);
        btn_local = view.findViewById(R.id.my_page_btn_local);

        setListenerEvent();


        if(LoginManager.getInstance().isLoggedIn()){
            String s = "你好，"+LoginManager.getInstance().getUsername();
            btn_login.setText(s);
        }else{
            btn_login.setText("用户未登录");
        }
    }

    private void setListenerEvent() {

        //设置接口
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(LoginManager.getInstance().isLoggedIn()) {
                    Toast.makeText(mContext, LoginManager.getInstance().getUsername()+"，祝你生活愉快！", Toast.LENGTH_SHORT).show();
                }else {
                    ((ClickEventImplByMainActivity)mContext).onClickToJumpToLoginActivity();
                }
            }
        });
        btn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(LoginManager.getInstance().isLoggedIn()) {
//                    Toast.makeText(mContext, LoginManager.getInstance().getUsername()+"，祝你生活愉快！", Toast.LENGTH_SHORT).show();
                    ((ClickEventImplByMainActivity)mContext).onClickToJumpToRecommendActivity("like");
                }else {
                    Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show();
                    ((ClickEventImplByMainActivity)mContext).onClickToJumpToLoginActivity();
                }
            }
        });
        btn_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ClickEventImplByMainActivity)mContext).onClickToJumpToLocalMusicActivity();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(LoginManager.getInstance().isLoggedIn()){
            String s = "你好，"+LoginManager.getInstance().getUsername();
            btn_login.setText(s);
        }else{
            btn_login.setText("用户未登录");
        }
    }
}
