package com.atguigu.yygh.hosp.controller;

import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.common.utils.MD5;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.atguigu.yygh.vo.hosp.HospitalSetQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@Api(tags = "醫院設置管理")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
@CrossOrigin
public class HospitalSetController {
    //注入service
    @Autowired
    private HospitalSetService hospitalSetService;

    //1.查詢醫院設置的所有信息
    @ApiOperation(value = "獲取所有醫院設置")
    @GetMapping("findAll")
    public Result findAllHospitalSet() {
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
    }

    //2.邏輯刪除
    @ApiOperation(value = "邏輯刪除醫院信息")
    @DeleteMapping("{id}")
    public Result removeHospSet(@PathVariable Long id) {
        boolean flag = hospitalSetService.removeById(id);

        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //3. 條件查詢帶分頁
    @PostMapping("findPage/{current}/{limit}")
    public Result findHospSet(@PathVariable long current,
                              @PathVariable long limit,
                              @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo) {
        //創建page對象，傳遞當前值，每頁記錄數
        Page<HospitalSet> page = new Page<>(current, limit);
        //條件構造
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        String hostname = hospitalSetQueryVo.getHosname();//醫院名稱
        String hoscode = hospitalSetQueryVo.getHoscode();//醫院編號
        if (StringUtils.isEmpty(hoscode) && StringUtils.isEmpty(hostname)) {
            return Result.fail();
        }
        if (!StringUtils.isEmpty(hoscode)) {
            wrapper.like("hostname", hospitalSetQueryVo.getHosname());
        }
        if (!StringUtils.isEmpty(hostname)) {
            wrapper.eq("hoscode", hospitalSetQueryVo.getHoscode());
        }
        Page<HospitalSet> hospitalSetPage = hospitalSetService.page(page, wrapper);
        return Result.ok(hospitalSetPage);
    }

    //4添加醫院設置
    @PostMapping("saveHospiutalSet")
    public Result saveHospitalSet(@RequestBody HospitalSet hospitalSet) {
// 設置狀態1使用 0不能使用
        hospitalSet.setStatus(1);
        //簽名密鑰
        Random random = new Random();
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis() + "" + random.nextInt(1000)));
        //調用service
        boolean save = hospitalSetService.save(hospitalSet);
        if (save) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //5 根據id獲取醫院設置

    @GetMapping("getHospSet/{id}")
    public Result getHospSet(@PathVariable Long id) {
        try {
            //模擬異常
            int a = 1 / 0;
        } catch (Exception e) {
            throw new YyghException("失敗", 201);
        }

        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.ok(hospitalSet);
    }

    //6修改醫院設置
    @PostMapping("updateHospitalSet")
    public Result updateHospitalSet(@RequestBody HospitalSet hospitalSet) {
        boolean flag = hospitalSetService.updateById(hospitalSet);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //7批量刪除醫院設置
    @DeleteMapping("batchRemove")
    public Result batchRemoveHospitalSet(@RequestBody List<String> idList) {
        hospitalSetService.removeByIds(idList);
        return Result.ok();
    }

    //8.醫院設置鎖定和解鎖
    @PutMapping("lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable Long id,
                                  @PathVariable Integer status) {
        //根據id 查詢醫院設置信息
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        hospitalSet.setStatus(status);
        hospitalSetService.updateById(hospitalSet);
        return Result.ok();
    }

    //9.發送簽名密鑰
    @PutMapping("sendKey/{id}")
    public Result lockHospitalSet(@PathVariable Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String signKey = hospitalSet.getSignKey();
        String hoscode = hospitalSet.getHoscode();
        // TODO 發送短信
        return Result.ok();

    }

}
