package com.shelly.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shelly.entity.pojo.Talk;
import com.shelly.entity.vo.query.PageQuery;
import com.shelly.entity.vo.query.TalkQuery;
import com.shelly.entity.vo.res.TalkBackInfoResp;
import com.shelly.entity.vo.res.TalkBackResp;
import com.shelly.entity.vo.res.TalkResp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Shelly6
* @description 针对表【t_talk】的数据库操作Mapper
* @createDate 2024-07-22 20:24:46
* @Entity generator.entity.Talk
*/
public interface TalkMapper extends BaseMapper<Talk> {

    List<TalkBackResp> selectBackTalkList(@Param("param")TalkQuery talkQuery);

    TalkBackInfoResp selectTalkBackById(@Param("talkId") Integer talkId);

    TalkResp selectTalkById(@Param("talkId") Integer talkId);

    List<TalkResp> selectTalkList(@Param("param") PageQuery pageQuery);
}




