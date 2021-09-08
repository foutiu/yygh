package com.z.yygh.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.z.yygh.common.exception.YyghException;
import com.z.yygh.common.helper.HttpRequestHelper;
import com.z.yygh.common.result.ResultCodeEnum;
import com.z.yygh.enums.OrderStatusEnum;
import com.z.yygh.enums.PaymentStatusEnum;
import com.z.yygh.enums.PaymentTypeEnum;
import com.z.yygh.hosp.client.HospitalFeignClient;
import com.z.yygh.model.order.OrderInfo;
import com.z.yygh.model.order.PaymentInfo;
import com.z.yygh.order.mapper.PaymentInfoMapper;
import com.z.yygh.order.service.OrderService;
import com.z.yygh.order.service.PaymentService;
import com.z.yygh.vo.order.SignInfoVo;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author z
 * @date 2021/8/31
 * @apiNote
 */
@Service
public class PaymentServiceImpl  extends
        ServiceImpl<PaymentInfoMapper, PaymentInfo> implements PaymentService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private HospitalFeignClient hospitalFeignClient;
    //向支付记录表中添加信息
    @Override
    public void savePaymentInfo(OrderInfo order, Integer paymentType) {
        //根据订单id和支付类型，查询支付纪录表是否存在相同订单
        QueryWrapper<PaymentInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id", order.getId());
        wrapper.eq("payment_type", paymentType);
        Integer count = baseMapper.selectCount(wrapper);
        if (count > 0) {

            return;
        }
        // 保存交易记录
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setCreateTime(new Date());
        paymentInfo.setOrderId(order.getId());
        paymentInfo.setPaymentType(paymentType);
        paymentInfo.setOutTradeNo(order.getOutTradeNo());
        paymentInfo.setPaymentStatus(PaymentStatusEnum.UNPAID.getStatus());
        String subject = new DateTime(order.getReserveDate()).toString("yyyy-MM-dd")+"|"+order.getHosname()+"|"+order.getDepname()+"|"+order.getTitle();
        paymentInfo.setSubject(subject);
        paymentInfo.setTotalAmount(order.getAmount());
        baseMapper.insert(paymentInfo);

    }

    //更改订单状态，处理支付结果
    @Override
    public void paySuccess(String out_trade_no, Integer status, Map<String, String> resultMap) {
        //1 根据订单编号得到支付记录
        QueryWrapper<PaymentInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("out_trade_no", out_trade_no);
        wrapper.eq("payment_type", PaymentTypeEnum.WEIXIN.getStatus());
        PaymentInfo paymentInfo = baseMapper.selectOne(wrapper);

        //2 更新支付纪录信息
        paymentInfo.setPaymentStatus(PaymentStatusEnum.PAID.getStatus());
        paymentInfo.setCallbackTime(new Date());
        paymentInfo.setTradeNo(resultMap.get("transaction_id"));
        paymentInfo.setCallbackContent(resultMap.toString());
        baseMapper.updateById(paymentInfo);

        //3 根据订单得到订单信息
        //4 更新订单信息
        OrderInfo orderInfo = orderService.getById(paymentInfo.getOrderId());
        orderInfo.setOrderStatus(OrderStatusEnum.PAID.getStatus());
        orderService.updateById(orderInfo);

        //5 调用医院接口，更新订单支付信息
        SignInfoVo signInfoVo
                = hospitalFeignClient.getSignInfoVo(orderInfo.getHoscode());
        if(null == signInfoVo) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("hoscode",orderInfo.getHoscode());
        reqMap.put("hosRecordId",orderInfo.getHosRecordId());
        reqMap.put("timestamp", HttpRequestHelper.getTimestamp());
        String sign = HttpRequestHelper.getSign(reqMap, signInfoVo.getSignKey());
        reqMap.put("sign", sign);
        JSONObject result = HttpRequestHelper.sendRequest(reqMap, signInfoVo.getApiUrl()+"/order/updatePayStatus");
        if(result.getInteger("code") != 200) {
            throw new YyghException(result.getString("message"), ResultCodeEnum.FAIL.getCode());
        }
    }

    @Override
    public PaymentInfo getPaymentInfo(Long orderId, Integer paymentType) {
        QueryWrapper<PaymentInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        queryWrapper.eq("payment_type", paymentType);
        return baseMapper.selectOne(queryWrapper);
    }
}
