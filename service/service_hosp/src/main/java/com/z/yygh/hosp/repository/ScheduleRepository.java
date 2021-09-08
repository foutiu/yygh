package com.z.yygh.hosp.repository;

import com.z.yygh.model.hosp.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

/**
 * @author z
 * @date 2021/8/21
 * @apiNote
 */
public interface ScheduleRepository extends MongoRepository<Schedule,String> {
    //根据医院编号和科室编号查询
    Schedule getScheduleByHoscodeAndHosScheduleId(String hoscode, String depcode);
    //根据医院编号、科室编号和工作日期，查询排班详细信息
    List<Schedule> findScheduleByHoscodeAndDepcodeAndWorkDate(String hoscode, String depcode, Date toDate);
}
