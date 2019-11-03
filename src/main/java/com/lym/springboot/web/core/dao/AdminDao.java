package com.lym.springboot.web.core.dao;

import com.lym.springboot.web.common.constant.PageBean;
import com.lym.springboot.web.core.domain.WebAdmin;
import com.lym.springboot.web.core.domain.WebAdminExample;
import com.lym.springboot.web.core.domain.WebRole;
import com.lym.springboot.web.core.dto.request.AdminListParam;
import com.lym.springboot.web.core.dto.response.AdminListResponse;
import com.lym.springboot.web.core.mapper.WebAdminMapper;
import com.lym.springboot.web.util.PageHelperUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by liuyanmin on 2019/10/17.
 */
@Repository
public class AdminDao {

    @Autowired
    private WebAdminMapper webAdminMapper;
    @Autowired
    private RoleDao roleDao;

    public WebAdmin getByUserAndPwd(String username, String password) {
        WebAdminExample example = new WebAdminExample();
        WebAdminExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        criteria.andPasswordEqualTo(password);
        criteria.andDeletedEqualTo(false);
        return webAdminMapper.selectOneByExample(example);
    }

    public WebAdmin getByUsername(String username) {
        WebAdminExample example = new WebAdminExample();
        WebAdminExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        criteria.andDeletedEqualTo(false);
        return webAdminMapper.selectOneByExample(example);
    }

    public void updateById(WebAdmin webAdmin) {
        webAdmin.setUpdateTime(LocalDateTime.now());
        webAdminMapper.updateByPrimaryKeySelective(webAdmin);
    }

    public WebAdmin getById(Integer id) {
        return webAdminMapper.selectByPrimaryKey(id);
    }

    public PageBean<AdminListResponse> list(AdminListParam adminListParam) {
        // 分页
        PageHelperUtil.startPage(adminListParam.getPageNum(), adminListParam.getPageSize());

        WebAdminExample example = new WebAdminExample();
        WebAdminExample.Criteria criteria = example.createCriteria();
        criteria.andDeletedEqualTo(false);
        if (StringUtils.isNotBlank(adminListParam.getUsername())) {
            criteria.andUsernameLike("%" + adminListParam.getUsername() + "%");
        }
        if (StringUtils.isNotBlank(adminListParam.getFullname())) {
            criteria.andFullnameLike("%" + adminListParam.getFullname() + "%");
        }
        if (StringUtils.isNotBlank(adminListParam.getEmail())) {
            criteria.andEmailLike("%" + adminListParam.getEmail() + "%");
        }
        if (adminListParam.getGender() != null) {
            criteria.andGenderEqualTo(adminListParam.getGender());
        }
        if (StringUtils.isNotBlank(adminListParam.getMobile())) {
            criteria.andMobileLike("%" + adminListParam.getMobile() + "%");
        }
        if (adminListParam.getStatus() != null) {
            criteria.andStatusEqualTo(adminListParam.getStatus());
        }
        if (adminListParam.getSuperAdmin() != null) {
            criteria.andSuperAdminEqualTo(adminListParam.getSuperAdmin());
        }
        if (StringUtils.isNotBlank(adminListParam.getOrderBy())) {
            example.setOrderByClause(adminListParam.getOrderBy());
        }
        List<WebAdmin> admins = webAdminMapper.selectByExample(example);

        List<AdminListResponse> adminList = new ArrayList<>();
        // 查询角色名称
        if (admins != null && admins.size() > 0) {
            List<Integer> roleIds = admins.stream().map(WebAdmin::getRoleId).collect(Collectors.toList());
            List<WebRole> roles = roleDao.getByRoleIds(roleIds);
            Map<Integer, String> roleNameMap = new HashMap<>();
            if (roles != null) {
                roles.forEach(role -> roleNameMap.put(role.getId(), role.getRoleName()));
            }

            for (WebAdmin admin : admins) {
                AdminListResponse response = new AdminListResponse()
                        .setAdminId(admin.getId())
                        .setUsername(admin.getUsername())
                        .setFullname(admin.getFullname())
                        .setEmail(admin.getEmail())
                        .setGender(admin.getGender())
                        .setMobile(admin.getMobile())
                        .setAvatar(admin.getAvatar())
                        .setLastLoginIp(admin.getLastLoginIp())
                        .setLastLoginTime(admin.getLastLoginTime())
                        .setStatus(admin.getStatus())
                        .setRoleId(admin.getRoleId())
                        .setRoleName(roleNameMap.get(admin.getRoleId()))
                        .setSuperAdmin(admin.getSuperAdmin())
                        .setAddTime(admin.getAddTime())
                        .setUpdateTime(admin.getUpdateTime());

                adminList.add(response);
            }
        }

        PageBean<AdminListResponse> pageBean = new PageBean<>(adminList);
        return pageBean;
    }

    public void insert(WebAdmin admin) {
        admin.setAddTime(LocalDateTime.now());
        admin.setUpdateTime(LocalDateTime.now());
        webAdminMapper.insertSelective(admin);
    }

}
