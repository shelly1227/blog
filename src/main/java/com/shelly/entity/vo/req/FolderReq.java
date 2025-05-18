package com.shelly.entity.vo.req;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class FolderReq {
    @NotBlank(message = "文件夹名不能为空")
    String fileName;
    @NotBlank(message = "文件夹路径不能为空")
    String filePath;
}
