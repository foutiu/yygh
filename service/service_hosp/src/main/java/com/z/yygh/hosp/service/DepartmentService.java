package com.z.yygh.hosp.service;

import com.z.yygh.model.hosp.Department;
import com.z.yygh.vo.hosp.DepartmentQueryVo;
import com.z.yygh.vo.hosp.DepartmentVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author z
 * @date 2021/8/21
 * @apiNote
 */
public interface DepartmentService {
    //上传科室接口
    void save(Map<String, Object> parampMap);

    //查询科室接口
    Page<Department> findPageDepartment(int page, int limit, DepartmentQueryVo departmentQueryVo);

    //删除科室接口
    void remove(String hoscode, String depcode);

    //根据医院编号，查询所有科室列表
    List<DepartmentVo> findDeptTree(String hoscode);

    //根据医院编号和科室编号，查询科室名称
    String getDepName(String hoscode, String depcode);

    //根据医院编号和科室编号，查询科室
    Department getDepartment(String hoscode, String depcode);
}
