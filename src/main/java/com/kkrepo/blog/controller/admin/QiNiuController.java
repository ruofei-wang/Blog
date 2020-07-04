package com.kkrepo.blog.controller.admin;

import com.github.pagehelper.PageInfo;
import com.kkrepo.blog.common.exception.BaseException;
import com.kkrepo.blog.common.exception.ErrorCode;
import com.kkrepo.blog.common.model.QiNiuEntity;
import com.kkrepo.blog.properties.BlogProperties;
import com.kkrepo.blog.properties.QiniuProperties;
import com.kkrepo.blog.support.R;
import com.kkrepo.blog.support.ResultWrap;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.storage.model.FileListing;
import com.qiniu.util.Auth;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author WangRuofei
 * @create 2020-06-27 9:03 下午
 * @copyright (c) 2020, kkrepo.com All Rights Reserved
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/storage/qiniu")
public class QiNiuController {

    @Autowired
    private BlogProperties properties;

    /**
     * 获取文件列表
     */
    @GetMapping(value = "/list")
    public R list() {
        QiniuProperties qiniu = properties.getQiniu();
        this.check(qiniu);
        try {
            //构造一个带指定Zone对象的配置类
            Configuration cfg = new Configuration(Zone.zone1());
            //...其他参数参考类注释
            Auth auth = Auth.create(qiniu.getAk(), qiniu.getSk());
            BucketManager bucketManager = new BucketManager(auth, cfg);
            //文件名前缀
            String prefix = "";
            //每次迭代的长度限制，最大1000，推荐值 1000
            int limit = 1000;
            //指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
            String delimiter = "";
            //列举空间文件列表
            FileListing fileListing = bucketManager.listFiles(qiniu.getBn(), prefix, null, limit, delimiter);
            List<QiNiuEntity> list = new ArrayList<>();
            for (FileInfo item : fileListing.items) {
                QiNiuEntity qiNiuEntity = new QiNiuEntity(item.hash, item.key, item.mimeType, item.fsize, qiniu.getUrl().trim() + "/" + item.key);
                list.add(qiNiuEntity);
            }
            PageInfo<QiNiuEntity> pageInfo = new PageInfo<>(list);
            int size = list.size();
            pageInfo.setTotal(size);
            pageInfo.setPageNum(1);
            pageInfo.setPageSize(size);
            return ResultWrap.ok(pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(e.getMessage());
        }
    }

    /**
     * 文件上传接口，调用七牛云开放的API
     *
     * @param file 上传的文件
     * @return 返回成功上传的文件：1.名称、2.外链地址
     * <p>
     * 注意：我们指定分布式ID生成器IdWorker工具类生成的随机数作为文件名称； 外链：如果你是第一次使用七牛云，你需要先了解一下如何为自己的七牛云对象储存配置外链地址，因为官方提供的测试外链有时间限制
     * <p>
     * 此部分我后续会写文档讲解
     */
    @RequestMapping("/upload")
    public R upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        QiniuProperties qiniu = properties.getQiniu();
        this.check(qiniu);
        if (!file.isEmpty()) {
            //上传文件路径
            String updateFilePath = "";
            //上传到七牛后保存的文件名
            String key = System.currentTimeMillis() + "";

            try {
                //将MutipartFile对象转换为File对象，相当于需要以本地作为缓冲区暂时储存文件
                //获取文件在服务器的储存位置
                File path = new File(ResourceUtils.getURL("classpath:").getPath());
                File filePath = new File(path.getAbsolutePath(), "upload/");
                if (!filePath.exists() && !filePath.isDirectory()) {
                    filePath.mkdir();
                }
                //获取原始文件名称
                String filename = file.getOriginalFilename();
                //获取文件类型
                key += filename.substring(filename.lastIndexOf("."));

                File localFile = new File(filePath, key);
                //写入磁盘
                file.transferTo(localFile);
                updateFilePath = filePath + "/" + key;

                //密钥配置
                Auth auth = Auth.create(qiniu.getAk(), qiniu.getSk());
                //第二种方式: 自动识别要上传的空间(bucket)的存储区域是华东、华北、华南。
                Zone z = Zone.autoZone();
                Configuration c = new Configuration(z);
                //创建上传对象
                UploadManager uploadManager = new UploadManager(c);
                //调用put方法上传
                Response res = uploadManager.put(updateFilePath, key, auth.uploadToken(qiniu.getBn()));
                //打印返回的信息
                //res.bodyString() 返回数据格式： {"hash":"FlHXdiArTIzeNy94EOxzlCQC7pDS","key":"1074213185631420416.png"}
                Map map = new HashMap<>();
                map.put("name", key);
                map.put("url", qiniu.getUrl() + key);

                if (localFile.exists()) {
                    localFile.delete(); //删除本地缓存的文件
                }
                return ResultWrap.ok(map);
            } catch (Exception e) {
                e.printStackTrace();
                throw new BaseException(e.getMessage());
            }
        }
        return ResultWrap.error(ErrorCode.FILE_ERROR);
    }

    /**
     * 七牛云开放API接口: 文件删除
     *
     * @param name 删除的文件名称，在七牛云API中，对应：key
     */
    @DeleteMapping("/delete")
    public R delete(
        @RequestParam("name") String name
    ) {
        QiniuProperties qiniu = properties.getQiniu();
        this.check(qiniu);
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        Auth auth = Auth.create(qiniu.getAk(), qiniu.getSk());
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(qiniu.getBn(), name);
            return ResultWrap.ok(1);
        } catch (QiniuException e) {
            e.printStackTrace();
            throw new BaseException(e.getMessage());
        }
    }

    /**
     * 七牛云开放API接口：单个文件查询
     *
     * @param name 要查询的文件名称
     */
    @GetMapping("/find")
    public R find(@RequestParam("name") String name) {
        QiniuProperties qiniu = properties.getQiniu();
        this.check(qiniu);
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        Auth auth = Auth.create(qiniu.getAk(), qiniu.getSk());
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            FileInfo fileInfo = bucketManager.stat(qiniu.getBn(), name);
            QiNiuEntity qiNiuEntity = new QiNiuEntity(fileInfo.hash, name, fileInfo.mimeType, fileInfo.fsize, qiniu.getUrl() + "/" + name);
            return ResultWrap.ok(qiNiuEntity);
        } catch (QiniuException e) {
            e.printStackTrace();
            throw new BaseException(e.getMessage());
        }
    }

    private void check(QiniuProperties qiniu) {
        if (StringUtils.isBlank(qiniu.getAk()) || StringUtils.isBlank(qiniu.getSk()) || StringUtils.isBlank(qiniu.getBn()) || StringUtils.isBlank(qiniu.getUrl())) {
            throw new BaseException("请先完善七牛云服务配置，再进行操作");
        }
    }
}
