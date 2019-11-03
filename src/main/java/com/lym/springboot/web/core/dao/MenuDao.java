package com.lym.springboot.web.core.dao;

import com.github.pagehelper.PageHelper;
import com.lym.springboot.web.common.constant.PageBean;
import com.lym.springboot.web.core.domain.*;
import com.lym.springboot.web.core.dto.response.MenuListResponse;
import com.lym.springboot.web.core.mapper.WebMenuMapper;
import com.lym.springboot.web.core.mapper.WebMenuPermissionReMapper;
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
 * Created by liuyanmin on 2019/10/18.
 */
@Repository
public class MenuDao {

    @Autowired
    private WebMenuMapper webMenuMapper;
    @Autowired
    private WebMenuPermissionReMapper webMenuPermissionReMapper;

    public void insertMenu(WebMenu menu) {
        menu.setAddTime(LocalDateTime.now());
        menu.setUpdateTime(LocalDateTime.now());
        webMenuMapper.insertSelective(menu);
    }

    public void insertMenuPermission(List<WebMenuPermissionRe> menuPermissionRes) {
        if (menuPermissionRes == null || menuPermissionRes.size() == 0) {
            return;
        }
        for (WebMenuPermissionRe menuPermissionRe : menuPermissionRes) {
            menuPermissionRe.setAddTime(LocalDateTime.now());
            menuPermissionRe.setUpdateTime(LocalDateTime.now());
        }
        webMenuPermissionReMapper.insertBatch(menuPermissionRes);
    }

    public List<MenuListResponse> list(Integer menuId) {

        WebMenuExample example = new WebMenuExample();
        WebMenuExample.Criteria criteria = example.createCriteria();
        criteria.andDeletedEqualTo(false);
        criteria.andParentIdEqualTo(menuId);
        example.setOrderByClause("sort_by asc");
        List<WebMenu> list = webMenuMapper.selectByExample(example);

        List<MenuListResponse> menuListResponses = new ArrayList<>();
        Map<Integer, MenuListResponse> map = new HashMap<>();

        // 查询每个菜单是否有子菜单
        if (list != null && list.size() > 0) {
            for (WebMenu menu : list) {
                MenuListResponse response = new MenuListResponse(menu);
                menuListResponses.add(response);
                map.put(menu.getId(), response);
            }

            List<Integer> menuIds = list.stream().map(WebMenu::getId).collect(Collectors.toList());
            WebMenuExample example2 = new WebMenuExample();
            WebMenuExample.Criteria criteria2 = example.createCriteria();
            criteria2.andParentIdIn(menuIds);
            List<WebMenu> list2 = webMenuMapper.selectByExample(example2);
            if (list2 != null && list2.size() > 0) {
                for (WebMenu menu : list2) {
                    MenuListResponse response = map.get(menu.getParentId());
                    if (response != null) {
                        response.setHasSubMenu(true);
                    }
                }
            }
        }
        return menuListResponses;
    }

    public WebMenu getById(Integer id) {
        return webMenuMapper.selectByPrimaryKey(id);
    }

    public List<WebMenuPermissionRe> getMenuPermission(Integer menuId) {
        WebMenuPermissionReExample example = new WebMenuPermissionReExample();
        WebMenuPermissionReExample.Criteria criteria = example.createCriteria();
        criteria.andMenuIdEqualTo(menuId);
        return webMenuPermissionReMapper.selectByExample(example);
    }

    public void update(WebMenu menu) {
        menu.setUpdateTime(LocalDateTime.now());
        webMenuMapper.updateByPrimaryKeySelective(menu);
    }

    public void deleteMenuPermission(Integer menuId) {
        WebMenuPermissionReExample example = new WebMenuPermissionReExample();
        WebMenuPermissionReExample.Criteria criteria = example.createCriteria();
        criteria.andMenuIdEqualTo(menuId);
        webMenuPermissionReMapper.deleteByExample(example);
    }

    public List<WebMenu> getAll() {
        WebMenuExample example = new WebMenuExample();
        WebMenuExample.Criteria criteria = example.createCriteria();
        criteria.andDeletedEqualTo(false);
        criteria.andStatusEqualTo(true);
        return webMenuMapper.selectByExample(example);
    }

    public List<WebMenuPermissionRe> getAllMenuPermission() {
        WebMenuPermissionReExample example = new WebMenuPermissionReExample();
        return webMenuPermissionReMapper.selectByExample(example);
    }

    public List<WebMenu> getMenuByIdList(List<Integer> menuIds) {
        WebMenuExample example = new WebMenuExample();
        WebMenuExample.Criteria criteria = example.createCriteria();
        criteria.andIdIn(menuIds);
        criteria.andDeletedEqualTo(false);
        criteria.andStatusEqualTo(true);
        return webMenuMapper.selectByExample(example);
    }

    public List<WebMenuPermissionRe> getMenuPermissionByMenuId(List<Integer> menuIds) {
        WebMenuPermissionReExample example = new WebMenuPermissionReExample();
        WebMenuPermissionReExample.Criteria criteria = example.createCriteria();
        criteria.andMenuIdIn(menuIds);
        return webMenuPermissionReMapper.selectByExample(example);
    }

    public List<WebMenuPermissionRe> getMenuPermissionByApiUri(String apiUri) {
        WebMenuPermissionReExample example = new WebMenuPermissionReExample();
        WebMenuPermissionReExample.Criteria criteria = example.createCriteria();
        criteria.andApiUriEqualTo(apiUri);
        return webMenuPermissionReMapper.selectByExample(example);
    }

    public List<WebMenu> getSubMenu(Integer parentId) {
        WebMenuExample example = new WebMenuExample();
        WebMenuExample.Criteria criteria = example.createCriteria();
        criteria.andDeletedEqualTo(false);
        criteria.andStatusEqualTo(true);
        criteria.andParentIdEqualTo(parentId);
        return webMenuMapper.selectByExample(example);
    }

}
