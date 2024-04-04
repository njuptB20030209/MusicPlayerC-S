//package com.njupt.test;
//
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.njupt.bean.UserInfo;
//import com.njupt.myapplication.R;
//import com.njupt.utils.MySQLiteHelper;
//
//import java.util.List;
//
//public class TestSqlite3 extends AppCompatActivity {
//    private MySQLiteHelper mySQLiteHelper;
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.test_sqlite);
//
//        mySQLiteHelper = new MySQLiteHelper(this);
//    }
//    public void insertData(View view){
//        UserInfo user1 = new UserInfo();
//        user1.setUserName("test1");
//        user1.setUserPwd("test1");
//        //插入数据库
//        long rowId = mySQLiteHelper.insertData(user1);
//        if(rowId == -1){
//            Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
//        }else {
//            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
//        }
//
//    }
//    public void deleteData(View view){
//        UserInfo user1 = new UserInfo();
//        user1.setUserName("test1");
//        user1.setUserPwd("test1");
//        int rowId = mySQLiteHelper.deleteData(user1);
//        if(rowId == 0){
//            Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
//        }else {
//            Toast.makeText(this, "success"+Integer.toString(rowId), Toast.LENGTH_SHORT).show();
//        }
//    }
//    public void updateData(View view){
//        UserInfo user1 = new UserInfo();
//        user1.setUserName("test1");
//        user1.setUserPwd("test1");
//        int rowId = mySQLiteHelper.updateData(user1);
//        if(rowId == 0){
//            Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
//        }else {
//            Toast.makeText(this, "success"+Integer.toString(rowId), Toast.LENGTH_SHORT).show();
//        }
//    }
//    public void queryData(View view){
//        List<UserInfo> test1 = mySQLiteHelper.queryFromDbByUserName("test1");
//    }
//}
