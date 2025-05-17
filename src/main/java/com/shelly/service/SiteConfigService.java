package com.shelly.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.shelly.entity.pojo.SiteConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
* @author Shelly6
* @description 针对表【t_site_config】的数据库操作Service
* @createDate 2024-07-22 20:24:46
*/

public interface SiteConfigService extends IService<SiteConfig> {

    String uploadSiteImg(MultipartFile file);

    SiteConfig getSiteConfig();

    void updateSiteConfig(SiteConfig siteConfig);
}
