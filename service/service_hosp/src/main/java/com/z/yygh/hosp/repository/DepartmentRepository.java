package com.z.yygh.hosp.repository;

import com.z.yygh.model.hosp.Department;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author z
 * @date 2021/8/21
 * @apiNote
 */
@Repository
public interface DepartmentRepository extends MongoRepository<Department,String> {
    //根据医院编号和科室编号查询
    Department getDepartmentByHoscodeAndDepcode(String hoscode, String depcode);

}
