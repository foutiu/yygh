package com.z.yygh.hosp.service;

import com.z.yygh.model.hosp.Hospital;
import com.z.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author z
 * @date 2021/8/20
 * @apiNote
 */
public interface HospitalService {
    //上传医院接口
    void save(Map<String, Object> paramMap);

    //根据医院编号查询
    Hospital getByHoscode(String hoscode);

    //医院列表（条件查询分页）
    Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);

    //更新医院上线状态
    void updateStatus(String id, Integer status);

    //获取医院详情
    Map<String, Object> getHospById(String id);

    //获取医院名称
    String getHospName(String hoscode);

    //根据医院名称查询
    List<Hospital> findHosName(String hosname);

    //根据医院编号获取医院预约挂号详情
    Map<String, Object> item(String hoscode);
}
