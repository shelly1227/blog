package com.shelly.entity.vo.Response;

import com.shelly.entity.vo.Response.FriendResp;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class FriendBackResp extends FriendResp {
    private LocalDateTime createTime;
}
