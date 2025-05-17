package com.shelly.strategy.context;

import com.shelly.enums.UploadModeEnum;
import com.shelly.strategy.UploadStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

import static com.shelly.enums.UploadModeEnum.getStrategy;

/**
 * 上传策略上下文
 *
 * @author ican
 */
@Service
public class UploadStrategyContext {
    /**
     * 上传模式
     */
    @Value("${upload.strategy}")
    private String uploadStrategy;

    @Autowired
    //localUploadStrategyImpl new LocalUploadStrategyImpl();
    private Map<String, UploadStrategy> uploadStrategyMap;


    /**
     * 上传文件
     *
     * @param file 文件
     * @param path 路径
     * @return {@link String} 文件地址
     */
    public String executeUploadStrategy(MultipartFile file, String path) {
        //获取对应策略实例
        String strategy = UploadModeEnum.getStrategy(uploadStrategy);
        return uploadStrategyMap.get(strategy).uploadFile(file, path);
    }

}