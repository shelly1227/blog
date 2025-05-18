package com.shelly.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.shelly.entity.pojo.Friend;
import com.shelly.entity.vo.res.FriendBackResp;
import com.shelly.entity.vo.req.FriendReq;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.query.FriendQuery;
import com.shelly.entity.vo.res.FriendResp;
import com.shelly.service.FriendService;
import com.shelly.mapper.FriendMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author Shelly6
* @description 针对表【t_friend】的数据库操作Service实现
* @createDate 2024-07-22 20:24:46
*/
@RequiredArgsConstructor
@Service
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend>
    implements FriendService{
    private final FriendMapper mapper;

    @Override
    public void addFriend(FriendReq friend) {
        // 新友链
       Friend newFriend = new Friend();
       BeanUtils.copyProperties(friend, newFriend);
        // 添加友链
        baseMapper.insert(newFriend);
    }

    @Override
    public void deleteFriend(List<Integer> integerList) {
        baseMapper.deleteBatchIds(integerList);
    }

    @Override
    public PageResult<FriendBackResp> listFriend(FriendQuery friendQuery) {
        // 创建分页对象，current 表示当前页码，size 表示每页条数
        Page<Friend> page = new Page<>(friendQuery.getOrigPage(), friendQuery.getSize());
        // 创建查询条件
        LambdaQueryWrapper<Friend> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(friendQuery.getKeyword())) {
            queryWrapper.like(Friend::getName, friendQuery.getKeyword());
        }
        queryWrapper.orderByDesc(Friend::getId);
        // 执行分页查询
        IPage<Friend> friendPage = mapper.selectPage(page, queryWrapper);
        // 如果没有查询到数据，返回空结果
        if (friendPage.getTotal() == 0) {
            return new PageResult<>();
        }
        // 将查询结果转换为 DTO
        List<FriendBackResp> friendBackDTOList = friendPage.getRecords().stream()
                .map(friend -> {
                    FriendBackResp dto = new FriendBackResp();
                    BeanUtils.copyProperties(friend, dto);
                    return dto;
                })
                .collect(Collectors.toList());
        // 返回分页结果
        return new PageResult<>(friendBackDTOList, friendPage.getTotal());
    }

    @Override
    public void updateFriend(FriendReq friend) {
        Friend newFriend = new Friend();
        BeanUtils.copyProperties(friend, newFriend);
        baseMapper.updateById(newFriend);
    }

    @Override
    public List<FriendResp> listFriendVO() {
        // 查询友链列表
        return mapper.selectFriendVOList();
    }
}




