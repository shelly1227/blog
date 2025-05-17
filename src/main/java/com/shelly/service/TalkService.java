package com.shelly.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shelly.entity.pojo.Talk;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.Query.PageQuery;
import com.shelly.entity.vo.Query.TalkQuery;
import com.shelly.entity.vo.Request.TalkReq;
import com.shelly.entity.vo.Response.TalkBackInfoResp;
import com.shelly.entity.vo.Response.TalkBackResp;
import com.shelly.entity.vo.Response.TalkResp;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
* @author Shelly6
* @description 针对表【t_talk】的数据库操作Service
* @createDate 2024-07-22 20:24:46
*/
public interface TalkService extends IService<Talk> {

    PageResult<TalkBackResp> listTalkBackVO(TalkQuery talkQuery);

    String uploadTalkCover(MultipartFile file);

    void addTalk(TalkReq talk);

    void delete(Integer talkId);

    void updateTalk(TalkReq talk);

    TalkBackInfoResp editTalk(Integer talkId);

    List<String> listTalkHome();

    PageResult<TalkResp> listTalkVO(PageQuery pageQuery);

    TalkResp getTalkById(Integer talkId);
}
