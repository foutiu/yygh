package com.z.yygh.hosp.controller;

import com.z.yygh.common.result.Result;
import com.z.yygh.hosp.service.HospitalService;
import com.z.yygh.model.hosp.Hospital;
import com.z.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author z
 * @date 2021/8/22
 * @apiNote
 */
@RestController
@RequestMapping(value ="/admin/hosp/hospital")
//@CrossOrigin
public class HospitalController {
    @Autowired
    private HospitalService hospitalService;

    //医院列表（条件查询分页）
    @GetMapping("list/{page}/{limit}")
    public Result listHosp(@PathVariable Integer page,
                           @PathVariable Integer limit,
                           HospitalQueryVo hospitalQueryVo){
        Page<Hospital> pageModel = hospitalService.selectHospPage(page,limit,hospitalQueryVo);
        return Result.ok(pageModel);
    }
    //更新医院上线状态
    @ApiOperation(value = "更新医院上线状态")
    @GetMapping("updateHospStatus/{id}/{status}")
    public Result updateHospStatus(@PathVariable String id, @PathVariable Integer status){
        hospitalService.updateStatus(id,status);
        return Result.ok();
    }

    //获取医院详情
    @ApiOperation(value = "获取医院详情")
    @GetMapping("showHospDetail/{id}")
    public Result showHospDetail(@PathVariable String id){
        Map<String, Object> map = hospitalService.getHospById(id);
        return Result.ok(map);
    }


}
