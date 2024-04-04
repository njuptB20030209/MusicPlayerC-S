package com.njupt.bean.toparsejson;

import com.njupt.bean.UserInfo;

/**
 * {"code":200,"message":"success",
 * "data":{"loginUser":{"uid":1,"username":"test","userPwd":""}}}
 */
public class LoginOrRegistResult {
    public int code;//200成功，501用户名错误，503密码错误，505用户名被使用
    public InnerClass data;
    public class InnerClass {
        public UserInfo loginUser;
    }
}
