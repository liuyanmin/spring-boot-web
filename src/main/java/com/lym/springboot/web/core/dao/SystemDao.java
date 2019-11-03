package com.lym.springboot.web.core.dao;

import com.lym.springboot.web.common.constant.PageBean;
import com.lym.springboot.web.core.domain.WebSystem;
import com.lym.springboot.web.core.domain.WebSystemExample;
import com.lym.springboot.web.core.dto.request.SystemListParam;
import com.lym.springboot.web.core.mapper.WebSystemMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by liuyanmin on 2019/10/22.
 */
@Repository
public class SystemDao {

    @Autowired
    private WebSystemMapper webSystemMapper;

    public WebSystem getByKeyName(String keyName) {
        WebSystemExample example = new WebSystemExample();
        WebSystemExample.Criteria criteria = example.createCriteria();
        criteria.andKeyNameEqualTo(keyName);
        return webSystemMapper.selectOneByExample(example);
    }

    public void insert(WebSystem system) {
        system.setAddTime(LocalDateTime.now());
        system.setUpdateTime(LocalDateTime.now());
        webSystemMapper.insertSelective(system);
    }

    public WebSystem getById(Integer id) {
        return webSystemMapper.selectByPrimaryKey(id);
    }

    public void update(WebSystem system) {
        system.setUpdateTime(LocalDateTime.now());
        webSystemMapper.updateByPrimaryKeySelective(system);
    }

    public PageBean<WebSystem> list(SystemListParam systemListParam) {
        WebSystemExample example = new WebSystemExample();
        WebSystemExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(systemListParam.getKeyName())) {
            criteria.andKeyNameLike("%" + systemListParam.getKeyName() + "%");
        }
        if (StringUtils.isNotBlank(systemListParam.getKeyValue())) {
            criteria.andKeyValueLike("%" + systemListParam.getKeyValue() + "%");
        }
        if (StringUtils.isNotBlank(systemListParam.getDescription())) {
            criteria.andDescriptionLike("%" + systemListParam.getDescription() + "%");
        }
        List<WebSystem> list = webSystemMapper.selectByExample(example);
        PageBean<WebSystem> pageBean = new PageBean<>(list);
        return pageBean;
    }
}
