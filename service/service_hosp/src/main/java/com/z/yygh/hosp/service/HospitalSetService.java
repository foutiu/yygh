package com.z.yygh.hosp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.z.yygh.model.hosp.HospitalSet;
import com.z.yygh.vo.order.SignInfoVo;

/**
 * @author z
 * @date 2021/8/14
 * @apiNote
 */
public interface HospitalSetService extends IService<HospitalSet> {
    //2 根据传递过来的医院编码，查询数据库，查询签名
    String getSignKey(String hoscode);
    //获取医院签名信息
    SignInfoVo getSignInfoVo(String hoscode);
}
