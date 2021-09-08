package com.z.yygh.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.z.yygh.oss.service.FileService;
import com.z.yygh.oss.utils.ConstantOssPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String upload(MultipartFile file) {
        // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
        String endpoint = ConstantOssPropertiesUtils.EDNPOINT;
        String accessKeyId = ConstantOssPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantOssPropertiesUtils.SECRECT;
        String bucketName = ConstantOssPropertiesUtils.BUCKET;


        try {
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            // 填写本地文件的完整路径。如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
            InputStream inputStream = null;
            inputStream = file.getInputStream();
            String filename = file.getOriginalFilename();
            //生成唯一随机值，使用UUID，添加到文件名称中
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            filename = uuid+"-"+filename;

            //按照当前日期，创建文件夹，上传到创建的文件夹里面
            String timeUrl = new DateTime().toString("yyy/MM/dd");
            filename = timeUrl+"/"+filename;

            // 依次填写Bucket名称（例如examplebucket）和Object完整路径（例如exampledir/exampleobject.txt）。Object完整路径中不能包含Bucket名称。
            ossClient.putObject(bucketName, filename, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();

            //上传之后文件路径
            String url = "https://"+bucketName+"."+endpoint+"/"+filename;
            return url;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
