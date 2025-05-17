package com.shelly.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.shelly.entity.pojo.BlogFile;
import com.shelly.entity.vo.Response.FileResp;
import com.shelly.entity.vo.Request.FolderReq;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.Query.FileQuery;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
* @author Shelly6
* @description 针对表【t_blog_file】的数据库操作Service
* @createDate 2024-07-22 20:24:46
*/
public interface BlogFileService extends IService<BlogFile> {

    void createFolder(FolderReq folder);

    void delete(List<Integer> fileIdList);

    PageResult<FileResp> listFile(FileQuery fileQuery);

    void uploadFile(MultipartFile file, String path);

    void downloadFile(java.lang.Integer fileId);
}
