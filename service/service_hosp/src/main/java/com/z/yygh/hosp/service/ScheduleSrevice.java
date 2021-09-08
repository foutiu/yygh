package com.z.yygh.hosp.service;

import com.z.yygh.model.hosp.Schedule;
import com.z.yygh.vo.hosp.ScheduleOrderVo;
import com.z.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author z
 * @date 2021/8/21
 * @apiNote
 */
public interface ScheduleSrevice {
    //上传排班接口
    void save(Map<String, Object> parampMap);
    //查询排班
    Page<Schedule> findPageSchedule(int page, int limit, ScheduleQueryVo scheduleQueryVo);
    //删除排班接口
    void remove(String hoscode, String hosScheduleId);
    //根据医院编号和科室编号，查询排版规则数据
    Map<String, Object> getScheduleRule(long page, long limit, String hoscode, String depcode);
    //查询排班详细信息
    List<Schedule> getDetailSchedule(String hoscode, String depcode, String workDate);
    //获取可预约排班数据
    Map<String, Object> getBookingScheduleRule(Integer page, Integer limit, String hoscode, String depcode);
    //根据排班id获取排班数据
    Schedule getScheduleId(String scheduleId);
    //根据排班id获取预约下单数据
    ScheduleOrderVo getScheduleOrderVo(String scheduleId);
    //更新排班数据
    void update(Schedule schedule);
}
