package com.njupt.utils;

/**
 * 所有activity共享登陆状态
 */
public class LoginManager {
    private static LoginManager instance;
    private boolean isLoggedIn = false;

    private int uid =-1;

    private String username="未登录";

    // 私有构造函数，防止外部直接实例化
    private LoginManager() {
    }

    // 获取单例实例
    public static synchronized LoginManager getInstance() {
        if (instance == null) {
            instance = new LoginManager();
        }
        return instance;
    }

    // 检查登录状态
    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public int getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    // 设置登录状态
    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public void setUidAndUsername(int uid,String username){
        this.uid=uid;
        this.username=username;
    }
}