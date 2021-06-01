package com.atguigu.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class UserData {
    @ExcelProperty("用戶編號")
    private int uid;

    @ExcelProperty("用戶名稱")
    private String username;


}
