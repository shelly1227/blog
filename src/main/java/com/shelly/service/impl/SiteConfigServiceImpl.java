package com.shelly.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shelly.constants.RedisConstant;

import com.shelly.entity.pojo.SiteConfig;
import com.shelly.enums.FilePathEnum;
import com.shelly.service.BlogFileService;
import com.shelly.service.RedisService;
import com.shelly.service.SiteConfigService;
import com.shelly.mapper.SiteConfigMapper;
import com.shelly.strategy.context.UploadStrategyContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

/**
* @author Shelly6
* @description 针对表【t_site_config】的数据库操作Service实现
* @createDate 2024-07-22 20:24:46
*/
@Service
@RequiredArgsConstructor
public class SiteConfigServiceImpl extends ServiceImpl<SiteConfigMapper, SiteConfig>
    implements SiteConfigService {
    private final UploadStrategyContext uploadStrategyContext;
    private final BlogFileServiceImpl blogFileService;
    private final RedisService redisService;
    private final SiteConfigMapper siteConfigMapper;

    @Override
    public String uploadSiteImg(MultipartFile file) {
        // 上传文件
        String url = uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.CONFIG.getPath());
        blogFileService.saveBlogFile(file, url, FilePathEnum.CONFIG.getFilePath());
        return url;
    }

    @Override
    //TODO 修改数据库配置
    public SiteConfig getSiteConfig() {

        SiteConfig siteConfig = redisService.getObject(RedisConstant.SITE_SETTING);
        if (Objects.isNull(siteConfig)) {
            // 从数据库中加载
            siteConfig = siteConfigMapper.selectById(1);
            System.out.println("enter site config");
            redisService.setObject(RedisConstant.SITE_SETTING, siteConfig);
            System.out.println("enter sit config");
        }
        System.out.println("siteConfig = " + siteConfig);
        return siteConfig;
    }

    @Override
    public void updateSiteConfig(SiteConfig siteConfig) {
        baseMapper.updateById(siteConfig);
        redisService.deleteObject(RedisConstant.SITE_SETTING);
    }
}




