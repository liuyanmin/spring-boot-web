package com.lym.springboot.web.core.dao;

import com.github.pagehelper.PageInfo;
import com.lym.springboot.web.common.constant.PageBean;
import com.lym.springboot.web.core.domain.WebLog;
import com.lym.springboot.web.core.domain.WebLogExample;
import com.lym.springboot.web.core.mapper.WebLogMapper;
import com.lym.springboot.web.util.PageHelperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by liuyanmin on 2019/9/30.
 */
@Repository
public class LogDao {

    @Autowired
    private WebLogMapper webLogMapper;

    public PageBean<WebLog> list(Integer pageNum, Integer pageSize) {
        // 分页
        PageHelperUtil.startPage(pageNum, pageSize);
        WebLogExample example = new WebLogExample();
        List<WebLog> logInfos = webLogMapper.selectByExample(example);
        PageBean<WebLog> pageBean = new PageBean<>(logInfos);
        return pageBean;
    }

    public int insert(WebLog logInfo) {
        logInfo.setAddTime(LocalDateTime.now());
        logInfo.setUpdateTime(LocalDateTime.now());
        return webLogMapper.insertSelective(logInfo);
    }

    public int updateById(WebLog logInfo) {
        logInfo.setUpdateTime(LocalDateTime.now());
        return webLogMapper.updateByPrimaryKeySelective(logInfo);
    }

}
