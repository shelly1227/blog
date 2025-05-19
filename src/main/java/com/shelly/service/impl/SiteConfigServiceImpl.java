package com.shelly.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.shelly.entity.pojo.SiteConfig;
import com.shelly.enums.FilePathEnum;
import com.shelly.enums.RedisConstants;
import com.shelly.service.SiteConfigService;
import com.shelly.mapper.SiteConfigMapper;
import com.shelly.strategy.context.UploadStrategyContext;
import com.shelly.utils.RedisUtil;
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
    private final RedisUtil redisService;
    private final SiteConfigMapper siteConfigMapper;

    @Override
    public String uploadSiteImg(MultipartFile file) {
        // 上传文件
        String url = uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.CONFIG.getPath());
        blogFileService.saveBlogFile(file, url, FilePathEnum.CONFIG.getFilePath());
        return url;
    }

    @Override
    public SiteConfig getSiteConfig() {
        SiteConfig siteConfig = redisService.get(RedisConstants.SITE_SETTING.getKey(), SiteConfig.class);
        if (Objects.isNull(siteConfig)) {
            // 从数据库中加载
            siteConfig = siteConfigMapper.selectById(1);
            System.out.println("enter site config");
            redisService.set(RedisConstants.SITE_SETTING, siteConfig);
            System.out.println("enter sit config");
        }
        System.out.println("siteConfig = " + siteConfig);
        return siteConfig;
    }

    @Override
    public void updateSiteConfig(SiteConfig siteConfig) {
        baseMapper.updateById(siteConfig);
        redisService.remove(RedisConstants.SITE_SETTING.getKey());
    }
}




