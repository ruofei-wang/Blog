package com.kkrepo.blog.controller.admin;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.github.pagehelper.PageInfo;
import com.kkrepo.blog.common.exception.BaseException;
import com.kkrepo.blog.common.model.OSSFileModel;
import com.kkrepo.blog.properties.AliOSSProperties;
import com.kkrepo.blog.properties.BlogProperties;
import com.kkrepo.blog.support.R;
import com.kkrepo.blog.support.ResultWrap;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/admin/storage/oss")
public class AliOSSController {

    private static final int OSS_MAX_KEYS = 1000;
    public static final String OSS_UPLOAD_PATH = "blog/";

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private BlogProperties properties;

    /**
     * 获取文件列表
     */
    @GetMapping(value = "/list")
    public R list(
        @RequestParam(value = "prefix", required = false, defaultValue = "") String prefix
    ) {
        AliOSSProperties ossProperties = properties.getOss();
        OSS ossClient = new OSSClientBuilder().build(ossProperties.getEndPoint(), ossProperties.getAk(), ossProperties.getSk());
        ObjectListing objectListing = ossClient.listObjects(new ListObjectsRequest(ossProperties.getBn()).withMaxKeys(OSS_MAX_KEYS).withPrefix(prefix).withEncodingType("url"));
        List<OSSObjectSummary> ossObjectSummaryList = objectListing.getObjectSummaries();
        List<OSSFileModel> ossFileModelList = ossObjectSummaryList.stream().map(x -> {
            try {
                String key = URLDecoder.decode(x.getKey(), "utf-8");
                return OSSFileModel.builder().name(key).url(ossProperties.getUrl() + key).size(x.getSize() / 1000).lastModifyTime(x.getLastModified()).build();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
        PageInfo<OSSFileModel> pageInfo = new PageInfo<>(ossFileModelList);
        int size = ossObjectSummaryList.size();
        pageInfo.setTotal(size);
        pageInfo.setPageNum(1);
        pageInfo.setPageSize(size);
        return ResultWrap.ok(pageInfo);
    }

    @RequestMapping("/upload")
    public R upload(
        @RequestParam(value = "file", required = true) MultipartFile[] files
    ) {
        AliOSSProperties ossProperties = properties.getOss();
        OSS ossClient = new OSSClientBuilder().build(ossProperties.getEndPoint(), ossProperties.getAk(), ossProperties.getSk());
        for (MultipartFile file : files) {
            // 获取文件名
            String fileName = OSS_UPLOAD_PATH + dateFormat.format(new Date()) + "/" + file.getOriginalFilename();
            try {
                ossClient.putObject(ossProperties.getBn(), fileName, file.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
                throw new BaseException(e.getMessage());
            }
        }
        ossClient.shutdown();
        return ResultWrap.ok(1);
    }

    @DeleteMapping("/delete")
    public R delete(
        @RequestParam("name") String name
    ) {
        AliOSSProperties ossProperties = properties.getOss();
        OSS ossClient = new OSSClientBuilder().build(ossProperties.getEndPoint(), ossProperties.getAk(), ossProperties.getSk());
        ossClient.deleteObject(ossProperties.getBn(), name);
        ossClient.shutdown();
        return ResultWrap.ok(name);
    }

}
