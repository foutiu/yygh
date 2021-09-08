package com.z.yygh.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.z.yygh.enums.RefundStatusEnum;
import com.z.yygh.model.order.PaymentInfo;
import com.z.yygh.model.order.RefundInfo;
import com.z.yygh.order.mapper.RefundInfoMapper;
import com.z.yygh.order.service.RefundInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RefundInfoServiceImpl extends ServiceImpl<RefundInfoMapper, RefundInfo> implements RefundInfoService {

    @Autowired
    private RefundInfoMapper refundInfoMapper;

    //保存退款记录
    @Override
    public RefundInfo saveRefundInfo(PaymentInfo paymentInfo) {
        //判断是否有重复的数据添加
        QueryWrapper<RefundInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id", paymentInfo.getOrderId());
        wrapper.eq("payment_type", paymentInfo.getPaymentType());
        RefundInfo refundInfo = baseMapper.selectOne(wrapper);
        if(null != refundInfo){//有相同数据
            return refundInfo;
        }
        //添加纪录
        refundInfo = new RefundInfo();
        refundInfo.setCreateTime(new Date());
        refundInfo.setOrderId(paymentInfo.getOrderId());
        refundInfo.setPaymentType(paymentInfo.getPaymentType());
        refundInfo.setOutTradeNo(paymentInfo.getOutTradeNo());
        refundInfo.setRefundStatus(RefundStatusEnum.UNREFUND.getStatus());
        refundInfo.setSubject(paymentInfo.getSubject());
        //paymentInfo.setSubject("test");
        refundInfo.setTotalAmount(paymentInfo.getTotalAmount());
        baseMapper.insert(refundInfo);
        return refundInfo;
    }
}