package com.hgh.tingyue.service;

import org.springframework.web.multipart.MultipartFile;

public interface OssService {
    /**
     * 上传文件到OSS
     *
     * @param file 要上传的文件
     * @return 文件的访问URL
     */
    String uploadFile(MultipartFile file);
}