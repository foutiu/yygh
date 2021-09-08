package com.z.yygh.hosp.controller;

import com.z.yygh.common.result.Result;
import com.z.yygh.hosp.service.ScheduleSrevice;
import com.z.yygh.model.hosp.Schedule;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author z
 * @date 2021/8/23
 * @apiNote
 */
@RestController
@RequestMapping("/admin/hosp/schedule")
//@CrossOrigin
public class ScheduleController {
    @Autowired
    private ScheduleSrevice scheduleSrevice;

    //根据医院编号和科室编号，查询排版规则数据
    @ApiOperation(value = "查询排版规则数据")
    @GetMapping("getScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result getScheduleRule(@PathVariable("page") long page,
                                  @PathVariable("limit") long limit,
                                  @PathVariable("hoscode") String hoscode,
                                  @PathVariable("depcode") String  depcode){
        Map<String, Object> map = scheduleSrevice.getScheduleRule(page,limit,hoscode,depcode);
        return Result.ok(map);
    }

    //根据医院编号、科室编号和工作日期，查询排班详细信息
    @ApiOperation(value = "查询排班详细信息")
    @GetMapping("getScheduleDetail/{hoscode}/{depcode}/{workDate}")
    public Result getScheduleDetail(@PathVariable("hoscode") String hoscode,
                                    @PathVariable("depcode") String  depcode,
                                    @PathVariable("workDate") String workDate){
        List<Schedule> list = scheduleSrevice.getDetailSchedule(hoscode,depcode,workDate);
        return Result.ok(list);
    }


}
