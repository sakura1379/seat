package com.zlr.seat.utils;

import com.alibaba.fastjson.JSON;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.storage.model.FileListing;
import com.qiniu.util.Auth;
import com.zlr.seat.entity.enums.ResultStatus;
import com.zlr.seat.exception.GlobleException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.utils
 * @Description
 * @create 2022-09-16-下午4:05
 */
@Slf4j
@Component
public class QiniuUtils {

    public static  final String url = "rg8at05gn.hn-bkt.clouddn.com";

    @Value("${qiniu.accessKey}")
    private  String accessKey;
    @Value("${qiniu.accessSecretKey}")
    private  String accessSecretKey;

    public boolean upload(MultipartFile file, String fileName){

        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.huanan());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String bucket = "zlr-blog";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        try {
            byte[] uploadBytes = file.getBytes();
            Auth auth = Auth.create(accessKey, accessSecretKey);
            String upToken = auth.uploadToken(bucket);
            Response response = uploadManager.put(uploadBytes, fileName, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
            log.info("上传文件成功：" + fileName);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean delete(String fileName){
        if (StringUtils.isBlank(fileName)) {
            return false;
        }
        String bucket = "zlr-blog";
        Auth auth = Auth.create(accessKey, accessSecretKey);
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.huanan());
        BucketManager bucketManager = new BucketManager(auth,cfg);

        try {
            Response res = bucketManager.delete(bucket, fileName);
            if (!res.isOK()) {
                log.error("删除文件失败:{}",res);
                throw new GlobleException(ResultStatus.FILE_NOT_DELETE);
            }
            return true;
        } catch (QiniuException ex) {
            log.error("删除文件失败:{0}",ex);
            throw new GlobleException(ResultStatus.FILE_NOT_DELETE);
        }
    }
}
