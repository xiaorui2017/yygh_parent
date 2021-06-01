package com.atguigu.easyexcel;

import com.alibaba.excel.EasyExcel;

public class TestRead {
    public static void main(String[] args) {
        //讀取文件路徑
        String filename = "F:\\excel\\01.xlsx";
        //調用方法實現讀取操作
        EasyExcel.read(filename, UserData.class, new ExcelListener()).sheet().doRead();
    }
}
