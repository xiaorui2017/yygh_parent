package com.atguigu.easyexcel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class TestWrite {
    public static void main(String[] args) {
        //構建數據list集合
        List<UserData> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            UserData userData = new UserData();
            userData.setUid(i);
            userData.setUsername("lucy" + i);
            list.add(userData);
        }
        //設置excel 文件路徑和文件名稱
        String filename = "F:\\excel\\01.xlsx";
        //調用方法實現寫操作
        EasyExcel.write(filename, UserData.class).sheet("用戶信息").doWrite(list);

    }
}
