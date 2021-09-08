package com.z.yygh.hosp.controller;

import com.z.yygh.common.result.Result;
import com.z.yygh.hosp.service.DepartmentService;
import com.z.yygh.vo.hosp.DepartmentVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author z
 * @date 2021/8/23
 * @apiNote
 */
@RestController
@RequestMapping("/admin/hosp/deparment")
//@CrossOrigin
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    //根据医院编号，查询所有科室列表
    @ApiOperation(value = "查询所有科室列表")
    @GetMapping("getDeptList/{hoscode}")
    public Result getDeptList(@PathVariable String hoscode){
        List<DepartmentVo> list = departmentService.findDeptTree(hoscode);
        return Result.ok(list);
    }
}
