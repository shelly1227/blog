package com.shelly.entity.dto;

import com.shelly.entity.pojo.ChatRecord;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@ApiModel(description = "聊天记录DTO")
public class ChatRecordDTO {

    /**
     * 聊天记录
     */
    @ApiModelProperty(value = "聊天记录")
    private List<ChatRecord> chatRecordList;

    /**
     * ip地址
     */
    @ApiModelProperty(value = "ip地址")
    private String ipAddress;

    /**
     * ip来源
     */
    @ApiModelProperty(value = "ip来源")
    private String ipSource;
}