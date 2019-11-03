package com.lym.springboot.web.core.dao;

import com.github.pagehelper.PageHelper;
import com.lym.springboot.web.common.constant.PageBean;
import com.lym.springboot.web.core.domain.WebPermission;
import com.lym.springboot.web.core.domain.WebPermissionExample;
import com.lym.springboot.web.core.dto.request.PermissionListParam;
import com.lym.springboot.web.core.mapper.WebPermissionMapper;
import com.lym.springboot.web.util.PageHelperUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuyanmin on 2019/10/18.
 */
@Repository
public class PermissionDao {

    @Autowired
    private WebPermissionMapper webPermissionMapper;

    public void insert(WebPermission permission) {
        permission.setAddTime(LocalDateTime.now());
        permission.setUpdateTime(LocalDateTime.now());
        webPermissionMapper.insertSelective(permission);
    }

    public List<WebPermission> getAll() {
        WebPermissionExample example = new WebPermissionExample();
        WebPermissionExample.Criteria criteria = example.createCriteria();
        criteria.andDeletedEqualTo(false);
        criteria.andStatusEqualTo(true);
        return webPermissionMapper.selectByExample(example);
    }

    public PageBean<WebPermission> list(PermissionListParam permissionListParam) {
        // 分页
        PageHelperUtil.startPage(permissionListParam.getPageNum(), permissionListParam.getPageSize());

        WebPermissionExample example = new WebPermissionExample();
        WebPermissionExample.Criteria criteria = example.createCriteria();
        criteria.andDeletedEqualTo(false);
        if (permissionListParam.getStatus() != null) {
            criteria.andStatusEqualTo(true);
        }
        if (StringUtils.isNotEmpty(permissionListParam.getPermissionName())) {
            criteria.andPermissionNameLike("%" + permissionListParam.getPermissionName() + "%");
        }
        if (StringUtils.isNotEmpty(permissionListParam.getOrderBy())) {
            example.setOrderByClause(permissionListParam.getOrderBy());
        }
        List<WebPermission> list = webPermissionMapper.selectByExample(example);
        PageBean<WebPermission> pageBean = new PageBean<>(list);
        return pageBean;
    }

    public void update(WebPermission permission) {
        permission.setUpdateTime(LocalDateTime.now());
        webPermissionMapper.updateByPrimaryKeySelective(permission);
    }

    public WebPermission getById(Integer id) {
        return webPermissionMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询所有权限名称
     * @return
     */
    public Map<Integer, String> getAllPermissionNameMap() {
        WebPermissionExample example = new WebPermissionExample();
        List<WebPermission> permissions = webPermissionMapper.selectByExample(example);
        Map<Integer, String> permissionNameMap = new HashMap<>();
        if (permissions != null && permissions.size() > 0) {
            permissions.forEach(e -> permissionNameMap.put(e.getId(), e.getPermissionName()));
        }
        return permissionNameMap;
    }

    /**
     * 查询所有权限信息
     * @return
     */
    public Map<Integer, WebPermission> getAllPermissionMap() {
        WebPermissionExample example = new WebPermissionExample();
        List<WebPermission> permissions = webPermissionMapper.selectByExample(example);
        Map<Integer, WebPermission> permissionNameMap = new HashMap<>();
        if (permissions != null && permissions.size() > 0) {
            permissions.forEach(e -> permissionNameMap.put(e.getId(), e));
        }
        return permissionNameMap;
    }
}
