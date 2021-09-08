package com.z.yygh.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.z.yygh.model.order.OrderInfo;
import com.z.yygh.model.order.PaymentInfo;

import java.util.Map;

public interface PaymentService extends IService<PaymentInfo> {
    /**
     * 向支付记录表中添加信息
     * @param order
     * @param paymentType 支付类型（1：微信 2：支付宝）
     */
    void savePaymentInfo(OrderInfo order, Integer paymentType);

    //更改订单状态，处理支付结果
    void paySuccess(String out_trade_no, Integer status, Map<String, String> resultMap);

    /**
     * 获取支付记录
     * @param orderId
     * @param paymentType
     * @return
     */
    PaymentInfo getPaymentInfo(Long orderId, Integer paymentType);

}
