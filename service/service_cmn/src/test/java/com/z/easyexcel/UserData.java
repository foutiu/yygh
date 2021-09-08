package com.z.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author z
 * @date 2021/8/19
 * @apiNote
 */
@Data
public class UserData {
    @ExcelProperty("用户编号")
    private int udi;
    @ExcelProperty("用户名称")
    private String username;
}
