package com.atguigu.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.yygh.cmn.listener.DictListener;
import com.atguigu.yygh.cmn.mapper.DictMapper;
import com.atguigu.yygh.cmn.service.DictService;
import com.atguigu.yygh.model.cmn.Dict;
import com.atguigu.yygh.vo.cmn.DictEeVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    private DictMapper dictMapper;

    @Override
    public List<Dict> findChildData(Long id) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        List<Dict> dictList = baseMapper.selectList(wrapper);
        for (Dict dict : dictList) {
            Long dictId = dict.getId();
            boolean isChild = this.isChildren(dictId);
            dict.setHasChildren(isChild);
        }
        return dictList;
    }


    //導出數據字典接口
    @Override
    public void exportDictData(HttpServletResponse response) throws IOException {
        //設置下載信息
        response.setContentType("application/vnd.mx-excel");
        response.setCharacterEncoding("utf-8");
        //這裏URLEncoder.encode 可以防止中文亂碼 當然這和easyexcel沒有關係
//        String filename= URLEncoder.encode("數據字典","UTF-8");
        String fiuleName = "dict";
        response.setHeader("Content-disposition", "attachment;filename=" + fiuleName + ".xlsx");
        //查詢數據庫
        List<Dict> dictList = baseMapper.selectList(null);
        //Dict --->DictEevo
        List<DictEeVo> dictVoList = new ArrayList<>();
        for (Dict dict : dictList) {
            DictEeVo dictEeVo = new DictEeVo();
            BeanUtils.copyProperties(dict, dictEeVo);
            dictVoList.add(dictEeVo);
        }
        //調用方法進行操作
        EasyExcel.write(response.getOutputStream(), DictEeVo.class).sheet("dict").doWrite(dictVoList);
    }

    //導入數據字典接口
    @Override
    public void importDictData(MultipartFile file) {

        try {
            EasyExcel.read(file.getInputStream(), DictEeVo.class, new DictListener(dictMapper)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //判斷id下麪是否有子節點
    private boolean isChildren(Long id) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        Integer count = baseMapper.selectCount(wrapper);

        return count > 0;
        //  return baseMapper.selectCount(new QueryWrapper<Dict>().eq("parent_id", id)) > 0);
    }
}
