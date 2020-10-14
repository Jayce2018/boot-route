package com.jayce.boot.route.common.util.template;

import lombok.Data;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * 按模板导出ExcelVO
 *
 * @author 111223/sunjie
 * @date 2020/9/16 15:40
 */
@Data
public class ExcelVO {
    /**
     * Excel工作簿
     */
    private Workbook workbook;

    /**
     * 输出文件名
     */
    private String fileName;

    /**
     * 文件后缀标识
     */
    private String suffix;
}
