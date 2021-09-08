package com.z.yygh.hosp.repository;

import com.z.yygh.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author z
 * @date 2021/8/20
 * @apiNote
 */
public interface HospitalRepository extends MongoRepository<Hospital,String> {
    //判断是否存在数据
    Hospital getHospitalByHoscode(String hoscode);

    //根据医院名称查询
    List<Hospital> findHospitalByHosnameLike(String hosname);
}
