package com.z.yygh.hosp.controller.api;

import com.z.yygh.common.exception.YyghException;
import com.z.yygh.common.helper.HttpRequestHelper;
import com.z.yygh.common.result.Result;
import com.z.yygh.common.result.ResultCodeEnum;
import com.z.yygh.common.util.MD5;
import com.z.yygh.hosp.service.DepartmentService;
import com.z.yygh.hosp.service.HospitalService;
import com.z.yygh.hosp.service.HospitalSetService;
import com.z.yygh.hosp.service.ScheduleSrevice;
import com.z.yygh.model.hosp.Department;
import com.z.yygh.model.hosp.Hospital;
import com.z.yygh.model.hosp.Schedule;
import com.z.yygh.vo.hosp.DepartmentQueryVo;
import com.z.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author z
 * @date 2021/8/20
 * @apiNote
 */
@RestController
@RequestMapping("/api/hosp")
public class ApiController {

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private HospitalSetService hospitalSetService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ScheduleSrevice scheduleSrevice;

    //删除排班接口
    @PostMapping("schedule/remove")
    public Result removeSchedule(HttpServletRequest request) {
        //获取医院传递过来的信息
        Map<String, String[]> requestMap = request.getParameterMap();
        //为避免后面遍历，将map中的String[]转换成Object
        Map<String, Object> parampMap = HttpRequestHelper.switchMap(requestMap);

        String hoscode = (String)parampMap.get("hoscode");
        String hosScheduleId = (String)parampMap.get("hosScheduleId");

        //1.获取医院系统传递过来的签名
        String hospSign = (String) parampMap.get("sign");

        //2.根据传递过来的医院编码，查询数据库，查询签名
        String signKey = hospitalSetService.getSignKey(hoscode);

        //3.把查询出来的签名进行MD5加密
        String signKeyMD5 = MD5.encrypt(signKey);

        //4.判断签名是否一致
        if(!signKeyMD5.equals(hospSign)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        scheduleSrevice.remove(hoscode,hosScheduleId);
        return Result.ok();

    }

    //查询排班
    @PostMapping("schedule/list")
    public Result findSchedule(HttpServletRequest request){
        //获取医院传递过来的信息
        Map<String, String[]> requestMap = request.getParameterMap();
        //为避免后面遍历，将map中的String[]转换成Object
        Map<String, Object> parampMap = HttpRequestHelper.switchMap(requestMap);

        //科室编号
        String hoscode = (String)parampMap.get("hoscode");
        //排班编号
        String depcode = (String)parampMap.get("depcode");
        int page = StringUtils.isEmpty(parampMap.get("page")) ? 1 : Integer.parseInt((String) parampMap.get("page"));
        int limit = StringUtils.isEmpty(parampMap.get("limit")) ? 1 : Integer.parseInt((String) parampMap.get("limit"));

        //1.获取医院系统传递过来的签名
        String hospSign = (String) parampMap.get("sign");

        //2.根据传递过来的医院编码，查询数据库，查询签名
        String signKey = hospitalSetService.getSignKey(hoscode);

        //3.把查询出来的签名进行MD5加密
        String signKeyMD5 = MD5.encrypt(signKey);

        //4.判断签名是否一致
        if(!signKeyMD5.equals(hospSign)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);
        scheduleQueryVo.setDepcode(depcode);

        //调用service方法
        Page<Schedule> pageModel = scheduleSrevice.findPageSchedule(page,limit,scheduleQueryVo);
        return Result.ok(pageModel);
    }

    //上传排班接口
    @PostMapping("saveSchedule")
    public Result saveSchedule(HttpServletRequest request){
        //获取医院传递过来的信息
        Map<String, String[]> requestMap = request.getParameterMap();
        //为避免后面遍历，将map中的String[]转换成Object
        Map<String, Object> parampMap = HttpRequestHelper.switchMap(requestMap);

        String hoscode = (String)parampMap.get("hoscode");

        //1.获取医院系统传递过来的签名
        String hospSign = (String) parampMap.get("sign");

        //2.根据传递过来的医院编码，查询数据库，查询签名
        String signKey = hospitalSetService.getSignKey(hoscode);

        //3.把查询出来的签名进行MD5加密
        String signKeyMD5 = MD5.encrypt(signKey);

        //4.判断签名是否一致
        if(!signKeyMD5.equals(hospSign)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        scheduleSrevice.save(parampMap);
        return Result.ok();
    }

    //删除科室接口
    @PostMapping("department/remove")
    public Result removeDepartment(HttpServletRequest request){
        //获取医院传递过来的信息
        Map<String, String[]> requestMap = request.getParameterMap();
        //为避免后面遍历，将map中的String[]转换成Object
        Map<String, Object> parampMap = HttpRequestHelper.switchMap(requestMap);

        String hoscode = (String)parampMap.get("hoscode");
        String depcode = (String)parampMap.get("depcode");

        //1.获取医院系统传递过来的签名
        String hospSign = (String) parampMap.get("sign");

        //2.根据传递过来的医院编码，查询数据库，查询签名
        String signKey = hospitalSetService.getSignKey(hoscode);

        //3.把查询出来的签名进行MD5加密
        String signKeyMD5 = MD5.encrypt(signKey);

        //4.判断签名是否一致
        if(!signKeyMD5.equals(hospSign)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        departmentService.remove(hoscode,depcode);
        return Result.ok();

    }

    //查询科室接口
    @PostMapping("department/list")
    public Result findDepartment(HttpServletRequest request){
        //获取医院传递过来的信息
        Map<String, String[]> requestMap = request.getParameterMap();
        //为避免后面遍历，将map中的String[]转换成Object
        Map<String, Object> parampMap = HttpRequestHelper.switchMap(requestMap);

        //医院编号
        String hoscode = (String)parampMap.get("hoscode");
        int page = StringUtils.isEmpty(parampMap.get("page")) ? 1 : Integer.parseInt((String) parampMap.get("page"));
        int limit = StringUtils.isEmpty(parampMap.get("limit")) ? 1 : Integer.parseInt((String) parampMap.get("limit"));

        //1.获取医院系统传递过来的签名
        String hospSign = (String) parampMap.get("sign");

        //2.根据传递过来的医院编码，查询数据库，查询签名
        String signKey = hospitalSetService.getSignKey(hoscode);

        //3.把查询出来的签名进行MD5加密
        String signKeyMD5 = MD5.encrypt(signKey);

        //4.判断签名是否一致
        if(!signKeyMD5.equals(hospSign)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hoscode);

        //调用service方法
        Page<Department> pageModel = departmentService.findPageDepartment(page,limit,departmentQueryVo);
        return Result.ok(pageModel);
    }

    //上传科室接口
    @PostMapping("saveDepartment")
    public Result saveDepartment(HttpServletRequest request){
        //获取医院传递过来的信息
        Map<String, String[]> requestMap = request.getParameterMap();
        //为避免后面遍历，将map中的String[]转换成Object
        Map<String, Object> parampMap = HttpRequestHelper.switchMap(requestMap);

        //获取医院的编号
        String hoscode = (String) parampMap.get("hoscode");

        //1.获取医院系统传递过来的签名
        String hospSign = (String) parampMap.get("sign");

        //2.根据传递过来的医院编码，查询数据库，查询签名
        String signKey = hospitalSetService.getSignKey(hoscode);

        //3.把查询出来的签名进行MD5加密
        String signKeyMD5 = MD5.encrypt(signKey);

        //4.判断签名是否一致
        if(!signKeyMD5.equals(hospSign)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        departmentService.save(parampMap);
        return Result.ok();

    }
    //查询医院
    @PostMapping("hospital/show")
    public Result getHospital(HttpServletRequest request){
        //获取医院传递过来的信息
        Map<String, String[]> requestMap = request.getParameterMap();
        //为避免后面遍历，将map中的String[]转换成Object
        Map<String, Object> parampMap = HttpRequestHelper.switchMap(requestMap);

        //获取医院的编号
        String hoscode = (String) parampMap.get("hoscode");

        //1.获取医院系统传递过来的签名
        String hospSign = (String) parampMap.get("sign");

        //2.根据传递过来的医院编码，查询数据库，查询签名
        String signKey = hospitalSetService.getSignKey(hoscode);

        //3.把查询出来的签名进行MD5加密
        String signKeyMD5 = MD5.encrypt(signKey);

        //4.判断签名是否一致
        if(!signKeyMD5.equals(hospSign)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        Hospital hospital = hospitalService.getByHoscode(hoscode);

        return Result.ok(hospital);
    }
    //上传医院接口
    @PostMapping("saveHospital")
    public Result saveHosp(HttpServletRequest request){
        //获取医院传递过来的信息
        Map<String, String[]> requestMap = request.getParameterMap();
        //为避免后面遍历，将map中的String[]转换成Object
        Map<String, Object> parampMap = HttpRequestHelper.switchMap(requestMap);
        //核验签名是否一致
        //1.获取医院系统传递过来的签名
        String hospSign = (String) parampMap.get("sign");
        //2.根据传递过来的医院编码，查询数据库，查询签名
        String hoscode = (String) parampMap.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);
        //3.把查询出来的签名进行MD5加密
        String signKeyMD5 = MD5.encrypt(signKey);
        //4.判断签名是否一致
        if(!signKeyMD5.equals(hospSign)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        //图片数据采取base64工具类传输，在传输过程中“+”转换为了“ ”，因此我们要转换回来
        String logoData = (String) parampMap.get("logoData");
        logoData = logoData.replace(" ","+");
        parampMap.put("logoData",logoData);

        //调用service的方法
        hospitalService.save(parampMap);
        return Result.ok();
    }
}
