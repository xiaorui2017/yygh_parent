package com.atguigu.yygh.cmn.service;

import com.atguigu.yygh.model.cmn.Dict;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface DictService extends IService<Dict> {
    //根據數據id查詢子數據列表
    List<Dict> findChildData(Long id);
    //導出數據字典接口
    void exportDictData(HttpServletResponse response) throws IOException;
    //導入數據字典
    void importDictData(MultipartFile file) throws IOException;

}
