package com.jiem.lighten.common.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * excel工具类
 *
 * @author: haojunjie
 * @date: 2022/6/22 10:57
 */
public class ExcelUtils {

    /**
     * Excel分页导出
     *
     * @param response          response
     * @param fileName          文件名
     * @param sheetName         sheetName
     * @param pojoClass         对象Class
     * @param writeDateConsumer 数据
     */
    public static void repeatedWrite(HttpServletResponse response, String fileName, String sheetName, Class<?> pojoClass
            , BiConsumer<ExcelWriter, WriteSheet> writeDateConsumer) throws IOException {
        if (!StringUtils.hasText(fileName)) {
            fileName = UUIDUtil.getUuid();
        }

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        repeatedWrite(response.getOutputStream(), sheetName, pojoClass, writeDateConsumer);
    }

    /**
     * Excel分页导出
     *
     * @param outputStream      outputStream
     * @param sheetName         sheetName
     * @param pojoClass         对象Class
     * @param writeDateConsumer 数据
     */
    public static void repeatedWrite(OutputStream outputStream, String sheetName, Class<?> pojoClass
            , BiConsumer<ExcelWriter, WriteSheet> writeDateConsumer) throws IOException {
        ExcelWriter excelWriter = null;
        try {
            WriteSheet writeSheet = EasyExcel.writerSheet(sheetName).build();
            excelWriter = EasyExcel.write(outputStream, pojoClass).build();
            //防止数据为空时不生成文件
            excelWriter.write(Collections.emptyList(), writeSheet);
            writeDateConsumer.accept(excelWriter, writeSheet);
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    /**
     * Excel导出
     *
     * @param response  response
     * @param fileName  文件名
     * @param sheetName sheetName
     * @param list      数据List
     * @param pojoClass 对象Class
     */
    public static void exportExcel(HttpServletResponse response, String fileName, String sheetName, List<?> list,
                                   Class<?> pojoClass) throws IOException {
        if (!StringUtils.hasText(fileName)) {
            fileName = UUIDUtil.getUuid();
        }

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), pojoClass).sheet(sheetName).doWrite(list);
    }




}
