package com.jiem.lighten.modules.tumor.controller;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.jiem.lighten.common.base.pojo.Result;
import com.jiem.lighten.common.util.ExcelUtils;
import com.jiem.lighten.modules.tumor.excel.AutoCekCanExcel;
import com.jiem.lighten.modules.tumor.service.AutoCekCanService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.BiConsumer;

/**
 * 肿瘤登记报告查重
 *
 * @author: haojunjie
 * @date: 2023-11-04 9:54
 */
@RestController
@RequestMapping("/tumor/autoCekCan/")
public class AutoCekCanController {

    @Resource
    private AutoCekCanService autoCekCanService;

    /**
     * 拉取并保存肿瘤登记报告
     */
    @PostMapping("popAndSave")
    public Result<String> popAndSave(@RequestBody String cookie) {
        autoCekCanService.popAndSave(cookie);
        return Result.of(null);
    }

    /**
     * 导出
     */
    @GetMapping("excel/{type}")
    public void excel(HttpServletResponse response, @PathVariable("type") Integer type) throws IOException {
        BiConsumer<ExcelWriter, WriteSheet> writeDateConsumer = autoCekCanService.excel(type);
        ExcelUtils.repeatedWrite(response, "数据", "数据", AutoCekCanExcel.class, writeDateConsumer);
    }


}
