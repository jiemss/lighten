package com.jiem.lighten.modules.tumor.controller;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.jiem.lighten.common.base.pojo.Result;
import com.jiem.lighten.common.util.ExcelUtils;
import com.jiem.lighten.modules.tumor.pojo.BaseInfo;
import com.jiem.lighten.modules.tumor.service.BaseInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.BiConsumer;

/**
 * 基础信息 Controller
 *
 * @author: haojunjie
 * @date: 2023-10-21 14:52
 */
@RestController
@RequestMapping("/tumor/baseInfo/")
public class BaseInfoController {

    @Resource
    private BaseInfoService baseInfoService;

    /**
     * 拉取并保存基础数据
     */
    @PostMapping("popAndSaveBaseInfo")
    public Result<String> popAndSaveBaseInfo(@RequestBody String cookie) {
        baseInfoService.popAndSaveBaseInfo(cookie);
        return Result.of(null);
    }


    /**
     * 远程拉取地址字典
     */
    @PostMapping("popAndSaveAddressDict")
    public Result<String> popAndSaveAddressDict(@RequestBody String cookie) {
        baseInfoService.popAndSaveAddressDict(cookie);
        return Result.of(null);
    }

    /**
     * 转换街道
     */
    @GetMapping("convertJieDao")
    public Result<String> convertJieDao() {
        baseInfoService.convertJieDao();
        return Result.of(null);
    }


    /**
     * 拉取并保存基础数据
     */
    @GetMapping("excel")
    public void excel(HttpServletResponse response) throws IOException {
        BiConsumer<ExcelWriter, WriteSheet> writeDateConsumer = baseInfoService.excel();
        ExcelUtils.repeatedWrite(response, "数据", "数据", BaseInfo.class, writeDateConsumer);
    }

    /**
     * 远程拉取地址字典
     */
    @PostMapping("exeBaseInfo/{registr}/{streetCode}")
    public Result<Boolean> exeBaseInfo(@PathVariable("registr") String registr
            , @PathVariable("streetCode") String streetCode, @RequestBody String cookie) {
        return Result.of(baseInfoService.exeBaseInfo(registr, Long.parseLong(streetCode), cookie));
    }

}

