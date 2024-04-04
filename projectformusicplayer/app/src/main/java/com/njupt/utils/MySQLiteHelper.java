//package com.njupt.utils;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//import com.njupt.bean.UserInfo;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MySQLiteHelper extends SQLiteOpenHelper {
//    private static final String DB_NAME = "mySQLite.db";
//    private static final String TABLE_NAME = "userinfo";
//    private static final String CREATE_TABLE_SQL = "create table "+TABLE_NAME+
//            " (id integer primary key autoincrement,userName text,userPwd text);";
//    public MySQLiteHelper(Context context){
//        super(context,DB_NAME,null,1);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(CREATE_TABLE_SQL);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//
//    }
//    public long insertData(UserInfo user){
//        SQLiteDatabase db = getWritableDatabase();
//        ContentValues values = new ContentValues();
//
//        values.put("userName",user.getUserName());
//        values.put("userPwd",user.getUserPwd());
//
//        return db.insert(TABLE_NAME,null,values);
//    }
//    public int deleteData(UserInfo user){
//        SQLiteDatabase db = getWritableDatabase();
//
//        return db.delete(TABLE_NAME,
//                "userName like ? and userPwd like ?",
//                new String[]{user.getUserName(), user.getUserPwd()});
//    }
//    public int updateData(UserInfo user){
//        SQLiteDatabase db = getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//
//        values.put("userName","user.getUserName()");
//        values.put("userPwd","user.getUserPwd()");
//        return db.update(TABLE_NAME,values,
//                "userName like ? and userPwd like ?",
//                new String[]{user.getUserName(), user.getUserPwd()});
//    }
//    public List<UserInfo> queryFromDbByUserName(String name){
//        SQLiteDatabase db = getWritableDatabase();
//
//        List<UserInfo> userList = new ArrayList<>();
//
//        Cursor query = db.query(TABLE_NAME, new String[]{"userName", "userPwd"},
//                "userName like ?", new String[]{name},
//                null, null, null
//        );
//
//        if(query != null){
//            int index;
//            String userName=null,userPwd = null;
//            while(query.moveToNext()){
//                index =  query.getColumnIndex("userName");
//                if(index!=-1) {
//                  userName = query.getString(index);
//                }
//                index =  query.getColumnIndex("userPwd");
//                if(index!=-1) {
//                    userPwd = query.getString(index);
//                }
//                UserInfo userInfo = new UserInfo();
//                userInfo.setUserName(userName);
//                userInfo.setUserPwd(userPwd);
//
//                userList.add(userInfo);
//            }
//        }
//        assert query != null;
//        query.close();
//
//        return userList;
//    }
//
//}
