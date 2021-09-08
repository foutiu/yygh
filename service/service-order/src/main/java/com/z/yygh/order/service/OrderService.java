package com.z.yygh.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.z.yygh.model.order.OrderInfo;
import com.z.yygh.vo.order.OrderCountQueryVo;
import com.z.yygh.vo.order.OrderQueryVo;

import java.util.Map;

/**
 * @author z
 * @date 2021/8/29
 * @apiNote
 */
public interface OrderService extends IService<OrderInfo> {
    //生成挂号订单
    Object saveOrder(String scheduleId, Long patientId);

    //根据订单id查询订单详情
    OrderInfo getOrder(String orderId);

    //订单列表（条件查询带分页）
    IPage<OrderInfo> selectPage(Page<OrderInfo> pageParam, OrderQueryVo orderQueryVo);

    //取消预约
    Object cancelOrder(Long orderId);

    //就诊通知
    void patientTips();

    //预约统计
    Map<String, Object> getCountMap(OrderCountQueryVo orderCountQueryVo);
}
