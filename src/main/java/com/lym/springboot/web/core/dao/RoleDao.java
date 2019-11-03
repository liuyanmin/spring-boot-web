package com.lym.springboot.web.core.dao;

import com.github.pagehelper.PageHelper;
import com.lym.springboot.web.common.constant.PageBean;
import com.lym.springboot.web.core.domain.WebRole;
import com.lym.springboot.web.core.domain.WebRoleExample;
import com.lym.springboot.web.core.domain.WebRoleMenuRe;
import com.lym.springboot.web.core.domain.WebRoleMenuReExample;
import com.lym.springboot.web.core.dto.request.RoleListParam;
import com.lym.springboot.web.core.mapper.WebRoleMapper;
import com.lym.springboot.web.core.mapper.WebRoleMenuReMapper;
import com.lym.springboot.web.util.PageHelperUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by liuyanmin on 2019/10/18.
 */
@Repository
public class RoleDao {

    @Autowired
    private WebRoleMapper webRoleMapper;
    @Autowired
    private WebRoleMenuReMapper webRoleMenuReMapper;

    public void insertRole(WebRole role) {
        role.setAddTime(LocalDateTime.now());
        role.setUpdateTime(LocalDateTime.now());
        webRoleMapper.insertSelective(role);
    }

    public void insertRoleMenu(List<WebRoleMenuRe> roleMenuRes) {
        for (WebRoleMenuRe roleMenuRe : roleMenuRes) {
            roleMenuRe.setAddTime(LocalDateTime.now());
            roleMenuRe.setUpdateTime(LocalDateTime.now());
        }
        webRoleMenuReMapper.insertBatch(roleMenuRes);
    }

    public PageBean<WebRole> list(RoleListParam roleListParam) {
        // 分页
        PageHelperUtil.startPage(roleListParam.getPageNum(), roleListParam.getPageSize());

        WebRoleExample example = new WebRoleExample();
        WebRoleExample.Criteria criteria = example.createCriteria();
        criteria.andDeletedEqualTo(false);
        if (roleListParam.getStatus() != null) {
            criteria.andStatusEqualTo(roleListParam.getStatus());
        }
        if (StringUtils.isNotEmpty(roleListParam.getRoleName())) {
            criteria.andRoleNameLike("%" + roleListParam.getRoleName() + "%");
        }
        if (StringUtils.isNotEmpty(roleListParam.getOrderBy())) {
            example.setOrderByClause(roleListParam.getOrderBy());
        }
        List<WebRole> list = webRoleMapper.selectByExample(example);
        PageBean<WebRole> pageBean = new PageBean<>(list);
        return pageBean;
    }

    public WebRole getById(Integer id) {
        return webRoleMapper.selectByPrimaryKey(id);
    }

    public List<WebRole> getByRoleIds(List<Integer> roleIds) {
        if (roleIds == null || roleIds.size() == 0) {
            return null;
        }
        WebRoleExample example = new WebRoleExample();
        WebRoleExample.Criteria criteria = example.createCriteria();
        criteria.andIdIn(roleIds);
        return webRoleMapper.selectByExample(example);
    }

    public void updateRole(WebRole role) {
        role.setUpdateTime(LocalDateTime.now());
        webRoleMapper.updateByPrimaryKeySelective(role);
    }

    public void deleteRolePermission(Integer roleId) {
        WebRoleMenuReExample example = new WebRoleMenuReExample();
        WebRoleMenuReExample.Criteria criteria = example.createCriteria();
        criteria.andRoleIdEqualTo(roleId);
        webRoleMenuReMapper.deleteByExample(example);
    }

    public List<WebRoleMenuRe> getRolePermission(Integer roleId) {
        WebRoleMenuReExample example = new WebRoleMenuReExample();
        WebRoleMenuReExample.Criteria criteria = example.createCriteria();
        criteria.andRoleIdEqualTo(roleId);
        return webRoleMenuReMapper.selectByExample(example);
    }

    public WebRoleMenuRe getOneRoleMenu(Integer roleId, Integer menuId, Integer permissionId) {
        WebRoleMenuReExample example = new WebRoleMenuReExample();
        WebRoleMenuReExample.Criteria criteria = example.createCriteria();
        criteria.andRoleIdEqualTo(roleId);
        criteria.andMenuIdEqualTo(menuId);
        criteria.andPermissionIdEqualTo(permissionId);
        return webRoleMenuReMapper.selectOneByExample(example);
    }

    public List<WebRole> getAllRole() {
        WebRoleExample example = new WebRoleExample();
        WebRoleExample.Criteria criteria = example.createCriteria();
        criteria.andDeletedEqualTo(false);
        criteria.andStatusEqualTo(true);
        return webRoleMapper.selectByExample(example);
    }
}
