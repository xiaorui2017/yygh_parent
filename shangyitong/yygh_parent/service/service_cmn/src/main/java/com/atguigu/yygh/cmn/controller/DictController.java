package com.atguigu.yygh.cmn.controller;


import com.atguigu.yygh.cmn.service.DictService;
import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.model.cmn.Dict;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@Api(value = "數據字典接口")
@RestController
@RequestMapping("/admin/cmn/dict")
@CrossOrigin
public class DictController {

    @Autowired
    private DictService dictService;

    //導入數據字典
    @PostMapping("importData")
    public Result importDict(MultipartFile file) throws IOException {
        dictService.importDictData(file);
        return Result.ok();
    }

    //導出數據字典接口
    @GetMapping("exportData")
    public Result exportDict(HttpServletResponse response) throws IOException {
        dictService.exportDictData(response);
        return Result.ok();
    }

    //根據數據id查詢子數據列表
    @GetMapping("findChildData/{id}")
    public Result findChildData(@PathVariable Long id) {
        List<Dict> list = dictService.findChildData(id);
        return Result.ok(list);
    }
}
