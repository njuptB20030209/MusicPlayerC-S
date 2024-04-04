package com.njupt.bean.toparsejson;

import com.njupt.bean.UserCommentItem;
import com.njupt.bean.UserInfo;

import java.util.List;

/**
 * {"code":200,
 *         "message":"success",
 *         "data":{"userCommentList":[
 *         {"uid":1,"songId":1,"username":"test","comment":"好听"},
 *         {"uid":1,"songId":1,"username":"test","comment":"test"},
 *         {"uid":1,"songId":1,"username":"test","comment":"hhh"}]}}
 */
public class ComentResult {
    public int code;//200成功，505评论失败

    public InnerClass data;

    public class InnerClass {
        public List<UserCommentItem> userCommentList;
    }
}
